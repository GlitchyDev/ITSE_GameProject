package GameInfo;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by Robert on 8/27/2017.
 *
 * The Purpose of this class is to
 * - Tell where the "Window" of the game is
 * - Center the Window on the Player/Players ( Local Multiplayer averages locations )
 * - Render the viewport to the Screen, Blocks, Entitys, and ALL!
 */
public class WorldViewport {
    private Client client;
    private World world;
    // Determines where the center of the screen
    private int centerX;
    private int centerY;
    // How many block units should be rendered on screen
    private static int viewWidthX = 15;
    private static int viewHeightY = 15;
    // How many extra blocks/entities(by chunk) should be rendered outside of the viewport,
    private int extraViewX = 10;
    private int extraViewY = 10;
    // This affects how much "Smoothing" the screen has between movements, lower the more smoothing is done
    private double smoothingValueX = 0;
    private double smoothingValueY = 0;
    public static final double smoothingAmouunt = 1.1;
    // Used to except smoothing on the first frame
    private boolean isFirstFrame = true;

    private int viewportWidth = viewWidthX * World.getScaledUpSquareSize();
    private int viewportHeight = viewHeightY * World.getScaledUpSquareSize();
    public static int widthBuffer = 0;
    public static int heightBuffer = 0;




    public WorldViewport(Client client, World world, Stage primaryStage, Canvas canvas)
    {
        this.client = client;
        this.world = world;

        heightBuffer = 0;
        widthBuffer = 0;



        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            widthBuffer = (int)((primaryStage.getWidth()-6) - viewportWidth)/2;
            canvas.setWidth(primaryStage.getWidth()-6);
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            heightBuffer = (int)((primaryStage.getHeight()-39) - viewportHeight)/2;
            canvas.setHeight(primaryStage.getHeight()-39);
        });
        widthBuffer = (int)((primaryStage.getWidth()-6) - viewportWidth)/2 - 6;
        heightBuffer = (int)((primaryStage.getHeight()-39) - viewportHeight)/2;



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
            if(isFirstFrame)
            {
                isFirstFrame = false;
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
            if(isFirstFrame)
            {
                isFirstFrame = false;
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
     * The WorldViewport will attempt to render all Blocks and entities within its grasp
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

        ArrayList<EntityBase> entities = world.getAllEntitiesBetweenPoints(centerX + viewWidthX/2 + extraViewX,centerY + viewHeightY/2 + extraViewY,centerX - viewWidthX/2 - extraViewX,centerY - viewHeightY/2 - extraViewY);
        BlockBase[][] viewableBlocks = world.getAllBlocksBetweenPoints(centerX + viewWidthX/2 + extraViewX,centerY + viewHeightY/2 + extraViewY,centerX - viewWidthX/2 - extraViewX,centerY - viewHeightY/2 - extraViewY);
        for(int renderLayer = 0; renderLayer < 5; renderLayer++) {
            for (int y = 0; y < viewableBlocks[0].length; y++) {
                for (int x = 0; x < viewableBlocks.length; x++) {
                    if (viewableBlocks[x][y] != null) {
                        //System.out.println("X: " + x + "Y: " + y + " " + viewableBlocks.length);
                        viewableBlocks[x][y].renderBlock(canvas, gc, x - extraViewX + smoothingValueX, y + 1 - extraViewY + smoothingValueY, renderLayer);
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




        gc.setFill(Color.GRAY);
        //gc.setGlobalAlpha(0.5);
        gc.fillRect(0,0,canvas.getWidth(),heightBuffer);
        gc.fillRect(0,viewportHeight + heightBuffer,canvas.getWidth(),heightBuffer+1);
        gc.fillRect(0,0,widthBuffer,canvas.getHeight());
        gc.fillRect(viewportWidth + widthBuffer,0,widthBuffer,canvas.getHeight());
        gc.setGlobalAlpha(1.0);
        gc.setFill(Color.BLACK);


        // Debug Test Information
        gc.setFill(Color.BLUE);
        gc.fillText("Cord: " + client.getPlayers().get(0).getPlayerCharacter().getX() + ":" + client.getPlayers().get(0).getPlayerCharacter().getY(),350,10);
        gc.fillText("Chunk: " + World.getChunkNumfromCordNum(client.getPlayers().get(0).getPlayerCharacter().getX()) + ":" + World.getChunkNumfromCordNum(client.getPlayers().get(0).getPlayerCharacter().getY()),350,20);
    }



    public int getViewWidthX() {
        return viewWidthX;
    }
    public int getViewHeightY() {
        return viewHeightY;
    }



}
