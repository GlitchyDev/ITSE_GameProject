package GameStates;

import GameInfo.Environment.Blocks.BlockBase;
import GameInfo.Environment.Blocks.BlockTypeEnum;
import GameInfo.Environment.Blocks.HouseWall;
import GameInfo.Environment.Blocks.WallFloorBlock;
import GameInfo.Environment.Entities.AbstractClasses.EntityBase;
import GameInfo.Environment.Entities.Enums.ProPlayerStateEnum;
import GameInfo.Environment.World;
import GameInfo.WorldViewport;
import GameStates.Enums.GameStateEnum;
import GameInfo.GlobalGameData;
import RenderingHelpers.LightSpriteCreatorHelper;
import RenderingHelpers.PlayerSkinCreator;
import RenderingHelpers.RadiantLightProducer;
import RenderingHelpers.TextRenderHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.xml.soap.Text;
import java.util.UUID;


public class TitleScreenGameState extends GameStateBase {
    private BlockBase[][] blocks;
    private long stateStartTime;
    private final double movementLength = 0.8;
    private int viewportSize = 15;
    private int viewportBufer = 5;
    private final UUID uuid = UUID.randomUUID();


    public TitleScreenGameState(GlobalGameData globalGameData) {
        super(globalGameData);
        blocks = new BlockBase[viewportSize + viewportBufer*2][viewportSize + viewportBufer*2];
        stateStartTime = 0;
    }

    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {
        if(stateStartTime == 0)
        {
            stateStartTime = System.currentTimeMillis();
        }
        if(System.currentTimeMillis() > stateStartTime + movementLength*1000)
        {
            stateStartTime = System.currentTimeMillis();
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

                    double xBuffer = (System.currentTimeMillis() - (stateStartTime + movementLength*1000))/(movementLength*1000);
                    blocks[x][y].renderBlock(canvas, gc, (x - viewportBufer) + xBuffer, y + 1 - viewportBufer, renderLayer);

                    if(renderLayer == 1 && y == viewportBufer + 7)
                    {
                        fakeDrawPlayer(gc,xBuffer);
                    }
                }
            }
        }


        gc.setFill(Color.BLUE);
        gc.fillText("" + System.currentTimeMillis(),50,50);


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

        if((int)(System.currentTimeMillis()/1000.0)%2 == 0) {
            TextRenderHelper.drawText(265, 400, "Continue? <", gc, globalGameData);
        }
        else {
            TextRenderHelper.drawText(265, 400, "Continue?  <", gc, globalGameData);
        }

    }


    @Override
    public void enterState(GameStateEnum previousState) {
        PlayerSkinCreator.generateSkin(uuid, globalGameData);
        globalGameData.playSound("MainTheme",true);
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
                int index = (int) (Math.random() * 4);
                switch (index) {
                    case 0:
                        blocks[x][y] = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_WALL);
                        break;
                    case 1:
                        blocks[x][y] = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                        break;
                    case 2:
                        blocks[x][y] = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
                        break;
                    case 3:
                        blocks[x][y] = new HouseWall(globalGameData);
                        break;
                }
            } else {
                blocks[x][y] = new WallFloorBlock(globalGameData, BlockTypeEnum.TEST_FLOOR);
            }
        }

    }



}
