package GameInfo.Environment.Blocks;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import RenderingHelpers.RadiantLightProducer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by Robert on 8/28/2017.
 * This class aims to
 * - Simulate basic "Wall" "Floor" logic, and handles the random generation of both in the test map
 */
public class WallFloorBlock extends BlockBase {
    // Entities currently contained
    private ArrayList<EntityBase> entities;
    // The assigned Sprite, Wall or Floor
    private Image sprite;
    // For Wall, it requires a secondary sprite in order to work

    public WallFloorBlock(GlobalGameData globalGameData)
    {
        entities = new ArrayList<>();
        if(globalGameData.getRandom().nextInt(6) == 1)
        {
            blockType = BlockTypeEnum.TEST_WALL;
            sprite = globalGameData.getSprite("Test_Wall");
        }
        else
        {
            blockType = BlockTypeEnum.TEST_FLOOR;
            sprite = globalGameData.getSprite("Test_Floor_" + (globalGameData.getRandom().nextInt(6) + 1));
        }
    }

    public WallFloorBlock(GlobalGameData globalGameData, BlockTypeEnum type)
    {
        entities = new ArrayList<>();

        if(type == BlockTypeEnum.TEST_WALL)
        {
            blockType = BlockTypeEnum.TEST_WALL;
            sprite = globalGameData.getSprite("Test_Wall");
        }
        else
        {
            blockType = BlockTypeEnum.TEST_FLOOR;
            sprite = globalGameData.getSprite("Test_Floor");

        }
    }

    @Override
    public void tickBlock(World world)
    {




    }

    @Override
    public void renderBlock(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 0)
        {
            if (blockType == BlockTypeEnum.TEST_FLOOR) {
                drawSpriteAtXY(sprite, gc, x, y, 0, 0, true);


                /*
                if(entities.size() > 0) {
                    gc.setGlobalAlpha(0.1);
                    gc.setFill(Color.BLUE);
                    drawRectangleAtXY(gc, x, y, 0, 0, 40, 40);
                    gc.setGlobalAlpha(1.0);
                }
                */
                recalculateLight();

            }
        }
        if(renderLayer == 1) {
            if (blockType == BlockTypeEnum.TEST_WALL) {

                drawSpriteAtXY(sprite, gc, x, y, 0, -World.getScaledUpSquareSize() / 4 * 3 + 2,true);
                recalculateLight();
            }
        }



    }

    @Override
    public boolean checkCollision(World world, EntityBase entity) {

        if(entities.size() > 0)
        {
            for(EntityBase eb: entities)
            {
                // Entity re-acting to colision events here
                // Return here as well
            }
        }
        return blockType == BlockTypeEnum.TEST_FLOOR && (entities.size() == 0|| entities.contains(entity));

    }

    //These methods allow you to set the sprites the block uses
    public void setSprite(GlobalGameData globalGameData, String spriteName)
    {
        sprite = globalGameData.getSprite(spriteName);
    }

    @Override
    public void enterBlock(EntityBase entity) {
        entities.add(entity);
    }

    @Override
    public void exitBlock(EntityBase entity) {
        entities.remove(entity);
    }


}
