package GameInfo.Environment.Entities;

import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameInfo.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.DirectionalPadEnum;

import java.util.HashMap;

public class PlayerEntityBase extends EntityBase {
    protected Player player;
    protected HashMap<String,Boolean> booleanInputCache;
    protected DirectionalPadEnum directionPadInputCache;

    public PlayerEntityBase(int x, int y, Player player) {
        super(x, y);
        this.player = player;
        booleanInputCache = new HashMap<>();
        directionPadInputCache = DirectionalPadEnum.NONE;
    }

    @Override
    public void tickEntity(GlobalGameData globalGameData, World world) {
        player.getController().poll();
        if(player.getController().getButtonB())
        {
            switch(player.getController().getDirectionalPad())
            {
                case NORTH:
                    moveRelative(world,0,1);
                    break;
                case SOUTH:
                    moveRelative(world,0,-1);

                    break;
                case EAST:
                    moveRelative(world,-1,0);

                    break;
                case WEST:
                    moveRelative(world,1,0);

                    break;
                case NONE:
                    break;
            }
        }
        else {
            if (player.getController().getDirectionalPad() != directionPadInputCache) {
                directionPadInputCache = player.getController().getDirectionalPad();
                switch (directionPadInputCache) {
                    case NORTH:
                        if (!player.getController().getButtonA()) {
                            if (world.getBlockFromCords(x, y + 1).checkAvailability(this)) {
                                moveRelative(world, 0, 1);
                            }
                        } else {
                            moveRelative(world, 0, 1);
                        }
                        break;
                    case SOUTH:
                        if (!player.getController().getButtonA()) {
                            if (world.getBlockFromCords(x, y - 1).checkAvailability(this)) {
                                moveRelative(world, 0, -1);
                            }
                        } else {
                            moveRelative(world, 0, -1);
                        }
                        break;
                    case EAST:
                        if (!player.getController().getButtonA()) {
                            if (world.getBlockFromCords(x - 1, y).checkAvailability(this)) {
                                moveRelative(world, -1, 0);
                            }
                        } else {
                            moveRelative(world, -1, 0);
                        }
                        break;
                    case WEST:
                        if (!player.getController().getButtonA()) {
                            if (world.getBlockFromCords(x + 1, y).checkAvailability(this)) {
                                moveRelative(world, 1, 0);
                            }
                        } else {
                            moveRelative(world, 1, 0);
                        }
                        break;
                    case NONE:
                        break;
                }
            }
        }
    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, int x, int y, int renderLayer) {
        if (renderLayer == 0) {
            gc.setFill(Color.PINK);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x * World.getUnitRatio(), y * World.getUnitRatio(), World.getUnitRatio(), World.getUnitRatio());
            gc.fillRect(x * World.getUnitRatio() + 1, y * World.getUnitRatio() + 1, World.getUnitRatio() - 2, World.getUnitRatio() - 2);
        }
    }


}
