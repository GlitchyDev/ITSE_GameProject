package Pathfinding;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.World;
import com.sun.org.apache.xpath.internal.functions.FuncUnparsedEntityURI;

import java.util.ArrayList;

public class LineOfSightHelper {
    private static ArrayList<Position> blocksLookedAt = new ArrayList<>();
    private static Position targetLocation;
    private static boolean foundTarget = false;


    public static boolean lineOfSight(World world, int x1, int y1, int x2, int y2, int maxDistance)
    {
        if(x1 == x2)
        {

            int direction = 1;
            if(y1-y2 > 0)
            {
                direction = -1;
            }
            int adjust = direction;
            while(true)
            {
                if(Math.abs(adjust) >= maxDistance)
                {
                    return false;
                }
                BlockBase b = world.getBlockFromCords(x1, y1 + adjust);

                if(!BlockTypeEnum.isWalkable(b.getBlockType()))
                {
                    return false;
                }
                if(y1+adjust == y2)
                {
                    return true;
                }
                adjust += direction;
            }
        }
        else
        {
            if(y1 == y2) {
                int direction = 1;
                if (x1 - x2 > 0) {
                    direction = -1;
                }
                int adjust = direction;
                while (true) {
                    if (Math.abs(adjust) >= maxDistance) {
                        return false;
                    }
                    BlockBase b = world.getBlockFromCords(x1 + adjust, y1);

                    if(!BlockTypeEnum.isWalkable(b.getBlockType())) {
                        return false;
                    }
                    if (x1 + adjust == x2) {
                        return true;
                    }
                    adjust += direction;
                }
            }
        }
        return false;
    }

    public static double calculateDistance(int x1, int y1, int x2, int y2)
    {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }

}
