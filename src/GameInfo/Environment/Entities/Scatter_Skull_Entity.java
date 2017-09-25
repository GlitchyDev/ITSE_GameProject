package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.Entities.Enums.ScatterSkullStateEnum;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import sample.TestRenderHelper;

import java.util.ArrayList;


/**
 * THe purpose of this entity is to
 * - Be the first enemy in the game
 * - Serve as a NonPathfinding Enemy/Hazard
 *
 * RENAME at earliest convinience
 */
public class Scatter_Skull_Entity extends DamageableEntityBase {
    private Image head_sprite;
    private Image spine_sprite;

    private ScatterSkullStateEnum currentState;
    private long stateStartTime;
    private DamageableEntityBase currentTarget;
    public Scatter_Skull_Entity(World world, GlobalGameData globalGameData, int x, int y) {
        super(world, globalGameData, x, y);
        entityType = EntityType.SCATTER_SKULL;

        head_sprite = globalGameData.getSprite("Skull");
        spine_sprite = globalGameData.getSprite("Spine_Part");

        stateStartTime = System.currentTimeMillis();
        currentState = ScatterSkullStateEnum.INACTIVE;
        currentTarget = null;
    }

    @Override
    public void tickEntity() {
        switch(currentState)
        {
            case INACTIVE:
                if(globalGameData.getRandom().nextInt(120) == 0) {
                    System.out.println("Activated");
                    boolean activated = false;
                    double lowestDistance = Double.MAX_VALUE;
                    int distance = 2;
                    ArrayList<EntityBase> entities = world.getAllEntitiesBetweenPoints(x+distance,y+distance,x-distance,y-distance);
                    entities.remove(this);
                    for(EntityBase entity: entities)
                    {
                        if( lowestDistance > distanceFromEntity(entity))
                        {
                            if(entity.isDamageable()) {
                                currentTarget = (DamageableEntityBase) entity;
                                lowestDistance = distanceFromEntity(entity);
                                activated = true;
                            }

                        }
                    }
                    if(activated)
                    {
                        stateStartTime = System.currentTimeMillis();
                        currentState = ScatterSkullStateEnum.ACTIVATE;
                    }
                }
                break;
            case ACTIVATE:
                // After X time passes, enter "Erupt, dealing X damage to all nearby
                break;
            case ERUPT:
                // Deal damage to all nearby targets
                break;
            case WOBBLE:
                // Idle, search for nearby targets
                break;
            case ATTACK:
                // Throw head towards nearest target until hits wall, spawns new Scatter skull,
                // Enters new mode
                break;
            case RETREAT:
        }



    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 1) {
            switch(currentState)
            {
                case INACTIVE:
                    gc.drawImage(globalGameData.getSprite("Skull"), (int) (World.getScaledUpSquareSize() * x + 0.5 + 4), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1));
                    break;
                case ACTIVATE:
                    double percentage;
                    double timeLength = 4;
                    if((System.currentTimeMillis() - stateStartTime) / 1000.0 <= timeLength)
                    {
                        percentage = 1.0 / (timeLength * 1000.0) * (System.currentTimeMillis() - stateStartTime);
                    }
                    else
                    {
                        percentage = 1;
                    }
                    gc.drawImage(globalGameData.getSprite("Skull"), (int) (World.getScaledUpSquareSize() * x + 0.5 + 4), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1));
                    Image glitchStatic = TestRenderHelper.resample(TestRenderHelper.generateRandomStatic(15,15),2,false);
                    gc.setGlobalAlpha(0.4 * percentage);
                    gc.drawImage(TestRenderHelper.adjustImage(glitchStatic,globalGameData.getSprite("Skull_Map")),(int)(World.getScaledUpSquareSize() * x + 0.5 + TestRenderHelper.findCenterXMod(glitchStatic)), (int) (World.getScaledUpSquareSize() * y + 0.5));
                    gc.setGlobalAlpha(1.0);
                    // After X time passes, enter "Erupt, dealing X damage to all nearby
                    break;
                case ERUPT:
                    // Deal damage to all nearby targets
                    break;
                case WOBBLE:
                    // Idle, search for nearby targets
                    break;
                case ATTACK:
                    // Throw head towards nearest target until hits wall, spawns new Scatter skull,
                    // Enters new mode
                    break;
                case RETREAT:
                    break;
            }




        }
            //gc.setStroke(Color.RED);
            //gc.strokeLine(0,0,(int)(x*World.getScaledUpSquareSize() + 0.5 + World.getScaledUpSquareSize()/2),(int)(y*World.getScaledUpSquareSize() + 0.5 + World.getScaledUpSquareSize()/2));

            /*
            double wobble = 0;

            int multiplier = 20;
            for(int i = (int)(headraise / 8); i >= 0; i--)
            {
                wobble = Math.sin(Math.PI/1200.0 * (System.currentTimeMillis() - creationTime - 500 * i)) * (4 + 0.5 * ((headraise / 8) -i));
                gc.drawImage(spine_sprite, (int) (World.getScaledUpSquareSize() * x + 0.5 + 10 + wobble), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1 - headraise + 15 + 8 * i ));
            }

            wobble = Math.sin(Math.PI/1200.0 * (System.currentTimeMillis() - creationTime)) * 8;
            gc.drawImage(head_sprite, (int) (World.getScaledUpSquareSize() * x + 0.5 + 4 + wobble * 1.2), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1 - headraise));
            */


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

