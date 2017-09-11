package GameInfo.Environment.Entities;

import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameInfo.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sample.DirectionalPadEnum;
import sample.TestRenderHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerEntityBase extends EntityBase {
    protected Player player;
    protected HashMap<String,Boolean> booleanInputCache;
    protected DirectionalPadEnum directionPadInputCache;
    protected DirectionalPadEnum previousDirection;

    protected Color color;
    protected ArrayList<Image> sprites;

    public PlayerEntityBase(GlobalGameData globalGameData, int x, int y, Player player) {
        super(x, y);
        this.player = player;
        booleanInputCache = new HashMap<>();
        directionPadInputCache = DirectionalPadEnum.NONE;
        previousDirection = DirectionalPadEnum.SOUTH;
        color = Color.rgb((int)(Math.random() * 256),(int)(Math.random() * 256),(int)(Math.random() * 256));
        sprites = new ArrayList<>();


        sprites.add(globalGameData.getSprite("Pro_Back_Moving_1"));
        sprites.add(globalGameData.getSprite("Pro_Back_Moving_2"));
        sprites.add(globalGameData.getSprite("Pro_Back_Moving_3"));

        sprites.add(globalGameData.getSprite("Pro_Right_Moving_1"));
        sprites.add(globalGameData.getSprite("Pro_Right_Moving_2"));
        sprites.add(globalGameData.getSprite("Pro_Right_Moving_3"));

        sprites.add(globalGameData.getSprite("Pro_Front_Moving_1"));
        sprites.add(globalGameData.getSprite("Pro_Front_Moving_2"));
        sprites.add(globalGameData.getSprite("Pro_Front_Moving_3"));

        sprites.add(globalGameData.getSprite("Pro_Left_Moving_1"));
        sprites.add(globalGameData.getSprite("Pro_Left_Moving_2"));
        sprites.add(globalGameData.getSprite("Pro_Left_Moving_3"));

    }

    @Override
    public void tickEntity(GlobalGameData globalGameData, World world) {
        player.getController().poll();
        if(player.getController().getButtonY())
        {
            moveAbsolute(world, 2147483347,2147483347 );
        }
        else {
            if (player.getController().getButtonB()) {
                switch (player.getController().getDirectionalPad()) {
                    case NORTH:
                        moveRelative(world, 0, 1);
                        break;
                    case SOUTH:
                        moveRelative(world, 0, -1);

                        break;
                    case EAST:
                        moveRelative(world, -1, 0);

                        break;
                    case WEST:
                        moveRelative(world, 1, 0);

                        break;
                    case NONE:
                        break;
                }
            } else {
                if (player.getController().getDirectionalPad() != directionPadInputCache) {
                    directionPadInputCache = player.getController().getDirectionalPad();
                    if(player.getController().getDirectionalPad() != DirectionalPadEnum.NONE)
                    {
                        previousDirection = player.getController().getDirectionalPad();
                    }
                    switch (directionPadInputCache) {
                        case NORTH:
                            if (!player.getController().getButtonA()) {
                                if (world.getBlockFromCords(x, y + 1).checkAvailability(world, this)) {
                                    world.getBlockFromCords(x, y + 1).enterBlock(this);
                                    world.getBlockFromCords(x, y).exitBlock(this);
                                    moveRelative(world, 0, 1);
                                }
                            } else {
                                world.getBlockFromCords(x, y).exitBlock(this);
                                moveRelative(world, 0, 1);
                            }
                            break;
                        case SOUTH:
                            if (!player.getController().getButtonA()) {
                                if (world.getBlockFromCords(x, y - 1).checkAvailability(world, this)) {
                                    world.getBlockFromCords(x, y - 1).enterBlock(this);
                                    world.getBlockFromCords(x, y).exitBlock(this);
                                    moveRelative(world, 0, -1);
                                }
                            } else {
                                world.getBlockFromCords(x, y).exitBlock(this);
                                moveRelative(world, 0, -1);
                            }
                            break;
                        case EAST:
                            if (!player.getController().getButtonA()) {
                                if (world.getBlockFromCords(x - 1, y).checkAvailability(world, this)) {
                                    world.getBlockFromCords(x - 1, y).enterBlock(this);
                                    world.getBlockFromCords(x, y).exitBlock(this);
                                    moveRelative(world, -1, 0);
                                }
                            } else {
                                world.getBlockFromCords(x, y).exitBlock(this);
                                moveRelative(world, -1, 0);
                            }
                            break;
                        case WEST:
                            if (!player.getController().getButtonA()) {
                                if (world.getBlockFromCords(x + 1, y).checkAvailability(world, this)) {
                                    world.getBlockFromCords(x + 1, y).enterBlock(this);
                                    world.getBlockFromCords(x, y).exitBlock(this);
                                    moveRelative(world, 1, 0);
                                }
                            } else {
                                world.getBlockFromCords(x, y).exitBlock(this);
                                moveRelative(world, 1, 0);
                            }
                            break;
                        case NONE:
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if (renderLayer == 1) {
            //gc.drawImage(TestRenderHelper.resample(sprites.get(0),2),x - sprites.get(0).getHeight() * 2 - World.getUnitRatio(),y);
           // gc.drawImage(TestRenderHelper.resample(sprites.get(0),2),(int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5) - (World.getUnitRatio() * 2 - sprites.get(0).getHeight()) );

            int id = 0;
            switch(previousDirection)
            {
                case NORTH:
                    id = 0;
                    break;
                case EAST:
                    id = 3;
                    break;
                case SOUTH:
                    id = 6;
                    break;
                case WEST:
                    id = 9;
                    break;
            }

            int cycle = 1000;

            if(System.currentTimeMillis() % cycle < cycle/4*1) {
                gc.drawImage(TestRenderHelper.resample(sprites.get(id + 0),2),(int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5) - (World.getUnitRatio() * 2 - sprites.get(0).getHeight()) );
            }
            else
            {
                if(System.currentTimeMillis() % cycle < cycle/4*2) {
                    gc.drawImage(TestRenderHelper.resample(sprites.get(id + 1),2),(int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5) - (World.getUnitRatio() * 2 - sprites.get(0).getHeight()) );

                }
                else
                {
                    if(System.currentTimeMillis() % cycle < cycle/4*3) {
                        gc.drawImage(TestRenderHelper.resample(sprites.get(id + 0),2),(int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5) - (World.getUnitRatio() * 2 - sprites.get(0).getHeight()) );

                    }
                    else
                    {
                        gc.drawImage(TestRenderHelper.resample(sprites.get(id + 2),2),(int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5) - (World.getUnitRatio() * 2 - sprites.get(0).getHeight()) );

                    }
                }
            }



            /*
            gc.setFill(color);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x * World.getUnitRatio(), y * World.getUnitRatio(), World.getUnitRatio(), World.getUnitRatio());
            gc.fillRect(x * World.getUnitRatio() + 1, y * World.getUnitRatio() + 1, World.getUnitRatio() - 2, World.getUnitRatio() - 2);
            */
        }
    }


}
