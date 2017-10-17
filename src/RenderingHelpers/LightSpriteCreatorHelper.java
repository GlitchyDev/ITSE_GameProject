package RenderingHelpers;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;


import java.util.HashMap;

public class LightSpriteCreatorHelper {
    private static HashMap<String,Image> cachedShadowSprites = new HashMap<>();

    public static Image createShadow(Image image) {
        if (cachedShadowSprites.containsKey(image.toString())) {
            return cachedShadowSprites.get(image.toString());
        } else {
            double amount = 1.0;
            WritableImage returnImage = new WritableImage((int)(image.getWidth()),(int)(image.getHeight()));
            for(int x = 0; x < image.getWidth(); x++)
            {
                for(int y = 0; y < image.getHeight(); y++)
                {
                    Color c = image.getPixelReader().getColor(x,y);
                    int red = (int) ((c.getRed() * (amount) / 255) * 255);
                    int green = (int) ((c.getGreen() * (amount) / 255) * 255);
                    int blue = (int) ((c.getBlue() * (amount) / 255) * 255);
                    c = Color.rgb(red, green, blue,c.getOpacity());
                    returnImage.getPixelWriter().setColor(x,y,c);
                }
            }
            cachedShadowSprites.put(image.toString(), returnImage);
            return returnImage;
        }
    }


}
