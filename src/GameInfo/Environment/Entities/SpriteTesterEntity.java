package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import RenderingHelpers.RadiantLightProducer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class SpriteTesterEntity extends EntityBase {

    public SpriteTesterEntity(World world, GlobalGameData globalGameData, int x, int y) {
        super(world, globalGameData, x, y);
    }

    @Override
    public void tickEntity() {
        RadiantLightProducer.produceLight(world,x,y,10);

    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 1) {
            double passedTime = (System.currentTimeMillis() - creationTime) / 1000.0;


            if (passedTime % 4 <= 1) {
                drawSpriteAtXY(globalGameData.getSprite("Pro_Front_Light"), gc, x, y, 1.5, (World.getScaledUpSquareSize() - globalGameData.getSprite("Pro_Front").getHeight() - World.getScaledUpSquareSize() / 2));
            } else {
                if (passedTime % 4 <= 2) {
                    drawSpriteAtXY(globalGameData.getSprite("Pro_Left_Light"), gc, x, y, 1.5, (World.getScaledUpSquareSize() - globalGameData.getSprite("Pro_Front").getHeight() - World.getScaledUpSquareSize() / 2));
                } else {
                    if (passedTime % 4 <= 3) {
                        drawSpriteAtXY(globalGameData.getSprite("Pro_Back_Light"), gc, x, y, 1.5, (World.getScaledUpSquareSize() - globalGameData.getSprite("Pro_Front").getHeight() - World.getScaledUpSquareSize() / 2));
                    } else {
                        drawSpriteAtXY(globalGameData.getSprite("Pro_Right_Light"), gc, x, y, 1.5, (World.getScaledUpSquareSize() - globalGameData.getSprite("Pro_Front_Really").getHeight() - World.getScaledUpSquareSize() / 2));
                    }
                }
            }
        }


    }
}
