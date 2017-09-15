package GameInfo.Environment.Structures;

public enum StructureEventCollisionOverrideEnum {
    CHECK_BLOCK_COLLISIONS, // Means for now, you can still move here, the block will still determine its collisions
    CAN_MOVE_DEFINITE, // Means that 100% YOU CAN MOVE HERE
    CAN_NOT_MOVE_DEFINITE, // Means 100% YOU CAN NOT MOVE HERE!

}
