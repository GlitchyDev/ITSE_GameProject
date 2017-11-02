package Pathfinding;

import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.PathfindingDebugBlock;
import GameInfo.Environment.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * The purpose of this class is
 * - Pathfind from 1 point to another
 * Issues
 * - Doesn't detect "Impossible Paths"
 * - Doesn't work against chunks (Entity Tick Rewrite)
 */


public class PathfindingHelper
{
    private static ArrayList<PathfindingNode> openList = new ArrayList<>();
    private static ArrayList<PathfindingNode> closedList = new ArrayList<>();
    private static PathfindingNode firstNode;

    private static int maxSearchCheck = 0;


    /**
     * Used for Pathfinding without Diagnals
     * @param world
     * @param xStart
     * @param yStart
     * @param xTarget
     * @param yTarget
     * @return A List of Positions of the best path towards the player
     */
    public static ArrayList<Position> findPathNonDiagnal(World world, int xStart, int yStart, int xTarget, int yTarget, int maxSearchCheck)
    {
        openList.clear();
        closedList.clear();
        firstNode = new PathfindingNode(null,xStart,yStart,0,xTarget, yTarget);
        PathfindingHelper.maxSearchCheck = maxSearchCheck;


        PathfindingNode finalNode = proccessPathfindingNode(world,firstNode,xTarget,yTarget,0);
        ArrayList<Position> positions = new ArrayList<>();
        PathfindingNode cacheNode = finalNode;
        if(finalNode == null)
        {
            return null;
        }
        while(cacheNode.getParentNode() != null)
        {
            positions.add(new Position(cacheNode.getX(),cacheNode.getY()));
            cacheNode = cacheNode.getParentNode();
        }
        Collections.reverse(positions);

        return positions;
    }

    private static PathfindingNode proccessPathfindingNode(World world, PathfindingNode currentNode, int xTarget, int yTarget, int index)
    {
        if(index > maxSearchCheck)
        {
            return null;
        }
        openList.remove(currentNode);
        closedList.add(currentNode);
        getConnectedTiles(world,currentNode,xTarget,yTarget);
        int lowestF = Integer.MAX_VALUE;
        PathfindingNode nextNode = currentNode;
        for(PathfindingNode node : openList)
        {
            if(node.getF() < lowestF)
            {
                lowestF = node.getF();
                nextNode = node;
            }
        }

        if(nextNode.getX() == xTarget && nextNode.getY() == yTarget)
        {
            return nextNode;
        }
        else
        {
            return proccessPathfindingNode(world,nextNode,xTarget,yTarget,index+1);
        }
    }


    private static void getConnectedTiles(World world, PathfindingNode parentNode, int targetX, int targetY)
    {
        openList.remove(parentNode);
        closedList.add(parentNode);
        int x = parentNode.getX();
        int y = parentNode.getY();
        checkTile(x,y+1,targetX, targetY, world,parentNode);
        checkTile(x,y-1,targetX, targetY,world,parentNode);
        checkTile(x+1,y,targetX, targetY,world,parentNode);
        checkTile(x-1,y,targetX, targetY,world,parentNode);
    }

    private static void checkTile(int x, int y, int targetX, int targetY, World world, PathfindingNode parentNode)
    {
        if(BlockTypeEnum.isWalkable(world.getBlockFromCords(x,y).getBlockType()) && (world.getEntitiesAtPos(x,y).isEmpty() || (x == targetX) && (y==targetY)))
        {
            if(isOnOpenList(x,y))
            {
                PathfindingNode node =  getNode(x,y,openList);
                if(parentNode.getG() + 5 < node.getG())
                {
                    System.out.println("Recalculated G");
                    node.setG(parentNode.getG() + 5);
                    node.setNextNode(parentNode);
                }
            }
            else
            {
                if(!isOnClosedList(x,y))
                {
                    openList.add(new PathfindingNode(parentNode,x,y,parentNode.getG() + 5,targetX,targetY));
                }
                else
                {

                }
            }
        }
    }

    private static boolean alreadyAddedToList(int x, int y)
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


    public static PathfindingNode getNode(int x, int y, ArrayList<PathfindingNode> nodes)
    {
        for(PathfindingNode node: nodes)
        {
            if(node.getX() == x && node.getY() == y)
            {
                return node;
            }
        }
        return null;
    }


    public static boolean isOnOpenList(int x, int y)
    {
        for(PathfindingNode node: openList)
        {
            if(node.getX() == x && node.getY() == y)
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnClosedList(int x, int y)
    {
        for(PathfindingNode node: closedList)
        {
            if(node.getX() == x && node.getY() == y)
            {
                return true;
            }
        }
        return false;
    }



}
/*
public class PathfindingHelper {
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