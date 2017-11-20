package GameStates;

import GameInfo.Environment.Blocks.*;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameStates.Enums.GameStateEnum;
import HardwareAdaptors.ControllerType;
import HardwareAdaptors.XBoxController;
import RenderingHelpers.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import Utility.Main;

import java.util.UUID;

public class TutorialGameState extends GameStateBase {
    private final int[][] drawnMap = new int[][]{
            {2,2,2,2,2,1,1,1,1,1,1,1,1,1,1},
            {2,0,0,0,2,0,0,1,1,1,1,1,1,1,1},
            {2,0,0,0,2,0,0,0,1,0,0,1,1,1,1},
            {2,2,3,1,2,0,0,0,0,0,0,0,0,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,4,1,1},
            {1,1,0,0,0,0,0,0,0,0,4,4,4,4,1},
            {1,1,0,0,0,0,0,0,0,4,4,4,4,4,1},
            {1,1,1,0,0,0,0,0,0,0,4,4,4,1,1},
            {1,1,1,1,0,0,0,0,0,0,0,1,1,1,1},
            {1,1,1,1,1,1,0,0,0,0,1,1,1,1,1},
            {1,1,1,1,1,0,0,0,0,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
    };
    private final UUID uuid = UUID.randomUUID();
    private BlockBase[][] blocks = new BlockBase[15][15];
    private long startTime = System.currentTimeMillis();
    private int currentLight = 4;
    private XBoxController controller;
    private int currentImage = 0;
    private boolean switchState = false;
    private long clickTime = 0;


    public TutorialGameState(GlobalGameData globalGameData) {
        super(globalGameData);

        for(int x = 0; x < 15; x++)
        {
            for(int y = 0; y < 15; y++)
            {
                switch(drawnMap[x][y])
                {
                    case 0:
                        blocks[x][y] = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                        break;
                    case 1:
                        blocks[x][y] = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_WALL);
                        break;
                    case 2:
                        blocks[x][y] = new HouseWall(globalGameData);
                        break;
                    case 3:
                        blocks[x][y] = new DoorBlock(globalGameData);
                        break;
                    case 4:
                        blocks[x][y] = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                        ((WallFloorBlock)blocks[x][y]).setSprite(globalGameData,"Test_Floor_6");
                        break;
                    default:
                        blocks[x][y] = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                        break;
                }
            }
            PlayerSkinCreator.generateSkin(uuid,globalGameData);
            controller = globalGameData.getConnectedControllers().get(0);
            startTime = System.currentTimeMillis();
            switchState = false;
            clickTime = 0;
        }
    }

    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {
        if(switchState)
        {
            if((System.currentTimeMillis() - clickTime)/1000.0 > 1.0)
            {
                globalGameData.switchGameState(GameStateEnum.TitleScreen);
            }
        }
        else
        {
            controller.poll();
            if(controller.isAnyThingPressed())
            {
                switchState = true;
                clickTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    protected void doRender(Canvas canvas, GraphicsContext gc) {

        RadiantLightProducer.produceLight(7,7,currentLight,blocks);
        RadiantLightProducer.produceLight(1,1,3,blocks);

        for(int renderLayer = 0; renderLayer < 5; renderLayer++) {
            for (int y = 0; y < drawnMap[0].length; y++) {
                for (int x = 0; x < drawnMap.length; x++) {

                    blocks[y][x].renderBlock(canvas, gc, x, y, renderLayer);
                }
            }
            if (renderLayer == 0) {
                drawFakeItem(gc);
            }
            if (renderLayer == 2) {
                drawFakePlayer(gc);
                drawFakeEnemies(gc);
            }
            if (renderLayer == 3) {
                if (controller.getControllerType() == ControllerType.XBoxController) {
                    TextRenderHelper.drawCenteredText(300, 210, "Use [RT] to toggle light", gc, globalGameData);
                    TextRenderHelper.drawCenteredText(300, 310, "Use [Direction Pad] to move", gc, globalGameData);

                } else {
                    TextRenderHelper.drawCenteredText(300, 210, "Use [0] to toggle light", gc, globalGameData);
                    TextRenderHelper.drawCenteredText(300, 310, "Use [Arrow Keys] to move", gc, globalGameData);
                }

                // Collect Items
                TextRenderHelper.drawCenteredText(120, 95, "Collect for extra points", gc, globalGameData);
                TextRenderHelper.drawCenteredText(120, 115, "And some health!", gc, globalGameData);



                TextRenderHelper.drawText(350, 95, "The Alsi hates light", gc, globalGameData);
                TextRenderHelper.drawText(350, 115, "The Skulls love it", gc, globalGameData);
                TextRenderHelper.drawText(350, 135, "When you hear the music, run", gc, globalGameData);


                //TextRenderHelper.drawCenteredText(100, 95, "Collect for extra points", gc, globalGameData);


                // Exit
                TextRenderHelper.drawCenteredText(430, 550, "Remember to stay alive", gc, globalGameData);

                TextRenderHelper.drawCenteredText(430, 570, " Press [Anything] to return to Menu", gc, globalGameData);

            }
        }


        double duration = 0.0;
        if(switchState)
        {
            duration = (System.currentTimeMillis() - clickTime)/1000.0;

        }
        else
        {
           duration = 1.0 - (System.currentTimeMillis() - startTime)/1000.0;
        }
        if(duration < 1.0)
        {
            gc.setFill(Color.BLACK);
            gc.setGlobalAlpha(duration);
            gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        }
    }
    public void drawFakeEnemies(GraphicsContext gc) {
        int random = (int) (Math.random() * 10);
        if (random == 0) {
            currentImage = (int) (Math.random() * 4);
        }
        int xOffset = ImageRenderHelper.findCenterXMod(globalGameData.getSprite("Face_Static_1"));
        int yOffset = ImageRenderHelper.findCenterYMod(globalGameData.getSprite("Face_Static_1"));

        gc.setGlobalAlpha(0.8);
        switch (currentImage) {
            case 0:
                gc.drawImage(globalGameData.getSprite("Face_Static_1"), 8 * World.getScaledUpSquareSize() + xOffset, 10 * World.getScaledUpSquareSize() + yOffset);
                break;
            case 1:
                gc.drawImage(globalGameData.getSprite("Face_Static_2"), 8 * World.getScaledUpSquareSize() + xOffset, 10 * World.getScaledUpSquareSize() + yOffset);
                break;
            case 2:
                gc.drawImage(globalGameData.getSprite("Face_Static_3"), 8 * World.getScaledUpSquareSize() + xOffset, 10 * World.getScaledUpSquareSize() + yOffset);
                break;
            case 3:
                gc.drawImage(globalGameData.getSprite("Face_Static_4"), 8 * World.getScaledUpSquareSize() + xOffset, 10 * World.getScaledUpSquareSize() + yOffset);
                break;
        }
        gc.setGlobalAlpha(1.0);
        xOffset = ImageRenderHelper.findCenterXMod(globalGameData.getSprite("Active_Skull_Forward"));
        yOffset = ImageRenderHelper.findCenterYMod(globalGameData.getSprite("Active_Skull_Forward"));


        if (blocks[8][4].getPreviousLightLevel() > 3) {
            gc.drawImage(globalGameData.getSprite("Active_Skull_Forward"), 8 * World.getScaledUpSquareSize() + xOffset, 4 * World.getScaledUpSquareSize() + yOffset);
        }
        else
        {
            gc.drawImage(globalGameData.getSprite("Skull_Forward"), 8 * World.getScaledUpSquareSize() + xOffset, 4 * World.getScaledUpSquareSize() + yOffset);
        }
        Image shadow1 = LightSpriteCreatorHelper.createShadow(globalGameData.getSprite("Active_Skull_Forward"));
        gc.setGlobalAlpha(RadiantLightProducer.determineDarkness(blocks[8][4].getPreviousLightLevel()));
        gc.drawImage(shadow1,8 * World.getScaledUpSquareSize() + xOffset,4 * World.getScaledUpSquareSize() + yOffset);
        gc.setGlobalAlpha(1.0);



        xOffset = ImageRenderHelper.findCenterXMod(globalGameData.getSprite("Alsi_Front"));
        yOffset = ImageRenderHelper.findCenterYMod(globalGameData.getSprite("Alsi_Front"));



        String direction = "Front";
        double progress = (((System.currentTimeMillis()-startTime)/1000.0)% 1.5);
        if(progress < 0.4)
        {
            direction = "Front";
        }
        else
        {
            if(progress < 0.8)
            {
                direction = "Left";
            }
            else
            {
                if(progress < 1.2)
                {
                    direction = "Back";
                }
                else
                {
                    direction = "Right";
                }
            }
        }
        String eyes = "";
        if (Main.blinking) {
            double duration = (System.currentTimeMillis() - Main.lastBlinkStartTime) / 1000.0;
            if (duration < 0.2) {
                eyes = "_Blink_1";
            } else {
                if (duration < 0.4) {
                    eyes = "_Blink_2";
                } else {
                    if (duration < 0.6) {
                        eyes = "_Blink_1";
                    }
                }
            }
        }


        for(int i = 0; i < 20; i++) {
            int xOffset2 = (int) (Math.random() * 12);
            xOffset2 = (int) (xOffset2 - 12.0 / 2.0);
            int yOffset2 = (int) (Math.random() * 6);
            yOffset2 = (int) (yOffset2 - 6.0 / 2.0);

            if(blocks[5][5].getPreviousLightLevel() < 2) {
                gc.setGlobalAlpha(0.02);
                gc.drawImage(globalGameData.getSprite("Alsi_" + direction + eyes), 5 * World.getScaledUpSquareSize() + xOffset + xOffset2, 4 * World.getScaledUpSquareSize() + yOffset + yOffset2);
                Image shadow2 = LightSpriteCreatorHelper.createShadow(globalGameData.getSprite("Alsi_Front"));
                gc.setGlobalAlpha(RadiantLightProducer.determineDarkness(blocks[5][5].getPreviousLightLevel()) * 0.02);
                gc.drawImage(shadow2, 5 * World.getScaledUpSquareSize() + xOffset + xOffset2, 4 * World.getScaledUpSquareSize() + yOffset + yOffset2);
                gc.setGlobalAlpha(1.0);
            }
        }


    }

    public void drawFakeItem(GraphicsContext gc)
    {
        Image sprite1 = globalGameData.getSprite("ScoreItem_Pawn");


        double xOffset = ImageRenderHelper.findCenterXMod(sprite1);
        double yOffset = ImageRenderHelper.findCenterYMod(sprite1);
        gc.drawImage(sprite1,2 * World.getScaledUpSquareSize() + xOffset,1 * World.getScaledUpSquareSize() + yOffset);
        Image shadow1 = LightSpriteCreatorHelper.createShadow(sprite1);
        gc.setGlobalAlpha(RadiantLightProducer.determineDarkness(blocks[1][2].getPreviousLightLevel()));
        gc.drawImage(shadow1,2 * World.getScaledUpSquareSize() + xOffset,1 * World.getScaledUpSquareSize() + yOffset);
        gc.setGlobalAlpha(1.0);

        Image sprite2 = globalGameData.getSprite("ScoreItem_Rock");
        xOffset = ImageRenderHelper.findCenterXMod(sprite2);
        yOffset = ImageRenderHelper.findCenterYMod(sprite2);
        gc.drawImage(sprite2,3 * World.getScaledUpSquareSize() + xOffset,1 * World.getScaledUpSquareSize()  + yOffset);
        Image shadow2 = LightSpriteCreatorHelper.createShadow(sprite2);
        gc.setGlobalAlpha(RadiantLightProducer.determineDarkness(blocks[2][2].getPreviousLightLevel()));
        gc.drawImage(shadow2,3 * World.getScaledUpSquareSize() + xOffset,1 * World.getScaledUpSquareSize() + yOffset);
        gc.setGlobalAlpha(1.0);

    }

    public void drawFakePlayer(GraphicsContext gc)
    {
        String direction = "Front";
        String head = "P1" + "_" + direction + "_Head";
        String body = "P1" + "_" + direction + "_Body";
        String leg = "P1" + "_" + direction + "_Legs_1";

        final double animationLength = 10.0;

        double progress = (((System.currentTimeMillis()-startTime)/1000.0)% animationLength*2);
        //System.out.println(progress);
        switch((int)progress)
        {
            case 0:
            case 1:
            case 2:
                break;
            case 3:
            case 4:
                head += "_Look_Left";
                break;
            case 5:
            case 6:
                break;
            case 7:
            case 8:
                head += "_Look_Right";
                break;
            case 9:
            case 10:
            case 11:
            case 12:
                break;
            case 13:
                body += "_Light_Away";
                break;
            case 14:
                body += "_Light_Off";
                break;
            case 15:
                body += "_Light_Off";
                break;
            case 16:
                double onPercentage =  progress - 16;
                currentLight = (int)(4 + onPercentage * 10.0);
                body += "_Light_On";
                break;
            case 17:
                body += "_Light_On";
                currentLight = 14 - (int)(Math.random()*2);
                break;
            case 18:
                double offPercentage =  progress - 18.0;
                currentLight = (int)(4 + ((1.0 - offPercentage) * 10));
                body += "_Light_Away";
                break;
            case 19:
                currentLight = 4;
                break;
        }

        Image sprite = globalGameData.getSprite(uuid.toString() + "|" + head + "|" + body + "|" + leg);
        gc.drawImage(sprite,7 * World.getScaledUpSquareSize(),6 * World.getScaledUpSquareSize() - 10);
        Image shadow = LightSpriteCreatorHelper.createShadow(sprite);
        gc.setGlobalAlpha(RadiantLightProducer.determineDarkness(currentLight));
        gc.drawImage(shadow,7 * World.getScaledUpSquareSize(),6 * World.getScaledUpSquareSize() - 10);
        gc.setGlobalAlpha(1.0);
    }
    @Override
    public void enterState(GameStateEnum previousState) {
        startTime = System.currentTimeMillis();
        switchState = false;
        clickTime = 0;
    }

    @Override
    public void exitState(GameStateEnum newState) {

    }
}
