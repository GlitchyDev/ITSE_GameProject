package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import sample.TestRenderHelper;

public class TestSkullEntity extends DamageableEntityBase {
    private Image sprite;

    public TestSkullEntity(World world, GlobalGameData globalGameData, int x, int y) {
        super(world, globalGameData, x, y);
        sprite = TestRenderHelper.resample(globalGameData.getSprite("Skull"),2);
    }

    @Override
    public void takeDamage(DamageType damageType, int damageAmount) {

    }

    @Override
    public void takeDamage(EntityBase causer, DamageType damageType, int damageAmount) {

    }

    @Override
    public void tickEntity() {

    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 0) {
            gc.drawImage(sprite, (int) (World.getScaledUpSquareSize() * x + 0.5 + 4), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1));
        }
    }
}
