package GameInfo.Environment;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.TestRockEntity;
import GameInfo.Environment.Structures.StructureBase;
import GameInfo.Environment.Structures.TestRoomStructure;
import GameInfo.GlobalGameData;

import java.util.ArrayList;

/**
 * Created by Robert on 8/27/2017.
 *
 * Stores big areas of Blocks and Ticks the Entities Inside, and ticks the entities
 * Viewport can grab Blocks of them to render
 */
public class Chunk {
    // 100 Blocks by 100
    private BlockBase[][] blockBaseList;
    private ArrayList<EntityBase> entities;
    private ArrayList<StructureBase> structures;



    public Chunk(GlobalGameData globalGameData, BlockBase[][] blockBaseList, ArrayList<EntityBase> entities, World world, int relativeChunkX, int relativeChunkY)
    {
        this.blockBaseList = blockBaseList;
        this.entities = entities;
        this.structures = new ArrayList<>();
    }
    public Chunk(GlobalGameData globalGameData, World world, int relativeChunkX, int relativeChunkY)
    {
        this.blockBaseList = new BlockBase[World.getChunkSize()][World.getChunkSize()];
        // This is the default "Noise" Generation for the chunk
        for(int x = 0; x < World.getChunkSize(); x++)
        {
            for(int y = 0; y < World.getChunkSize(); y++)
            {
                blockBaseList[x][y] = new WallFloorBlock(globalGameData);
            }
        }



        this.entities = new ArrayList<>();
        this.entities.add(new TestRockEntity(world, globalGameData, World.getPosNumFromChunkNum(relativeChunkX),World.getPosNumFromChunkNum(relativeChunkY),globalGameData.getSprite("Standing_Mirror")));

        this.structures = new ArrayList<>();

    }

    public void generateStructures(GlobalGameData globalGameData, World world, int relativeChunkX, int relativeChunkY)
    {

        // Number of Structures attempting to spawn in this chunk
        for(int i = 0; i < 200; i++) {
            // Here you can see for the X cords I do random.nextInt(World.getChunkSize() - 5)
            // This prevents the structure from picking a location that is partially outside the chunk
            TestRoomStructure s = new TestRoomStructure(world, globalGameData, World.getPosNumFromChunkNum(relativeChunkX) + globalGameData.getRandom().nextInt(World.getChunkSize() - 5), World.getPosNumFromChunkNum(relativeChunkY) + globalGameData.getRandom().nextInt(World.getChunkSize() - 5));
            if (s.attemptBuildStructure()) {
                world.getStructures().add(s);
                structures.add(s);
            }

        }

    }
    // Setters and Getters
    public BlockBase[][] getBlockBaseList() {
        return blockBaseList;
    }

    /*
    Adds the Blocks contained within the specified grid to the Array
     */
    public ArrayList<EntityBase> getEntities() {
        return entities;
    }

    public boolean isStructureAtRelative(int relativeX, int relativeY)
    {
        //System.out.println("Start Check!");
        for(StructureBase structure: structures)
        {
            if(relativeX >= structure.getStructureX() && relativeX < structure.getStructureX() + structure.getStructureWidth())
            {
                if(relativeY >= structure.getStructureY() && relativeY < structure.getStructureY() + structure.getStructureHeight())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public StructureBase getStructureAtPos(int relativeX, int relativeY)
    {

        for(StructureBase structure: structures)
        {
            if(relativeX >= structure.getStructureX() && relativeX < structure.getStructureX() + structure.getStructureWidth())
            {
                if(relativeY >= structure.getStructureY() && relativeY < structure.getStructureY() + structure.getStructureHeight())
                {
                    return structure;
                }
            }
        }
        return null;
    }

    public ArrayList<StructureBase> getStructures()
    {
        return structures;
    }

    



}
