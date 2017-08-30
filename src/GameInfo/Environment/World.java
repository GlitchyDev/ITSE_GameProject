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
        if(z == 0)
        {
            return 0;
        }
        else
        {
            return z * 100;
        }
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

    public BlockBase[][] addBlocksInsideChunk(Chunk chunk, BlockBase[][] blocks, int x1, int y1, int x2, int y2) {
        int upperLeftX = getPosNumFromChunkNum(getChunkNumfromCordNum(x1));
        int upperLeftY = getPosNumFromChunkNum(getChunkNumfromCordNum(y1));
        System.out.println("Upper Left for Chunk " + upperLeftX + "," + upperLeftY);
        System.out.println("Upper Left for Search " + x1 + "," + y1);
        System.out.println("Lower Right for Search " + x2 + "," + y2);



        // X1 Y1 represent the position of upper left corner of the search, X2 Y2 The lower left
        // UpperRightX represents the position upper Chunk corner, we need to limit our search to inside the chunk bounderies

        for (int x = x1; x <= upperLeftX && x > upperLeftX - World.getChunkSize() && x > x2; x--)
        {
            System.out.println("Nothing is going through");
            for (int y = y1; y <= upperLeftY && y > upperLeftY - World.getChunkSize() && y > y2; x--)
            {
                // Assuming this is right, this means the blocked search is inside the search grid
                blocks[x1 - x][y1 - y] = chunk.getBlockBaseList()[upperLeftX - x][upperLeftY - y];
            }
        }


        return blocks;
    }

    public void viewBlocks(BlockBase[][] blocks)
    {
        for(int x = 0; x < blocks.length; x++)
        {
            for(int y = 0; y < blocks[x].length; y++)
            {
                if(blocks[x][y] == null)
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
