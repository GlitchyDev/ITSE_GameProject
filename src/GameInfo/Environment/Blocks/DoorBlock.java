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
    // The assigned sprites
    private Image spriteOpen;
    private Image spriteClosed;

    public DoorBlock(GlobalGameData globalGameData)
    {
        entities = new ArrayList<>();
        blockType = BlockTypeEnum.DOOR_CLOSED;
        spriteClosed = globalGameData.getSprite("Test_Door_Closed");
        spriteOpen = globalGameData.getSprite("Test_Door_Open");
    }

    public DoorBlock(GlobalGameData globalGameData, BlockTypeEnum type)
    {
        entities = new ArrayList<>();
        blockType = type;
        spriteClosed = globalGameData.getSprite("Test_Door_Closed");
        spriteOpen = globalGameData.getSprite("Test_Door_Open");
    }

    @Override
    public void tickBlock(World world) {

    }

    @Override
    public void renderBlock(Canvas canvas, GraphicsContext gc, double x, double y, int renderLayer) {
        if(renderLayer == 1) {
            if (blockType == BlockTypeEnum.DOOR_OPEN)
                gc.drawImage(spriteOpen, (int) (x * World.getScaledUpSquareSize() + 0.5), (int) (y * World.getScaledUpSquareSize() + 0.5) - World.getScaledUpSquareSize() / 4 * 3);
            else
                gc.drawImage(spriteClosed, (int) (x * World.getScaledUpSquareSize() + 0.5), (int) (y * World.getScaledUpSquareSize() + 0.5) - World.getScaledUpSquareSize() / 4 * 3);
        }
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
