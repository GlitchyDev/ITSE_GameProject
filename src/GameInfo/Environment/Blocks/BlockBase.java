package GameInfo.Environment.Blocks;

import GameInfo.Environment.Entities.EntityBase;
import GameInfo.Environment.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

/**
 * Created by Robert on 8/27/2017.
 *
 * The purpose of this class is
 * - Provide underlying flow and logic of all Block objects in the game
 */
public abstract class BlockBase {
    protected BlockTypeEnum blockType;

    public BlockBase()
    {
        blockType = BlockTypeEnum.TEST_FLOOR;
    }

    public abstract void renderBlock(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer);


    /**
     * @param entity Entity who is attempting to enter the space
     * @return If the entity is allowed to move to this space
     */
    public abstract boolean checkAvailability(World world, EntityBase entity);

    public abstract void enterBlock(EntityBase entity);

    public abstract void exitBlock(EntityBase entity);


    public BlockTypeEnum getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockTypeEnum blockType) {
        this.blockType = blockType;
    }
}
