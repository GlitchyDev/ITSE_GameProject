package GameInfo.Environment.Structures;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.DebugBlock;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.paint.Color;

public class DirtBlobStructure extends StructureBase {


    public DirtBlobStructure(World world, GlobalGameData globalGameData, int structureX, int structureY) {
        super(world, globalGameData, structureX, structureY);
        structureWidth = 6;
        structureHeight = 5;
    }

    @Override
    public void buildStructure() {
        for(int x = 0; x < structureWidth; x++)
        {
            for(int y = 0; y < structureHeight; y++)
            {
                //if statement fills 5x4 core w/ dirt
                if (x > 0 && x < structureWidth && y > 0 && y < structureHeight)
                {
                    WallFloorBlock b = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                    b.setSprite(globalGameData,"Test_Floor_6");
                    world.setBlockFromCords(structureX + x, structureY + y, b);
                    affectedBlocks.add(b);
                }
                //blocks on edge of structure have 60% change to be changed to dirt
                else
                {
                    if (Math.random() >= 0.7)
                    {
                        WallFloorBlock b = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                        b.setSprite(globalGameData,"Test_Floor_6");
                        world.setBlockFromCords(structureX + x, structureY + y, b);
                        affectedBlocks.add(b);
                    }
                }
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
    }

    @Override
    public void exitEvent(int x, int y) {


    }
}
