package GameInfo.Environment.Blocks;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import GameInfo.WorldViewport;
import RenderingHelpers.LightSpriteCreatorHelper;
import RenderingHelpers.RadiantLightProducer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Created by Robert on 8/27/2017.
 *
 * The purpose of this class is
 * - Provide underlying flow and logic of all Block objects in the game
 * - Serve as a common base for all Block extensions
 * - Serve as a physical manifestation of the terrain
 */
public abstract class BlockBase {
    protected BlockTypeEnum blockType;
    protected int currentLightLevel;
    protected int previousLightLevel;
    protected boolean isCurrentlyLit;


    public BlockBase()
    {
        blockType = BlockTypeEnum.TEST_FLOOR;
        currentLightLevel = 0;
        previousLightLevel = 0;
        isCurrentlyLit = false;

    }

    /**
     * Any logic needed to be preformed on the block is done here
     * @param world
     */
    public abstract void tickBlock(World world);

    /**
     * All Rendering for the Block is called here
     * @param canvas
     * @param gc
     * @param x
     * @param y
     * @param renderLayer
     */
    public abstract void renderBlock(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer);

    /**
     * Recalculates the light for this block, setting back to default for a light source to modify
     */
    public void recalculateLight()
    {
        previousLightLevel = currentLightLevel;
        if(isCurrentlyLit) {
            isCurrentlyLit = false;
            currentLightLevel = 0;
        }
    }




    /**
     * This method will tell if the Specified entity can enter this block, further triggering ExitBlock & EnterBlock
     * @param entity Entity who is attempting to enter the space
     * @return If the entity is allowed to move to this space
     */
    public abstract boolean checkCollision(World world, EntityBase entity);

    /**
     * This method triggers when an Entity gets the Collision Ok, and is now entering this block
     * @param entity
     */
    public abstract void enterBlock(EntityBase entity);

    /**
     * This method triggers when an Entity gets the Collision Ok, and is now exiting this block
     * @param entity
     */
    public abstract void exitBlock(EntityBase entity);

    public BlockTypeEnum getBlockType() {
        return blockType;
    }

    public void setCurrentlyLit(boolean currentlyLit) {
        isCurrentlyLit = currentlyLit;
    }

    public boolean isCurrentlyLit()
    {
        return isCurrentlyLit;
    }

    public void setCurrentLightLevel(int currentLightLevel) {

        this.currentLightLevel = currentLightLevel;
    }

    public int getCurrentLightLevel()
    {
        return currentLightLevel;
    }

    public int getPreviousLightLevel()
    {
        return previousLightLevel;
    }

    /**
     * A utility method that allows for blocks to properly draw, regardless of sprite size and in line with the Viewport Smoothing
     * @param sprite
     * @param gc
     * @param x
     * @param y
     * @param xOffset
     * @param yOffset
     * @param useLight
     */
    public void drawSpriteAtXY(Image sprite, GraphicsContext gc, double x, double y, double xOffset, double yOffset, boolean useLight)
    {
        if(useLight)
        {
            if(previousLightLevel != 0) {
                gc.drawImage(sprite,(int)(x * World.getScaledUpSquareSize() + 0.5 + xOffset + WorldViewport.widthBuffer), (int)(y * World.getScaledUpSquareSize() + 0.5 + yOffset + WorldViewport.heightBuffer)  );
                Image shadow = LightSpriteCreatorHelper.createShadow(sprite);
                gc.setGlobalAlpha(RadiantLightProducer.determineDarkness(previousLightLevel));
                gc.drawImage(shadow, (int) (x * World.getScaledUpSquareSize() + 0.5 + xOffset + WorldViewport.widthBuffer), (int) (y * World.getScaledUpSquareSize() + 0.5 + yOffset + WorldViewport.heightBuffer));
                gc.setGlobalAlpha(1.0);
            }
            else
            {
                gc.setFill(Color.BLACK);
                drawRectangleAtXY(gc,x,y,0,0,World.getScaledUpSquareSize(),World.getScaledUpSquareSize());
            }
        }
        else
        {
            gc.drawImage(sprite,(int)(x * World.getScaledUpSquareSize() + 0.5 + xOffset + WorldViewport.widthBuffer), (int)(y * World.getScaledUpSquareSize() + 0.5 + yOffset + WorldViewport.heightBuffer)  );

        }
    }


    public void drawRectangleAtXY(GraphicsContext gc, double x, double y, int xOffset, int yOffset, double width, double height)
    {
        gc.fillRect((int)(x * World.getScaledUpSquareSize() + 0.5 + xOffset) + WorldViewport.widthBuffer, (int)(y * World.getScaledUpSquareSize() + 0.5 + yOffset) + WorldViewport.heightBuffer,width,height  );
    }
}
