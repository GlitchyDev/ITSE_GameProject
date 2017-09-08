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
    private EntityBase entity;

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
    public void renderBlock(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 0) {
            if (blockType == BlockTypeEnum.TEST_WALL) {
                gc.setFill(Color.GOLD);
            }
            if (blockType == BlockTypeEnum.TEST_FLOOR) {
                gc.setFill(Color.RED);
            }
            gc.fillRect((int)(x * World.getUnitRatio()), (int)(y * World.getUnitRatio()), World.getUnitRatio(), World.getUnitRatio());
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
