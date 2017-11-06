package RenderingHelpers;

import GameInfo.Environment.World;
import GameInfo.GlobalGameData;
import GameInfo.WorldViewport;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class TextRenderHelper {


    public static void drawText(int x, int y, String text, GraphicsContext gc, GlobalGameData globalGameData)
    {

        int charNum = 0;
        for(char c: text.toUpperCase().toCharArray())
        {
            if(c != ' ')
            {
                Image let = globalGameData.getSprite(String.valueOf(c));
                gc.drawImage(let,x + charNum*8,y);
            }
            charNum++;
        }
    }

    public static void drawCenteredText(int x, int y, String text, GraphicsContext gc, GlobalGameData globalGameData)
    {
        int width = text.length() * 8;

        int adjustedX = x - width/2;
        int charNum = 0;
        for(char c: text.toUpperCase().toCharArray())
        {
            if(c != ' ')
            {
                Image let = globalGameData.getSprite(String.valueOf(c));
                gc.drawImage(let,adjustedX + charNum*8,y);
            }
            charNum++;
        }
    }

    public static void drawViewportCenteredText(String text, GraphicsContext gc, GlobalGameData globalGameData, double x, double y, double xOffset, double yOffset)
    {
        int width = text.length() * 8;

        int charNum = 0;
        for(char c: text.toUpperCase().toCharArray())
        {
            if(c != ' ')
            {
                Image let = globalGameData.getSprite(String.valueOf(c));
                gc.drawImage(let,(int)(x * World.getScaledUpSquareSize() + 0.5 + xOffset + WorldViewport.widthBuffer - width/2 + 2 + 8 * charNum), (int)(y * World.getScaledUpSquareSize() + 0.5 + yOffset + WorldViewport.heightBuffer)  );
            }
            charNum++;
        }
    }
}
