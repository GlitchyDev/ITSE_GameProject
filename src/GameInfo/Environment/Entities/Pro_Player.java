package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.Entities.Enums.ProPlayerEnum;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameInfo.Player;
import RenderingHelpers.PlayerSkinCreator;
import RenderingHelpers.RadiantLightProducer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import HardwareAdaptors.DirectionalPadEnum;
import HardwareAdaptors.XBoxController;
import javafx.scene.paint.Color;

import java.util.ArrayList;

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

    private ProPlayerEnum entityState;
    private long stateStartTime;

    private int lightLevel = -1;
    private final double holdDownTime = 0.13;
    private final double moveTime = 0.1;



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

        System.out.println("Player is calling  this");
        PlayerSkinCreator.generateSkin(player,globalGameData);
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
                    entityState = ProPlayerEnum.MOVING;
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
                    entityState = ProPlayerEnum.IDLE;
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

                    entityState = ProPlayerEnum.ATTEMPTING_MOVE;
                    stateStartTime = System.currentTimeMillis();
                    cachedDirection = direction;
                }
                else
                {
                    if(direction != cachedDirection)
                    {
                        cachedDirection = direction;
                    }
                }
                break;
            case ATTEMPTING_MOVE:
                if(cachedDirection != direction)
                {
                    entityState = ProPlayerEnum.IDLE;
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
        double xOffset = 0;
        double yOffset = 0;
        if(entityState == ProPlayerEnum.MOVING)
        {
            switch(cachedDirection)
            {
                case NORTH:
                    yOffset = -((System.currentTimeMillis() - stateStartTime)/1000.0)/moveTime * World.getScaledUpSquareSize();
                    break;
                case SOUTH:
                    yOffset = ((System.currentTimeMillis() - stateStartTime)/1000.0)/moveTime * World.getScaledUpSquareSize();
                    break;
                case EAST:
                    xOffset = ((System.currentTimeMillis() - stateStartTime)/1000.0)/moveTime * World.getScaledUpSquareSize();
                    break;
                case WEST:
                    xOffset = -((System.currentTimeMillis() - stateStartTime)/1000.0)/moveTime * World.getScaledUpSquareSize();
                    break;
            }
        }

        if(renderLayer == 1) {
            drawSpriteAtXY(globalGameData.getSprite("Pro_Test_Darker"), gc, x, y, 1.5 + xOffset, (World.getScaledUpSquareSize() - 70 - World.getScaledUpSquareSize() / 2) + yOffset);
        }
        //drawRectangleAtXY(gc,x,y,(int)xOffset,(int)yOffset,World.getScaledUpSquareSize(),World.getScaledUpSquareSize());
        //gc.setFill(Color.WHITE);
        //System.out.println(entityState.toString());

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
