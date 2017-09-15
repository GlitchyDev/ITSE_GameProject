package GameInfo.Environment.Entities.AbstractClasses;

import GameInfo.Environment.Chunk;
import GameInfo.Environment.Entities.Enums.EntityType;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Robert on 8/27/2017.
 * An EntityBase within the World, thats not a block
 */
public abstract class EntityBase {
    protected World world;
    protected GlobalGameData globalGameData;
    protected EntityType entityType;
    protected int x;
    protected int y;

    public EntityBase(World world, GlobalGameData globalGameData, int x, int y)
    {
        this.world = world;
        this.globalGameData = globalGameData;
        this.x = x;
        this.y = y;
    }



    public abstract void tickEntity();

    public abstract void renderEntity(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer);



    public void moveRelative(int relativeX, int relativeY)
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

    public void moveAbsolute(int absoluteX, int absoluteY)
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

    public boolean advancedMoveRelative(int relativeX, int relativeY, boolean doCheckColisions, boolean doStructureEvents, boolean doGenerateEnterEvent, boolean doGenerateExitEvent)
    {
        Chunk oldChunk = world.getChunkFromCordXY(x, y);
        Chunk newChunk = world.getChunkFromCordXY(x + relativeX, y + relativeY);
        if(oldChunk != newChunk)
        {
            newChunk.getEntities().add(this);
            oldChunk.getEntities().remove(this);
        }
        if(doCheckColisions)
        {
            if(doStructureEvents)
            {
                if(newChunk.isStructureAtRelative(x + relativeX, y + relativeY))
                {
                    switch(newChunk.getStructureAtPos(x + relativeX, y + relativeY).checkCollision(x + relativeX, y + relativeY))
                    {
                        case CAN_NOT_MOVE_DEFINATE:
                            return false;
                        case CAN_MOVE:
                            if (!(world.getBlockFromCords(x + relativeX, y + relativeY).checkCollision(world, this))) {
                                return false;
                            }
                            break;
                    }
                }
                else
                {
                    if (!(world.getBlockFromCords(x + relativeX, y + relativeY).checkCollision(world, this))) {
                        return false;
                    }
                }
            }
            else {
                if (!(world.getBlockFromCords(x + relativeX, y + relativeY).checkCollision(world, this))) {
                    return false;
                }
            }
        }
        if(doGenerateEnterEvent)
        {
            if(doStructureEvents) {
                //System.out.println("We should be finding shit!");
                if (newChunk.isStructureAtRelative(x + relativeX, y + relativeY)) {
                    //System.out.println("We found shit!");
                    newChunk.getStructureAtPos(x + relativeX, y + relativeY).enterEvent(x + relativeX, y + relativeY);
                }
            }
            world.getBlockFromCords(x, y).exitBlock(this);
        }
        if(doGenerateExitEvent)
        {
            if(doStructureEvents) {
                if (newChunk.isStructureAtRelative(x + relativeX, y + relativeY)) {
                    newChunk.getStructureAtPos(x + relativeX, y + relativeY).exitEvent(x + relativeX, y + relativeY);
                }
            }
            world.getBlockFromCords(x + relativeX, y + relativeY).enterBlock(this);
        }
        x += relativeX;
        y += relativeY;
        return true;
    }

    public double distanceFromEntity(EntityBase entity)
    {
        return Math.hypot(x-entity.getX(), y-entity.getY());
    }
    public double distanceFromLocation(int x, int y)
    {
        return Math.hypot(this.x-x, this.y-y);
    }

    public int getX(){return x;}
    public int getY(){return y;}
    public void setX(int x){this.x = x;}
    public void setY(int y){this.y = y;}
}
