package GameInfo.Environment.Entities.Pathfinding;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.World;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The purpose of this class is
 * - Pathfind
 */


public class PathfindingMap
{
    public static ArrayList<PathfindingNode> openList = new ArrayList<>();
    public static ArrayList<PathfindingNode> closedList = new ArrayList<>();

    public static ArrayList<Position> findPathNonDiagnal(World world, int xStart, int yStart, int xTarget, int yTarget)
    {
        System.out.println("Starting Pathfinding at " + xStart + " " + yStart);
        PathfindingNode node = proccessPathfindingNode(world,new PathfindingNode(null,xStart,yStart,0,xTarget,yTarget),xTarget,yTarget);


        ArrayList<Position> positionList = new ArrayList<>();
        PathfindingNode temp = node;
        while(!temp.isPrimaryNode())
        {
            positionList.add(new Position(temp.getX(),temp.getY()));
            temp = temp.getParentNode();
        }
        Collections.reverse(positionList);
        openList.clear();
        closedList.clear();
        return positionList;
    }

    public static PathfindingNode proccessPathfindingNode(World world, PathfindingNode node, int xTarget, int yTarget)
    {
        openList.remove(node);
        closedList.add(node);

        openList.addAll(getConnectedTiles(world,node,xTarget,yTarget));

        if(openList.size() != 0)
        {
            PathfindingNode temp = openList.get(0);
            int lowest = Integer.MAX_VALUE;
            for(PathfindingNode potentialNode: openList)
            {
                if(node.getF() < lowest)
                {
                    lowest = node.getF();
                    temp = potentialNode;
                }
            }
            if(temp.getX() == xTarget && temp.getG() == yTarget)
            {
                return temp;
            }
            else
            {
                openList.remove(temp);
                closedList.add(temp);
                return proccessPathfindingNode(world,temp,xTarget,yTarget);
            }
        }
        else
        {
            return node;
        }
    }


    public static ArrayList<PathfindingNode> getConnectedTiles(World world, PathfindingNode parentNode, int targetX, int targetY)
    {
        ArrayList<PathfindingNode> paths = new ArrayList<>();
        int x = parentNode.getX();
        int y = parentNode.getY();
        int cost = 5;
        if(BlockTypeEnum.isWalkable(world.getBlockFromCords(x+1,y).getBlockType()))
        {
            //System.out.println("Found Right");
            if(!alreadyAddedToList(x+1,y))
            paths.add(new PathfindingNode(parentNode, x+1,y,parentNode.getG() + cost, targetX,targetY));
        }
        if(BlockTypeEnum.isWalkable(world.getBlockFromCords(x-1,y).getBlockType()))
        {
           //System.out.println("Found Left");
            if(!alreadyAddedToList(x-1,y))
                paths.add(new PathfindingNode(parentNode, x-1,y,parentNode.getG() + cost,targetX,targetY));
        }
        if(BlockTypeEnum.isWalkable(world.getBlockFromCords(x,y+1).getBlockType()))
        {
            //System.out.println("Found North");
            if(!alreadyAddedToList(x,y+1))
                paths.add(new PathfindingNode(parentNode, x,y+1,parentNode.getG() + cost, targetX,targetY));
        }
        if(BlockTypeEnum.isWalkable(world.getBlockFromCords(x,y-1).getBlockType()))
        {
            //System.out.println("Found South");
            if(!alreadyAddedToList(x,y-1))
                paths.add(new PathfindingNode(parentNode, x,y-1,parentNode.getG() + cost,targetX,targetY));
        }

        return paths;
    }


