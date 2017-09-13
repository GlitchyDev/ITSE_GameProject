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
            gc.strokeRect(x * World.getUnitRatio(), y * World.getUnitRatio(), World.getUnitRatio(), World.getUnitRatio());
            gc.fillRect(x * World.getUnitRatio() + 3, y * World.getUnitRatio() + 3, World.getUnitRatio() - 6, World.getUnitRatio() - 6);
        }

    }




}
