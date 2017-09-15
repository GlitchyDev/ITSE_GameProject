package GameInfo.Environment.Blocks;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import sample.TestRenderHelper;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Robert on 8/28/2017.
 */
public class WallFloorBlock extends BlockBase {
    private ArrayList<EntityBase> entities;
    private Image sprite;
    private Image secondarySprite;

    public WallFloorBlock(GlobalGameData globalGameData)
    {
        entities = new ArrayList<>();


        if(globalGameData.getRandom().nextInt(4) == 1)
        {
            blockType = BlockTypeEnum.TEST_WALL;
            sprite = TestRenderHelper.resample(globalGameData.getSprite("Test_Wall_Modified"),2);
            secondarySprite = TestRenderHelper.resample(globalGameData.getSprite("Test_Wall_Top"),2);
        }
        else
        {
            blockType = BlockTypeEnum.TEST_FLOOR;
            sprite = TestRenderHelper.resample(globalGameData.getSprite("Test_Floor"),2);

        }
    }

    public WallFloorBlock(GlobalGameData globalGameData, BlockTypeEnum type)
    {
        entities = new ArrayList<>();

        if(type == BlockTypeEnum.TEST_WALL)
        {
            blockType = BlockTypeEnum.TEST_WALL;
            sprite = TestRenderHelper.resample(globalGameData.getSprite("Test_Wall_Modified"),2);
            secondarySprite = TestRenderHelper.resample(globalGameData.getSprite("Test_Wall_Top"),2);
        }
        else
        {
            blockType = BlockTypeEnum.TEST_FLOOR;
            sprite = TestRenderHelper.resample(globalGameData.getSprite("Test_Floor"),2);

        }
    }

    @Override
    public void renderBlock(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 0)
        {
            if (blockType == BlockTypeEnum.TEST_FLOOR) {
                gc.drawImage(sprite,(int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5));
                if(entities.size() > 0)
                {
                    gc.setFill(Color.BLUE);
                    gc.fillRect((x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5), 5,5);
                }
            }
        }
        if(renderLayer == 1) {
            if (blockType == BlockTypeEnum.TEST_WALL) {
                gc.drawImage(sprite,(int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5) + 10);
                gc.drawImage(secondarySprite,(int)(x * World.getUnitRatio() + 0.5), (int)(y * World.getUnitRatio() + 0.5) - 30);


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

    @Override
    public void enterBlock(EntityBase entity) {
        entities.add(entity);
    }

    @Override
    public void exitBlock(EntityBase entity) {
        entities.remove(entity);
    }
}
