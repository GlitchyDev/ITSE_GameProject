package GameInfo.Environment.Structures;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.DebugBlock;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.paint.Color;

public class TestRoomStructure extends StructureBase {


    public TestRoomStructure(World world, GlobalGameData globalGameData, int structureX, int structureY) {
        super(world, globalGameData, structureX, structureY);
        structureWidth = 5;
        structureHeight = 5;
    }

    @Override
    public void buildStructure() {
        for(int x = 0; x < structureWidth; x++)
        {
            for(int y = 0; y < structureHeight; y++)
            {
                DebugBlock b = new DebugBlock();
                world.setBlockFromCords(structureX + x, structureY + y, b);
                affectedBlocks.add(b);
            }
        }
    }

    @Override
    public StructureEventCollisionOverrideEnum checkCollision(int x, int y) {
        return StructureEventCollisionOverrideEnum.CAN_MOVE;
    }

    @Override
    public void enterEvent(int x, int y) {
        Color c = Color.rgb((int)(Math.random() * 256),(int)(Math.random() * 256),(int)(Math.random() * 256));
        for(BlockBase block: affectedBlocks)
        {
            ((DebugBlock)block).setColor(c);
        }
    }

    @Override
    public void exitEvent(int x, int y) {


    }
}
