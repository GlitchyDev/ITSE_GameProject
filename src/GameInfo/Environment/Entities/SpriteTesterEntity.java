package GameInfo.Environment.Entities;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameInfo.Player;
import Pathfinding.LineOfSightHelper;
import Pathfinding.Position;
import RenderingHelpers.PlayerSkinCreator;
import RenderingHelpers.RadiantLightProducer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.UUID;

public class SpriteTesterEntity extends EntityBase {
    private Player p;

    public SpriteTesterEntity(World world, GlobalGameData globalGameData, int x, int y) {
        super(world, globalGameData, x, y);
        p = globalGameData.getConnectedPlayers().get(0);
        PlayerSkinCreator.generateSkin(p,globalGameData);
       // sprites = PlayerSkinCreator.allSprites(globalGameData);
    }

    @Override
    public void tickEntity() {
        RadiantLightProducer.produceLight(world, x, y, 3);
    }

    public int legCycle(int i)
    {
        switch(i)
        {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 1;
            case 3:
                return 3;
            default:
                return 1;
        }
    }
    public String headCycle(int i)
    {
        switch(i)
        {
            case 2:
                return "Head_Look_Left";
            case 8:
                return "Head_Really";
            case 9:
                return "Head_Eyes_Closed";
            case 10:
                return "Head_Really";
            case 18:
                return "Head_Look_Right";
            default:
                return "Head";
        }
    }
    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 1) {
            double passedTime = (System.currentTimeMillis() - creationTime) / 1000.0;
            String direction = "Front";
            String head = "P1_" + direction + "_" + headCycle((int)(passedTime%20+1));
            String body = "P1_" + direction + "_Body";
            String legs = "P1_" + direction + "_Legs_" + legCycle((int)(passedTime%4+1));

            Image image = globalGameData.getSprite(p.getUuid().toString() + "|" + head + "|" + body + "|" + legs);
            //System.out.println(p.getUuid().toString() + "|" + head + "|" + body + "|" + legs);

            drawSpriteAtXY(image,gc,x,y,1.5,(World.getScaledUpSquareSize()-70-World.getScaledUpSquareSize()/2),true);

        }

    }
}
