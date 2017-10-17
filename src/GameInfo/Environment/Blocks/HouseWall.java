package GameInfo.Environment.Blocks;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class HouseWall extends BlockBase {
    private Image sprite;

    public HouseWall(GlobalGameData globalGameData)
    {
        sprite = globalGameData.getSprite("House_Wall");
        blockType = BlockTypeEnum.HOUSE_WALL;
    }

    @Override
    public void tickBlock(World world) {

    }

    @Override
    public void renderBlock(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 1)
        {
            drawSpriteAtXY(sprite,gc,x,y,0,-sprite.getHeight() + World.getScaledUpSquareSize(),true);


            recalculateLight();
        }
    }

    @Override
    public boolean checkCollision(World world, EntityBase entity) {
        return false;
    }

    @Override
    public void enterBlock(EntityBase entity) {

    }

    @Override
    public void exitBlock(EntityBase entity) {

    }
}
