package RenderingHelpers;

import GameInfo.Environment.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * This class intends to store the "Resampled" versions of sprites
 */
public class ImageRenderHelper {
    public static HashMap<Image,Image> cachedSprites = new HashMap<>();


    public static Image resample(Image input, int scaleFactor, boolean useCache) {
        if(useCache) {
            if (cachedSprites.containsKey(input)) {
                System.out.println("CACHEING!");
                return cachedSprites.get(input);

            }
        }
        final int W = (int) input.getWidth();
        final int H = (int) input.getHeight();
        final int S = scaleFactor;
        WritableImage output = new WritableImage(
                W * S,
                H * S
        );
        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                final int argb = reader.getArgb(x, y);
                for (int dy = 0; dy < S; dy++) {
                    for (int dx = 0; dx < S; dx++) {
                        writer.setArgb(x * S + dx, y * S + dy, argb);
                    }
                }
            }
        }
        if(useCache) {
            cachedSprites.put(input, output);
        }
        return output;
    }

    public static Image generateRandomStatic(int width, int height)
    {
        WritableImage generateStatic = new WritableImage(width,height);
        for(int pixelX = 0; pixelX < generateStatic.getWidth(); pixelX++)
        {
            for(int pixelY = 0; pixelY < generateStatic.getHeight(); pixelY++)
            {
                int grayness = (int)(Math.random() * 256);
                generateStatic.getPixelWriter().setColor(pixelX,pixelY, Color.rgb(grayness,grayness,grayness));
            }
        }
        return generateStatic;
    }

    public static Image adjustImage(Image main, Image modifyer)
    {
        WritableImage newImage = new WritableImage((int)(main.getWidth()),(int)(main.getHeight()));
        PixelWriter writer = newImage.getPixelWriter();
        for(int pixelX = 0; pixelX < main.getWidth(); pixelX++)
        {
            for(int pixelY = 0; pixelY < main.getHeight(); pixelY++)
            {
                Color originalColor = main.getPixelReader().getColor(pixelX,pixelY);

                Color color = Color.rgb((int)(originalColor.getRed() * 255),(int)(originalColor.getGreen() * 255),(int)(originalColor.getBlue() * 255),modifyer.getPixelReader().getColor(pixelX,pixelY).getRed());
                writer.setColor(pixelX,pixelY,color);
            }
        }
        return newImage;
    }

    public static int findCenterXMod(Image image)
    {
        return (int)(World.getScaledUpSquareSize() - image.getWidth())/2;
    }
    public static int findCenterYMod(Image image)
    {
        return (int)(World.getScaledUpSquareSize() - image.getHeight())/2;
    }


}
