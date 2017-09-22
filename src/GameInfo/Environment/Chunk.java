package GameInfo.Environment;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.TestRockEntity;
import GameInfo.Environment.Structures.StructureBase;
import GameInfo.Environment.Structures.TestStructures;
import GameInfo.GlobalGameData;

import java.util.ArrayList;

/**
 * Created by Robert on 8/27/2017.
 *
 * Stores big areas of Blocks and Ticks the Entities Inside, and ticks the entities
 * Stores/Spawns Structures involved inside the chunk
 * Viewport can grab Blocks of them to render
 *
 */
public class Chunk {
    // A 2D array of all the blocks inside the chunk
    private BlockBase[][] blockBaseList;
    private ArrayList<EntityBase> entities;
    private ArrayList<StructureBase> structures;


    // A "Dummy" Constructor for if we wish to specifiy all behaviors inside
    public Chunk(GlobalGameData globalGameData, BlockBase[][] blockBaseList, ArrayList<EntityBase> entities, World world, int relativeChunkX, int relativeChunkY)
    {
        this.blockBaseList = blockBaseList;
        this.entities = entities;
        this.structures = new ArrayList<>();
    }

    // Default Constructor, creates random terrain and spawns structures
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


    /**
     * This Method generates structures involved in ALL chunks, will be changed eventually to a managing class
     * @param globalGameData
     * @param world
     * @param relativeChunkX
     * @param relativeChunkY
     */
    public void generateStructures(GlobalGameData globalGameData, World world, int relativeChunkX, int relativeChunkY)
    {

        // This example spawns "TestStructures", it attempts to spawn 200 but will usually only spawn about 80-90, due to overlapping preventions
        // The larger the structure the lower chance it will succeed in spawning, this can be prevented by spawning it before all other structures
        for(int i = 0; i < 200; i++) {
            // Here you can see for the X cords I do random.nextInt(World.getChunkSize() - 5)
            // This prevents the structure from picking a location that is partially outside the chunk
            TestStructures s = new TestStructures(world, globalGameData, World.getPosNumFromChunkNum(relativeChunkX) + globalGameData.getRandom().nextInt(World.getChunkSize() - 5), World.getPosNumFromChunkNum(relativeChunkY) + globalGameData.getRandom().nextInt(World.getChunkSize() - 5));
            // This triggers when the Structure has Cleared Collisions and spawned blocks and is "Worthy" of storage
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


    /**
     * Checks if a Structure exists at the X,Y Specified
     * @param relativeX
     * @param relativeY
     * @return
     */
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

    /**
     * Grabs the Structure at the specified X,Y Cords
     * @param relativeX
     * @param relativeY
     * @return
     */
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
