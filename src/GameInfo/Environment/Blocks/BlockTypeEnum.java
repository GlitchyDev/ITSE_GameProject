package GameInfo.Environment.Blocks;

/**
 * Created by Robert on 8/28/2017.
 * This Enum specifies which BlockType a Block is, so it can be identified and properly casted to
 */
public enum BlockTypeEnum {
    TEST_FLOOR,
    TEST_WALL,
    DOOR_OPEN,
    DOOR_CLOSED,
    DEBUG_BLOCK,
    PATHFINDING_DEBUG;

    public static boolean isWalkable(BlockTypeEnum type) {
        switch (type) {
            case TEST_FLOOR:
                return true;
            case TEST_WALL:
                return false;
            case DOOR_OPEN:
                return true;
            case DOOR_CLOSED:
                return false;
            case DEBUG_BLOCK:
                return true;
            case PATHFINDING_DEBUG:
                return true;
        }
        return true;
    }


    public static boolean isOpaque(BlockTypeEnum type) {
        switch (type) {
            case TEST_FLOOR:
                return false;
            case TEST_WALL:
                return true;
            case DEBUG_BLOCK:
                return false;
            case PATHFINDING_DEBUG:
                return false;
        }
        return true;
    }


    public static int caculateLightCost(BlockTypeEnum type, int light) {
        switch (type) {
            case TEST_FLOOR:
                return light - 1;
            case TEST_WALL:
                return light;
            case DEBUG_BLOCK:
                return light;
            case PATHFINDING_DEBUG:
                return light - 1;
        }
        return 0;
    }



}