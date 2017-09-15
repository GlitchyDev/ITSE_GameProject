package GameInfo.Environment.Structures;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.DebugBlock;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.paint.Color;

public class TestStructures extends StructureBase {


    public TestStructures(World world, GlobalGameData globalGameData, int structureX, int structureY) {
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

    // We don't care, just check the block for behavior
    @Override
    public StructureEventCollisionOverrideEnum checkCollision(int x, int y) {
        return StructureEventCollisionOverrideEnum.CHECK_BLOCK_COLLISIONS;
    }

    // This example says when someone moves to a block inside the Structure, change all the colors of all the TestBlocks randomly
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
