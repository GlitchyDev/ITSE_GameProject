package GameInfo.Environment.Entities.Pathfinding;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.World;

import java.util.ArrayList;

/**
 * The purpose of this class is
 * - Pathfind
 */
public class PathfindingMap {
    private ArrayList<PathfindingNode> openList = new ArrayList<>();
    private ArrayList<PathfindingNode> closedList = new ArrayList<>();
    private ArrayList<PathfindingNode> path = new ArrayList<>();

    public ArrayList<Position> findPathWalkableNonDiagnal(World world, int xStart, int yStart, int xTarget, int yTarget) {
        PathfindingNode primaryNode = new PathfindingNode(null,xStart,yStart,0,xTarget,yTarget);

        proccessNode(world,primaryNode,xTarget,yTarget);




        return null;

    }

    public void proccessNode(World world, PathfindingNode node, int xTarget, int yTarget)
    {
        openList.add(node);
        openList.addAll(getPaths(world,node,node.getX(),node.getY(),xTarget,yTarget));
        openList.remove(node);
        closedList.add(node);
    }


    public ArrayList<PathfindingNode> getPaths(World world, PathfindingNode parentNode, int x, int y, int targetX, int targetY)
    {
        ArrayList<PathfindingNode> paths = new ArrayList<>();

        if(!world.isEntityAtPos(x+1,y) && BlockTypeEnum.isWalkable(world.getBlockFromCords(x+1,y).getBlockType()))
        {
            paths.add(new PathfindingNode(parentNode, x+1,y,10,targetX,targetY));
        }
        if(!world.isEntityAtPos(x-1,y) && BlockTypeEnum.isWalkable(world.getBlockFromCords(x-1,y).getBlockType()))
        {
            paths.add(new PathfindingNode(parentNode, x-1,y,10,targetX,targetY));
        }
        if(!world.isEntityAtPos(x,y+1) && BlockTypeEnum.isWalkable(world.getBlockFromCords(x,y+1).getBlockType()))
        {
            paths.add(new PathfindingNode(parentNode, x,y+1,10,targetX,targetY));
        }
        if(!world.isEntityAtPos(x,y-1) && BlockTypeEnum.isWalkable(world.getBlockFromCords(x,y-1).getBlockType()))
        {
            paths.add(new PathfindingNode(parentNode, x,y-1,10,targetX,targetY));
        }


        return paths;
    }


}
