package GameStates;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.HouseWall;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameStates.Enums.GameStateEnum;
import GameStates.Enums.TitleSceenMiniState;
import HardwareAdaptors.DirectionalEnum;
import HardwareAdaptors.XBoxController;
import RenderingHelpers.LightSpriteCreatorHelper;
import RenderingHelpers.PlayerSkinCreator;
import RenderingHelpers.RadiantLightProducer;
import RenderingHelpers.TextRenderHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.UUID;


public class TitleScreenGameState extends GameStateBase {
    private BlockBase[][] blocks;
    private long stateStartTime;
    private long lastMoveTime;
    private final double movementLength = 0.8;
    private final double loadingLength = 2;
    private final double buttonLoadTime = 1;
    private final double exitLength = 2;
    private int viewportSize = 15;
    private int viewportBufer = 5;
    private final UUID uuid = UUID.randomUUID();
    private TitleSceenMiniState state;

    private XBoxController controller;
    private DirectionalEnum cacheDirection = DirectionalEnum.NONE;
    private long lastCursorMove = 0;
    private int cursurNum = 0;


    public TitleScreenGameState(GlobalGameData globalGameData) {
        super(globalGameData);
        blocks = new BlockBase[viewportSize + viewportBufer*2][viewportSize + viewportBufer*2];
        stateStartTime = 0;
        lastMoveTime = 0;
        state = TitleSceenMiniState.TS_LOADING;
        controller = globalGameData.getConnectedControllers().get(0);
    }

    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {
        if(stateStartTime == 0)
        {
            stateStartTime = System.currentTimeMillis();
        }

        switch(state)
        {
            case TS_LOADING:
                if(System.currentTimeMillis() > (stateStartTime + loadingLength*1000.0))
                {
                    state = TitleSceenMiniState.TS_IDLE;
                    stateStartTime = System.currentTimeMillis();
                }
                break;
            case TS_IDLE:
                double progress = 1.0/buttonLoadTime * ((System.currentTimeMillis() - stateStartTime)/1000.0);
                if(progress > 1)
                {
                    controller.poll();
                    if(controller.getDirectionalPad() != cacheDirection)
                    {
                        cacheDirection = controller.getDirectionalPad();
                        switch(cacheDirection)
                        {
                            case NORTH:
                                if(cursurNum != 0)
                                {
                                    lastCursorMove = System.currentTimeMillis();
                                    cursurNum--;
                                    globalGameData.playSound("menuSwitchItem",false,0.1);
                                }
                                else
                                {
                                    lastCursorMove = System.currentTimeMillis();
                                    cursurNum = 4;
                                    globalGameData.playSound("menuSwitchItem",false,0.1);
                                }
                                break;
                            case SOUTH:
                                if(cursurNum != 4)
                                {
                                    lastCursorMove = System.currentTimeMillis();
                                    globalGameData.playSound("menuSwitchItem",false,0.1);
                                    cursurNum++;
                                }
                                else
                                {
                                    lastCursorMove = System.currentTimeMillis();
                                    cursurNum = 0;
                                    globalGameData.playSound("menuSwitchItem",false,0.1);
                                }
                                break;
                            case EAST:
                            case WEST:
                                state = TitleSceenMiniState.TS_Exit;
                                stateStartTime = System.currentTimeMillis();
                                globalGameData.playSound("menuSelectItem",false,0.1);

                                break;

                        }
                    }
                }

                break;
            case TS_Exit:
                if(System.currentTimeMillis() > (stateStartTime + exitLength*1000))
                {
                    menuSelect();
                }
                break;
        }


        if(System.currentTimeMillis() > lastMoveTime + movementLength*1000.0)
        {
            lastMoveTime = System.currentTimeMillis();
            moveBlocks(1,0);
        }
        for(int x = 0; x < viewportSize + viewportBufer*2; x++)
        {
            for(int y = 0; y < viewportSize + viewportBufer*2; y++)
            {
                if(blocks[x][y] == null)
                {
                    generateBlock(x,y);
                }
            }
        }
        RadiantLightProducer.produceLight(viewportBufer+7,viewportBufer+7,5,blocks);
    }

