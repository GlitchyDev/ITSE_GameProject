package GameInfo.Environment;

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
