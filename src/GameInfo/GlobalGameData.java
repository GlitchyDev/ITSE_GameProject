package GameInfo;

import GameStates.DebugControlsGameState;
import GameStates.GameStateBase;
import GameStates.MainMenuGameState;
import GameStates.TestWorldGameState;
import HardwareAdaptors.DebugController;
import HardwareAdaptors.XBoxController;
import RenderingHelpers.ImageRenderHelper;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 *  The purpose of this Class is to store all Global GameData used by GameStates such as
 *  - All other GameStates
 *  - Loaded Assets
 *  - The "Random" class, assuming one is needed
 *  - The Network Connection ( Client or Server )
 */
public class GlobalGameData {
    private HashMap<String, GameStateBase> gameStates;
    private HashMap<String, Image> spriteAssets;
    private HashMap<String, Media> soundAssets;
    private Stage primaryStage;
    private Canvas canvas;

    private GameStateEnum currentGameState;
    private ArrayList<XBoxController> connectedControllers;
    private ArrayList<Player> connectedPlayers;
    private Random random;

    private HashMap<String,MediaPlayer> currentSounds;

    public GlobalGameData(GameStateEnum startingState,Stage primaryStage, Canvas canvas)
    {
        System.out.println("Global Gamestate: Creating Global GameState");
        gameStates = new HashMap<>();
        spriteAssets = new HashMap<>();
        soundAssets = new HashMap<>();
        this.currentGameState = startingState;
        this.primaryStage = primaryStage;
        this.canvas = canvas;
        random = new Random();
        connectedControllers = new ArrayList<>();
        connectedPlayers = new ArrayList<>();

        currentSounds = new HashMap<>();
        connectedControllers.addAll(scanForControllers());

        loadAssets();

        createGameStates();
    }

    private void createGameStates()
    {
        gameStates.put("MainMenu", new MainMenuGameState(this));
        gameStates.put("TestWorld", new TestWorldGameState(this,primaryStage,canvas));
        gameStates.put("DebugControls", new DebugControlsGameState(this));
    }




