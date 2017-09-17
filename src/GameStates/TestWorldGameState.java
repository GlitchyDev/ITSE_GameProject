package GameStates;

import GameInfo.*;
import GameInfo.Environment.Entities.Pro_Player;
import GameInfo.Environment.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.XBoxController;

import java.util.ArrayList;

/**
 * The "World of the Game", keepping track of the World, viewport, and the Client
 * Created by Robert on 8/28/2017.
 */
public class TestWorldGameState extends GameStateBase {
    private World world;
    private Viewport viewport;
    private Client client;



    public TestWorldGameState(GlobalGameData globalGameData)
    {
        super(globalGameData);
        world = new World(globalGameData);
        viewport = new Viewport(client,world);

    }

    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {
        for(Player p : client.getPlayers())
        {
            p.getPlayerCharacter().tickEntity();
        }

    }

    @Override
    protected void doRender(Canvas canvas, GraphicsContext gc) {
        viewport.render(canvas,gc);

        /*
        gc.setFill(Color.BLACK);
        gc.setGlobalAlpha(0.08);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setGlobalAlpha(1.0);
        */




        gc.setFill(Color.BLACK);
        gc.fillText("FPS: " + lastFPS,250,50);
        gc.fillText("LogicPercentage: " + lastLogicFramePercentage,250,60);
        gc.fillText("RenderPercentage: " + lastRenderFramePercentage,250,70);
    }

    @Override
    public void enterState(GameStateEnum previousState) {
        System.out.println("Test World: Loading State");


        // Add independant code that can inflict controller controls on the entity




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
                world.getChunkFromChunkXY(0,0).getEntities().add(p.getPlayerCharacter());

            }
            client = new Client(players);

        }
        else
        {
            Player p1 = new Player(globalGameData.getConnectedControllers().get(0),null);
            p1.setPlayerCharacter(new Pro_Player(world,globalGameData,p1,5,5));
            world.getChunkFromChunkXY(0,0).getEntities().add(p1.getPlayerCharacter());
            client = new Client(p1);
        }

        viewport = new Viewport(client,world);


    }

    @Override
    public void exitState(GameStateEnum lastState) {
        client.getPlayers().get(0).getPlayerCharacter().setX(5);
        client.getPlayers().get(0).getPlayerCharacter().setY(5);
    }

    public Viewport getViewport() {
        return viewport;
    }
}
