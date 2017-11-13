package GameInfo.Environment.Entities;

import GameInfo.Environment.Blocks.DebugBlock;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.Entities.Enums.ProPlayerBodyState;
import GameInfo.Environment.Entities.Enums.ProPlayerEmotion;
import GameInfo.Environment.Entities.Pro_Player;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import HardwareAdaptors.DirectionalEnum;
import RenderingHelpers.ImageRenderHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * This class aims to
 * - Implement a mandatory reason to turn on the players light
 */
public class Alsi_Entity extends EntityBase {
    // Target Helpers
    private Pro_Player target;
    private int distance = 12;
    private long lastApproach = 0;
    private long lastRelocate = 0;
    private final double approachTime = 1.0;
    private final double relocateTime = 0.35;

    // Render Helpers
    private final int staticXWiggle = 12;
    private final int staticYWiggle = 6;

    public Alsi_Entity(World world, GlobalGameData globalGameData, int x, int y, Pro_Player target) {
        super(world, globalGameData, x, y);
        entityType = EntityType.ALSI;
        this.target = target;
        world.getBlockFromCords(x,y).exitBlock(this);
    }

    @Override
    public void tickEntity() {
        if(lastRelocate == 0)
        {
            world.getBlockFromCords(x,y).exitBlock(this);
        }

        if(!target.isDead()) {
            // Decrease Distance & Attack
            if (System.currentTimeMillis() > lastApproach + approachTime * 1000.0) {
                if (distance == 0) {
                    target.takeDamage(this, DamageType.DEBUG, 100);
                } else {
                    if (target.getBodyState() == ProPlayerBodyState.LIGHT_ON) {
                        distance = 15;
                    } else {
                        distance--;
                    }
                }
                lastApproach = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() > lastRelocate + relocateTime * 1000.0) {
                double angle = Math.random() * Math.PI * 2;
                int xOffset = (int) (Math.cos(angle) * distance);
                int yOffset = (int) (Math.sin(angle) * distance);
                moveAbsolute(target.getX() + xOffset, target.getY() + yOffset);
                System.out.println("Moved " + xOffset + " " + yOffset + " Distance " + distance);
                lastRelocate = System.currentTimeMillis();
            }
        }


    }


    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(!target.isDead())
        {
            gc.setGlobalAlpha(0.02);
            for(int i = 0; i < 10; i++)
            {
                int xOffset = (int)(Math.random() * staticXWiggle);
                xOffset = xOffset - staticXWiggle/2;
                int yOffset = (int)(Math.random() * staticYWiggle);
                yOffset = yOffset - staticYWiggle/2;
                drawSpriteAtXY(globalGameData.getSprite("Alsi_Front"), gc, x, y, ImageRenderHelper.findCenterXMod(globalGameData.getSprite("Alsi_Front")) + xOffset, yOffset, false);
            }
        }
        else
        {
            if(target.getCauserOfDeath() == EntityType.ALSI) {
                moveAbsolute(target.getX(), target.getY());
                gc.setGlobalAlpha(0.02);
                double progress = (System.currentTimeMillis() - target.getDeathStartTime()) / 1000.0;
                for (int i = 0; i < 10; i++) {
                    int xOffset = (int) (Math.random() * staticXWiggle * (1 + progress));
                    xOffset = (int) (xOffset - staticXWiggle / 2 * (1 + progress));
                    int yOffset = (int) (Math.random() * staticYWiggle * (1 + progress));
                    yOffset = (int) (yOffset - staticYWiggle / 2 * (1 + progress));
                    drawSpriteAtXY(globalGameData.getSprite("Alsi_Front"), gc, x, y, ImageRenderHelper.findCenterXMod(globalGameData.getSprite("Alsi_Front")) + xOffset, yOffset, false);
                }

                if (progress > 3.0 && progress < 6.5) {
                    if (target.getPrimaryDirection() == DirectionalEnum.NORTH) {
                        if (globalGameData.getRandom().nextBoolean()) {
                            target.setPrimaryDirection(DirectionalEnum.EAST);

                        } else {
                            target.setPrimaryDirection(DirectionalEnum.WEST);
                        }
                    }
                    target.setEmotion(ProPlayerEmotion.ALSI_Close_Eyes_1);
                } else {
                    if (progress > 6.5 && progress < 9.5) {
                        if (target.getPrimaryDirection() == DirectionalEnum.EAST || target.getPrimaryDirection() == DirectionalEnum.WEST) {
                            target.setPrimaryDirection(DirectionalEnum.SOUTH);
                        }
                        target.setEmotion(ProPlayerEmotion.ALSI_Close_Eyes_2);
                    } else {
                        if (progress > 9.5) {
                            target.setEmotion(ProPlayerEmotion.ALSI_EVIL);
                        }
                    }
                }
            }
            else {
                if (target.getCauserOfDeath() == EntityType.HAUNTED_SKULL) {
                    double progress = (System.currentTimeMillis() - target.getDeathStartTime()) / 1000.0;
                    gc.setGlobalAlpha(0.02 * (1 - progress));
                    for (int i = 0; i < 10; i++) {
                        int xOffset = (int) (Math.random() * staticXWiggle * (1 + progress));
                        xOffset = xOffset - (int) (staticXWiggle * (1 + progress)) / 2;
                        int yOffset = (int) (Math.random() * staticYWiggle * (1 + progress));
                        yOffset = yOffset - (int) (staticYWiggle * (1 + progress)) / 2;
                        drawSpriteAtXY(globalGameData.getSprite("Alsi_Front"), gc, x, y, ImageRenderHelper.findCenterXMod(globalGameData.getSprite("Alsi_Front")) + xOffset, yOffset, false);
                    }

                    if(progress < 5)
                    {
                        double dark = (progress) / 5.0;
                        gc.setGlobalAlpha(dark);
                        gc.setFill(Color.BLACK);
                        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
                    }
                    else
                    {
                        double dark = 1 - (progress - 5.0) / 5.0;
                        gc.setGlobalAlpha(dark);
                        gc.setFill(Color.BLACK);
                        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
                    }
                }
                else
                {
                    //
                }
            }
        }
        gc.setGlobalAlpha(1);
    }
}

