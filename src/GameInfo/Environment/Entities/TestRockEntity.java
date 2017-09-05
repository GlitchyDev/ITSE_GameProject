package GameInfo.Environment.Entities;

import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class TestRockEntity extends EntityBase {
    private Image sprite;
    public TestRockEntity(int x, int y, Image image) {
        super(x, y);
        sprite = image;
    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, int x, int y, int renderLayer) {
        if(renderLayer == 1)
        {
            gc.setGlobalAlpha(1.0);
            int imageWidth = (int) ((sprite.getWidth() * World.getUnitRatio())/10);
            int imageHeight = (int) ((sprite.getHeight() * World.getUnitRatio())/10);
            int imageBufferHeight = (World.getUnitRatio() - imageHeight);
            int imageBufferWidth = (World.getUnitRatio() - imageWidth) / 2;
            gc.drawImage(sprite,x * World.getUnitRatio() + imageBufferHeight, y * World.getUnitRatio() + imageBufferWidth,imageWidth,imageHeight);
        }
    }
}
