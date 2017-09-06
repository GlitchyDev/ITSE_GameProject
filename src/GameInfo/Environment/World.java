package GameInfo.Environment;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Entities.EntityBase;
import GameInfo.GlobalGameData;
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
    private static int unitRatio = 30;
    private static int chunkSize = 100;
    private HashMap<String,Chunk> chunks;
    private GlobalGameData globalGameData;

    public static int getUnitRatio() {
        return unitRatio;
    }
    public static int getChunkSize() {
        return chunkSize;
    }

    public World(GlobalGameData globalGameData)
    {
        chunks = new HashMap<>();
        this.globalGameData = globalGameData;
        chunks.put(0 + "," + 0,new Chunk(globalGameData,this,0,0));
    }


    public int getPosNumFromChunkNum(int z)
    {
        if(z == 0)
        {
            return 0;
        }
        else {
            if(z >= 0)
            {
                return z * World.getChunkSize();
            }
            else
            {
                return z * World.getChunkSize() + 1;
            }

        }
    }

    public Chunk getChunkFromChunkXY(int x, int y)
    {
        if(!chunks.containsKey(x + "," + y))
        {
           chunks.put(x + "," + y, new Chunk(globalGameData, this, x, y));
        }
        return chunks.get(x + "," + y);
    }
    public Chunk getChunkFromCordXY(int x, int y)
    {
        return getChunkFromChunkXY(getChunkNumfromCordNum(x),getChunkNumfromCordNum(y));
    }

    public BlockBase getBlockFromCords(int x, int y)
    {
        int chunkX = getChunkNumfromCordNum(x);
        int chunkY = getChunkNumfromCordNum(y);
        System.out.println();
        System.out.println("Chunk X " + chunkX + " Y" + chunkY);
        System.out.println("Chunk Start X " + getPosNumFromChunkNum(chunkX) + " Y" + getPosNumFromChunkNum(chunkY));
        System.out.println("Pos X " + x + " Y" + y);
        int chunkStartX = getPosNumFromChunkNum(chunkX);
        int chunkStartY = getPosNumFromChunkNum(chunkY);
        Chunk chunk = getChunkFromChunkXY(chunkX, chunkY);

        return chunk.getBlockBaseList()[x - chunkStartX][y - chunkStartY];

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
            if(isNeg)
            {
                return z / World.getChunkSize() + -1;
            }
            else {
                return z / World.getChunkSize();
            }
        }

    }

    public void addBlocksInsideChunk(Chunk chunk, int chunkX, int chunkY, BlockBase[][] blocks, int x1, int y1, int x2, int y2) {

        /*
        System.out.println("Corner: X: " + chunkX + " Y: " + chunkY);
        System.out.println("CornerCH: X: " + getPosNumFromChunkNum(chunkX) + " Y: " + getPosNumFromChunkNum(chunkY));
        System.out.println("1: X: " + x1 + " Y: " + y1);
        System.out.println("2: X: " + x2 + " Y: " + y2);
        */

        for(int x = getPosNumFromChunkNum(chunkX); x <= getPosNumFromChunkNum(chunkX) + World.getChunkSize() - 1; x++)
        {
            for(int y = getPosNumFromChunkNum(chunkY); y <= getPosNumFromChunkNum(chunkY) + World.getChunkSize() - 1; y++)
            {
                if(x1 >= x && x2 <= x)
                {
                    if(y1 >= y && y2 <= y)
                    {
                        blocks[x1 - x][y1 - y] = chunk.getBlockBaseList()[x - getPosNumFromChunkNum(chunkX)][y - getPosNumFromChunkNum(chunkY)];
                    }
                }
            }
        }

    }










}
