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
                if(globalGameData.getRandom().nextInt(50) == 0) {
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
                if(System.currentTimeMillis() >= stateStartTime + 2000)
                {
                    stateStartTime = System.currentTimeMillis();
                    currentState = ScatterSkullStateEnum.ERUPT;
                }
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
                    gc.drawImage(globalGameData.getSprite("Skull"), (int) (World.getScaledUpSquareSize() * x + 0.5 +  TestRenderHelper.findCenterXMod(globalGameData.getSprite("Skull"))), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1));
                    break;
                case ACTIVATE:
                    double activatePercentage;
                    double activationLength = 2;
                    if((System.currentTimeMillis() - stateStartTime) / 1000.0 <= activationLength)
                    {
                        activatePercentage = 1.0 / (activationLength * 1000.0) * (System.currentTimeMillis() - stateStartTime);
                    }
                    else
                    {
                        activatePercentage = 1;
                    }
                    // gc.drawImage(globalGameData.getSprite("Skull"), (int) (World.getScaledUpSquareSize() * x + 0.5 +  TestRenderHelper.findCenterXMod(globalGameData.getSprite("Skull"))), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1));
                    gc.drawImage(globalGameData.getSprite("Skull_Activate_" + (int)(1+(activatePercentage*2))), (int) (World.getScaledUpSquareSize() * x + 0.5 +  TestRenderHelper.findCenterXMod(globalGameData.getSprite("Skull"))), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1));
                    //Image activationGlitchStatic = TestRenderHelper.resample(TestRenderHelper.generateRandomStatic(15,15),2,false);
                    gc.setGlobalAlpha(0.4 * activatePercentage);
                    //gc.drawImage(TestRenderHelper.adjustImage(activationGlitchStatic,globalGameData.getSprite("Skull_Map")),(int)(World.getScaledUpSquareSize() * x + 0.5 + TestRenderHelper.findCenterXMod(activationGlitchStatic)), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1));
                    gc.setGlobalAlpha(1.0);
                    // After X time passes, enter "Erupt, dealing X damage to all nearby
                    break;
                case ERUPT:
                    double eruptPercentage;
                    double eruptLength = 1.5;
                    if((System.currentTimeMillis() - stateStartTime) / 1000.0 <= eruptLength)
                    {
                        eruptPercentage = 1.0 / (eruptLength * 1000.0) * (System.currentTimeMillis() - stateStartTime);
                    }
                    else
                    {
                        eruptPercentage = 1;
                    }

                    double yOffset = Math.sin(eruptPercentage * Math.PI/2) * 100.0;
                    double xOffset = Math.sin(eruptPercentage * Math.PI) * 10;

                    for(int i = 0; i < eruptPercentage * (100/14); i++)
                    {
                        xOffset = Math.sin(((System.currentTimeMillis() - stateStartTime)/1000.0 + 0.5*i) * Math.PI ) * 5;
                        gc.drawImage(globalGameData.getSprite("Spine_Part"), (int) (World.getScaledUpSquareSize() * x + 0.5 +  TestRenderHelper.findCenterXMod(globalGameData.getSprite("Spine_Part")) + xOffset), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1 - yOffset + 30 + i*14));
                        gc.setGlobalAlpha(0.3);
                        //Image eruptGlitchStatic = TestRenderHelper.resample(TestRenderHelper.generateRandomStatic(14,7),2,false);
                        //gc.drawImage(TestRenderHelper.adjustImage(eruptGlitchStatic,globalGameData.getSprite("Spine_Map")),(int)(World.getScaledUpSquareSize() * x + 0.5 + TestRenderHelper.findCenterXMod(eruptGlitchStatic)) + xOffset, (int) (World.getScaledUpSquareSize() *  y + 0.5 + 1 - yOffset + 30 + i*14));
                        gc.setGlobalAlpha(1.0);

                    }
                    xOffset = Math.sin(eruptPercentage * Math.PI) * 10;
                    gc.drawImage(globalGameData.getSprite("Skull_Activate_" + (int)(3+(eruptPercentage*5))), (int) (World.getScaledUpSquareSize() * x + 0.5 +  TestRenderHelper.findCenterXMod(globalGameData.getSprite("Skull")) + xOffset), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1 - yOffset));
                    //Image eruptGlitchStatic = TestRenderHelper.resample(TestRenderHelper.generateRandomStatic(19,17),2,false);
                    gc.setGlobalAlpha(0.4);
                    //gc.drawImage(TestRenderHelper.adjustImage(eruptGlitchStatic,globalGameData.getSprite("Skull_Map")),(int)(World.getScaledUpSquareSize() * x + 0.5 + TestRenderHelper.findCenterXMod(eruptGlitchStatic)) + xOffset, (int) (World.getScaledUpSquareSize() * y + 0.5 + 1 - yOffset) - 2);
                    gc.setGlobalAlpha(1.0);

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

