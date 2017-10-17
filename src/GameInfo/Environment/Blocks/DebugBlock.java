package GameInfo.Environment.Blocks;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import GameInfo.Viewport;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 * This Class is designed to
 * - Demonstrate adding custom Block Types to the game!
 */
public class DebugBlock extends BlockBase {
    private Color c;

    // Every Block in its constructor needs to specify its BlockType along with any other logic it needs done
    public DebugBlock()
    {
        this.blockType = BlockTypeEnum.DEBUG_BLOCK;
        c = Color.GRAY;
    }

    @Override
    public void tickBlock(World world)
    {

    }

    @Override
    public void renderBlock(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        // This is specifying this renders on the absolute lowest rendering level
        if(renderLayer == 0) {
            gc.setFill(c);
            drawRectangleAtXY(gc,x,y,0,0,World.getScaledUpSquareSize(),World.getScaledUpSquareSize());
            recalculateLight();
        }
    }

    // We always want players/enemies to be able to enter this block, therefore its marked as true
    @Override
    public boolean checkCollision(World world, EntityBase entity) {
        return true;
    }

    @Override
    public void enterBlock(EntityBase entity) {

    }

    @Override
    public void exitBlock(EntityBase entity) {

    }

    public void setColor(Color color)
    {
        c = color;
    }


}
