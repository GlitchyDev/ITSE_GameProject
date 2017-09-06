package GameInfo;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Chunk;
import GameInfo.Environment.ChunkID;
import GameInfo.Environment.Entities.EntityBase;
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
    // Determines where the center of the screen
    private int centerX;
    private int centerY;
    // How many block units should be rendered on screen
    private int viewWidthX = 15;
    private int viewHeightY = 15;
    // How many extra blocks should be rendered
    private int extraViewX = 10;
    private int extraViewY = 10;
    private Client client;
    private World world;


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
            centerX = client.getPlayers().get(0).getPlayerCharacter().getX();
            centerY = client.getPlayers().get(0).getPlayerCharacter().getY();
        }
        else
        {
            int averageX = 0;
            int averageY = 0;
            for(Player p: client.getPlayers())
            {
                averageX += p.getPlayerCharacter().getX();
                averageY += p.getPlayerCharacter().getY();
            }
            centerX = averageX / client.getPlayers().size();
            centerY = averageY / client.getPlayers().size();
        }
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
        BlockBase[][] viewableBlocks = new BlockBase[viewWidthX][viewHeightY];


        ChunkID upperLeftChunk = new ChunkID(world.getChunkNumfromCordNum(centerX + viewWidthX/2 + extraViewX), world.getChunkNumfromCordNum((int)(centerY + viewHeightY/2 + extraViewY)));
        ChunkID lowerRightChunk = new ChunkID(world.getChunkNumfromCordNum(centerX - viewWidthX/2 - extraViewX), world.getChunkNumfromCordNum((int)(centerY - viewHeightY/2 - extraViewY)));


        for(int x = lowerRightChunk.getChunkX(); x <= upperLeftChunk.getChunkX(); x++)
        {
            for(int y = lowerRightChunk.getChunkY(); y <= upperLeftChunk.getChunkY(); y++)
            {
                //System.out.println("Added Chunk " + x + "," + y);
                entities.addAll(world.getChunkFromChunkXY(x,y).getEntities());
                world.addBlocksInsideChunk(world.getChunkFromChunkXY(x,y),x,y,viewableBlocks,(int)(centerX + viewWidthX/2),(int)(centerY + viewHeightY/2),(int)(centerX - viewWidthX/2),(int)(centerY - viewHeightY/2));
            }
        }
        //world.viewBlocks(viewableBlocks);

        for(int renderLayer = 0; renderLayer < 5; renderLayer++) {
            for (int x = 0; x < viewableBlocks.length; x++) {
                for (int y = 0; y < viewableBlocks[x].length; y++) {
                    if (viewableBlocks[x][y] != null) {
                        viewableBlocks[x][y].renderBlock(canvas, gc, x, y, renderLayer);
                    }
                }
            }
            for (EntityBase entity : entities) {
                entity.renderEntity(canvas, gc, (int)(centerX + viewWidthX / 2 - entity.getX()), (int)(centerY + viewHeightY / 2 - entity.getY()), renderLayer);
            }
        }


        gc.setFill(Color.BLACK);
        gc.fillText("Cord: " + centerX + ":" + centerY,300,10);
        gc.setFill(Color.BLACK);
        gc.fillText("Chunk: " + world.getChunkNumfromCordNum(client.getPlayers().get(0).getPlayerCharacter().getX()) + ":" + world.getChunkNumfromCordNum(client.getPlayers().get(0).getPlayerCharacter().getY()),300,20);
        gc.setFill(Color.BLACK);
        gc.fillText("Chunk Pos: " + world.getPosNumFromChunkNum(world.getChunkNumfromCordNum(client.getPlayers().get(0).getPlayerCharacter().getX())) + ":" + world.getPosNumFromChunkNum(world.getChunkNumfromCordNum(client.getPlayers().get(0).getPlayerCharacter().getY())),300,30);
    }


    /**

    public void oldRender(Canvas canvas, GraphicsContext gc)
    {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        determineCenter();

        //ArrayList<Chunk> chunkList = new ArrayList<>();
        ArrayList<EntityBase> entities = new ArrayList<>();
        BlockBase[][] viewableBlocks = new BlockBase[viewWidthX][viewHeightY];


        ChunkID upperLeftChunk = new ChunkID(world.getChunkNumfromCordNum(centerX + viewWidthX/2), world.getChunkNumfromCordNum(centerY + viewHeightY/2));
        ChunkID lowerRightChunk = new ChunkID(world.getChunkNumfromCordNum(centerX - viewWidthX/2), world.getChunkNumfromCordNum(centerY - viewHeightY/2));


        for(int x = lowerRightChunk.getChunkX(); x <= upperLeftChunk.getChunkX(); x++)
        {
            for(int y = lowerRightChunk.getChunkY(); y <= upperLeftChunk.getChunkY(); y++)
            {
                //System.out.println("Added Chunk " + x + "," + y);
                entities.addAll(world.getChunkFromChunkXY(x,y).getEntities());
                world.addBlocksInsideChunk(world.getChunkFromChunkXY(x,y),x,y,viewableBlocks,centerX + viewWidthX/2,centerY + viewHeightY/2,centerX - viewWidthX/2,centerY - viewHeightY/2);
            }
        }
        //world.viewBlocks(viewableBlocks);

        for(int renderLayer = 0; renderLayer < 5; renderLayer++) {
            for (int x = 0; x < viewableBlocks.length; x++) {
                for (int y = 0; y < viewableBlocks[x].length; y++) {
                    if (viewableBlocks[x][y] != null) {
                        viewableBlocks[x][y].renderBlock(canvas, gc, x, y, renderLayer);
                    }
                }
            }
            for (EntityBase entity : entities) {
                entity.renderEntity(canvas, gc, centerX + viewWidthX / 2 - entity.getX(), centerY + viewHeightY / 2 - entity.getY(), renderLayer);
            }
        }


        gc.setFill(Color.BLACK);
        gc.fillText("Cord: " + centerX + ":" + centerY,300,10);
        gc.setFill(Color.BLACK);
        gc.fillText("Chunk: " + world.getChunkNumfromCordNum(client.getPlayers().get(0).getPlayerCharacter().getX()) + ":" + world.getChunkNumfromCordNum(client.getPlayers().get(0).getPlayerCharacter().getY()),300,20);
        gc.setFill(Color.BLACK);
        gc.fillText("Chunk Pos: " + world.getPosNumFromChunkNum(world.getChunkNumfromCordNum(client.getPlayers().get(0).getPlayerCharacter().getX())) + ":" + world.getPosNumFromChunkNum(world.getChunkNumfromCordNum(client.getPlayers().get(0).getPlayerCharacter().getY())),300,30);
    }

     */


    public double getViewWidthX() {
        return viewWidthX;
    }
    public double getViewHeightY() {
        return viewHeightY;
    }



}
