package GameInfo.Environment.Entities.Pathfinding;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.PathfindingDebugBlock;
import GameInfo.Environment.Entities.Pro_Player;
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
    private static PathfindingNode firstNode;


    public static ArrayList<Position> findPathNonDiagnal(World world, int xStart, int yStart, int xTarget, int yTarget)
    {
        openList.clear();
        closedList.clear();
        firstNode = null;
        if(xStart == xTarget && yStart == yTarget)
        {
            ArrayList<Position> p = new ArrayList<>();
            p.add(new Position(xStart,yStart));
            return p;
        }
        firstNode = new PathfindingNode(null,xStart,yStart,0,xStart,yTarget);
        PathfindingNode finalNode = proccessPathfindingNode(world,firstNode,xTarget,yTarget);



        ArrayList<Position> positionList = new ArrayList<>();
        PathfindingNode n = finalNode;
        int temp = 0;
        while(!finalNode.isPrimaryNode())
        {
            if(n != null) {
                positionList.add(new Position(n.getX(), n.getY()));
                n = n.getParentNode();
                temp++;
            }
            else
            {
                System.out.println("Broken node is " + temp );
                break;
            }
        }
        Collections.reverse(positionList);
        return positionList;
    }

    public static PathfindingNode proccessPathfindingNode(World world, PathfindingNode currentNode, int xTarget, int yTarget)
    {
        PathfindingMap.openList.addAll(PathfindingMap.getConnectedTiles(world,currentNode,xTarget,yTarget));
        int leastF = Integer.MAX_VALUE;

        PathfindingNode newNode = currentNode;
        for(PathfindingNode node: PathfindingMap.openList)
        {
            node.recalculateH(xTarget,yTarget);
            if(node.getF() < leastF)
            {
                currentNode = node;
                leastF = newNode.getF();
            }
        }

        PathfindingMap.openList.remove(currentNode);
        PathfindingMap.closedList.add(currentNode);

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