package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import RenderingHelpers.ImageRenderHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.ScoreManager;

public class ScoreItem extends EntityBase {
    private Image sprite;
    private int spriteID;
    public ScoreItem(World world, GlobalGameData globalGameData, int x, int y) {
        super(world, globalGameData, x, y);
        spriteID = globalGameData.getRandom().nextInt(17) + 1;
        sprite = globalGameData.getSprite("ScoreItem (" + spriteID + ")");
    }

    @Override
    public void tickEntity() {
        for(EntityBase entity: world.getAllEntitiesBetweenPoints(x+1,y+1,x-1,y-1))
        {
            if(entity.getEntityType()== EntityType.PLAYER)
            {
                
            }
        }
    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        switch(spriteID)
        {

        }
        double xOffset = ImageRenderHelper.findCenterXMod(sprite);
        double yOffset = ImageRenderHelper.findCenterYMod(sprite);
        drawSpriteAtXY(sprite,gc,x,y,xOffset,yOffset,true);
    }
}
