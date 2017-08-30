package GameInfo.Environment;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.Entities.EntityBase;
import GameInfo.Environment.World;

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




    public Chunk(BlockBase[][] blockBaseList, ArrayList<EntityBase> entities)
    {
        this.blockBaseList = blockBaseList;
        this.entities = entities;
    }
    public Chunk()
    {
        this.blockBaseList = new BlockBase[World.getChunkSize()][World.getChunkSize()];
        for(int x = 0; x < World.getChunkSize(); x++)
        {
            for(int y = 0; y < World.getChunkSize(); y++)
            {
                blockBaseList[x][y] = new WallFloorBlock();
            }
        }
        this.entities = new ArrayList<>();
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
