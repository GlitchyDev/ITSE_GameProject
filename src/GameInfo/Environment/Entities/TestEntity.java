package GameInfo.Environment.Entities;

import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by Robert on 8/28/2017.
 */
public class TestEntity extends EntityBase {

    public TestEntity(int x, int y) {
        super(x, y);
    }

    @Override
    public void tickEntity(GlobalGameData globalGameData, World world) {

    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if (renderLayer == 0) {
            gc.setFill(Color.PINK);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x * World.getUnitRatio(), y * World.getUnitRatio(), World.getUnitRatio(), World.getUnitRatio());
            gc.fillRect(x * World.getUnitRatio() + 1, y * World.getUnitRatio() + 1, World.getUnitRatio() - 2, World.getUnitRatio() - 2);
        }

    }




}
