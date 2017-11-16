package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import RenderingHelpers.ImageRenderHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class The_Fog_Entity extends EntityBase {
    private int currentImage = 0;
    private Pro_Player currentTarget;
    private long lastMoveTime = 0;
    private final double moveTimeLength = 1.8;


    public The_Fog_Entity(World world, GlobalGameData globalGameData, int x, int y, Pro_Player currentTarget) {
        super(world, globalGameData, x, y);
        this.currentTarget = currentTarget;
        entityType = EntityType.THE_FOG;


        double angle = Math.random() * Math.PI * 2;
        int xOffset = (int) (Math.cos(angle) * 25);
        int yOffset = (int) (Math.sin(angle) * 25);
        moveAbsolute(currentTarget.getX() + xOffset, currentTarget.getY() + yOffset);

        globalGameData.playSound("TheStatic",true,0.0);
        globalGameData.playSound("TheMessage",true,0.0);
    }

    @Override
    public void tickEntity() {



        final double staticDistance = 15.0;
        final double messageDistance = 9.0;
        final double staticVolume = 0.2;
        final double messageVolume = 0.25;
        if(!currentTarget.isDead()) {
            if(distanceFromEntity(currentTarget) < staticDistance)
            {
                if(distanceFromEntity(currentTarget) < messageDistance)
                {
                    double distancePercentage = 1.0 - 1.0/messageDistance*(distanceFromEntity(currentTarget));
                    globalGameData.setSoundVolume("TheMessage",messageVolume*distancePercentage);
                }
                else
                {
                    globalGameData.setSoundVolume("TheMessage",0);
                }
                double distancePercentage = 1.0 - 1.0/staticDistance*(distanceFromEntity(currentTarget));
                globalGameData.setSoundVolume("TheStatic",staticVolume*distancePercentage);
            }
            else
            {
                globalGameData.setSoundVolume("TheStatic",0);
            }
            if (distanceFromEntity(currentTarget) > 20) {
                double angle = getAngleToPlayer();
                int xOffset = (int) (Math.cos(angle) * 19);
                int yOffset = (int) (Math.sin(angle) * 19);
                moveAbsolute(currentTarget.getX() + xOffset, currentTarget.getY() + yOffset);
            }
            if (System.currentTimeMillis() > lastMoveTime + moveTimeLength * 1000.0)
            {
                if(distanceFromEntity(currentTarget) < 2)
                {
                    currentTarget.takeDamage(this, DamageType.EXPLOSIVE,1);
                }
                int mod = -(int)(Math.random() * 2);
                double angle = getAngleToPlayer();
                int xOffset = (int) (Math.cos(angle) * (distanceFromEntity(currentTarget) + mod));
                int yOffset = (int) (Math.sin(angle) * (distanceFromEntity(currentTarget) + mod));
                moveAbsolute(currentTarget.getX() + xOffset, currentTarget.getY() + yOffset);
                lastMoveTime = System.currentTimeMillis();
            }
        }
        else
        {
            if(currentTarget.getCauserOfDeath() != EntityType.THE_FOG)
            {
                globalGameData.setSoundVolume("TheMessage",globalGameData.getSoundVolume("TheMessage")-0.001);
                globalGameData.setSoundVolume("TheStatic",globalGameData.getSoundVolume("TheStatic")-0.001);
            }
        }

    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {

        double mult = 1.0;
        if(currentTarget.isDead() && currentTarget.getCauserOfDeath() != EntityType.THE_FOG)
        {
            double progress = 1.0/5.0*((System.currentTimeMillis() - currentTarget.getDeathStartTime())/1000.0);
            mult = 1.0 - progress;
        }
        if (renderLayer == 2) {
            int random = (int) (Math.random() * 10);
            if (random == 0) {
                currentImage = (int) (Math.random() * 4);
            }
            int xOffset = ImageRenderHelper.findCenterXMod(globalGameData.getSprite("Face_Static_1"));
            int yOffset = ImageRenderHelper.findCenterYMod(globalGameData.getSprite("Face_Static_1"));

            gc.setGlobalAlpha(0.8*mult);
            switch (currentImage) {
                case 0:
                    drawSpriteAtXY(globalGameData.getSprite("Face_Static_1"), gc, x, y, xOffset, yOffset, false);
                    break;
                case 1:
                    drawSpriteAtXY(globalGameData.getSprite("Face_Static_2"), gc, x, y, xOffset, yOffset, false);
                    break;
                case 2:
                    drawSpriteAtXY(globalGameData.getSprite("Face_Static_3"), gc, x, y, xOffset, yOffset, false);
                    break;
                case 3:
                    drawSpriteAtXY(globalGameData.getSprite("Face_Static_4"), gc, x, y, xOffset, yOffset, false);
                    break;
            }
            gc.setGlobalAlpha(1.0);

        }
        if(renderLayer == 3) {
            final double staticRender = 0.02;
            if (!currentTarget.isDead())
            {
                if (distanceFromEntity(currentTarget) <= 8) {
                    double opacity = 1.0 - (1.0 / 7.0) * distanceFromEntity(currentTarget);
                    gc.setGlobalAlpha(staticRender * opacity);
                    int imageRandom = (int) (Math.random() * 4) + 1;
                    gc.drawImage(globalGameData.getSprite("Static_" + imageRandom), 0, 0);

                    imageRandom = (int) (Math.random() * 4) + 1;
                    gc.drawImage(globalGameData.getSprite("Static_" + imageRandom), 0, 0);

                    imageRandom = (int) (Math.random() * 4) + 1;
                    gc.drawImage(globalGameData.getSprite("Static_" + imageRandom), 0, 0);

                    gc.setGlobalAlpha(1.0);
                }
            }
            else
            {
                if(currentTarget.getCauserOfDeath() != EntityType.THE_FOG) {
                    // Cause of death was not the fog

                }
                else
                {
                    // Cause of death was the fog
                    double progress = 1.0 / 10 + 0.93 * ((System.currentTimeMillis() - currentTarget.getDeathStartTime()) / 1000.0);
                    gc.setGlobalAlpha(staticRender + (1.0 - staticRender) * progress);
                    int imageRandom = (int) (Math.random() * 4) + 1;
                    gc.drawImage(globalGameData.getSprite("Static_" + imageRandom), 0, 0);

                    imageRandom = (int) (Math.random() * 4) + 1;
                    gc.drawImage(globalGameData.getSprite("Static_" + imageRandom), 0, 0);

                    imageRandom = (int) (Math.random() * 4) + 1;
                    gc.drawImage(globalGameData.getSprite("Static_" + imageRandom), 0, 0);

                    gc.setGlobalAlpha(1.0);
                }
            }

        }
    }



    public  float getAngleToPlayer() {
        float angle = (float) Math.toDegrees(Math.atan2(currentTarget.getY() - y, currentTarget.getX() - x));
        if(angle < 0){
            angle += 360;
        }
        return angle;
    }
}
