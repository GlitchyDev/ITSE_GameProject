package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.Entities.Enums.ProPlayerEnum;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameInfo.Player;
import RenderingHelpers.RadiantLightProducer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import HardwareAdaptors.DirectionalPadEnum;
import HardwareAdaptors.XBoxController;

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

    // The Previous Frame's direction
    private DirectionalPadEnum cachedDirection;
    // The last Nondiagnal direction we have recieved, used to keep the sprite pointing in the right direction
    private DirectionalPadEnum primaryDirection;

    private ProPlayerEnum entityState;
    private long stateStartTime;

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

        entityState = ProPlayerEnum.IDLE;
        stateStartTime = System.currentTimeMillis();

    }

    /*

    public Image determineSprite()
    {
        // This will help determine time specific animations to states
        double statePassedTime = (System.currentTimeMillis() - stateStartTime)/1000.0;



        // This will help determine what "Primary" Sprite is needed    z
        String directionIntro = "";
        switch(primaryDirection)
        {
            case NORTH:
                directionIntro = "Pro_Back";
                break;
            case SOUTH:
                directionIntro = "Pro_Front";
                break;
            case EAST:
                directionIntro = "Pro_Right";
                break;
            case WEST:
                directionIntro = "Pro_Left";
                break;
            case NONE:
                directionIntro = "Pro_Front";
                break;
            default:
                directionIntro = primaryDirection.toString();
                System.out.println(directionIntro);
        }


        switch(entityState)
        {

            case IDLE:
                if(statePassedTime < 0.2)
                {
                    return globalGameData.getSprite(directionIntro + "_Moving_1");
                }
                if(statePassedTime < 0.4)
                {
                    return globalGameData.getSprite(directionIntro + "_Moving_2");
                }
                if(statePassedTime < 0.6) {
                    return globalGameData.getSprite(directionIntro + "_Moving_1");
                }
                return globalGameData.getSprite(directionIntro + "_Moving_3");
            case WALKING:
                switch(primaryDirection)
                {
                    case NORTH:
                        break;
                    case SOUTH:
                        break;
                    case EAST:
                        break;
                    case WEST:
                        break;
                }
                break;
            case DAMAGED:
                switch(primaryDirection)
                {
                    case NORTH:
                        break;
                    case SOUTH:
                        break;
                    case EAST:
                        break;
                    case WEST:
                        break;
                }
                break;
            case PULLING_OUT_TORCH:
                switch(primaryDirection)
                {
                    case NORTH:
                        break;
                    case SOUTH:
                        break;
                    case EAST:
                        break;
                    case WEST:
                        break;
                }
                break;
        }
        return globalGameData.getSprite("Rock_Test");
    }
    */

    @Override
    public void tickEntity() {
        /*
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
            if(!DirectionalPadEnum.isDiagnal(controller.getDirectionalPad())) {
                primaryDirection = controller.getDirectionalPad();
            }
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

*/
        //RadiantLightProducer.produceLight(world,x,y,10);
    }





    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 1) {

            /*
            Image sprite = determineSprite();
            if(sprite == null)
            {
                System.out.println("BEEP");
            }
            drawSpriteAtXY(determineSprite(),gc,x,y,1.5,(World.getScaledUpSquareSize()-sprite.getHeight()-World.getScaledUpSquareSize()/2));
            */
        }
    }

    @Override
    public boolean takeDamage(DamageType damageType, int damageAmount) {
        currentHealth -= damageAmount;
        if(damageAmount <= 0)
        {
            isDead = true;
        }
        return true;
    }

    @Override
    public boolean takeDamage(EntityBase causer, DamageType damageType, int damageAmount) {
        takeDamage(damageType,damageAmount);
        return true;
    }


}
