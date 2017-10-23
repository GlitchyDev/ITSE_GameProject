package Pathfinding;

import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.PathfindingDebugBlock;
import GameInfo.Environment.World;

import java.util.ArrayList;
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
    private static int timeOut = 0;
    private static int currentTime = 0;
    private static boolean timedOut = false;


    /**
     * Used for Pathfinding without Diagnals
     * @param world
     * @param xStart
     * @param yStart
     * @param xTarget
     * @param yTarget
     * @return A List of Positions of the best path towards the player
     */


    public static ArrayList<Position> findPathNonDiagnal(World world, int xStart, int yStart, int xTarget, int yTarget, int timeOut)
    {
        openList.clear();
        closedList.clear();
        PathfindingHelper.timeOut = timeOut;
        PathfindingHelper.timedOut = false;
        currentTime = 0;
        firstNode = null;
        if(xStart == xTarget && yStart == yTarget)
        {
            ArrayList<Position> p = new ArrayList<>();
            p.add(new Position(xStart,yStart));
            return p;
        }
        firstNode = new PathfindingNode(null,xStart,yStart,0,xStart,yTarget);
        PathfindingNode finalNode = proccessPathfindingNode(world,firstNode,xTarget,yTarget);

        if(!timedOut) {


            ArrayList<Position> positionList = new ArrayList<>();
            PathfindingNode n = finalNode;
            while (!finalNode.isPrimaryNode()) {
                if (n != null) {
                    positionList.add(new Position(n.getX(), n.getY()));
                    n = n.getParentNode();
                } else {
                    //System.out.println("Broken node is " + temp );
                    break;
                }
            }
            Collections.reverse(positionList);
            for(PathfindingNode node: closedList)
            {
                world.setBlockFromCords(node.getX(),node.getY(),new PathfindingDebugBlock(node));
            }
            for(PathfindingNode node: openList)
            {
                world.setBlockFromCords(node.getX(),node.getY(),new PathfindingDebugBlock(node));
            }
            return positionList;
        }
        else
        {
            return null;
        }
    }

    private static PathfindingNode proccessPathfindingNode(World world, PathfindingNode currentNode, int xTarget, int yTarget)
    {
        currentTime++;
        if(currentTime >= timeOut)
        {
            timedOut = true;
            return currentNode;
        }
        PathfindingHelper.openList.addAll(PathfindingHelper.getConnectedTiles(world,currentNode,xTarget,yTarget));
        double leastF = Integer.MAX_VALUE;

        PathfindingNode newNode = currentNode;
        for(PathfindingNode node: PathfindingHelper.openList)
        {
            node.recalculateH(xTarget,yTarget);
            if(node.getF() < leastF)
            {
                currentNode = node;
                leastF = newNode.getF();
            }
        }

        PathfindingHelper.openList.remove(currentNode);
        PathfindingHelper.closedList.add(currentNode);

        /*
        PathfindingDebugBlock b1 = new PathfindingDebugBlock(currentNode);
        world.setBlockFromCords(currentNode.getX(), currentNode.getY(), b1);
        */

        //System.out.println();
       //System.out.println("Current Node " + currentNode.getX() + " " + currentNode.getY());
       // System.out.println("Target Player " +  xTarget + " " + yTarget);
        if(currentNode.getX() == xTarget && currentNode.getY() == yTarget)
        {
           // System.out.println("You win!");
            return newNode;
        }
        else
        {
            return proccessPathfindingNode(world,currentNode,xTarget,yTarget);
        }




    }


    private static ArrayList<PathfindingNode> getConnectedTiles(World world, PathfindingNode parentNode, int targetX, int targetY)
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