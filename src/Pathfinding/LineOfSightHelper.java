package Pathfinding;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.World;
import HardwareAdaptors.DirectionalEnum;

import java.util.ArrayList;

public class LineOfSightHelper {
    private static ArrayList<Position> blocksLookedAt = new ArrayList<>();
    private static Position targetLocation;
    private static boolean foundTarget = false;


    public static boolean lineOfSight(World world, int x1, int y1, int relative, DirectionalEnum directionalEnum)
    {
       switch(directionalEnum)
       {
           case NORTH:
               for(int y = y1; y <= y1 + relative; y++)
               {
                   BlockBase b = world.getBlockFromCords(x1,y);
                   if(!BlockTypeEnum.isWalkable(b.getBlockType()))
                   {
                       return false;
                   }
               }
               return true;
           case EAST:
               for(int x = x1; x >= x1 + relative; x--)
               {
                   BlockBase b = world.getBlockFromCords(x,y1);
                   if(!BlockTypeEnum.isWalkable(b.getBlockType()))
                   {
                       return false;
                   }
               }
               return true;
           case SOUTH:
               for(int y = y1; y >= y1 + relative; y--)
               {
                   BlockBase b = world.getBlockFromCords(x1,y);
                   if(!BlockTypeEnum.isWalkable(b.getBlockType()))
                   {
                       return false;
                   }
               }
               return true;
           case WEST:
               for(int x = x1; x <= x1 + relative; x++)
               {
                   BlockBase b = world.getBlockFromCords(x,y1);
                   if(!BlockTypeEnum.isWalkable(b.getBlockType()))
                   {
                       return false;
                   }
               }
               return true;
       }
       return false;
    }

    public static double calculateDistance(int x1, int y1, int x2, int y2)
    {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

}
