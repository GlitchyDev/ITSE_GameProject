package GameInfo.Environment.Entities;

import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.TestRenderHelper;

public class TestRockEntity extends EntityBase {
    private Image sprite;
    public TestRockEntity(int x, int y, Image image) {
        super(x, y);
        sprite = TestRenderHelper.resample(image,10);
    }

    @Override
    public void tickEntity(GlobalGameData globalGameData, World world) {

    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, int x, int y, int renderLayer) {
        if(renderLayer == 1) {
            gc.setGlobalAlpha(1.0);


            //gc.drawImage(TestRenderHelper.resample(sprite,10),x * World.getUnitRatio(), y * World.getUnitRatio());

            gc.drawImage(sprite,x * World.getUnitRatio(), y * World.getUnitRatio());
        }
    }
}
