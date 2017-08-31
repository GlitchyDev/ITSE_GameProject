package GameStates;

import GameInfo.Client;
import GameInfo.Environment.Entities.TestEntity;
import GameInfo.Environment.World;
import GameInfo.Player;
import GameInfo.Viewport;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.XBoxController;

/**
 * Created by Robert on 8/28/2017.
 */
public class TestWorldGameState extends GameStateBase {
    private World world;
    private Viewport viewport;
    private Client client;
    private XBoxController x;
    private boolean moved;


    public TestWorldGameState(XBoxController x)
    {
        world = new World();
        TestEntity e = new TestEntity(5,5);
        world.getChunkFromChunkXY(0,0).getEntities().add(e);
        Player p = new Player(x,e);
        client = new Client(p);
        viewport = new Viewport(client,world);
        this.x = x;

        moved = false;
    }

    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {

        x.poll();
        switch(x.getDirectionalPad())
        {
            case NORTH:
                if(!moved) {
                    client.getPlayers().get(0).getPlayerCharacter().moveRelative(world,0,1);
                    moved = true;
                }
                break;
            case SOUTH:
                if(!moved) {
                    client.getPlayers().get(0).getPlayerCharacter().moveRelative(world,0,-1);
                    moved = true;
                }
                break;
            case EAST:
                if(!moved) {
                    client.getPlayers().get(0).getPlayerCharacter().moveRelative(world,-1,0);
                    moved = true;
                }
                break;
            case WEST:
                if(!moved) {
                    client.getPlayers().get(0).getPlayerCharacter().moveRelative(world,1,0);
                    moved = true;
                }
                break;
            case NONE:
                moved = false;
                break;
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
}