    public void menuSelect()
    {
        switch(cursurNum)
        {
            case 0:
                globalGameData.switchGameState(GameStateEnum.TestWorld);
                break;
            case 1:
                globalGameData.switchGameState(GameStateEnum.TutorialScreen);
                break;
            case 2:
                globalGameData.switchGameState(GameStateEnum.CreditScreen);
                break;
            case 3:
                globalGameData.switchGameState(GameStateEnum.HighScoreScreen);
                break;
            case 4:
                globalGameData.switchGameState(GameStateEnum.SubmitScoreScreen);
                break;
        }
    }

    @Override
    protected void doRender(Canvas canvas, GraphicsContext gc) {

        for(int renderLayer = 0; renderLayer < 5; renderLayer++) {
            for (int y = 0; y < blocks[0].length; y++) {
                for (int x = 0; x < blocks.length; x++) {

                    double xBuffer = (System.currentTimeMillis() - (lastMoveTime + movementLength*1000))/(movementLength*1000);
                    blocks[x][y].renderBlock(canvas, gc, (x - viewportBufer) + xBuffer, y + 1 - viewportBufer, renderLayer);

                    if(renderLayer == 1 && y == viewportBufer + 7)
                    {
                        fakeDrawPlayer(gc,xBuffer);
                    }
                }
            }
        }



        int offset = (int)(canvas.getWidth()/2 - globalGameData.getSprite("TitleIcon").getWidth()/2);
        gc.drawImage(globalGameData.getSprite("TitleIcon"),offset,30);

        gc.setGlobalAlpha(0.02);
        int random = (int)(Math.random() * 4) + 1;
        gc.drawImage(globalGameData.getSprite("Static_" + random),0,0);
        gc.setGlobalAlpha(1.0);

        gc.setGlobalAlpha(0.02);
        random = (int)(Math.random() * 4) + 1;
        gc.drawImage(globalGameData.getSprite("Static_" + random),0,0);
        gc.setGlobalAlpha(1.0);

        gc.setGlobalAlpha(0.02);
        random = (int)(Math.random() * 4) + 1;
        gc.drawImage(globalGameData.getSprite("Static_" + random),0,0);
        gc.setGlobalAlpha(1.0);


        if(state == TitleSceenMiniState.TS_IDLE || state == TitleSceenMiniState.TS_Exit) {
            double progress = 0;
            if(state == TitleSceenMiniState.TS_IDLE) {
                progress = 1.0 / exitLength * ((System.currentTimeMillis() - stateStartTime) / 1000.0);
            }
            else
            {
                progress = 1- 1.0 / buttonLoadTime * ((System.currentTimeMillis() - stateStartTime) / 1000.0);
            }

            if(progress < 1) {
                gc.setGlobalAlpha(progress);
            }
            String cursor = "<<";
            if ((int) ((System.currentTimeMillis() - lastCursorMove) / 1000.0) % 2 == 0) {
                cursor = " <";
            } else {
                cursor = "  <";
            }
            if(state == TitleSceenMiniState.TS_Exit)
            {
                cursor = "<";
            }

            int buffer = 10;
            if(cursurNum == 0)
            {
                TextRenderHelper.drawText(265, 400, "Continue?" + cursor, gc, globalGameData);
            }
            else
            {
                TextRenderHelper.drawText(265, 400, "Continue?", gc, globalGameData);
            }

            if(cursurNum == 1)
            {
                TextRenderHelper.drawText(265, 415 + buffer, "Tutorial" + cursor, gc, globalGameData);
            }
            else
            {
                TextRenderHelper.drawText(265, 415 + buffer, "Tutorial", gc, globalGameData);
            }

            if(cursurNum == 2)
            {
                TextRenderHelper.drawText(265, 430 + buffer * 2, "Credits" + cursor, gc, globalGameData);
            }
            else
            {
                TextRenderHelper.drawText(265, 430 + buffer * 2, "Credits", gc, globalGameData);
            }
            if(cursurNum == 3)
            {
                TextRenderHelper.drawText(265,445 + buffer * 3, "HighScores" + cursor, gc, globalGameData);
            }
            else
            {
                TextRenderHelper.drawText(265, 445 + buffer * 3, "HighScores", gc, globalGameData);
            }
            if(cursurNum == 4)
            {
                TextRenderHelper.drawText(265,460 + buffer * 4, "Exit?" + cursor, gc, globalGameData);
            }
            else
            {
                TextRenderHelper.drawText(265, 460 + buffer * 4, "Exit?", gc, globalGameData);
            }




            gc.setGlobalAlpha(1.0);
        }

        if(state == TitleSceenMiniState.TS_LOADING)
        {
            double progress = 1 - 1.0/loadingLength * ((System.currentTimeMillis() - stateStartTime)/1000.0);
            if(progress < 0)
            {
                progress = 1;
            }
            gc.setGlobalAlpha(progress);
            gc.setFill(Color.BLACK);
            gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
            gc.setGlobalAlpha(1.0);
        }


        if(state == TitleSceenMiniState.TS_Exit)
        {
            double progress = 1.0/exitLength * ((System.currentTimeMillis() - stateStartTime)/1000.0);
            if(progress < 0)
            {
                progress = 1;
            }
            if(cursurNum == 0) {
                globalGameData.setSoundVolume("MainTheme", 1 - progress);
            }
            gc.setGlobalAlpha(progress);
            gc.setFill(Color.BLACK);
            gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
            gc.setGlobalAlpha(1.0);
        }
    }


