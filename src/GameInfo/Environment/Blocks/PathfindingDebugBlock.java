package GameInfo.Environment.Blocks;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Viewport;
import Pathfinding.PathfindingNode;
import GameInfo.Environment.World;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PathfindingDebugBlock extends BlockBase {
    private PathfindingNode node;
    private Color color;

    public PathfindingDebugBlock(PathfindingNode node)
    {
        this.node = node;
        blockType = BlockTypeEnum.PATHFINDING_DEBUG;
        color = Color.GRAY;
    }

    @Override
    public void tickBlock(World world)
    {

    }

    @Override
    public void renderBlock(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 0) {
            gc.setFill(color);
            drawRectangleAtXY(gc,x,y,0,0,World.getScaledUpSquareSize(),World.getScaledUpSquareSize());
            //gc.fillRect((int)(x * World.getScaledUpSquareSize() + 0.5 + Viewport.widthBuffer), (int)(y * World.getScaledUpSquareSize() + 0.5 + Viewport.heightBuffer), World.getScaledUpSquareSize(), World.getScaledUpSquareSize());
        }


        if(renderLayer == 4) {
            gc.setFill(Color.GREEN);
            gc.fillText("F: " + node.getF(), (int) (x * World.getScaledUpSquareSize() + 0.5), (int) (y * World.getScaledUpSquareSize() + 0.5));
            gc.fillText("G: " + node.getG(), (int) (x * World.getScaledUpSquareSize() + 0.5), (int) (y * World.getScaledUpSquareSize() + 0.5) + 10);
            gc.fillText("H: " + node.getH(), (int) (x * World.getScaledUpSquareSize() + 0.5), (int) (y * World.getScaledUpSquareSize() + 0.5) + 20);

            recalculateLight();
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
        this.color = color;
    }
}
