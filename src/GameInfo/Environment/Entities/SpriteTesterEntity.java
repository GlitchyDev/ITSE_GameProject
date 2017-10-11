package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import Pathfinding.LineOfSightHelper;
import Pathfinding.Position;
import RenderingHelpers.PlayerSkinCreator;
import RenderingHelpers.RadiantLightProducer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class SpriteTesterEntity extends EntityBase {
    private ArrayList<Image> sprites;

    public SpriteTesterEntity(World world, GlobalGameData globalGameData, int x, int y) {
        super(world, globalGameData, x, y);
        sprites = PlayerSkinCreator.createFront("P1","P1","P1",globalGameData);
    }

    @Override
    public void tickEntity() {
        RadiantLightProducer.produceLight(world, x, y, 10);
    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 1) {
            double passedTime = (System.currentTimeMillis() - creationTime) / 1000.0;
            int imageNum = (int)passedTime % sprites.size();
            //Image image = sprites.get(imageNum);

            drawSpriteAtXY(sprites.get(imageNum),gc,x,y,1.5,(World.getScaledUpSquareSize()-70-World.getScaledUpSquareSize()/2));


        }

    }
}