    @Override
    public void enterState(GameStateEnum previousState) {
        PlayerSkinCreator.generateSkin(uuid, globalGameData);
        if(previousState == GameStateEnum.TestWorld || previousState == GameStateEnum.MainMenu) {
            globalGameData.playSound("MainTheme", true);
        }
        for(int x = 0; x < viewportSize + viewportBufer*2; x++)
        {
            for(int y = 0; y < viewportSize + viewportBufer*2; y++)
            {
                if(blocks[x][y] == null)
                {
                    generateBlock(x,y);
                }
            }
        }
    }

    @Override
    public void exitState(GameStateEnum newState) {
        if(newState == GameStateEnum.TestWorld) {
            globalGameData.stopSound("MainTheme");
        }
        blocks = new BlockBase[viewportSize + viewportBufer*2][viewportSize + viewportBufer*2];
        stateStartTime = 0;
        lastMoveTime = 0;
        state = TitleSceenMiniState.TS_LOADING;
    }

    //*********************************************************************************
    //*********************************************************************************

    public void fakeDrawPlayer(GraphicsContext gc, double xOffset)
    {
        String direction = "Left";
        String head = "P1" + "_" + direction + "_Head";
        String body = "P1" + "_" + direction + "_Body_Light_On";
        String leg = "P1" + "_" + direction + "_";
        switch ((int) ((System.currentTimeMillis() / 400) % 4)) {
            case 0:
                leg += "Legs_1";
                break;
            case 1:
                leg += "Legs_2";
                break;
            case 2:
                leg += "Legs_1";
                break;
            case 3:
                leg += "Legs_3";
                break;
        }

        Image sprite = globalGameData.getSprite(uuid.toString() + "|" + head + "|" + body + "|" + leg);
        gc.drawImage(sprite,7 * World.getScaledUpSquareSize() + (int)(xOffset),6 * World.getScaledUpSquareSize() - 10);
        Image shadow = LightSpriteCreatorHelper.createShadow(sprite);
        gc.setGlobalAlpha(RadiantLightProducer.determineDarkness(5));
        gc.drawImage(shadow,7 * World.getScaledUpSquareSize() + (int)(xOffset),6 * World.getScaledUpSquareSize() - 10);
        gc.setGlobalAlpha(1.0);
    }

    public void moveBlocks(int adjustX, int adjustY)
    {

        BlockBase[][] adjustGrid = new BlockBase[viewportSize + viewportBufer*2][viewportSize + viewportBufer*2];
        for(int x = 0; x < viewportSize + viewportBufer*2; x++)
        {
            for(int y = 0; y < viewportSize + viewportBufer*2; y++)
            {
                if(x + adjustX >= 0 && x + adjustX < viewportSize + viewportBufer*2)
                {
                    if(y + adjustY >= 0 && y + adjustY < viewportSize + viewportBufer*2)
                    {
                        adjustGrid[x+adjustX][y+adjustY] = blocks[x][y];
                    }
                }

            }
        }
        blocks = adjustGrid;

    }


    public void generateBlock(int x, int y)
    {
        if(blocks[x][y] == null) {

            if (y <= viewportSize / 2 + viewportBufer - 3 || y > viewportSize / 2 + viewportBufer) {
                int index = (int) (Math.random() * 6);
                switch (index) {
                    case 0:
                        blocks[x][y] = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_WALL);
                        break;
                    case 1:
                        blocks[x][y] = new HouseWall(globalGameData);
                        break;
                    default:
                        blocks[x][y] = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                        break;
                }
            } else {
                blocks[x][y] = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
            }
        }

    }



}
