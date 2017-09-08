package GameInfo.Environment.Blocks;

import GameInfo.Environment.Entities.EntityBase;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sample.TestRenderHelper;

import java.util.Random;

/**
 * Created by Robert on 8/28/2017.
 */
public class WallFloorBlock extends BlockBase {
    private EntityBase entity;
    private Image sprite;
    private Image secondarySprite;

    public WallFloorBlock(GlobalGameData globalGameData)
    {
        Random random = new Random();
        if(random.nextInt(3) == 1)
        {
            blockType = BlockTypeEnum.TEST_WALL;
            sprite = TestRenderHelper.resample(globalGameData.getSprite("Test_Wall_Modified"),2);
            secondarySprite = TestRenderHelper.resample(globalGameData.getSprite("Test_Wall_Top"),2);
        }
        else
        {
            blockType = BlockTypeEnum.TEST_FLOOR;
            sprite = TestRenderHelper.resample(globalGameData.getSprite("Test_Floor"),2);

        }
    }
    @Override
    public void renderBlock(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 0)
        {
            if (blockType == BlockTypeEnum.TEST_FLOOR) {
                gc.drawImage(sprite,(int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5));
            }
        }
        if(renderLayer == 1) {
            if (blockType == BlockTypeEnum.TEST_WALL) {
                gc.drawImage(sprite,(int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5) + 15);
                gc.drawImage(secondarySprite,(int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5) - 25);


            }
        }
        if(renderLayer == 2) {
            if (entity == null) {
                //gc.setFill(Color.BLUE);
                //gc.fillText("E", x * World.getUnitRatio(), y * World.getUnitRatio());
            }
        }


    }

    @Override
    public boolean checkAvailability(World world, EntityBase entity) {

        return blockType == BlockTypeEnum.TEST_FLOOR && this.entity == null;

    }

    @Override
    public void enterBlock(EntityBase entity) {
        this.entity = entity;
    }

    @Override
    public void exitBlock(EntityBase entity) {
        this.entity = null;
    }
}
