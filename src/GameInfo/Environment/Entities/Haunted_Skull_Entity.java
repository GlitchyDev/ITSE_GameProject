package GameInfo.Environment.Entities;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.Entities.Enums.HauntedSkullStateEnum;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import HardwareAdaptors.DirectionalEnum;
import Pathfinding.PathfindingHelper;
import Pathfinding.Position;
import RenderingHelpers.ImageRenderHelper;
import RenderingHelpers.TextRenderHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;


/**
 * THe purpose of this entity is to
 * - Be the first enemy in the game
 * - Serve as a NonPathfinding Enemy/Hazard
 *
 * RENAME at earliest convinience
 */
public class Haunted_Skull_Entity extends DamageableEntityBase {
    // State Manager
    private HauntedSkullStateEnum currentState;
    private long stateStartTime;

    // Targeting and current direction
    private DamageableEntityBase currentTarget;
    private DirectionalEnum currentDirection;

    // If it was previously a player
    private boolean wasPlayer = false;

    private long lastSeen;
    private final double averageRememberTime = 3.0;

    private final double moveTime = 0.63;

    // The current Path
    private ArrayList<Position> currentPath;

    // How long it takes to activate
    private final double activateStateLength = 0.3;
    // The Max it should be able to see in a direction
    private final int maxPathFindingRange = 5;
    private final int maxVisionRange = 5;


    private String lostReason = "";


    public Haunted_Skull_Entity(World world, GlobalGameData globalGameData, int x, int y, boolean wasPlayer) {
        super(world, globalGameData, x, y);
        entityType = EntityType.HAUNTED_SKULL;

        stateStartTime = System.currentTimeMillis();
        currentState = HauntedSkullStateEnum.INACTIVE;

        lastSeen = 0;

        currentTarget = null;
        this.wasPlayer = wasPlayer;

        currentDirection = DirectionalEnum.randomDirection(globalGameData);
        currentPath = new ArrayList<>();
    }

    @Override
    public void tickEntity() {
        if(currentTarget != null && (currentTarget.isDead()))
        {
            stateStartTime = System.currentTimeMillis();
            currentState = HauntedSkullStateEnum.INACTIVE;
            currentTarget = null;
        }
        switch(currentState)
        {
            case INACTIVE:
                //Block b = world.getBlockFromCords(x,y);
                if(globalGameData.getRandom().nextInt(30) == 0) {
                    ArrayList<EntityBase> possibleTargets = world.findEntitiesWithinRadius(this.x,this.y,15);
                    EntityBase target = null;
                    for(EntityBase entity: possibleTargets)
                    {
                        if(entity != null && entity.getEntityType() == EntityType.PLAYER && !((DamageableEntityBase)(entity)).isDead())
                        {
                            if(canSeeEntity(entity)) {
                                target = entity;
                            }
                        }
                    }
                    if(target != null)
                    {
                        currentTarget = (DamageableEntityBase) target;

                        currentState = HauntedSkullStateEnum.ACTIVATE;
                        stateStartTime = System.currentTimeMillis();
                        globalGameData.playSound("menuSelectItem",false,0.15);
                    }

                }

                break;
            case ACTIVATE:
                if(System.currentTimeMillis() > stateStartTime + activateStateLength*1000)
                {
                    currentState = HauntedSkullStateEnum.NAVIGATE_TO_TARGET;
                    stateStartTime = System.currentTimeMillis();
                }
                DirectionalEnum targetDirection = DirectionalEnum.determineDirection(x,y, currentTarget.getX(),currentTarget.getY());
                if(currentDirection != targetDirection)
                {
                    currentDirection = targetDirection;
                }
                //System.out.println(DirectionalEnum.determineDirection(x,y, currentTarget.getX(),currentTarget.getY()));
                // After X time passes, enter "Erupt, dealing X damage to all nearby
                break;
            case NAVIGATE_TO_TARGET:
                DirectionalEnum playerDirection = DirectionalEnum.determineDirection(x,y, currentTarget.getX(),currentTarget.getY());
                if(currentPath != null && currentPath.size() > 1)
                {
                    DirectionalEnum nextSpotDirection = DirectionalEnum.determineDirection(x,y, currentPath.get(1).getX() ,currentPath.get(1).getY());
                    currentDirection = nextSpotDirection;
                }
                else
                {
                    currentDirection = playerDirection;
                }


                if (System.currentTimeMillis() > stateStartTime + moveTime*1000.0) {
                    if(distanceFromEntity(currentTarget) > 1) {
                        currentPath = PathfindingHelper.findPathNonDiagnal(world, x, y, currentTarget.getX(), currentTarget.getY(),500);
                        if(currentPath != null && canSeeEntity(currentTarget)) {
                            advancedMoveRelative(currentPath.get(0).getX() - x, currentPath.get(0).getY() - y, true, true, true, true);
                            stateStartTime = System.currentTimeMillis();
                        }
                        else
                        {
                            currentState = HauntedSkullStateEnum.INACTIVE;
                            stateStartTime = System.currentTimeMillis();
                            currentTarget = null;
                        }
                    }
                    else
                    {
                        currentTarget.takeDamage(this,DamageType.BLUNT,1);
                        stateStartTime = System.currentTimeMillis();
                    }
                }

                // No requirements
                break;
        }



    }

