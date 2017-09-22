package GameInfo.Environment.Structures;

import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;

import java.util.ArrayList;

public class BasicHouse extends StructureBase {


    public BasicHouse(World world, GlobalGameData globalGameData, int structureX, int structureY) {
        super(world, globalGameData, structureX, structureY);
        structureWidth = 7;
        structureHeight = 8;
    }

    @Override
    public void buildStructure() {
        for(int x = 0; x < structureWidth; x++)
        {
            for(int y = 0; y < structureHeight; y++)
            {
                //builds walls around edges, except door in center of next-to-last row
                if ((x == 0 || y == 1 || x == structureWidth - 1 || y == structureHeight - 1) && !(y == 1 && x == Math.ceil(structureWidth/2)))
                {
                    WallFloorBlock b = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_WALL);
                    world.setBlockFromCords(structureX + x, structureY + y, b);
                    affectedBlocks.add(b);
                }
                //fills inside w/ floor
                //y == 0 is left alone to make placing the structure easier (i.e. cheat, make sure block outside door will always be in the same chunk as the structure)
                else if (y != 0)
                {
                    WallFloorBlock b = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                    world.setBlockFromCords(structureX + x, structureY + y, b);
                    affectedBlocks.add(b);
                }
            }
        }
    }

    @Override
    //same as StructureBase's attemptBuildStructure method, but checks whether the door is blocked as well
    public boolean attemptBuildStructure()
    {
        ArrayList<StructureBase> structureBases = world.getChunkFromCordXY(structureX,structureY).getStructures();
        // This checks if any previous structures already added to world ( THIS STRUCT SHOULDN"T BE INCLUDED YET )

        boolean passed = true;
        //System.out.println("Checking Structure " + structureX + " " + structureY);
        for(StructureBase struct: structureBases)
        {
            //System.out.println("Does Structure " + struct.getStructureX() + " " + struct.getStructureY() + " " + " overlap?");
            if(doesOverlap(struct)) {
                //System.out.println("It does! ");
                passed = false;
            }
        }

        //checks whether there's a wall block in front of the door and prevents building if so
        //who decided Math.ceil should return a double when the result is, by definition, an integer
        if (world.getBlockFromCords(structureX + (int)Math.ceil(structureWidth/2), structureY).getBlockType() == BlockTypeEnum.TEST_WALL)
            passed = false;

        if(passed) {
            //System.out.println("No overlaps! So we build!");
            buildStructure();
        }
        return passed;


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
