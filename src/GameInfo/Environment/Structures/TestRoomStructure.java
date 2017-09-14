package GameInfo.Environment.Structures;

import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.DebugBlock;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameStates.GameStateBase;

public class TestRoomStructure extends StructureBase {


    public TestRoomStructure(World world, GlobalGameData globalGameData, int structureX, int structureY) {
        super(world, globalGameData, structureX, structureY);
        structureWidth = 5;
        structureHeight = 5;
    }

    @Override
    public void buildStructure() {
        System.out.println("Built Structure @ "+ + structureX + " " + structureY);
        for(int x = 0; x < structureWidth; x++)
        {
            for(int y = 0; y < structureHeight; y++)
            {
                world.setBlockFromCords(structureX + x, structureY + y, new DebugBlock());

            }
        }
    }
}
