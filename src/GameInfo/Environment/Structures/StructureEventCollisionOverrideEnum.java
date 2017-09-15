package GameInfo.Environment.Structures;

public enum StructureEventCollisionOverrideEnum {
    CAN_MOVE, // Means for now, you can still move here, the block will still determine its collisions
    CAN_MOVE_DEFININATE, // Means that 100% YOU CAN MOVE HERE
    CAN_NOT_MOVE_DEFINATE, // Means 100% YOU CAN NOT MOVE HERE!

}
