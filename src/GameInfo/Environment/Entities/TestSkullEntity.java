package GameInfo.Environment.Entities;

import GameInfo.Environment.Blocks.DebugBlock;
import GameInfo.Environment.Blocks.PathfindingDebugBlock;
import GameInfo.Environment.Entities.AbstractClasses.DamageableEntityBase;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.DamageType;
import GameInfo.Environment.Entities.Pathfinding.PathfindingMap;
import GameInfo.Environment.Entities.Pathfinding.PathfindingNode;
import GameInfo.Environment.Entities.Pathfinding.Position;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameInfo.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sample.TestRenderHelper;

import java.util.ArrayList;

public class TestSkullEntity extends DamageableEntityBase {
    private Image sprite;
    private Pro_Player targetPlayer;
    private long lastUpdate;
    private ArrayList<Position> cachePositionList;
    private int cacheNum;


    public TestSkullEntity(World world, GlobalGameData globalGameData, int x, int y) {
        super(world, globalGameData, x, y);
        sprite = TestRenderHelper.resample(globalGameData.getSprite("Skull"),2);
        cachePositionList = new ArrayList<>();
        cacheNum = 0;
    }

    @Override
    public void takeDamage(DamageType damageType, int damageAmount) {

    }

    @Override
    public void takeDamage(EntityBase causer, DamageType damageType, int damageAmount) {

    }

    @Override
    public void tickEntity() {
        if(targetPlayer == null) {
            targetPlayer = (Pro_Player) globalGameData.getConnectedPlayers().get(0).getPlayerCharacter();
            lastUpdate = System.currentTimeMillis();
        }
        else {
            if (System.currentTimeMillis() > lastUpdate + 500) {
                lastUpdate = System.currentTimeMillis();

                if(cachePositionList.size() == 0 || cacheNum >= 5)
                {
                    ArrayList<Position> positionList = PathfindingMap.findPathNonDiagnal(world,x,y,targetPlayer.getX(),targetPlayer.getY());
                    cachePositionList.clear();

                    for(int i = 0; i < positionList.size(); i++)
                    {
                        if(i <= 5)
                        {
                            cachePositionList.add(positionList.get(i));
                        }
                    }
                    cacheNum = 0;
                }

                if(cachePositionList.size() != 0) {
                    System.out.println("Moving Relative " + (cachePositionList.get(0).getX() - x) + " " + (cachePositionList.get(0).getY() - y));
                    advancedMoveRelative(cachePositionList.get(0).getX() - x, cachePositionList.get(0).getY() - y,true,true,true,true);
                    cacheNum++;
                    cachePositionList.remove(cachePositionList.get(0));
                }





            }
        }



    }

    @Override
    public void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 0) {
            gc.drawImage(sprite, (int) (World.getScaledUpSquareSize() * x + 0.5 + 4), (int) (World.getScaledUpSquareSize() * y + 0.5 + 1));
        }
    }
}
