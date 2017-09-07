package GameInfo.Environment.Entities;

import GameInfo.Environment.Chunk;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Robert on 8/27/2017.
 * An EntityBase within the World, thats not a block
 */
public abstract class EntityBase {
    protected int x;
    protected int y;

    public EntityBase(int x, int y)
    {
        this.x = x;
        this.y = y;
}

    public abstract void tickEntity(GlobalGameData globalGameData, World world);

    public abstract void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer);



    public void moveRelative(World world, int relativeX, int relativeY)
    {
        Chunk oldChunk = world.getChunkFromCordXY(x, y);
        Chunk newChunk = world.getChunkFromCordXY(x + relativeX, y + relativeY);
        if(oldChunk == newChunk)
        {
            x += relativeX;
            y += relativeY;
        }
        else
        {
            newChunk.getEntities().add(this);
            oldChunk.getEntities().remove(this);
            x += relativeX;
            y += relativeY;
        }

    }

    public void moveAbsolute(World world, int absoluteX, int absoluteY)
    {
        Chunk oldChunk = world.getChunkFromCordXY(x, y);
        Chunk newChunk = world.getChunkFromCordXY(absoluteX, absoluteY);
        if(oldChunk == newChunk)
        {
            x = absoluteX;
            y = absoluteY;
        }
        else
        {
            newChunk.getEntities().add(this);
            oldChunk.getEntities().remove(this);
            x = absoluteX;
            y = absoluteY;
        }
    }

    public int getX(){return x;}
    public int getY(){return y;}
    public void setX(int x){this.x = x;}
    public void setY(int y){this.y = y;}
}
