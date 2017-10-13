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
    private final double holdDownTime = 0.2;
    private final double moveTime = 1.0;



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
                if(System.currentTimeMillis() > stateStartTime + holdDownTime)
                {
                    entityState = ProPlayerEnum.MOVING;
                    stateStartTime = System.currentTimeMillis();
                }
                break;
            case MOVING:
                if(System.currentTimeMillis() > stateStartTime + moveTime)
                {
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
                if(direction != DirectionalPadEnum.NONE && cachedDirection == DirectionalPadEnum.NONE)
                {
                    entityState = ProPlayerEnum.ATTEMPTING_MOVE;
                    stateStartTime = System.currentTimeMillis();
                    cachedDirection = direction;
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
