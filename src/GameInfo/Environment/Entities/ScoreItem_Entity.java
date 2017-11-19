package GameInfo.Environment.Entities;

import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import RenderingHelpers.ImageRenderHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.PlayerScoreManager;

public class ScoreItem_Entity extends EntityBase {
    private Image sprite;
    private long startTime = -1;
    private final double disapearLength = 0.2;
    private boolean isCollected = false;
    public ScoreItem_Entity(World world, GlobalGameData globalGameData, int x, int y) {
        super(world, globalGameData, x, y);
        if((int)(Math.random() * 2) == 0)
        {
            sprite = globalGameData.getSprite("ScoreItem_Pawn");
        }
        else
        {
            sprite = globalGameData.getSprite("ScoreItem_Rock");

        }
        world.setBlockFromCords(x,y,new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR));
    }

    @Override
    public void tickEntity() {
        if(!isCollected) {
            for (EntityBase entity : world.getAllEntitiesBetweenPoints(x + 1, y + 1, x - 1, y - 1)) {
                if (entity.getEntityType() == EntityType.PLAYER) {
                    PlayerScoreManager.addToScore(100);
                    if(((Pro_Player)entity).getHealth() < 5) {
                        ((Pro_Player) entity).takeHealing(DamageType.DEBUG,1);
                    }
                    isCollected = true;
                    startTime = System.currentTimeMillis();
                }
            }
        }
        else
        {
            if(System.currentTimeMillis() > startTime + disapearLength*1000.0)
            {
                world.getChunkFromCordXY(x, y).removeEntity(this);
                world.getBlockFromCords(x, y).exitBlock(this);
            }
        }
    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 1) {
            double progress = 1.0 - ((System.currentTimeMillis()-startTime)/1000.0)/disapearLength;
            if(startTime == -1)
            {
                progress = 1;
            }
            gc.setGlobalAlpha(progress);
            double xOffset = ImageRenderHelper.findCenterXMod(sprite);
            double yOffset = ImageRenderHelper.findCenterYMod(sprite);
            drawSpriteAtXY(sprite, gc, x, y + 1, xOffset, yOffset, true);
            gc.setGlobalAlpha(1.0);
        }

    }
}
