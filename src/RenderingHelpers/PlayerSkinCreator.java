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
    private static final List<String> headModifiers = Arrays.asList("Head","Head_Look_Left","Head_Look_Right","Head_Really","Head_Evil", "Head_Eyes_Closed");
    private static final List<String> bodyModifiers = Arrays.asList("Body","Body_Light_On","Body_Light_Off","Body_Light_Away");
    private static final List<String> legModifiers = Arrays.asList("Legs_1","Legs_2","Legs_3");

    private static final List<String> frontExclusive = Arrays.asList("Head_Look_Left","Head_Look_Right");
    private static final List<String> backExclusive = Arrays.asList();
    private static final List<String> leftExclusive = Arrays.asList();
    private static final List<String> rightExclusive = Arrays.asList();


    public static void generateSkin(Player player, GlobalGameData globalGameData){
        // Determine Skin Type from Player using Connection
        String[] args = player.getSkinID().split(",");
        String headType = args[0];
        String bodyType = args[1];
        String legType = args[2];

        System.out.println(player.getSkinID() + "~" + player.getSkinID().split("|"));

        createSprites(player.getUuid(),"Front",headType,bodyType,legType, globalGameData);
        createSprites(player.getUuid(),"Back",headType,bodyType,legType, globalGameData);
        createSprites(player.getUuid(),"Left",headType,bodyType,legType, globalGameData);
        createSprites(player.getUuid(),"Right",headType,bodyType,legType, globalGameData);



    }


    private static void createSprites(UUID uuid, String mode, String headType, String bodyType, String legType, GlobalGameData globalGameData)
    {
        for(String head: headModifiers)
        {
            if(canUseMod(mode,head))
            {
                for(String body: bodyModifiers)
                {
                    if(canUseMod(mode,body))
                    {
                        for(String legs: legModifiers)
                        {
                            if(canUseMod(mode,legs))
                            {

                                WritableImage base = new WritableImage(40,70);
                                String name = uuid.toString();
                                name += "|" + headType + "_" + mode + "_" + head;
                                name += "|" + bodyType + "_" + mode + "_" + body;
                                name += "|" + legType + "_" + mode + "_" + legs;
                                System.out.println(name);

                                if(!mode.equals("Back")) {
                                    addSpriteToBase(base, globalGameData.getSprite(legType + "_" + mode + "_" + legs));
                                    addSpriteToBase(base, globalGameData.getSprite(headType + "_" + mode + "_" + head));
                                    addSpriteToBase(base, globalGameData.getSprite(bodyType + "_" + mode + "_" + body));
                                }
                                else
                                {
                                    addSpriteToBase(base, globalGameData.getSprite(legType + "_" + mode + "_" + legs));
                                    addSpriteToBase(base, globalGameData.getSprite(bodyType + "_" + mode + "_" + body));
                                    addSpriteToBase(base, globalGameData.getSprite(headType + "_" + mode + "_" + head));
                                }

                                globalGameData.getSprites().put(name,base);
                            }
                        }
                    }
                }
            }
        }




    }

    private static void addSpriteToBase(WritableImage base, Image sprite)
    {
        PixelWriter writer = base.getPixelWriter();
        PixelReader reader = sprite.getPixelReader();

        for(int x = 0; x < base.getWidth(); x++)
        {
            for(int y = 0; y < base.getHeight(); y++)
            {
                if(x < sprite.getWidth() && y < sprite.getHeight()) {
                    Color c = reader.getColor(x, y);
                    if (c.getOpacity() == 1.0) {
                        writer.setColor(x, y, c);
                    }
                }
            }
        }
    }

    private static boolean canUseMod(String direction, String mod)
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
