package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.*;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameInfo.Player;
import RenderingHelpers.PlayerSkinCreator;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import HardwareAdaptors.DirectionalPadEnum;
import HardwareAdaptors.XBoxController;

/**
 * This class aims to
 * - Be the basic Player Character, Protagonist ( Shortened to Pro )
 * - Implement Controls for the Player
 */
public class Pro_Player extends DamageableEntityBase {
    private Player player;
    private XBoxController controller;

    private DirectionalPadEnum cachedDirection;
    private DirectionalPadEnum primaryDirection;

    private ProPlayerStateEnum entityState;
    private long stateStartTime;

    private int lightLevel = -1;
    private final double holdDownTime = 0.13;
    private final double moveTime = 0.1;

    private ProPlayerEmotion emotion = ProPlayerEmotion.NONE;
    private ProPlayerBodyState bodyState = ProPlayerBodyState.NONE;
    private String headType = "P1";
    private String bodyType = "P1";
    private String legType = "P1";


    public Pro_Player(World world, GlobalGameData globalGameData, Player player, int x, int y) {
        super(world, globalGameData, x, y);
        this.player = player;
        this.controller = player.getController();
        entityType = EntityType.PLAYER;
        currentHealth = 5;
        maxHealth = 5;
        cachedDirection = DirectionalPadEnum.NONE;
        primaryDirection = DirectionalPadEnum.SOUTH;
        entityState = ProPlayerStateEnum.IDLE;
        stateStartTime = System.currentTimeMillis();

        System.out.println("Player is calling  this");
        PlayerSkinCreator.generateSkin(player,globalGameData);
        String[] args = player.getSkinID().split(",");
        headType = args[0];
        bodyType = args[1];
        legType = args[2];

    }

    @Override
    public void tickEntity() {
        buttonInput();
        switch(entityState)
        {
            case IDLE:
                break;
            case ATTEMPTING_MOVE:
                if(System.currentTimeMillis() > stateStartTime + holdDownTime*1000)
                {
                    entityState = ProPlayerStateEnum.MOVING;
                    stateStartTime = System.currentTimeMillis();
                }
                break;
            case MOVING:
                if(System.currentTimeMillis() > stateStartTime + moveTime*1000)
                {
                    switch(cachedDirection)
                    {
                        case NORTH:
                            moveRelative(0,1);
                            break;
                        case SOUTH:
                            moveRelative(0,-1);
                            break;
                        case EAST:
                            moveRelative(-1,0);
                            break;
                        case WEST:
                            moveRelative(1,0);
                            break;
                    }
                    entityState = ProPlayerStateEnum.IDLE;
                    stateStartTime = System.currentTimeMillis();
                }
                break;
            case DAMAGED:
                break;

        }

    }

    public void buttonInput()
    {
        controller.poll();
        DirectionalPadEnum direction = controller.getDirectionalPad();
        switch(entityState)
        {
            case IDLE:
                if(direction != DirectionalPadEnum.NONE && cachedDirection == DirectionalPadEnum.NONE && !DirectionalPadEnum.isDiagnal(direction))
                {

                    entityState = ProPlayerStateEnum.ATTEMPTING_MOVE;
                    stateStartTime = System.currentTimeMillis();
                    cachedDirection = direction;
                    primaryDirection = direction;
                }
                else
                {
                    if(direction != cachedDirection)
                    {
                        cachedDirection = direction;
                        if(direction != DirectionalPadEnum.NONE)
                        {
                            primaryDirection = direction;
                        }

                    }
                }
                break;
            case ATTEMPTING_MOVE:
                if(cachedDirection != direction)
                {
                    entityState = ProPlayerStateEnum.IDLE;
                    stateStartTime = System.currentTimeMillis();
                    cachedDirection = direction;
                }
                break;
            case MOVING:
                break;
            case DAMAGED: break;

        }
    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 2) {
            double xOffset = 0;
            double yOffset = 0;

            String direction = "Front";
            if(entityState == ProPlayerStateEnum.MOVING)
            {
                switch(primaryDirection)
                {
                    case NORTH:
                        yOffset = -((System.currentTimeMillis() - stateStartTime)/1000.0)/moveTime * (World.getScaledUpSquareSize());
                        break;
                    case SOUTH:
                        yOffset = ((System.currentTimeMillis() - stateStartTime)/1000.0)/moveTime * (World.getScaledUpSquareSize());
                        break;
                    case EAST:
                        xOffset = ((System.currentTimeMillis() - stateStartTime)/1000.0)/moveTime * World.getScaledUpSquareSize();
                        break;
                    case WEST:
                        xOffset = -((System.currentTimeMillis() - stateStartTime)/1000.0)/moveTime * World.getScaledUpSquareSize();
                        break;
                }
            }
            switch(primaryDirection)
            {
                case NORTH:
                    direction = "Back";
                    break;
                case SOUTH:
                    direction = "Front";
                    break;
                case EAST:
                    direction = "Right";
                    break;
                case WEST:
                    direction = "Left";
                    break;
            }
            String head = headType + "_" + direction + "_Head";
            String body = bodyType + "_" + direction + "_Body";
            String leg = legType + "_" + direction + "_";
            if(entityState == ProPlayerStateEnum.ATTEMPTING_MOVE || entityState == ProPlayerStateEnum.MOVING) {
                switch((int)(System.currentTimeMillis() / (moveTime/5) % 5 ))
                    {
                    case 0:
                        leg += "Legs_1";
                        break;
                    case 1:
                        leg += "Legs_2";
                        break;
                    case 2:
                        leg += "Legs_1";
                        break;
                    case 3:
                        leg += "Legs_3";
                        break;
                }
            }
            else
            {
                leg += "Legs_1";
            }

            Image image = globalGameData.getSprite(player.getUuid().toString() + "|" + head + "|" + body + "|" + leg);
            gc.setGlobalAlpha(0.2);
            drawSpriteAtXY(image, gc, x, y, 1.5 + (xOffset/2.0), (World.getScaledUpSquareSize() - 70 - World.getScaledUpSquareSize() / 2) + (yOffset/2.0));
            gc.setGlobalAlpha(1.0);
            drawSpriteAtXY(image, gc, x, y, 1.5 + xOffset, (World.getScaledUpSquareSize() - 70 - World.getScaledUpSquareSize()/2 ) + yOffset);
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
