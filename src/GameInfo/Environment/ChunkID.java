package GameInfo.Environment;

/**
 * This class exists to simplify the art of Chunk Cords for longterm storage
 */
public class ChunkID {
    private int chunkX;
    private int chunkY;

    public ChunkID(int chunkX,int chunkY)
    {
        this.chunkX = chunkX;
        this.chunkY = chunkY;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }
}
