package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Robert on 8/28/2017.
 */
public class TestEntity extends EntityBase {

    public TestEntity(World world, GlobalGameData globalGameData, int x, int y) {
        super(world, globalGameData, x, y);
    }

    @Override
    public void tickEntity() {

    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if (renderLayer == 0) {
            gc.setFill(Color.PINK);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x * World.getScaledUpSquareSize(), y * World.getScaledUpSquareSize(), World.getScaledUpSquareSize(), World.getScaledUpSquareSize());
            gc.fillRect(x * World.getScaledUpSquareSize() + 3, y * World.getScaledUpSquareSize() + 3, World.getScaledUpSquareSize() - 6, World.getScaledUpSquareSize() - 6);
        }

    }




}
