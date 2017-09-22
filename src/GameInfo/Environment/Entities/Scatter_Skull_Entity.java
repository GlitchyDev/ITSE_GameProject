package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sample.TestRenderHelper;


/**
 * THe purpose of this entity is to
 * - Be the first enemy in the game
 * - Serve as a NonPathfinding Enemy/Hazard
 */
public class Scatter_Skull_Entity extends DamageableEntityBase {
    private Image head_sprite;
    private Image spine_sprite;
    private double headraise;


    public Scatter_Skull_Entity(World world, GlobalGameData globalGameData, int x, int y) {
        super(world, globalGameData, x, y);
        head_sprite = TestRenderHelper.resample(globalGameData.getSprite("Skull"),2);
        spine_sprite = TestRenderHelper.resample(globalGameData.getSprite("Spine_Part"),2);
        headraise = 0;

    }

    @Override
    public void tickEntity() {
        if(headraise >= -60) {
            headraise -= 0.5;

        }



    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 2) {
            double wobble = Math.sin(Math.PI/1000.0 * (System.currentTimeMillis() - creationTime)) * 10;
            gc.drawImage(head_sprite, (int) (World.getScaledUpSquareSize() * x + 0.5 + 4 + wobble), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1 + headraise));
            gc.setStroke(Color.RED);
            //gc.strokeLine(0,0,(int)(x*World.getScaledUpSquareSize() + 0.5 + World.getScaledUpSquareSize()/2),(int)(y*World.getScaledUpSquareSize() + 0.5 + World.getScaledUpSquareSize()/2));

            if(headraise < -10)
            {
                wobble = Math.sin(Math.PI/1000.0 * (System.currentTimeMillis() - creationTime - 500)) * 10;
                wobble = 0;
                gc.drawImage(spine_sprite, (int) (World.getScaledUpSquareSize() * x + 0.5 + 10 + wobble), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1 + headraise + 10));
            }

        }
    }


    @Override
    public boolean takeDamage(DamageType damageType, int damageAmount) {
        return true;
    }

    @Override
    public boolean takeDamage(EntityBase causer, DamageType damageType, int damageAmount) {
        return true;
    }
}
