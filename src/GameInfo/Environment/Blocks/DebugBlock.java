package GameInfo.Environment.Blocks;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class DebugBlock extends BlockBase {
    private Color c = Color.BLUE;

    public DebugBlock()
    {
        this.blockType = BlockTypeEnum.DEBUG_BLOCk;
    }

    @Override
    public void renderBlock(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 0) {
            gc.setFill(c);
            gc.fillRect((int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5), World.getUnitRatio(), World.getUnitRatio());
        }
    }

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
