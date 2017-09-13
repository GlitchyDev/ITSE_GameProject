package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameInfo.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.DirectionalPadEnum;
import sample.TestRenderHelper;
import sample.XBoxController;

import java.util.ArrayList;

public class Pro_Player extends DamageableEntityBase {
    private Player player;
    private XBoxController controller;
    private ArrayList<Image> sprites;

    private DirectionalPadEnum cachedDirection;
    private DirectionalPadEnum primaryDirection;


    // This all controls the "Hold Down and move" logic
    private boolean isHoldingDown;
    private long holdDownStartTime;
    private float holdDownCylcleLength;
    private int holdDownCycleCount;


    public Pro_Player(World world, GlobalGameData globalGameData, Player player, int x, int y) {
        super(world, globalGameData, x, y);
        this.player = player;
        this.controller = player.getController();

        entityType = EntityType.PLAYER;
        currentHealth = 5;
        maxHealth = 5;

        cachedDirection = DirectionalPadEnum.NONE;
        primaryDirection = DirectionalPadEnum.SOUTH;
        isHoldingDown = false;
        holdDownStartTime = 0;
        holdDownCylcleLength = 0.4f;
        holdDownCycleCount = 0;

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

        if (cachedDirection != controller.getDirectionalPad())
        {
            if(controller.getDirectionalPad() != DirectionalPadEnum.NONE)
            {
                primaryDirection = controller.getDirectionalPad();
                holdDownStartTime = System.currentTimeMillis();
                isHoldingDown = true;
                holdDownCycleCount = 0;
            }
            else
            {
                isHoldingDown = false;
            }
            cachedDirection = controller.getDirectionalPad();
        }
        else
        {
            if(isHoldingDown)
            {
                if( (long)(holdDownCycleCount * holdDownCylcleLength) <= System.currentTimeMillis() - holdDownStartTime - (long)(holdDownCylcleLength * holdDownCycleCount * 1000))
                {
                    switch (cachedDirection) {
                        case NORTH:
                            moveRelative(0, 1);
                            holdDownCycleCount++;
                            break;
                        case SOUTH:
                            moveRelative(0, -1);
                            holdDownCycleCount++;
                            break;
                        case EAST:
                            moveRelative(-1, 0);
                            holdDownCycleCount++;
                            break;
                        case WEST:
                            moveRelative( 1, 0);
                            holdDownCycleCount++;
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
        int id = 0;
        switch(primaryDirection)
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

        if(isHoldingDown)
        {
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
        }
        else
        {
            gc.drawImage(TestRenderHelper.resample(sprites.get(id + 0),2),(int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5) - (World.getUnitRatio() * 2 - sprites.get(0).getHeight()) );
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
