package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameInfo.Player;
import GameStates.TestWorldGameState;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.DirectionalPadEnum;
import sample.TestRenderHelper;
import sample.XBoxController;

import java.util.ArrayList;

/**
 * This class aims to
 * - Be the basic Player Character, Protagonist ( Shortened to Pro )
 * - Implement Controls for the Player
 */
public class Pro_Player extends DamageableEntityBase {
    // The Player assigned to this Entity
    private Player player;
    // The Controller
    private XBoxController controller;
    // The current Sprite storage system, probably will be changed
    private ArrayList<Image> sprites;

    // The Previous Frame's direction
    private DirectionalPadEnum cachedDirection;
    // The last Nondiagnal direction we have recieved, used to keep the sprite pointing in the right direction
    private DirectionalPadEnum primaryDirection;


    // This all controls the "Hold Down and move" logic
    private boolean isMoving = false;
    // How long it takes for a movement to register
    private double movementDelay = 0.09;
    private long lastMovement = 0;


    public Pro_Player(World world, GlobalGameData globalGameData, Player player, int x, int y) {
        super(world, globalGameData, x, y);
        this.player = player;
        this.controller = player.getController();

        entityType = EntityType.PLAYER;
        currentHealth = 5;
        maxHealth = 5;

        cachedDirection = DirectionalPadEnum.NONE;
        primaryDirection = DirectionalPadEnum.SOUTH;


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
    public void tickEntity() {
        controller.poll();

        // Debug command "Reset World"
        if(controller.getBack())
        {
            globalGameData.resetWorld();
            return;
        }

        // Debug Command "Poll Structures"
        if(controller.getStart())
        {
            System.out.println("Poll of Structures in chunk " + world.getChunkFromCordXY(x,y).getStructures().size());
        }
        // Debug Command "Fly through shit"
        if (controller.getButtonB()) {
            switch (player.getController().getDirectionalPad()) {
                case NORTH:
                    moveRelative(0, 1);
                    return;
                case SOUTH:
                    moveRelative(0, -1);
                    return;
                case EAST:
                    moveRelative(-1, 0);
                    return;
                case WEST:
                    moveRelative(1, 0);
                    return;
                case NONE:
                    return;
            }
        }

        // If we have reached here, we are attempting to implement controls

        if (!isMoving && cachedDirection == DirectionalPadEnum.NONE && controller.getDirectionalPad() != DirectionalPadEnum.NONE) {
            isMoving = true;
            lastMovement = System.currentTimeMillis();
            cachedDirection = controller.getDirectionalPad();
            primaryDirection = controller.getDirectionalPad();
        } else {
            if (controller.getDirectionalPad() == DirectionalPadEnum.NONE) {
                cachedDirection = controller.getDirectionalPad();
                isMoving = false;
            } else {
                if (cachedDirection != controller.getDirectionalPad() && !DirectionalPadEnum.isDiagnal(controller.getDirectionalPad())) {
                    primaryDirection = controller.getDirectionalPad();
                    isMoving = false;
                }
            }
        }
        if (isMoving && System.currentTimeMillis() >= lastMovement + (long) (movementDelay * 1000) && !(controller.getTrigger() < -0.9)) {
            switch (cachedDirection) {
                case NORTH:
                    if (player.getController().getButtonA()) {
                        moveRelative(0, 1);
                    } else {
                        advancedMoveRelative(0,1,true,true,true,true);
                    }
                    break;
                case SOUTH:
                    if (player.getController().getButtonA()) {
                        moveRelative(0, -1);
                    } else {
                        advancedMoveRelative(0,-1,true,true,true,true);
                    }
                    break;
                case EAST:
                    if (player.getController().getButtonA()) {
                        moveRelative(-1, 0);
                    } else {
                        advancedMoveRelative(-1,0,true,true,true,true);
                    }
                    break;
                case WEST:
                    if (player.getController().getButtonA()) {
                        moveRelative(1, 0);
                    } else {
                        advancedMoveRelative(1,0,true,true,true,true);
                    }
                    break;
                case NONE:
                    break;
            }
            isMoving = false;
        }
    }





    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 1) {
            int id = 0;
            switch (primaryDirection) {
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

            if (isMoving) {
                int cycle = 250;
                if (System.currentTimeMillis() % cycle < cycle / 4 * 1) {
                    gc.drawImage(TestRenderHelper.resample(sprites.get(id + 0), 2), (int) (x * World.getUnitRatio() + 0.5), (int) (y * World.getUnitRatio() + 0.5) - (World.getUnitRatio() * 2 - sprites.get(0).getHeight()));
                } else {
                    if (System.currentTimeMillis() % cycle < cycle / 4 * 2) {
                        gc.drawImage(TestRenderHelper.resample(sprites.get(id + 1), 2), (int) (x * World.getUnitRatio() + 0.5), (int) (y * World.getUnitRatio() + 0.5) - (World.getUnitRatio() * 2 - sprites.get(0).getHeight()));

                    } else {
                        if (System.currentTimeMillis() % cycle < cycle / 4 * 3) {
                            gc.drawImage(TestRenderHelper.resample(sprites.get(id + 0), 2), (int) (x * World.getUnitRatio() + 0.5), (int) (y * World.getUnitRatio() + 0.5) - (World.getUnitRatio() * 2 - sprites.get(0).getHeight()));

                        } else {
                            gc.drawImage(TestRenderHelper.resample(sprites.get(id + 2), 2), (int) (x * World.getUnitRatio() + 0.5), (int) (y * World.getUnitRatio() + 0.5) - (World.getUnitRatio() * 2 - sprites.get(0).getHeight()));

                        }
                    }
                }
            } else {
                gc.drawImage(TestRenderHelper.resample(sprites.get(id + 0), 2), (int) (x * World.getUnitRatio() + 0.5), (int) (y * World.getUnitRatio() + 0.5) - (World.getUnitRatio() * 2 - sprites.get(0).getHeight()));
            }
        }
    }

    @Override
    public void takeDamage(DamageType damageType, int damageAmount) {
        currentHealth -= damageAmount;
        if(damageAmount <= 0)
        {
            isDead = true;
        }
    }

    @Override
    public void takeDamage(EntityBase causer, DamageType damageType, int damageAmount) {
        takeDamage(damageType,damageAmount);
    }


}