    public static boolean alreadyAddedToList(int x, int y)
    {
        for(PathfindingNode node: openList)
        {
            if(x == node.getX() && y == node.getY())
            {
                return true;
            }
        }
        for(PathfindingNode node: closedList)
        {
            if(x == node.getX() && y == node.getY())
            {
                return true;
            }
        }
        return false;
    }



}
/*
public class PathfindingMap {
    private ArrayList<PathfindingNode> openList = new ArrayList<>();
    private ArrayList<PathfindingNode> closedList = new ArrayList<>();
    private ArrayList<PathfindingNode> path = new ArrayList<>();

    public ArrayList<Position> findPathWalkableNonDiagnal(World world, int xStart, int yStart, int xTarget, int yTarget) {
        PathfindingNode primaryNode = new PathfindingNode(null,xStart,yStart,0,xTarget,yTarget);


        //System.out.println("Adding Original Node");
        PathfindingNode node = proccessNode(world,primaryNode,xTarget,yTarget);

        ArrayList<Position> positions = new ArrayList<>();
        while(!node.isPrimaryNode())
        {
            positions.add(new Position(node.getX(),node.getY()));
        }
        Collections.reverse(positions);

        openList.clear();
        closedList.clear();
        return positions;






    }

    public PathfindingNode proccessNode(World world, PathfindingNode node, int xTarget, int yTarget)
    {
        //System.out.println("Begin Processing node " + node.getX() + " " + node.getY());
        openList.addAll(getPaths(world,node,node.getX(),node.getY(),xTarget,yTarget));
        closedList.add(node);
        PathfindingNode newNode = findLowestFScoreInOpen();
        if(newNode.getX() != xTarget || newNode.getY() != yTarget) {
            openList.remove(newNode);
            closedList.add(newNode);
            return proccessNode(world,newNode,xTarget,yTarget);
        }
        else
        {
            return newNode;
        }
    }

    public PathfindingNode findLowestFScoreInOpen()
    {
        int least = Integer.MAX_VALUE;
        PathfindingNode returnNode = null;
        //System.out.println("Open list size " + openList.size());
        for(PathfindingNode node: openList)
        {
            //System.out.println(node.getF() + " < " + least);
            if(node.getF() < least)
            {
                //System.out.println("added least");
                least = node.getF();
                returnNode = node;
            }
        }
        return returnNode;
    }

    public boolean alreadyAddedToList(int x, int y)
    {
        for(PathfindingNode node: openList)
        {
            if(x == node.getX() && y == node.getY())
            {
                return true;
            }
        }
        for(PathfindingNode node: closedList)
        {
            if(x == node.getX() && y == node.getY())
            {
                return true;
            }
        }
        return false;
    }

    public ArrayList<PathfindingNode> getPaths(World world, PathfindingNode parentNode, int x, int y, int targetX, int targetY)
    {
        ArrayList<PathfindingNode> paths = new ArrayList<>();

        if(BlockTypeEnum.isWalkable(world.getBlockFromCords(x+1,y).getBlockType()) && !alreadyAddedToList(x+1,y))
        {
            //System.out.println("Found Right");
            paths.add(new PathfindingNode(parentNode, x+1,y,parentNode.getG() + 10, targetX,targetY));
        }
        if(BlockTypeEnum.isWalkable(world.getBlockFromCords(x-1,y).getBlockType()) && !alreadyAddedToList(x-1,y))
        {
            //System.out.println("Found Left");
            paths.add(new PathfindingNode(parentNode, x-1,y,parentNode.getG() + 10,targetX,targetY));
        }
        if(BlockTypeEnum.isWalkable(world.getBlockFromCords(x,y+1).getBlockType()) && !alreadyAddedToList(x,y+1))
        {
            //System.out.println("Found North");
            paths.add(new PathfindingNode(parentNode, x,y+1,parentNode.getG() + 10, targetX,targetY));
        }
        if(BlockTypeEnum.isWalkable(world.getBlockFromCords(x,y-1).getBlockType()) && !alreadyAddedToList(x,y-1))
        {
            //System.out.println("Found South");
            paths.add(new PathfindingNode(parentNode, x,y-1,parentNode.getG() + 10,targetX,targetY));
        }


        return paths;
    }


}
*/