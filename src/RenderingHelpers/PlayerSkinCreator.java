package RenderingHelpers;

import GameInfo.GlobalGameData;
import GameInfo.Player;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.*;

public class PlayerSkinCreator {
    public static ArrayList<UUID> skinsCreated = new ArrayList<>();
    private static final List<String> headModifiers = Arrays.asList("Head","Head_Look_Left","Head_Look_Right","Head_Really");
    private static final List<String> bodyModifiers = Arrays.asList("Body","Body_Light_On","Body_Light_Off","Body_Light_Away");
    private static final List<String> legModifiers = Arrays.asList("Legs_1","Legs_2","Legs_3");

    private static final List<String> frontExclusive = Arrays.asList("Head_Look_Left","Head_Look_Right","Head_Really");
    private static final List<String> backExclusive = Arrays.asList();
    private static final List<String> leftExclusive = Arrays.asList();
    private static final List<String> rightExclusive = Arrays.asList();


    public static void generateSkin(Player player, HashMap<String,Image> sprites){
        // Determine Skin Type from Player using Connection
        String headType = "P1";
        String bodyType = "P1";
        String legType = "P1";

        ArrayList<Image> generatedSprites = new ArrayList<>();



        // Front Standing
    }

    public static ArrayList<Image> createFront(String headType, String bodyType, String legType, GlobalGameData globalGameData)
    {
        System.out.println("Creating front");
        ArrayList<Image> images = new ArrayList<>();
        for(String head: headModifiers)
        {
            if(canUseMod("Front",head))
            {
                for(String body: bodyModifiers)
                {
                    if(canUseMod("Front",body))
                    {
                        for(String legs: legModifiers)
                        {
                            if(canUseMod("Front",legs))
                            {
                                WritableImage base = new WritableImage(38,70);
                                addSpriteToBase(base,globalGameData.getSprite(legType + "_" + "Front" + "_" + legs));
                                addSpriteToBase(base,globalGameData.getSprite(headType + "_" + "Front" + "_" + head));
                                addSpriteToBase(base,globalGameData.getSprite(bodyType + "_" + "Front" + "_" + body));
                                images.add(base);


                            }
                        }
                    }
                }
            }
        }
        return images;
    }

    public static void addSpriteToBase(WritableImage base, Image sprite)
    {
        PixelWriter writer = base.getPixelWriter();
        PixelReader reader = sprite.getPixelReader();

        for(int x = 0; x < base.getWidth(); x++)
        {
            for(int y = 0; y < base.getHeight(); y++)
            {
                Color c = reader.getColor(x,y);
                if(c.getOpacity() == 1.0)
                {
                    writer.setColor(x,y,c);
                }
            }
        }
    }

    public static boolean canUseMod(String direction, String mod)
    {
        switch(direction)
        {
            case "Front":
                return ((!backExclusive.contains(mod) && !leftExclusive.contains(mod) && !rightExclusive.contains(mod)) || frontExclusive.contains(mod));
            case "Back":
                return ((!frontExclusive.contains(mod) && !leftExclusive.contains(mod) && !rightExclusive.contains(mod)) || backExclusive.contains(mod));
            case "Left":
                return ((!backExclusive.contains(mod) && !frontExclusive.contains(mod) && !rightExclusive.contains(mod)) || leftExclusive.contains(mod));
            case "Right":
                return ((!backExclusive.contains(mod) && !leftExclusive.contains(mod) && !frontExclusive.contains(mod)) || rightExclusive.contains(mod));
        }
        return false;
    }
}
