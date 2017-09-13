package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.TestRenderHelper;

public class TestRockEntity extends EntityBase {
    private Image sprite;
    public TestRockEntity(World world, GlobalGameData globalGameData, int x, int y, Image image) {
        super(world, globalGameData, x, y);
        sprite = TestRenderHelper.resample(image,2);
    }

    @Override
    public void tickEntity() {
    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 1) {
            gc.setGlobalAlpha(1.0);


            //gc.drawImage(TestRenderHelper.resample(sprite,10),x * World.getUnitRatio(), y * World.getUnitRatio());

            gc.drawImage(sprite,x * World.getUnitRatio(), y * World.getUnitRatio());
        }
    }
}
