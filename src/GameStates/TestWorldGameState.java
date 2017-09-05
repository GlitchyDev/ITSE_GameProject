package GameStates;

import GameInfo.*;
import GameInfo.Environment.Entities.EntityBase;
import GameInfo.Environment.Entities.TestEntity;
import GameInfo.Environment.World;
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


    public TestWorldGameState(GlobalGameData globalGameData)
    {
        super(globalGameData);
        world = new World(globalGameData);
        TestEntity e = new TestEntity(5,5);
        world.getChunkFromChunkXY(0,0).getEntities().add(e);
        Player p = new Player(x,e);
        client = new Client(p);
        viewport = new Viewport(client,world);
        moved = false;
    }

    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {

        x.poll();
        if(x.getLeftStickPress())
        {
            client.getPlayers().get(0).getPlayerCharacter().moveRelative(world,(int)-x.getLeftStickX(),(int)-x.getLeftStickY());
        }
        switch(x.getDirectionalPad())
        {
            case NORTH:
                if(!moved) {
                    EntityBase entity = client.getPlayers().get(0).getPlayerCharacter();
                    if(world.getBlockFromCords(entity.getX(),entity.getY() + 1).checkAvailability(entity)) {
                        entity.moveRelative(world, 0, 1);
                        moved = true;
                    }
                }
                break;
            case SOUTH:
                if(!moved) {
                    EntityBase entity = client.getPlayers().get(0).getPlayerCharacter();
                    if(world.getBlockFromCords(entity.getX(),entity.getY() - 1).checkAvailability(entity)) {
                        entity.moveRelative(world, 0, -1);
                        moved = true;
                    }
                }
                break;
            case EAST:
                if(!moved) {
                    EntityBase entity = client.getPlayers().get(0).getPlayerCharacter();
                    if(world.getBlockFromCords(entity.getX() - 1,entity.getY()).checkAvailability(entity)) {
                        entity.moveRelative(world, -1, 0);
                        moved = true;
                    }
                }
                break;
            case WEST:
                if(!moved) {
                    EntityBase entity = client.getPlayers().get(0).getPlayerCharacter();
                    if(world.getBlockFromCords(entity.getX() + 1,entity.getY()).checkAvailability(entity)) {
                        entity.moveRelative(world, 1, 0);
                        moved = true;
                    }
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

    @Override
    public void enterState(GameStateEnum previousState) {
        x = globalGameData.getConnectedControllers().get(0);
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
