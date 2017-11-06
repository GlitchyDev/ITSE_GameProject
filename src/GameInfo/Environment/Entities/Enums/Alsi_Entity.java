package GameInfo.Environment.Entities.Enums;

import GameInfo.Environment.Blocks.DebugBlock;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Pro_Player;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Alsi_Entity extends EntityBase {
    private Pro_Player target;
    private int distance = 15;
    private long lastMove = 0;
    private final double movementTime = 1.0;
    public Alsi_Entity(World world, GlobalGameData globalGameData, int x, int y, Pro_Player target) {
        super(world, globalGameData, x, y);
        this.target = target;
    }

    @Override
    public void tickEntity() {
        if(lastMove == 0)
        {
            lastMove = System.currentTimeMillis();
        }

        if(System.currentTimeMillis() > lastMove + movementTime * 1000.0) {
            distance--;
            if (distance == 0) {
                //advancedMoveAbsolute(target.getX(), target.getY(), true, true, true, true);
                target.takeDamage(DamageType.DEBUG, 10);
            } else {
                if(!target.isDead()) {
                    double angle = Math.random() * Math.PI * 2;
                    int xOffset = (int) (Math.cos(angle) * distance);
                    int yOffset = (int) (Math.sin(angle) * distance);
                    moveAbsolute(target.getX() + xOffset,target.getY() + yOffset);
                    System.out.println("Moved " + xOffset + " " + yOffset + " Distance " + distance);


                }
            }
            lastMove = System.currentTimeMillis();
        }


    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        drawSpriteAtXY(globalGameData.getSprite("Alsi_Front"),gc,x,y+1,0,0,false);
    }
}
