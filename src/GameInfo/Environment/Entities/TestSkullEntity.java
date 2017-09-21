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
    private PathfindingNode firstNode;
    private PathfindingNode currentNode;
    private Pro_Player targetPlayer;
    private long lastUpdate;
    private boolean continueSearch;

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
        if(targetPlayer == null) {
            targetPlayer = (Pro_Player) globalGameData.getConnectedPlayers().get(0).getPlayerCharacter();
            firstNode = new PathfindingNode(null, x, y, 0, targetPlayer.getX(), targetPlayer.getY());
            currentNode = firstNode;
            PathfindingDebugBlock debug = new PathfindingDebugBlock(currentNode);
            PathfindingMap.openList.add(currentNode);
            //debug.setColor(Color.GREEN);
            lastUpdate = System.currentTimeMillis();
            world.setBlockFromCords(x,y,debug);
            continueSearch = true;
        }
        else {
            if (System.currentTimeMillis() > lastUpdate + 2000 && continueSearch) {
                currentNode.recalculateH(targetPlayer.getX(),targetPlayer.getY());
                lastUpdate = System.currentTimeMillis();

                PathfindingMap.openList.addAll(PathfindingMap.getConnectedTiles(world,currentNode,targetPlayer.getX(),targetPlayer.getY()));

                int leastF = Integer.MAX_VALUE;
                for(PathfindingNode node: PathfindingMap.openList)
                {
                    node.recalculateH(targetPlayer.getX(),targetPlayer.getY());
                    if(node.getF() < leastF)
                    {
                        currentNode = node;
                        leastF = currentNode.getF();
                    }
                }

                PathfindingMap.openList.remove(currentNode);
                PathfindingMap.closedList.add(currentNode);
                PathfindingDebugBlock b1 = new PathfindingDebugBlock(currentNode);
                world.setBlockFromCords(currentNode.getX(), currentNode.getY(), b1);

                System.out.println();
                System.out.println("Current Node " + currentNode.getX() + " " + currentNode.getY());
                System.out.println("Target Player " +  targetPlayer.getX() + " " + targetPlayer.getY());
                if(currentNode.getX() == targetPlayer.getX() && currentNode.getY() == targetPlayer.getY())
                {
                    System.out.println("You win!");
                    continueSearch = false;
                    PathfindingNode temp = currentNode;
                    while(!temp.isPrimaryNode())
                    {
                        PathfindingDebugBlock b = (PathfindingDebugBlock) world.getBlockFromCords(temp.getX(),temp.getY());
                        b.setColor(Color.BLACK);
                        world.setBlockFromCords(temp.getX(), temp.getY(), b);
                        temp = temp.getParentNode();
                    }
                    PathfindingDebugBlock b = (PathfindingDebugBlock) world.getBlockFromCords(temp.getX(),temp.getY());
                    b.setColor(Color.BLACK);
                    world.setBlockFromCords(temp.getX(), temp.getY(), b);

                    return;
                }

                for (PathfindingNode node : PathfindingMap.openList) {
                    PathfindingDebugBlock b = new PathfindingDebugBlock(node);
                    b.setColor(Color.RED);
                    world.setBlockFromCords(node.getX(), node.getY(), b);
                }
                for (PathfindingNode node : PathfindingMap.closedList) {
                    PathfindingDebugBlock b = (PathfindingDebugBlock) world.getBlockFromCords(node.getX(),node.getY());
                    b.setColor(Color.BLUE);
                    if(node == currentNode)
                    {
                        b.setColor(Color.YELLOW);
                    }
                    world.setBlockFromCords(node.getX(), node.getY(), b);
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
