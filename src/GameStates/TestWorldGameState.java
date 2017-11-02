package GameStates;

import GameInfo.*;
import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Chunk;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.Entities.Pro_Player;
import GameInfo.Environment.Entities.Haunted_Skull_Entity;
import GameInfo.Environment.Entities.SpriteTesterEntity;
import GameInfo.Environment.World;
import GameStates.Enums.GameStateEnum;
import GameStates.Enums.MainWorldMiniState;
import RenderingHelpers.TextRenderHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import HardwareAdaptors.XBoxController;

import java.util.ArrayList;

/**
 * The "World of the Game", keepping track of the World, viewport, and the Client
 * Created by Robert on 8/28/2017.
 */
public class TestWorldGameState extends GameStateBase {
    private World world;
    private WorldViewport viewport;
    private Client client;
    private Stage primaryStage;
    private Canvas canvas;
    private double stateStart;
    private MainWorldMiniState state;
    private final double fadeIn = 2.0;
    private final double fadeOut = 2.0;



    public TestWorldGameState(GlobalGameData globalGameData, Stage primaryStage, Canvas canvas)
    {
        super(globalGameData);
        world = new World(globalGameData);
        viewport = new WorldViewport(client,world,primaryStage,canvas);
        this.primaryStage = primaryStage;
        this.canvas = canvas;
        this.stateStart = System.currentTimeMillis();
        state = MainWorldMiniState.ENTER;

    }

    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {
        if(state == MainWorldMiniState.ENTER)
        {
            double progress = 1.0/fadeIn * ((System.currentTimeMillis() - stateStart)/1000.0);
            if(progress > 1)
            {
                state = MainWorldMiniState.IDLE;
                stateStart = System.currentTimeMillis();
            }

        }

        for(Player p : client.getPlayers())
        {
            p.getPlayerCharacter().tickEntity();


            for(Chunk c: world.getChunksFromPosWithRadius(p.getPlayerCharacter().getX(),p.getPlayerCharacter().getY(),2)) {
                //Chunk c = world.getChunkFromCordXY(p.getPlayerCharacter().getX(),p.getPlayerCharacter().getY());
                for (BlockBase[] b : c.getBlockBaseList()) {
                    for (BlockBase block : b) {
                        block.tickBlock(world);
                    }
                }
                c.updateChunk();
            }
            for (EntityBase e: world.getAllEntitiesBetweenPoints(p.getPlayerCharacter().getX() + 25,p.getPlayerCharacter().getY() + 25,p.getPlayerCharacter().getX() - 25,p.getPlayerCharacter().getY() - 25)) {
                e.tickEntity();
            }






        }


    }

    @Override
    protected void doRender(Canvas canvas, GraphicsContext gc) {
        viewport.render(canvas,gc);

        gc.setFill(Color.BLACK);
        gc.setGlobalAlpha(0.05);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setGlobalAlpha(1.0);



        int mb = 1024 * 1024;
        Runtime instance = Runtime.getRuntime();
        double usedMemory = (instance.totalMemory() - instance.freeMemory()) / mb;


        double passedSeconds = (System.currentTimeMillis() - stateStart)/1000.0;
        TextRenderHelper.drawText(100,50,String.valueOf(passedSeconds),gc,globalGameData);

        gc.setFill(Color.BLUE);
        if(lastFPS <= 57)
        {
            gc.setFill(Color.ORANGE);
        }
        gc.fillText("FPS: " + lastFPS,350,50);

        gc.setFill(Color.BLUE);
        if(lastLogicFramePercentage >= 20)
        {
            gc.setFill(Color.ORANGE);
        }
        gc.fillText("LogicPercentage: " + lastLogicFramePercentage,350,60);

        gc.setFill(Color.BLUE);
        if(lastRenderFramePercentage >= 10)
        {
            if(lastRenderFramePercentage >= 20)
            {
                gc.setFill(Color.RED);
            }
            else {
                gc.setFill(Color.ORANGE);
            }
        }
        gc.fillText("RenderPercentage: " + lastRenderFramePercentage,350,70);

        gc.setFill(Color.BLUE);
        gc.fillText("Used Memory: " + usedMemory + " MB",350,80);
        if(usedMemory > 100)
        {
            System.gc();
        }


        if(state == MainWorldMiniState.ENTER)
        {
            double progress = 1.0 - 1.0/fadeIn * ((System.currentTimeMillis() - stateStart)/1000.0);
            if(progress < 1)
            {
                gc.setGlobalAlpha(progress);
                gc.setFill(Color.BLACK);
                gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
                gc.setGlobalAlpha(1);
            }

        }

    }

    @Override
    public void enterState(GameStateEnum previousState) {
        System.out.println("Test World: Loading State");


        /*
        if(globalGameData.getConnectedControllers().size() > 1)
        {
            System.out.println("Test World: Detected Multiple Controllers! Creating Multiple Player Objects!");
            int i = 0;
            ArrayList<Player> players = new ArrayList<>();
            for(XBoxController controller: globalGameData.getConnectedControllers())
            {
                Player p = new Player(globalGameData.getConnectedControllers().get(i),null);
                p.setPlayerCharacter(new Pro_Player(world,globalGameData,p,i + 5,i + 5));
                i++;
                players.add(p);
                //world.getChunkFromChunkXY(0,0).getEntities().add(p.getPlayerCharacter());
                world.attemptSpawn(p.getPlayerCharacter(),globalGameData);
            }
            client = new Client(players);
        }
        else
        {
            Player p1 = new Player(globalGameData.getConnectedControllers().get(0),null);
            p1.setPlayerCharacter(new Pro_Player(world,globalGameData,p1,5,5));
            world.attemptSpawn(p1.getPlayerCharacter(),globalGameData);
            //world.getChunkFromChunkXY(0,0).getEntities().add(p1.getPlayerCharacter());
            client = new Client(p1);
        }
        */

        Player p1 = new Player(globalGameData.getConnectedControllers().get(0),null);
        p1.setPlayerCharacter(new Pro_Player(world,globalGameData,p1,globalGameData.getRandom().nextInt(500)-250,globalGameData.getRandom().nextInt(500)-250));
        world.attemptSpawn(p1.getPlayerCharacter(),globalGameData);
        //world.getChunkFromChunkXY(0,0).getEntities().add(p1.getPlayerCharacter());
        client = new Client(p1);


        globalGameData.getConnectedPlayers().addAll(client.getPlayers());
        viewport = new WorldViewport(client,world,primaryStage,canvas);

        //SpriteTesterEntity test = new SpriteTesterEntity(world,globalGameData,3,3);
        //.attemptSpawn(test,globalGameData);
        globalGameData.playSound("CaveWaterDrops",true,0.2);

        stateStart = System.currentTimeMillis();

    }


    @Override
    public void exitState(GameStateEnum lastState) {
        client.getPlayers().get(0).getPlayerCharacter().setX(5);
        client.getPlayers().get(0).getPlayerCharacter().setY(5);
        globalGameData.stopSound("CaveWaterDrops");

    }

    public WorldViewport getViewport() {
        return viewport;
    }
}
