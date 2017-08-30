package GameInfo.Environment;

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










}
