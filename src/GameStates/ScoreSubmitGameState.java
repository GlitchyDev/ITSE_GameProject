package GameStates;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.HouseWall;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameStates.Enums.GameStateEnum;
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
import ScoreSystems.PlayerScoreManager;
import ScoreSystems.ScoreManager;

import java.util.UUID;


public class ScoreSubmitGameState extends GameStateBase {
    private long startTime;
    private XBoxController controller;
    private DirectionalEnum cacheDirection = DirectionalEnum.NONE;
    private final UUID uuid = UUID.randomUUID();

    private int cursorNum = 1;
    private char letter1 = 'A';
    private char letter2 = 'A';
    private char letter3 = 'A';

    private BlockBase[][] blocks;
    private long lastMoveTime;
    private final double movementLength = 0.8;
    private int viewportSize = 15;
    private int viewportBufer = 5;


    public ScoreSubmitGameState(GlobalGameData globalGameData) {
        super(globalGameData);
        blocks = new BlockBase[viewportSize + viewportBufer*2][viewportSize + viewportBufer*2];
        lastMoveTime = 0;
        startTime = System.currentTimeMillis();
        controller = globalGameData.getConnectedControllers().get(0);
    }

    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {
        controller.poll();
        if(cacheDirection != controller.getDirectionalPad()) {
            cacheDirection = controller.getDirectionalPad();

            switch (controller.getDirectionalPad()) {
                case NORTH:
                    if (cursorNum == 0) {
                        globalGameData.getPrimaryStage().close();
                    }
                    if (cursorNum == 1) {
                        letter1++;
                        if (letter1 > 'Z') {
                            letter1 = 'A';
                        }
                        globalGameData.playSound("menuSwitchItem",false,0.1);
                    }
                    if (cursorNum == 2) {
                        letter2++;
                        if (letter2 > 'Z') {
                            letter2 = 'A';
                        }
                        globalGameData.playSound("menuSwitchItem",false,0.1);
                    }
                    if (cursorNum == 3) {
                        letter3++;
                        if (letter3 > 'Z') {
                            letter3 = 'A';
                        }
                        globalGameData.playSound("menuSwitchItem",false,0.1);
                    }
                    if (cursorNum == 4) {
                        String name = String.valueOf(letter1 + letter2 + letter3);
                        ScoreManager.submitScore(name, PlayerScoreManager.getTopScore().getScore());
                        globalGameData.getPrimaryStage().close();
                    }
                    break;
                case SOUTH:
                    if (cursorNum == 0) {
                        globalGameData.getPrimaryStage().close();
                    }
                    if (cursorNum == 1) {
                        letter1--;
                        if (letter1 < 'A') {
                            letter1 = 'Z';
                        }
                        globalGameData.playSound("menuSwitchItem",false,0.1);
                    }
                    if (cursorNum == 2) {
                        letter2--;
                        if (letter2 < 'A') {
                            letter2 = 'Z';
                        }
                        globalGameData.playSound("menuSwitchItem",false,0.1);
                    }
                    if (cursorNum == 3) {
                        letter3--;
                        if (letter3 < 'A') {
                            letter3 = 'Z';
                        }
                        globalGameData.playSound("menuSwitchItem",false,0.1);
                    }
                    if (cursorNum == 4) {
                        String name = String.valueOf(letter1 + letter2 + letter3);
                        ScoreManager.submitScore(name, PlayerScoreManager.getTopScore().getScore());
                        globalGameData.getPrimaryStage().close();

                    }
                    break;
                case EAST:
                    cursorNum++;
                    if (cursorNum > 4) {
                        cursorNum = 0;
                    }
                    globalGameData.playSound("menuSwitchItem",false,0.1);
                    break;
                case WEST:
                    cursorNum--;
                    if (cursorNum < 0) {
                        cursorNum = 4;
                    }
                    globalGameData.playSound("menuSwitchItem",false,0.1);

                    break;
            }
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

        // Scores
        String cursor = "<<";
        if ((int) ((System.currentTimeMillis() - lastMoveTime) / 1000.0) % 2 == 0) {
            cursor = " <";
        } else {
            cursor = "  <";
        }



        if(cursorNum == 0)
        {
            TextRenderHelper.drawText(150, 310, "[EXIT]" + cursor, gc, globalGameData);
        }
        else
        {
            TextRenderHelper.drawText(150, 310, "[EXIT]", gc, globalGameData);
        }

        final int cursorLength = 30;
        if(cursorNum == 1)
        {
            TextRenderHelper.drawText(230 + cursorLength * 0, 310, letter1 + cursor, gc, globalGameData);
        }
        else
        {
            TextRenderHelper.drawText(230 + cursorLength * 0, 310, letter1 + "", gc, globalGameData);
        }

        if(cursorNum == 2)
        {
            TextRenderHelper.drawText(230 + cursorLength * 1, 310, letter2 + cursor, gc, globalGameData);
        }
        else
        {
            TextRenderHelper.drawText(230 + cursorLength * 1, 310, letter2 + "", gc, globalGameData);
        }

        if(cursorNum == 3)
        {
            TextRenderHelper.drawText(230 + cursorLength * 2, 310, letter3 + cursor, gc, globalGameData);
        }
        else
        {
            TextRenderHelper.drawText(230 + cursorLength * 2, 310, letter3 + "", gc, globalGameData);
        }

        if(cursorNum == 4)
        {
            TextRenderHelper.drawText(320, 310, "[Submit and Exit]" + cursor, gc, globalGameData);
        }
        else
        {
            TextRenderHelper.drawText(320, 310, "[Submit and Exit]", gc, globalGameData);
        }


        int offset = (int)(canvas.getWidth()/2 - globalGameData.getSprite("TitleIcon").getWidth()/2);
        gc.drawImage(globalGameData.getSprite("TitleIcon"),offset,30);

        TextRenderHelper.drawCenteredText(300,120,String.valueOf("Your Top Score " + PlayerScoreManager.getTopScore().getScore()),gc,globalGameData);


        double duration = (System.currentTimeMillis() - this.startTime)/1000.0;

        if(duration < 1.0)
        {
            gc.setFill(Color.BLACK);
            gc.setGlobalAlpha(duration);
            gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
            System.out.println(duration);
        }
        gc.setGlobalAlpha(1.0);
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

        PlayerScoreManager.updateScores();
        ScoreManager.updateScores();
        startTime = System.currentTimeMillis();

    }


    @Override
    public void exitState(GameStateEnum newState) {

    }

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
