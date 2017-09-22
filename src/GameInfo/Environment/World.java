package GameInfo.Environment;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.DebugBlock;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Pathfinding.PathfindingMap;
import GameInfo.Environment.Entities.Pathfinding.Position;
import GameInfo.Environment.Entities.Pro_Player;
import GameInfo.Environment.Entities.TestSkullEntity;
import GameInfo.Environment.Structures.StructureBase;
import GameInfo.GlobalGameData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Robert on 8/27/2017.
 * This class is designed to
 * - Host the "World" that gets rendered to the screen
 * - Holds Chunks
 * - Controls ChunkSize
 * - Structures
 * - EntityBase
 * - Players
 *
 */
public class World {
    // How many pixels a standard square should take up
    private static int scaleUpPercent = 2;
    private static int standardSquareSize = 20;
    // How large a chunk should be ( 100 is ideal! )
    private static int chunkSize = 100;
    private HashMap<String,Chunk> chunks;
    private ArrayList<StructureBase> structures;


    private GlobalGameData globalGameData;

    public static int getScaleUpPercent() {
        return scaleUpPercent;
    }
    public static int getScaledUpSquareSize() {
        return scaleUpPercent * standardSquareSize;
    }

    public static int getChunkSize() {
        return chunkSize;
    }

    public World(GlobalGameData globalGameData)
    {
        chunks = new HashMap<>();
        structures = new ArrayList<>();
        this.globalGameData = globalGameData;
        //getChunkFromChunkXY(0,0);


    }


    /**
     * Grabs the Starting Position of the Chunk Number specified
     * @param z
     * @return
     */
    public static int getPosNumFromChunkNum(int z)
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

    /**
     * Grabs a Chunk from its Relative Cords
     * @param x
     * @param y
     * @return
     */
    public Chunk getChunkFromChunkXY(int x, int y)
    {
        if(!chunks.containsKey(x + "," + y))
        {
           chunks.put(x + "," + y, new Chunk(globalGameData, this, x, y));
           chunks.get(x + "," + y).generateStructures(globalGameData, this, x, y);
        }
        return chunks.get(x + "," + y);
    }

    /**
     * For the Lazy, this method grabs the Chunk that occupies the specified Cords
     * @param x
     * @param y
     * @return
     */
    public Chunk getChunkFromCordXY(int x, int y)
    {
        return getChunkFromChunkXY(getChunkNumfromCordNum(x),getChunkNumfromCordNum(y));
    }


    /**
     * Grabs the Block at a specified cords, sparing the hassle of tracking down the owning chunk
     * @param x
     * @param y
     * @return
     */
    public BlockBase getBlockFromCords(int x, int y)
    {
        int chunkX = getChunkNumfromCordNum(x);
        int chunkY = getChunkNumfromCordNum(y);
        int chunkStartX = getPosNumFromChunkNum(chunkX);
        int chunkStartY = getPosNumFromChunkNum(chunkY);
        Chunk chunk = getChunkFromChunkXY(chunkX, chunkY);

        return chunk.getBlockBaseList()[x - chunkStartX][y - chunkStartY];
    }

    /**
     * Sets the Block at a specified cords, sparing the hassle of tracking down the owning chunk
     * @param x
     * @param y
     * @param block
     */
    public void setBlockFromCords(int x, int y, BlockBase block)
    {
        int chunkX = getChunkNumfromCordNum(x);
        int chunkY = getChunkNumfromCordNum(y);
        int chunkStartX = getPosNumFromChunkNum(chunkX);
        int chunkStartY = getPosNumFromChunkNum(chunkY);
        Chunk chunk = getChunkFromChunkXY(chunkX, chunkY);
        chunk.getBlockBaseList()[x - chunkStartX][y - chunkStartY] = block;
    }


    /**
     * Gets the Chunks Relative Cord from a regular cord
     * @param z
     * @return
     */
    public static int getChunkNumfromCordNum(int z)
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

    /**
     * This adds all blocks inside the specified square to their relative positions in the 2D array
     * - Best used for Pathfinding and Viewport Rendering
     * @param chunk
     * @param chunkX
     * @param chunkY
     * @param blocks
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void addBlocksInsideChunk(Chunk chunk, int chunkX, int chunkY, BlockBase[][] blocks, int x1, int y1, int x2, int y2) {

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

    public void addEntityToWorld(EntityBase entityBase)
    {
       getChunkFromCordXY(entityBase.getX(),entityBase.getY()).getEntities().add(entityBase);
    }

    public boolean isEntityAtPos(int x, int y)
    {
        Chunk chunk = getChunkFromCordXY(x,y);
        for(EntityBase entity: chunk.getEntities())
        {
            if(entity.getX() == x && entity.getY() == y)
            {
                return true;
            }
        }
        return false;
    }

    public ArrayList<EntityBase> getEntitiesAtPos(int x, int y)
    {
        Chunk chunk = getChunkFromCordXY(x,y);
        ArrayList<EntityBase> entities = new ArrayList<>();
        for(EntityBase entity: chunk.getEntities())
        {
            if(entity.getX() == x && entity.getY() == y)
            {
                entities.add(entity);
            }
        }
        return entities;
    }


    public ArrayList<StructureBase> getStructures()
    {
        return structures;
    }









}
