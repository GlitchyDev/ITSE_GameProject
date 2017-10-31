package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.Entities.Enums.HauntedSkullStateEnum;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import HardwareAdaptors.DirectionalEnum;
import Pathfinding.LineOfSightHelper;
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
    private HauntedSkullStateEnum currentState;
    private long stateStartTime;
    private DamageableEntityBase currentTarget;
    private DirectionalEnum currentDirection;

    private boolean wasPlayer = false;

    private final double activateStateLength = 0.3;
    private ArrayList<Position> currentPath;


    public Haunted_Skull_Entity(World world, GlobalGameData globalGameData, int x, int y, boolean wasPlayer) {
        super(world, globalGameData, x, y);
        entityType = EntityType.HAUNTED_SKULL;

        stateStartTime = System.currentTimeMillis();
        currentState = HauntedSkullStateEnum.INACTIVE;
        world.clearArea(x,y,5);

        currentTarget = null;
        this.wasPlayer = wasPlayer;

        currentDirection = DirectionalEnum.randomDirection(globalGameData);
        currentPath = new ArrayList<>();
    }

    @Override
    public void tickEntity() {
        if(currentTarget != null && currentTarget.isDead())
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
                        if(entity.getEntityType() == EntityType.PLAYER && entity.isDamageable() && !((DamageableEntityBase)entity).isDead())
                        {
                            if(target == null) {
                                target = entity;
                            }
                            else
                            {
                                if(globalGameData.getRandom().nextInt(3) == 0)
                                {
                                    target = entity;
                                }
                            }
                        }
                    }
                    if(target != null)
                    {
                        currentTarget = (DamageableEntityBase) target;

                        currentState = HauntedSkullStateEnum.ACTIVATE;
                        stateStartTime = System.currentTimeMillis();
                        globalGameData.playSound("AlertSFX",false,0.2);
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
                if(currentPath.size() > 1)
                {
                    DirectionalEnum nextSpotDirection = DirectionalEnum.determineDirection(x,y, currentPath.get(1).getX() ,currentPath.get(1).getY());
                    currentDirection = nextSpotDirection;
                }
                else
                {
                    currentDirection = playerDirection;
                }


                if (System.currentTimeMillis() > stateStartTime + 1000) {
                    if(distanceFromEntity(currentTarget) > 1) {
                        currentPath = PathfindingHelper.findPathNonDiagnal(world, x, y, currentTarget.getX(), currentTarget.getY());
                        advancedMoveRelative(currentPath.get(0).getX() - x, currentPath.get(0).getY() - y, true, true, true, true);
                        stateStartTime = System.currentTimeMillis();
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

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        boolean answer = LineOfSightHelper.lineOfSight(world,this.x,this.y,10, currentDirection);
        gc.setGlobalAlpha(1.0);
        gc.setFill(Color.BLUE);
        TextRenderHelper.drawText(50,30,"Can see " + answer,gc,globalGameData);

        if(renderLayer == 1) {
            switch(currentDirection) {

                case NORTH:
                    if(!wasPlayer) {
                        drawSpriteAtXY(globalGameData.getSprite("Skull_Backwards"), gc, x, y + 1, 5, 3, true);
                    }
                    else
                    {
                        drawSpriteAtXY(globalGameData.getSprite("P_Skull_Backwards"), gc, x, y + 1, 5, 3, true);

                    }
                    break;
                case EAST:
                    if(!wasPlayer) {
                        drawSpriteAtXY(globalGameData.getSprite("Skull_Right"), gc, x, y + 1, 5, 3, true);
                    }
                    else
                    {
                        drawSpriteAtXY(globalGameData.getSprite("P_Skull_Right"), gc, x, y + 1, 5, 3, true);

                    }                break;
                case SOUTH:
                    if(!wasPlayer) {
                        drawSpriteAtXY(globalGameData.getSprite("Skull_Forward"), gc, x, y + 1, 5, 3, true);
                    }
                    else
                    {
                        drawSpriteAtXY(globalGameData.getSprite("P_Skull_Forward"), gc, x, y + 1, 5, 3, true);

                    }                break;
                case WEST:
                    if(!wasPlayer) {
                        drawSpriteAtXY(globalGameData.getSprite("Skull_Left"), gc, x, y + 1, 5, 3, true);
                    }
                    else
                    {
                        drawSpriteAtXY(globalGameData.getSprite("P_Skull_Left"), gc, x, y + 1, 5, 3, true);

                    }                break;
            }

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
}

