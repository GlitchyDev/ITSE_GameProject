package GameInfo.Environment.Blocks;

import GameInfo.Environment.Entities.EntityBase;
import GameInfo.Environment.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Created by Robert on 8/28/2017.
 */
public class WallFloorBlock extends BlockBase {


    public WallFloorBlock()
    {
        Random random = new Random();
        if(random.nextInt(3) == 1)
        {
            blockType = BlockTypeEnum.TEST_WALL;
        }
        else
        {
            blockType = BlockTypeEnum.TEST_FLOOR;
        }
    }
    @Override
    public void renderBlock(Canvas canvas, GraphicsContext gc, int x, int y, int renderLayer) {
        if(renderLayer == 0) {
            if (blockType == BlockTypeEnum.TEST_WALL) {
                gc.setFill(Color.GOLD);
            }
            if (blockType == BlockTypeEnum.TEST_FLOOR) {
                gc.setFill(Color.RED);
            }
            gc.fillRect(x * World.getUnitRatio(), y * World.getUnitRatio(), World.getUnitRatio(), World.getUnitRatio());
        }
    }

    @Override
    public boolean checkAvailability(EntityBase entity) {
        return blockType == BlockTypeEnum.TEST_FLOOR;
    }
}
