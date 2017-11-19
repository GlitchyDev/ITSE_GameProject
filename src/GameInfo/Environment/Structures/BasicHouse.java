package GameInfo.Environment.Structures;

import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.HouseWall;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.Blocks.DoorBlock;
import GameInfo.Environment.Entities.ScoreItem_Entity;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;

import java.util.ArrayList;

/* Created by Charlie on 2017-09-22
 * This class implements a simple house structure, with four walls and a door at the bottom center
 */

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
            for(int y = 0; y < structureHeight; y++) {
                //builds walls around edges, except door in center of next-to-last row
                //Sets correct sprite for each block (top/bottom sides, left/right sides, corners)
                //First the left wall and right walls, then the top and bottom, including the corners and door
                if ((x == 0 || x == structureWidth - 1) && (y > 1 && y < structureHeight - 1)) {
                    HouseWall b = new HouseWall(globalGameData);
                    world.setBlockFromCords(structureX + x, structureY + y, b);
                    affectedBlocks.add(b);
                }
                //now top and bottom rows, which all have same sprite but door block
                else if ((y == structureHeight - 1 || y == 1)) {

                    if (y == 1 && x == (int) Math.ceil(structureWidth / 2)) {
                        DoorBlock b = new DoorBlock(globalGameData);
                        world.setBlockFromCords(structureX + x, structureY + y, b);
                        affectedBlocks.add(b);
                    } else if (y == structureHeight - 1 && x == 0) {
                        HouseWall b = new HouseWall(globalGameData);
                        world.setBlockFromCords(structureX + x, structureY + y, b);
                        affectedBlocks.add(b);
                    } else if (y == 1 && x == 0) {
                        HouseWall b = new HouseWall(globalGameData);
                        world.setBlockFromCords(structureX + x, structureY + y, b);
                        affectedBlocks.add(b);
                    } else if (y == 1 && x == structureWidth - 1) {
                        HouseWall b = new HouseWall(globalGameData);
                        world.setBlockFromCords(structureX + x, structureY + y, b);
                        affectedBlocks.add(b);
                    }  else if (y == structureHeight - 1 && x == structureWidth - 1) {
                        HouseWall b = new HouseWall(globalGameData);
                        world.setBlockFromCords(structureX + x, structureY + y, b);
                        affectedBlocks.add(b);
                    } else {
                        HouseWall b = new HouseWall(globalGameData);
                        world.setBlockFromCords(structureX + x, structureY + y, b);
                        affectedBlocks.add(b);
                    }
                }
                //fills inside of house and bottom row with floor
                else {
                    WallFloorBlock b = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                    world.setBlockFromCords(structureX + x, structureY + y, b);
                    affectedBlocks.add(b);

                    int random = globalGameData.getRandom().nextInt(100);
                    if(random == 0)
                    {
                        world.attemptSpawn(new ScoreItem_Entity(world,globalGameData,structureX+x,structureY+y),globalGameData);
                    }

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
