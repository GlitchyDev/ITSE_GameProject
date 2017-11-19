/* Created by Charlie on 2017-10-25
 * This class implements a dungeon, with uniform rooms and a randomly generated path through
 */

package GameInfo.Environment.Structures;
import GameInfo.Environment.Blocks.*;
import GameInfo.Environment.Entities.ScoreItem_Entity;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Dungeon extends StructureBase {


    public Dungeon(World world, GlobalGameData globalGameData, int structureX, int structureY) {
        super(world, globalGameData, structureX, structureY);
        structureWidth = 21;
        structureHeight = 21;
    }

    @Override
    public void buildStructure() {//would be good to check that there's a path to all rooms, but how?
        for (int x = 0; x < structureWidth; x++) {
            for (int y = 0; y < structureHeight; y++) {

                //draw walls every 4 blocks
                if (x % 4 == 0 || y % 4 == 0) {
                    WallFloorBlock b = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_WALL);
                    b.setSprite(globalGameData, "Brown_Test_Wall");
                    world.setBlockFromCords(structureX + x, structureY + y, b);
                    affectedBlocks.add(b);

                    if ((x % 4 == 2 || y % 4 == 2) && Math.random() > .5) { //65% chance of building door on each wall
                        WallFloorBlock b2 = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                        world.setBlockFromCords(structureX + x, structureY + y, b2);
                        affectedBlocks.add(b2);
                    }
                } else { //fill interior with floors (3x3 rooms)
                    int random = globalGameData.getRandom().nextInt(60);
                    if(random == 0)
                    {
                        world.attemptSpawn(new ScoreItem_Entity(world,globalGameData,structureX+x,structureY+y),globalGameData);
                    }
                    WallFloorBlock b = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                    world.setBlockFromCords(structureX + x, structureY + y, b);
                    affectedBlocks.add(b);
                }
            }
        }

        for (int x = 0; x < structureWidth; x += 4) { //check that all rooms have at least one entrance and add one (on random side) if none
            for (int y = 0; y < structureHeight; y += 4) {
                if (world.getBlockFromCords(structureX + x, structureY + y + 2).getBlockType() != BlockTypeEnum.TEST_FLOOR  //check each wall for an opening
                    && world.getBlockFromCords(structureX + x + 4, structureY + y + 2).getBlockType() != BlockTypeEnum.TEST_FLOOR
                    && world.getBlockFromCords(structureX + x + 2, structureY + y).getBlockType() != BlockTypeEnum.TEST_FLOOR
                    && world.getBlockFromCords(structureX + x + 2, structureY + y + 4).getBlockType() != BlockTypeEnum.TEST_FLOOR)
                {
                    switch (globalGameData.getRandom().nextInt(4)) {
                        case 0:
                            WallFloorBlock b1 = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                            world.setBlockFromCords(structureX + x + 2, structureY + y, b1);
                            affectedBlocks.add(b1);
                            break;
                        case 1:
                            WallFloorBlock b2 = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                            world.setBlockFromCords(structureX + x + 2, structureY + y + 4, b2);
                            affectedBlocks.add(b2);
                            break;
                        case 2:
                            WallFloorBlock b3 = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                            world.setBlockFromCords(structureX + x, structureY + y + 2, b3);
                            affectedBlocks.add(b3);
                            break;
                        case 3:
                            WallFloorBlock b4 = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                            world.setBlockFromCords(structureX + x + 2, structureY + y + 4, b4);
                            affectedBlocks.add(b4);
                            break;
                    }
                }
            }
        }

        //break down some walls to make "ruins"
        switch (globalGameData.getRandom().nextInt(4)) {
            case 0:
                //large circle
                int centerX = globalGameData.getRandom().nextInt(structureWidth - 10) + 5;
                int centerY = globalGameData.getRandom().nextInt(structureHeight - 10) + 5;
                buildFloorCircle(centerX, centerY, 5);
                break;

            case 1:
                //medium and small circle
                int centerX1 = globalGameData.getRandom().nextInt(structureWidth - 8) + 4;
                int centerY1 = globalGameData.getRandom().nextInt(structureHeight - 8) + 4;
                buildFloorCircle(centerX1, centerY1, 4);

                int centerX2 = globalGameData.getRandom().nextInt(structureWidth - 6) + 3;
                int centerY2 = globalGameData.getRandom().nextInt(structureHeight - 6) + 3;
                buildFloorCircle(centerX2, centerY2, 3);
                break;

            case 2:
                //small circles
                for (int i = 0; i < 3; i++) {
                    int centerX3 = globalGameData.getRandom().nextInt(structureWidth - 6) + 3;
                    int centerY3 = globalGameData.getRandom().nextInt(structureHeight - 6) + 3;
                    buildFloorCircle(centerX3, centerY3, 3);
                }
                break;

            case 3:
                //nothing
                WallFloorBlock d = new WallFloorBlock(globalGameData,BlockTypeEnum.TEST_FLOOR); //temporary, for testing
                world.setBlockFromCords(structureX + 11, structureY + 11, d);
                affectedBlocks.add(d);
                break;

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

    private void buildFloorCircle(int centerX, int centerY, int radius) {
        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int y = centerY - radius; y <= centerY + radius; y++) {
                if (Math.hypot(x - centerX, y - centerY) < radius) //uses distance formula to find whether block is in circle radius
                {
                    WallFloorBlock b = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                    world.setBlockFromCords(structureX + x, structureY + y, b);
                    affectedBlocks.add(b);
                }
            }
        }
    }
}
