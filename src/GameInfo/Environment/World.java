package GameInfo.Environment;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Viewport;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Robert on 8/27/2017.
 * This class is designed to
 * - Host the "World" that gets rendered to the screen
 * - Ticks "Blocks"
 * - EntityBase
 * - Players
 *
 * - Get Method to get 3
 */
public class World {
    private static int unitRatio = 27;
    private static int chunkSize = 100;
    private HashMap<String,Chunk> chunks;

    public static int getUnitRatio() {
        return unitRatio;
    }
    public static int getChunkSize() {
        return chunkSize;
    }

    public World()
    {
        chunks = new HashMap<>();
        chunks.put(0 + "," + 0,new Chunk());
    }


    public int getPosNumFromChunkNum(int z)
    {
            return z * 100;
    }

    public Chunk getChunkFromChunkXY(int x, int y)
    {
        if(!chunks.containsKey(x + "," + y))
        {
           chunks.put(x + "," + y, new Chunk());
        }
        return chunks.get(x + "," + y);
    }
    public Chunk getChunkFromCordXY(int x, int y)
    {
        return getChunkFromChunkXY(getChunkNumfromCordNum(x),getChunkNumfromCordNum(y));
    }

    public int getChunkNumfromCordNum(int z)
    {
        boolean isNeg = false;
        if(z < 0)
        {
            isNeg = true;
        }
        if((z / World.getChunkSize() == 0))
        {
            if(isNeg)
            {
               return -1;
            }
            else
            {
                return 0;
            }
        }
        else
        {
            return z/100;
        }

    }

    public void addBlocksInsideChunk(Chunk chunk, int chunkX, int chunkY, BlockBase[][] blocks, int x1, int y1, int x2, int y2) {

        System.out.println("Corner: X: " + chunkX + " Y: " + chunkY);
        System.out.println("1: X: " + x1 + " Y: " + y1);
        System.out.println("2: X: " + x2 + " Y: " + y2);


        for(int x = chunkX; x < getPosNumFromChunkNum(chunkX) + World.getChunkSize(); x++)
        {
            for(int y = chunkY; y < getPosNumFromChunkNum(chunkY) + World.getChunkSize(); y++)
            {
                if(x <= x1 && x >= x2 && y <= y1 && y >= y2)
                {
                    System.out.print("B");
                    blocks[x1 - x][y1 - y] = chunk.getBlockBaseList()[x - chunkX][y - chunkY];
                }
            }
        }
        System.out.println();
        viewBlocks(blocks);
        System.out.println();


        /*
        for (int x = x2; x >= upperLeftX && x <= upperLeftX + World.getChunkSize() && x <= x1; x++)
        {
            for (int y = y2; y >= upperLeftY && y <= upperLeftY + World.getChunkSize() && y <= y1; y++)
            {
                blocks[x1 - x][y1 - y] = chunk.getBlockBaseList()[x - upperLeftX][y - upperLeftY];
            }
        }
        */

    }

    public void viewBlocks(BlockBase[][] blocks)
    {
        for(int x = 0; x < blocks.length; x++)
        {
            for(int y = 0; y < blocks[x].length; y++)
            {
                if(blocks[y][x] == null)
                {
                    System.out.print("NULL|");
                }
                else
                {
                    System.out.print("GOOD|");
                }
            }
            System.out.println();
        }
    }









}