/*
 @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        gc.setGlobalAlpha(0.1);
        if(!target.isDead()) {
            drawSpriteAtXY(globalGameData.getSprite("Alsi_Front"), gc, x, y + 1, ImageRenderHelper.findCenterXMod(globalGameData.getSprite("Alsi_Front")), 0, false);
        }

        gc.setGlobalAlpha(0.02);
        double progress = 1 + (System.currentTimeMillis() - target.getDeathStartTime())/1000.0;
        if(target.getDeathStartTime() == 0)
        {
            progress = 1;
        }
        for(int i = 0; i < 10 * progress; i++)
        {
            int xOffset = 0;
            int yOffset = 0;
            if(!target.isDead() && target.getCauserOfDeath() == EntityType.ALSI) {
                xOffset = (int) (Math.random() * 12) - 6;
                yOffset = (int) (Math.random() * 4) - 2;
            }
            else
            {
                gc.setGlobalAlpha(progress / 20.0);
                System.out.println("progress " + progress);
                xOffset = (int) ((Math.random() * (1000.0/10.0*progress)));
                xOffset = xOffset - xOffset/2;
                yOffset = (int) ((Math.random() * (1000.0/10.0*progress)));
                yOffset = yOffset - yOffset/2;

            }
            drawSpriteAtXY(globalGameData.getSprite("Alsi_Front"),gc,x,y+1, ImageRenderHelper.findCenterXMod(globalGameData.getSprite("Alsi_Front")) + xOffset,yOffset,false);


        }
        gc.setGlobalAlpha(1.0);

    }
 */