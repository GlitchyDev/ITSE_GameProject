package GameStates;

import GameInfo.Environment.Blocks.*;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameStates.Enums.GameStateEnum;
import HardwareAdaptors.ControllerType;
import HardwareAdaptors.XBoxController;
import RenderingHelpers.LightSpriteCreatorHelper;
import RenderingHelpers.PlayerSkinCreator;
import RenderingHelpers.RadiantLightProducer;
import RenderingHelpers.TextRenderHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.UUID;

public class TutorialGameState extends GameStateBase {
    private final int[][] drawnMap = new int[][]{
            {0,0,0,0,2,1,1,1,1,1,1,1,1,1,1},
            {0,0,0,0,2,0,0,1,1,1,1,1,1,1,1},
            {0,0,0,0,2,0,0,0,1,0,0,1,1,1,1},
            {2,2,3,2,2,0,0,0,0,0,0,0,0,0,1},
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
        }
    }

    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {

    }

    @Override
    protected void doRender(Canvas canvas, GraphicsContext gc) {
        RadiantLightProducer.produceLight(7,7,currentLight,blocks);

        for(int renderLayer = 0; renderLayer < 5; renderLayer++) {
            for (int y = 0; y < drawnMap[0].length; y++) {
                for (int x = 0; x < drawnMap.length; x++) {

                    blocks[y][x].renderBlock(canvas,gc,x,y,renderLayer);
                }
            }
            if(renderLayer == 2) {
                fakeDrawPlayer(gc, 0);


                // Render Help Tip Light

                if (controller.getControllerType() == ControllerType.XBoxController) {
                    TextRenderHelper.drawCenteredText(300, 200, "Use [RT] to toggle light", gc, globalGameData);
                    TextRenderHelper.drawCenteredText(300, 310, "Use [Direction Pad] to move", gc, globalGameData);

                }
                else
                {
                    TextRenderHelper.drawCenteredText(300, 200, "Use [0] to toggle light", gc, globalGameData);
                    TextRenderHelper.drawCenteredText(300, 310, "Use [Arrow Keys] to move", gc, globalGameData);


                }
            }
        }
    }

    public void fakeDrawPlayer(GraphicsContext gc, double xOffset)
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
        gc.drawImage(sprite,7 * World.getScaledUpSquareSize() + (int)(xOffset),6 * World.getScaledUpSquareSize() - 10);
        Image shadow = LightSpriteCreatorHelper.createShadow(sprite);
        gc.setGlobalAlpha(RadiantLightProducer.determineDarkness(currentLight));
        gc.drawImage(shadow,7 * World.getScaledUpSquareSize() + (int)(xOffset),6 * World.getScaledUpSquareSize() - 10);
        gc.setGlobalAlpha(1.0);
    }
    @Override
    public void enterState(GameStateEnum previousState) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void exitState(GameStateEnum newState) {

    }
}
