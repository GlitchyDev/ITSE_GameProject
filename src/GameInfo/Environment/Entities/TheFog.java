package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class TheFog extends EntityBase {
    private int currentImage = 0;
    private Pro_Player currentTarget;
    private long lastMoveTime = 0;
    private final double moveTimeLength = 2.0;


    public TheFog(World world, GlobalGameData globalGameData, int x, int y, Pro_Player currentTarget) {
        super(world, globalGameData, x, y);
        this.currentTarget = currentTarget;
        entityType = EntityType.THE_FOG;


        double angle = Math.random() * Math.PI * 2;
        int xOffset = (int) (Math.cos(angle) * 25);
        int yOffset = (int) (Math.sin(angle) * 25);
        moveAbsolute(currentTarget.getX() + xOffset, currentTarget.getY() + yOffset);
    }

    @Override
    public void tickEntity() {
        if(!currentTarget.isDead()) {
            if (distanceFromEntity(currentTarget) > 20) {
                double angle = Math.random() * Math.PI * 2;
                int xOffset = (int) (Math.cos(angle) * 19);
                int yOffset = (int) (Math.sin(angle) * 19);
                moveAbsolute(currentTarget.getX() + xOffset, currentTarget.getY() + yOffset);
            }
            if (System.currentTimeMillis() > lastMoveTime + moveTimeLength * 1000.0)
            {
                if(distanceFromEntity(currentTarget) == 0)
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

    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if (renderLayer == 2) {
            int random = (int) (Math.random() * 20);
            if (random == 0) {
                int newImage = (int) (Math.random() * 4);
                switch (newImage) {
                    case 0:
                        currentImage = 0;
                        break;
                    case 1:
                        currentImage = 1;
                        break;
                    case 2:
                        currentImage = 2;
                        break;
                    case 3:
                        currentImage = 3;
                        break;
                }
            }
            switch (currentImage) {
                case 0:
                    drawSpriteAtXY(globalGameData.getSprite("TestEdgy"), gc, x, y + 1, 0, 0, false);
                    break;
                case 1:
                    drawSpriteAtXY(globalGameData.getSprite("TestEdgy2"), gc, x, y + 1, 0, 0, false);
                    break;
                case 2:
                    drawSpriteAtXY(globalGameData.getSprite("TestEdgy3"), gc, x, y + 1, 0, 0, false);
                    break;
                case 3:
                    drawSpriteAtXY(globalGameData.getSprite("TestEdgy4"), gc, x, y + 1, 0, 0, false);
                    break;
            }
        }
        if(renderLayer == 3) {
            if (distanceFromEntity(currentTarget) <= 7 && !currentTarget.isDead()) {
                double opacity = 1.0 - (1.0 / 7.0) * distanceFromEntity(currentTarget);
                //System.out.println(opacity);
                gc.setGlobalAlpha(0.02 * opacity);
                int imageRandom = (int) (Math.random() * 4) + 1;
                gc.drawImage(globalGameData.getSprite("Static_" + imageRandom), 0, 0);
                gc.setGlobalAlpha(1.0);

                gc.setGlobalAlpha(0.02 * opacity);
                imageRandom = (int) (Math.random() * 4) + 1;
                gc.drawImage(globalGameData.getSprite("Static_" + imageRandom), 0, 0);
                gc.setGlobalAlpha(1.0);

                gc.setGlobalAlpha(0.02 * opacity);
                imageRandom = (int) (Math.random() * 4) + 1;
                gc.drawImage(globalGameData.getSprite("Static_" + imageRandom), 0, 0);
                gc.setGlobalAlpha(1.0);
            }
            else
            {
                double progress = (System.currentTimeMillis()-currentTarget.getDeathStartTime())/currentTarget.getDeathTimeAnimationLength();
            }
        }
    }

    public float getAngleToPlayer() {
        float angle = (float) Math.toDegrees(Math.atan2(currentTarget.getY() - y, currentTarget.getX() - x));
        if(angle < 0){
            angle += 360;
        }
        return angle;
    }
}
