package GameStates;

import GameInfo.*;
import GameInfo.Environment.Entities.PlayerEntityBase;
import GameInfo.Environment.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
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
            p.getPlayerCharacter().tickEntity(globalGameData,world);
        }

    }

    @Override
    protected void doRender(Canvas canvas, GraphicsContext gc) {
        viewport.render(canvas,gc);

        gc.setFill(Color.BLACK);
        gc.fillText("FPS: " + lastFPS,250,50);
        gc.fillText("LogicPercentage: " + lastLogicFramePercentage,250,60);
        gc.fillText("RenderPercentage: " + lastRenderFramePercentage,250,70);
    }

    @Override
    public void enterState(GameStateEnum previousState) {
        System.out.println("Test World: Loading State");


        // Add independant code that can inflict controller controls on the entity


        Player p1 = new Player(globalGameData.getConnectedControllers().get(0),null);
        p1.setPlayerCharacter(new PlayerEntityBase(5,5,p1));
        if(globalGameData.getConnectedControllers().size() > 1)
        {
            System.out.println("Test World: Detected Multiple Controllers! Creating Multiple Player Objects!");
            Player p2 = new Player(globalGameData.getConnectedControllers().get(1),null);
            p2.setPlayerCharacter(new PlayerEntityBase(5,5,p2));
            ArrayList<Player> temp = new ArrayList<>();
            temp.add(p1);
            temp.add(p2);
            world.getChunkFromChunkXY(0,0).getEntities().add(p1.getPlayerCharacter());
            world.getChunkFromChunkXY(0,0).getEntities().add(p2.getPlayerCharacter());
            client = new Client(temp);

        }
        else
        {
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
