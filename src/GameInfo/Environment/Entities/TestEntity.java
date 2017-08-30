package GameInfo.Environment.Entities;

import GameInfo.Environment.World;
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
    public void renderEntity(Canvas canvas, GraphicsContext gc, int x, int y) {
        gc.setFill(Color.PINK);
        gc.fillRect(x * World.getUnitRatio(), y * World.getUnitRatio(), World.getUnitRatio(), World.getUnitRatio());


    }


}