    public void loadAssets()
    {
        String link = "/Sprites/Old/Pro_Test_Darker.png";
        InputStream stream = getClass().getResourceAsStream(link);
        Image image = null;
        if(System.getProperty("isJar").equals("true")) {
            System.out.println("Load Assets: Jar Asset Loading Mode");
            System.out.println("Load Assets: Loading Sprites");
            InputStream inputSprites = getClass().getResourceAsStream("/Sprites/SpriteAssetList.txt");
            Scanner spriteScanner = new Scanner(new InputStreamReader(inputSprites));
            while(spriteScanner.hasNextLine())
            {
                String spritePath = spriteScanner.nextLine();
                String[] parsed = spritePath.split("/");
                Image sprite = new Image(getClass().getResourceAsStream("/Sprites" + spritePath));
                if(parsed[parsed.length-1].replace(".png","").contains("Alphabet"))
                {
                    createLetters(sprite);
                }
                else {
                    spriteAssets.put(parsed[parsed.length - 1].replace(".png", ""), ImageRenderHelper.resample(sprite, 2, false));
                    System.out.println("    -" + parsed[parsed.length - 1].replace(".png", ""));
                }

            }


            System.out.println("Load Assets: Loading Sounds");
            InputStream inputSound = getClass().getResourceAsStream("/Sounds/SoundsAssetList.txt");
            Scanner soundScanner = new Scanner(new InputStreamReader(inputSound));
            while(soundScanner.hasNextLine())
            {
                String soundPath = soundScanner.nextLine();
                String[] parsed = soundPath.split("/");
                soundAssets.put(parsed[parsed.length-1].replace(".mp3",""),new Media(getClass().getResource("/Sounds" + soundPath).toString()));
                System.out.println("    -" + parsed[parsed.length-1].replace(".mp3",""));

            }

        }
        else
        {
            System.out.println("Load Assets: Project IDE Asset Loading Mode");
            System.out.println("Load Assets: Updating AssetList");
            System.out.println("Load Assets: Loading Sprites");
            // Loading Sprites
            File spriteFile = new File("GameAssets/Sprites/SpriteAssetList.txt");
            spriteFile.delete();
            try {
                PrintWriter writer = new PrintWriter(spriteFile);
                File startingFolder = new File("GameAssets/Sprites/");
                for(File innerFolders: startingFolder.listFiles())
                {
                    if(!innerFolders.getName().equals("SpriteAssetList.txt"))
                    {
                        for(File sprite: innerFolders.listFiles())
                        {
                            if(sprite.getName().contains(".png")) {
                                Image newSprite = new Image(getClass().getResource("/Sprites/" + innerFolders.getName() + "/" + sprite.getName()).toString());
                                if(sprite.getName().contains("Alphabet")) {
                                    createLetters(newSprite);
                                    writer.println("/" + innerFolders.getName() + "/" + sprite.getName());
                                }
                                else {
                                    spriteAssets.put(sprite.getName().replace(".png", ""), ImageRenderHelper.resample(newSprite, 2, false));
                                    writer.println("/" + innerFolders.getName() + "/" + sprite.getName());
                                    System.out.println("    -" + sprite.getName().replace(".png", ""));
                                }
                            }
                        }
                    }
                }
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Load Sounds
            System.out.println("Load Assets: Loading Sounds");
            File soundFile = new File("GameAssets/Sounds/SoundsAssetList.txt");
            soundFile.delete();
            try {
                PrintWriter writer = new PrintWriter(soundFile);
                File startingFolder = new File("GameAssets/Sounds/");
                for(File innerFolders: startingFolder.listFiles())
                {
                    if(!innerFolders.getName().equals("SoundsAssetList.txt"))
                    {
                        for(File sound: innerFolders.listFiles())
                        {
                            if(sound.getName().contains(".mp3")) {

                                Media media = new Media(getClass().getResource("/Sounds/" + innerFolders.getName() + "/" + sound.getName()).toString());
                                soundAssets.put(sound.getName().replace(".mp3", ""), media);
                                writer.println("/" + innerFolders.getName() + "/" + sound.getName());
                                System.out.println("    -" + sound.getName().replace(".mp3", ""));
                            }
                        }
                    }
                }
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void createLetters(Image image)
    {

        System.out.println("***" + (image==null));
        for(int letterCount = 0; letterCount < 45; letterCount++)
        {
            WritableImage letterSprite = new WritableImage(8,10);
            PixelReader reader = image.getPixelReader();
            for(int x = 0; x < 7; x++)
            {
                for(int y = 0; y < 10; y++)
                {
                    letterSprite.getPixelWriter().setColor(x,y,reader.getColor(x + letterCount*8,y));
                }
            }
            char letter = (char)('A' + letterCount);
            //System.out.println(letterCount);

            if(letterCount >= 26)
            {
                switch(letterCount)
                {
                    case 26:
                        letter = '1';
                        break;
                    case 27:
                        letter = '2';
                        break;
                    case 28:
                        letter = '3';
                        break;
                    case 29:
                        letter = '4';
                        break;
                    case 30:
                        letter = '5';
                        break;
                    case 31:
                        letter = '6';
                        break;
                    case 32:
                        letter = '7';
                        break;
                    case 33:
                        letter = '8';
                        break;
                    case 34:
                        letter = '9';
                        break;
                    case 35:
                        letter = '0';
                        break;
                    case 36:
                        letter = '!';
                        break;
                    case 37:
                        letter = '.';
                        break;
                    case 38:
                        letter = '?';
                        break;
                    case 39:
                        letter = ',';
                        break;
                    case 40:
                        letter = ':';
                        break;
                    case 41:
                        letter = '_';
                        break;
                    case 42:
                        letter = '[';
                        break;
                    case 43:
                        letter = ']';
                        break;
                    case 44:
                        letter = '*';
                        break;
                }
            }
            spriteAssets.put(String.valueOf(letter),letterSprite);
            //System.out.println("Placed letter " + letter);

        }


    }


    public ArrayList<XBoxController> scanForControllers()
    {
        System.out.println("Scan Controllers: Acquiring controllers");
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        ArrayList<XBoxController> xBoxControllers = new ArrayList<>();

        int controllerNum = 0;
        for(Controller controller: controllers)
        {
            if(controller.getType() == Controller.Type.GAMEPAD)
            {
                System.out.println("Scan Controllers: Acquired Controller " + controller.getName());
                xBoxControllers.add(new XBoxController(controller, controllerNum));
                controllerNum++;
            }
        }
        if(xBoxControllers.size() == 0)
        {
            System.out.println("Scan Controllers: Acquired no Controllers, revert to Keyboard controls");
            System.out.println("Scan Controllers: Creating DebugController to replace XBoxControls");
            for(Controller controller: controllers)
            {
                if(controller.getType() == Controller.Type.KEYBOARD)
                {
                    xBoxControllers.add(new DebugController(controller,0));
                }
            }
        }
        else
        {
            System.out.println("Scan Controllers: Acquired " + xBoxControllers.size() + " Controllers");
        }
        return xBoxControllers;
    }

    /*
    If this is needed, this will allow for someone to "Rescan" for controllers! Use wisely!

    private static ControllerEnvironment createDefaultEnvironment() throws ReflectiveOperationException {
        // Find constructor (class is package private, so we can't access it directly)
        Constructor<ControllerEnvironment> constructor = (Constructor<ControllerEnvironment>)
                Class.forName("net.java.games.input.DefaultControllerEnvironment").getDeclaredConstructors()[0];
        // Constructor is package private, so we have to deactivate access control checks
        constructor.setAccessible(true);
        // Create object with default constructor
        return constructor.newInstance();
    }
    */

    public void switchGameState(GameStateEnum newState)
    {
        gameStates.get(currentGameState.toString()).exitState(newState);
        gameStates.get(newState.toString()).enterState(currentGameState);
        currentGameState = newState;
    }

    // Getters and Setters

    public GameStateBase getGameState(String state)
    {
        return gameStates.get(state);
    }

    public void resetWorld()
    {
        gameStates.get("TestWorld").exitState(GameStateEnum.MainMenu);
        gameStates.get("MainMenu").enterState(GameStateEnum.TestWorld);
        gameStates.remove("TestWorld");
        gameStates.put("TestWorld",new TestWorldGameState(this,primaryStage,canvas));
        getGameState("TestWorld").enterState(GameStateEnum.MainMenu);
        System.gc();
    }

    public Image getSprite(String sprite)
    {
        if(spriteAssets.containsKey(sprite))
        {
            return spriteAssets.get(sprite);
        }
        else
        {
            //System.out.println("Image " + sprite + " was not found");
        }
        return null;
    }

    public void playSound(String sound,boolean repeat,double volume)
    {
        Media media = getSound(sound);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
         if(repeat) {
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        }
        else
        {
            mediaPlayer.setOnEndOfMedia(() -> currentSounds.remove(sound));
        }
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
        currentSounds.put(sound,mediaPlayer);
    }
    public void playSound(String sound,boolean repeat)
    {
        Media media = getSound(sound);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        if(repeat) {
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(new Duration(0.3)));
        }
        else
        {
            mediaPlayer.setOnEndOfMedia(() -> currentSounds.remove(sound));
        }
        mediaPlayer.play();
        currentSounds.put(sound,mediaPlayer);
    }

    public void stopSound(String sound)
    {
        if(currentSounds.containsKey(sound))
        {
            currentSounds.get(sound).stop();
            currentSounds.remove(sound);
        }
    }

    public HashMap<String,Image> getSpriteAssets()
    {
        return spriteAssets;
    }


    public Media getSound(String sound)
    {
        return soundAssets.get(sound);
    }

    public GameStateEnum getCurrentGameState() {
        return currentGameState;
    }

    public void setCurrentGameState(GameStateEnum currentGameState) {
        this.currentGameState = currentGameState;
    }

    public ArrayList<XBoxController> getConnectedControllers()
    {
        return connectedControllers;
    }

    public Random getRandom() {return random;}

    public ArrayList<Player> getConnectedPlayers() {
        return connectedPlayers;
    }
}
