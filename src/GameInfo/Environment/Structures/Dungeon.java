/* Created by Charlie on 2017-10-25
 * This class implements a dungeon, with uniform rooms and a randomly generated path through
 */

package GameInfo.Environment.Structures;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.DoorBlock;
import GameInfo.Environment.Blocks.HouseWall;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;

import java.util.ArrayList;

public class Dungeon extends StructureBase {


    public Dungeon(World world, GlobalGameData globalGameData, int structureX, int structureY) {
        super(world, globalGameData, structureX, structureY);
        structureWidth = 22;
        structureHeight = 22;
    }

    @Override
    public void buildStructure() {//would be good to check that there's a path to all rooms, but how?
        for (int x = 0; x < structureWidth; x++) {
            for (int y = 0; y < structureHeight; y++) {

                //draw walls every 4 blocks
                if (x % 4 == 0 || y % 4 == 0) {
                    WallFloorBlock b = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_WALL);
                    world.setBlockFromCords(structureX + x, structureY + y, b);
                    affectedBlocks.add(b);

                    if ((x % 4 == 2 || y % 4 == 2) && Math.random() > .5) { //50% chance of building door on every wall
                        DoorBlock d = new DoorBlock(globalGameData);
                        world.setBlockFromCords(structureX + x, structureY + y, d);
                        affectedBlocks.add(d);
                    }
                } else { //fill interior with floors (3x3 rooms)
                    WallFloorBlock b = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                    world.setBlockFromCords(structureX + x, structureY + y, b);
                    affectedBlocks.add(b);
                }
            }
        }

        for (int x = 0; x < structureWidth; x += 4) { //check that all rooms have at least one entrance and add one (on random side) if none
            for (int y = 0; y < structureHeight; y += 4) {
                if (world.getBlockFromCords(structureX + x, structureY + y + 2).getBlockType() != BlockTypeEnum.DOOR_CLOSED  //check each wall for a door - doors should always start closed
                    && world.getBlockFromCords(structureX + x + 4, structureY + y + 2).getBlockType() != BlockTypeEnum.DOOR_CLOSED
                    && world.getBlockFromCords(structureX + x + 2, structureY + y).getBlockType() != BlockTypeEnum.DOOR_CLOSED
                    && world.getBlockFromCords(structureX + x + 2, structureY + y + 4).getBlockType() != BlockTypeEnum.DOOR_CLOSED)
                {
                    int temp = (int)(Math.random() * 4);
                    switch (temp) {
                        case 0:
                            DoorBlock d1 = new DoorBlock(globalGameData);
                            world.setBlockFromCords(structureX + x, structureY + y + 2, d1);
                            affectedBlocks.add(d1);
                            break;
                        case 1:
                            DoorBlock d2 = new DoorBlock(globalGameData);
                            world.setBlockFromCords(structureX + x + 4, structureY + y + 2, d2);
                            affectedBlocks.add(d2);
                            break;
                        case 2:
                            DoorBlock d3 = new DoorBlock(globalGameData);
                            world.setBlockFromCords(structureX + x + 2, structureY + y, d3);
                            affectedBlocks.add(d3);
                            break;
                        case 3:
                            DoorBlock d4 = new DoorBlock(globalGameData);
                            world.setBlockFromCords(structureX + x + 2, structureY + y + 4, d4);
                            affectedBlocks.add(d4);
                            break;
                    }
                }
            }
        }

    }

    @Override
    //same as StructureBase's attemptBuildStructure method, but checks whether the door is blocked as well
    public boolean attemptBuildStructure() {
        ArrayList<StructureBase> structureBases = world.getChunkFromCordXY(structureX, structureY).getStructures();
        // This checks if any previous structures already added to world ( THIS STRUCT SHOULDN"T BE INCLUDED YET )

        boolean passed = true;
        //System.out.println("Checking Structure " + structureX + " " + structureY);
        for (StructureBase struct : structureBases) {
            //System.out.println("Does Structure " + struct.getStructureX() + " " + struct.getStructureY() + " " + " overlap?");
            if (doesOverlap(struct)) {
                //System.out.println("It does! ");
                passed = false;
            }
        }

        if (passed) {
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