    public boolean canSeeEntity(EntityBase entity)
    {
        for(BlockBase[] row: world.getAllBlocksBetweenPoints(x+2,y+2,x-2,y-2))
        {
            for(BlockBase block: row)
            {
                if(block != null) {
                    if (block.isCurrentlyLit() && BlockTypeEnum.isWalkable(block.getBlockType())) {
                        lastSeen = System.currentTimeMillis();
                        lostReason = "LIGHT AROUND!";
                        return true;

                    }
                }
            }
        }

        if(System.currentTimeMillis() < lastSeen + averageRememberTime * 1000.0)
        {
            lostReason = "REMEMBER";
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        gc.setGlobalAlpha(1.0);
        gc.setFill(Color.BLUE);
        TextRenderHelper.drawText(50,30,"Can see " + canSeeEntity(currentTarget) + " " + lostReason,gc,globalGameData);

        String modifier = "";
        if(wasPlayer)
        {
            modifier += "P_";
        }
        if(currentState == HauntedSkullStateEnum.NAVIGATE_TO_TARGET)
        {
            modifier += "Active_";
        }
        if(renderLayer == 1) {
            if(world.getBlockFromCords(this.x,this.y).getPreviousLightLevel() != 0) {
                switch (currentDirection) {

                    case NORTH:
                        drawSpriteAtXY(globalGameData.getSprite(modifier + "Skull_Backwards"), gc, x, y + 1, 5, 3, true);
                        break;
                    case EAST:
                        drawSpriteAtXY(globalGameData.getSprite(modifier + "Skull_Right"), gc, x, y + 1, 5, 3, true);
                        break;
                    case SOUTH:
                        drawSpriteAtXY(globalGameData.getSprite(modifier + "Skull_Forward"), gc, x, y + 1, 5, 3, true);
                        break;
                    case WEST:
                        drawSpriteAtXY(globalGameData.getSprite(modifier + "Skull_Left"), gc, x, y + 1, 5, 3, true);
                        break;
                }
            }

            if(currentState == HauntedSkullStateEnum.NAVIGATE_TO_TARGET ) {
                switch (currentDirection) {
                case NORTH:
                    drawSpriteAtXY(globalGameData.getSprite("Active_Backwards"), gc, x, y + 1, 5, 3, false);
                    break;
                case EAST:
                    drawSpriteAtXY(globalGameData.getSprite("Active_Right"), gc, x, y + 1, 5, 3, false);
                    break;
                case SOUTH:
                    drawSpriteAtXY(globalGameData.getSprite("Active_Forward"), gc, x, y + 1, 5, 3, false);
                    break;
                case WEST:
                    drawSpriteAtXY(globalGameData.getSprite("Active_Left"), gc, x, y + 1, 5, 3, false);
                    break;

            }            }
            switch(currentState)
            {
                case INACTIVE:
                    break;
                case ACTIVATE:
                    double passedTime = (System.currentTimeMillis() - (stateStartTime + activateStateLength))/1000.0;
                    double progress = passedTime/activateStateLength;
                    double alpha = Math.sin(passedTime/activateStateLength * Math.PI);
                    int offset = (int)(progress * World.getScaledUpSquareSize()/2);

                    gc.setGlobalAlpha(alpha);

                    drawSpriteAtXY(globalGameData.getSprite("Alert"),gc,x,y, ImageRenderHelper.findCenterXMod(globalGameData.getSprite("Alert")),-World.getScaledUpSquareSize()/4 + -offset,false);
                    gc.setGlobalAlpha(1.0);
                    break;
                default:
                    break;
            }




        }





    }

    @Override
    public boolean takeDamage(DamageType damageType, int damageAmount) {
        return true;
    }

    @Override
    public boolean takeDamage(EntityBase causer, DamageType damageType, int damageAmount) {
        return true;
    }

    public void setCurrentDirection(DirectionalEnum direction)
    {
        this.currentDirection = direction;
    }
}

