package GameInfo.Environment.Blocks;

import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/* Created by Charlie on 2017-09-29
 * This class implements a door block which starts closed by default,
 * but opens if the player moves toward it. Currently, the door can only face down.
 */

public class DoorBlock extends BlockBase {
    // Entities currently contained
    private ArrayList<EntityBase> entities;
    // The assigned sprite
    private Image sprite;

    public DoorBlock(GlobalGameData globalGameData)
    {
        entities = new ArrayList<>();
        blockType = BlockTypeEnum.DOOR_CLOSED;
        sprite = globalGameData.getSprite("Test_Door_Closed");
    }

    public DoorBlock(GlobalGameData globalGameData, BlockTypeEnum type)
    {
        entities = new ArrayList<>();

        if(type == BlockTypeEnum.DOOR_OPEN)
        {
            blockType = BlockTypeEnum.DOOR_OPEN;
            sprite = globalGameData.getSprite("Test_Door_Open");
        }
        else
        {
            blockType = BlockTypeEnum.DOOR_CLOSED;
            sprite = globalGameData.getSprite("Test_Door_Closed");
        }
    }

    @Override
    public void renderBlock(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        gc.drawImage(sprite,(int)(x * World.getScaledUpSquareSize() + 0.5), (int)(y * World.getScaledUpSquareSize() + 0.5) - World.getScaledUpSquareSize()/4*3);
    }

    @Override
    public boolean checkCollision(World world, EntityBase entity) {

        if(blockType == BlockTypeEnum.DOOR_CLOSED) {
            blockType = BlockTypeEnum.DOOR_OPEN;
            System.out.println("Door opened");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void enterBlock(EntityBase entity) {
        entities.add(entity);
    }

    @Override
    public void exitBlock(EntityBase entity) {
        entities.remove(entity);
        blockType = BlockTypeEnum.DOOR_CLOSED;
        System.out.println("Door closed");
    }




}
