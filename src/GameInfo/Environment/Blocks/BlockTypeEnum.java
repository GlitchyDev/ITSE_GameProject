package GameInfo.Environment.Blocks;

/**
 * Created by Robert on 8/28/2017.
 * This Enum specifies which BlockType a Block is, so it can be identified and properly casted to
 */
public enum BlockTypeEnum {
    TEST_FLOOR,
    TEST_WALL,
    DEBUG_BLOCK;

    public static boolean isWalkable(BlockTypeEnum type) {
        switch (type) {
            case TEST_FLOOR:
                return true;
            case TEST_WALL:
                return true;
            case DEBUG_BLOCK:
                return true;
        }
        return false;
    }

}