package GameInfo;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.ChunkID;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Pro_Player;
import GameInfo.Environment.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by Robert on 8/27/2017.
 *
 * The Purpose of this class is to
 * - Tell where the "Window" of the game is
 * - Center the Window on the Player/Players ( Local Multiplayer averages locations )
 * - Render the viewport to the Screen, Blocks, Entitys, and ALL!
 */
public class Viewport {
    private Client client;
    private World world;
    // Determines where the center of the screen
    private int centerX;
    private int centerY;
    // How many block units should be rendered on screen
    private int viewWidthX = 13;
    private int viewHeightY = 13;
    // How many extra blocks/entities(by chunk) should be rendered outside of the viewport,
    private int extraViewX = 5;
    private int extraViewY = 5;
    // This affects how much "Smoothing" the screen has between movements
    private double smoothingValueX = 0;
    private double smoothingValueY = 0;
    private final double smoothingAmouunt = 1.1;
    // Used to except smoothing on the first frame
    private boolean firstFrame = true;



    public Viewport(Client client, World world)
    {
        this.client = client;
        this.world = world;

    }

    /**
     * Determines the "Center of the Screen"
     * - It will determine how many players are on the Client Render
     *      - If One Player, the Screen will Center around them
     *      - If Multiple Players, the screen will average their locations for the center
     */
    public void determineCenter()
    {
        if(!client.isLocalClient())
        {
            if(firstFrame)
            {
                firstFrame = false;
            }
            else {
                smoothingValueX += centerX - client.getPlayers().get(0).getPlayerCharacter().getX();
                smoothingValueY += centerY - client.getPlayers().get(0).getPlayerCharacter().getY();
            }
                centerX = client.getPlayers().get(0).getPlayerCharacter().getX();
                centerY = client.getPlayers().get(0).getPlayerCharacter().getY();

        }
        else
        {

            int averageX = 0;
            int averageY = 0;
            for (Player p : client.getPlayers()) {
                averageX += p.getPlayerCharacter().getX();
                averageY += p.getPlayerCharacter().getY();
            }
            if(firstFrame)
            {
                firstFrame = false;
            }
            else {
                smoothingValueX += centerX - (averageX / client.getPlayers().size());
                smoothingValueY += centerY - (averageY / client.getPlayers().size());
            }
                centerX = averageX / client.getPlayers().size();
                centerY = averageY / client.getPlayers().size();

        }
        smoothingValueX = smoothingValueX / smoothingAmouunt;
        smoothingValueY = smoothingValueY / smoothingAmouunt;
    }


    /**
     * The Viewport will attempt to render all Blocks and entities within its grasp
     * - It will use its Width and Height to determine the relected "Culling" area
     *       - First it will determine which of the 9 chunks are currently visible ( Max 4 ) ( Min 1 )
     *       - It will visit the currently available chunks, and request blocks that fit within the Culling area
     *       - It will also ask for a list of entities that are within the culling distance
     *       - Then it will call the rendering methods of all the Entities and blocks providing their "Screen Center"

     */
    public void render(Canvas canvas, GraphicsContext gc)
    {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        determineCenter();

        //ArrayList<Chunk> chunkList = new ArrayList<>();
        ArrayList<EntityBase> entities = new ArrayList<>();
        BlockBase[][] viewableBlocks = new BlockBase[viewWidthX + 1 - viewWidthX % 2 + extraViewX * 2][viewHeightY + 1 - viewHeightY % 2 + extraViewY * 2];


        ChunkID upperLeftChunk = new ChunkID(World.getChunkNumfromCordNum(centerX + viewWidthX/2 + extraViewX ), World.getChunkNumfromCordNum( centerY + viewHeightY/2 + extraViewY));
        ChunkID lowerRightChunk = new ChunkID(World.getChunkNumfromCordNum(centerX - viewWidthX/2 - extraViewX ), World.getChunkNumfromCordNum( centerY - viewHeightY/2 - extraViewY));


        for(int x = lowerRightChunk.getChunkX(); x <= upperLeftChunk.getChunkX(); x++)
        {
            for(int y = lowerRightChunk.getChunkY(); y <= upperLeftChunk.getChunkY(); y++)
            {

                entities.addAll(world.getChunkFromChunkXY(x,y).getEntities());
                world.addBlocksInsideChunk(world.getChunkFromChunkXY(x,y),x,y,viewableBlocks,centerX + viewWidthX/2 + extraViewX,centerY + viewHeightY/2 + extraViewY,centerX - viewWidthX/2 - extraViewX,centerY - viewHeightY/2 - extraViewY);
            }
        }


        // Reramp this to go from Top of screen down for each layer!
        for(int renderLayer = 0; renderLayer < 5; renderLayer++) {
            for (int y = 0; y < viewableBlocks[0].length; y++) {
                for (int x = 0; x < viewableBlocks.length; x++) {
                    if (viewableBlocks[x][y] != null) {
                        //System.out.println("X: " + x + "Y: " + y + " " + viewableBlocks.length);
                        viewableBlocks[x][y].renderBlock(canvas, gc, x - extraViewX + smoothingValueX, y - extraViewY + smoothingValueY, renderLayer);
                    }
                    for (EntityBase entity : entities) {
                        if(entity.getX() == (centerX + viewWidthX/2) - (x - extraViewX))
                        {
                            if(entity.getY() == (centerY + viewHeightY/2) - (y - extraViewY))
                            {
                                entity.renderEntity(canvas, gc, (x - extraViewX) + smoothingValueX, (y - extraViewY) + smoothingValueY, renderLayer);



                            }
                        }
                    }
                }
            }
        }

        // Debug Test Information
        gc.setFill(Color.BLACK);
        gc.fillText("Cord: " + client.getPlayers().get(0).getPlayerCharacter().getX() + ":" + client.getPlayers().get(0).getPlayerCharacter().getY(),300,10);
        gc.setFill(Color.BLACK);
        gc.fillText("Chunk: " + world.getChunkNumfromCordNum(client.getPlayers().get(0).getPlayerCharacter().getX()) + ":" + world.getChunkNumfromCordNum(client.getPlayers().get(0).getPlayerCharacter().getY()),300,20);
    }



    public int getViewWidthX() {
        return viewWidthX;
    }
    public int getViewHeightY() {
        return viewHeightY;
    }



}
