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



    public TestWorldGameState(GlobalGameData globalGameData, Stage primaryStage, Canvas canvas)
    {
        super(globalGameData);
        world = new World(globalGameData);
        viewport = new WorldViewport(client,world,primaryStage,canvas);
        this.primaryStage = primaryStage;
        this.canvas = canvas;
        this.stateStart = System.currentTimeMillis();

    }

    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {
        for(Player p : client.getPlayers())
        {
            p.getPlayerCharacter().tickEntity();
            Chunk c = world.getChunkFromCordXY(p.getPlayerCharacter().getX(),p.getPlayerCharacter().getY());
            for(BlockBase[] b : c.getBlockBaseList())
            {
                for(BlockBase block : b)
                {
                    block.tickBlock(world);
                }
            }
            for(EntityBase entity: c.getEntities())
            {
                if(entity.getEntityType() != EntityType.PLAYER)
                {
                    entity.tickEntity();
                }
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

    }

    @Override
    public void enterState(GameStateEnum previousState) {
        System.out.println("Test World: Loading State");

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
                world.attemptSpawn(p.getPlayerCharacter(),i+5,i+5,true,globalGameData);


            }
            client = new Client(players);

        }
        else
        {
            Player p1 = new Player(globalGameData.getConnectedControllers().get(0),null);
            p1.setPlayerCharacter(new Pro_Player(world,globalGameData,p1,5,5));
            world.attemptSpawn(p1.getPlayerCharacter(),5,5,true,globalGameData);
            //world.getChunkFromChunkXY(0,0).getEntities().add(p1.getPlayerCharacter());
            client = new Client(p1);
        }

        globalGameData.getConnectedPlayers().addAll(client.getPlayers());
        viewport = new WorldViewport(client,world,primaryStage,canvas);


        //TestDebugPathfindingEntity pathfindingDebug = new TestDebugPathfindingEntity(world,globalGameData,0,5);
        //world.addEntityToWorld(pathfindingDebug);

        Haunted_Skull_Entity skullEntity = new Haunted_Skull_Entity(world,globalGameData,7,7);
        world.attemptSpawn(skullEntity,7,7,true,globalGameData);
        //world.addEntityToWorld(skullEntity);

        SpriteTesterEntity test = new SpriteTesterEntity(world,globalGameData,3,3);
        world.attemptSpawn(test,3,3,true,globalGameData);
        //world.addEntityToWorld(test);

        globalGameData.playSound("CaveWaterDrops",true,0.2);
        /*

        Media sound = globalGameData.getSound("CaveWaterDrops");
        backgroundMusic = new MediaPlayer(sound);
        backgroundMusic.setOnEndOfMedia(() -> backgroundMusic.seek(Duration.ZERO));
        backgroundMusic.play();
        */


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
