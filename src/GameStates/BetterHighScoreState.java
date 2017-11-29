package GameStates;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.HouseWall;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameStates.Enums.GameStateEnum;
import HardwareAdaptors.XBoxController;
import RenderingHelpers.LightSpriteCreatorHelper;
import RenderingHelpers.PlayerSkinCreator;
import RenderingHelpers.RadiantLightProducer;
import RenderingHelpers.TextRenderHelper;
import ScoreSystems.PlayerScoreManager;
import ScoreSystems.ScoreManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.UUID;

public class BetterHighScoreState extends GameStateBase {
    private long startTime;
    private XBoxController controller;
    private boolean switchState = false;
    private long clickTime = 0;
    private final UUID uuid = UUID.randomUUID();

    private BlockBase[][] blocks;
    private long stateStartTime;
    private long lastMoveTime;
    private final double movementLength = 0.8;
    private final double loadingLength = 2;
    private final double buttonLoadTime = 1;
    private final double exitLength = 2;
    private int viewportSize = 15;
    private int viewportBufer = 5;


    /**
     * Shows Better High Score States
     * @param globalGameData
     */
    public BetterHighScoreState(GlobalGameData globalGameData) {
        super(globalGameData);
        blocks = new BlockBase[viewportSize + viewportBufer*2][viewportSize + viewportBufer*2];
        stateStartTime = System.currentTimeMillis();
        lastMoveTime = 0;
        startTime = System.currentTimeMillis();
        controller = globalGameData.getConnectedControllers().get(0);
        switchState = false;
        clickTime = 0;
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


        for(int i = 0; i < ScoreManager.getTopScores().size(); i++)
        {
            TextRenderHelper.drawText(250,i*50+200,ScoreManager.getTopScores().get(i).toString(),gc,globalGameData);
        }


        TextRenderHelper.drawText(10,560,"Press [Anything] to exit",gc,globalGameData);




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
        gc.setGlobalAlpha(1.0);
    }

    @Override
    public void enterState(GameStateEnum previousState) {
        PlayerScoreManager.updateScores();
        ScoreManager.updateScores();
        stateStartTime = System.currentTimeMillis();
        startTime = System.currentTimeMillis();
        switchState = false;
        clickTime = 0;
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