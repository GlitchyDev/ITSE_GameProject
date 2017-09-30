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

}