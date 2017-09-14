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
        this.entities.add(new TestRockEntity(world, globalGameData, world.getPosNumFromChunkNum(relativeChunkX),world.getPosNumFromChunkNum(relativeChunkY),globalGameData.getSprite("Standing_Mirror")));

        this.structures = new ArrayList<>();

    }

    public void generateStructures(GlobalGameData globalGameData, World world, int relativeChunkX, int relativeChunkY)
    {
        // This is where we attempt to generate Structures
        // This is generating the test structure in 1/10 chunks
        System.out.println("Building Structures for Chunk " + relativeChunkX + " " + relativeChunkY);
        if(globalGameData.getRandom().nextInt(1) == 0)
        {
            System.out.println("Attempting Generation!");
            // Here you can see for the X cords I do random.nextInt(World.getChunkSize() - 5)
            // This prevents the structure from picking a location that is partially outside the chunk
            TestRoomStructure s = new TestRoomStructure(world, globalGameData, world.getPosNumFromChunkNum(relativeChunkX) + globalGameData.getRandom().nextInt(World.getChunkSize()),world.getPosNumFromChunkNum(relativeChunkX) - 5 + globalGameData.getRandom().nextInt(World.getChunkSize() - 5) );
            if(s.attemptBuildStructure())
            {
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

    



}
