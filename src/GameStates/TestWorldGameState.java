package GameStates;

import GameInfo.*;
import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Chunk;
import GameInfo.Environment.Entities.*;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.World;
import GameStates.Enums.GameStateEnum;
import GameStates.Enums.MainWorldMiniState;
import RenderingHelpers.TextRenderHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import Utility.DebugModeManager;
import ScoreSystems.PlayerScoreManager;

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

    private long lastMinuteIncrease = 0;


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

        if(System.currentTimeMillis() > lastMinuteIncrease + 1000.0 && !((Pro_Player)client.getPlayers().get(0).getPlayerCharacter()).isDead())
        {
            lastMinuteIncrease = System.currentTimeMillis();
            PlayerScoreManager.addToScore(1);
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

        //double passedSeconds = (System.currentTimeMillis() - stateStart)/1000.0;
        TextRenderHelper.drawCenteredText(300,50,String.valueOf("Score" + PlayerScoreManager.getCurrentScore()),gc,globalGameData);


        if(DebugModeManager.isDebugMode) {
            int mb = 1024 * 1024;
            Runtime instance = Runtime.getRuntime();
            double usedMemory = (instance.totalMemory() - instance.freeMemory()) / mb;

            gc.setFill(Color.BLUE);
            if (lastFPS <= 57) {
                gc.setFill(Color.ORANGE);
            }
            gc.fillText("FPS: " + lastFPS, 350, 50);

            gc.setFill(Color.BLUE);
            if (lastLogicFramePercentage >= 20) {
                gc.setFill(Color.ORANGE);
            }
            gc.fillText("LogicPercentage: " + lastLogicFramePercentage, 350, 60);

            gc.setFill(Color.BLUE);
            if (lastRenderFramePercentage >= 10) {
                if (lastRenderFramePercentage >= 20) {
                    gc.setFill(Color.RED);
                } else {
                    gc.setFill(Color.ORANGE);
                }
            }
            gc.fillText("RenderPercentage: " + lastRenderFramePercentage, 350, 70);

            gc.setFill(Color.BLUE);
            gc.fillText("Used Memory: " + usedMemory + " MB", 350, 80);
            if (usedMemory > 100) {
                System.gc();
            }
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
        Player p1 = new Player(globalGameData.getConnectedControllers().get(0),null);
        final int width = 10;
        Pro_Player player = new Pro_Player(world,globalGameData,p1,globalGameData.getRandom().nextInt(width)-width/2,globalGameData.getRandom().nextInt(width)-width/2);
        p1.setPlayerCharacter(player);
        world.attemptSpawn(p1.getPlayerCharacter(),globalGameData);
        client = new Client(p1);

        world.attemptSpawn(new Alsi_Entity(world,globalGameData,player.getX(),player.getY(),player),globalGameData);
        world.attemptSpawn(new The_Fog_Entity(world,globalGameData,player.getX()+2,player.getY(), player),globalGameData);


        globalGameData.getConnectedPlayers().addAll(client.getPlayers());
        viewport = new WorldViewport(client,world,primaryStage,canvas);

        globalGameData.playSound("CaveWaterDrops",true,0.2);

        stateStart = System.currentTimeMillis();
        lastMinuteIncrease = System.currentTimeMillis();

        PlayerScoreManager.resetScore();

    }


    @Override
    public void exitState(GameStateEnum lastState) {
        int x = client.getPlayers().get(0).getPlayerCharacter().getX();
        int y = client.getPlayers().get(0).getPlayerCharacter().getY();
        for(EntityBase entity: world.getAllEntitiesBetweenPoints(x + 20, y + 20, x - 20, y - 20))
        {
            if(entity.getEntityType() != EntityType.HAUNTED_SKULL)
            {
                if(entity.getEntityType() == EntityType.THE_FOG)
                {
                    globalGameData.stopSound("TheStatic");
                    globalGameData.stopSound("TheMessage");
                }
                world.getChunkFromCordXY(entity.getX(),entity.getY()).removeEntity(entity);
            }
        }
        globalGameData.stopSound("CaveWaterDrops");
        globalGameData.stopSound("elecHumExtended");

    }

    public WorldViewport getViewport() {
        return viewport;
    }
}
