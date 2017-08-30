package GameInfo.Environment.Blocks;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

/**
 * Created by Robert on 8/27/2017.
 */
public abstract class BlockBase {
    protected BlockTypeEnum blockType;

    public BlockBase()
    {
        blockType = BlockTypeEnum.TEST_FLOOR;
    }

    public abstract void renderBlock(Canvas canvas, GraphicsContext gc, int x, int y);

    public BlockTypeEnum getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockTypeEnum blockType) {
        this.blockType = blockType;
    }
}
