package GameInfo;

import GameStates.DebugControlsGameState;
import GameStates.GameStateBase;
import GameStates.MainMenuGameState;
import GameStates.TestWorldGameState;
import RenderingHelpers.BufferedRenderingContext;
import RenderingHelpers.ImageRenderHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import HardwareAdaptors.DebugController;
import HardwareAdaptors.XBoxController;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

/**
 *  The purpose of this Class is to store all Global GameData used by GameStates such as
 *  - All other GameStates
 *  - Loaded Assets
 *  - The "Random" class, assuming one is needed
 *  - The Network Connection ( Client or Server )
 */
public class GlobalGameData {
    private HashMap<String, GameStateBase> gameStates;
    private HashMap<String, Image> sprites;
    private HashMap<String, Media> sounds;

    private GameStateEnum currentGameState;
    private ArrayList<XBoxController> connectedControllers;
    private ArrayList<Player> connectedPlayers;
    private Random random;
    MediaPlayer player;

    public GlobalGameData(GameStateEnum startingState)
    {
        System.out.println("Global Gamestate: Creating Global GameState");
        gameStates = new HashMap<>();
        sprites = new HashMap<>();
        sounds = new HashMap<>();
        this.currentGameState = startingState;
        random = new Random();
        connectedControllers = new ArrayList<>();
        connectedPlayers = new ArrayList<>();

        connectedControllers.addAll(scanForControllers());

        loadAssets();

        createGameStates();
    }

    private void createGameStates()
    {
        gameStates.put("MainMenu", new MainMenuGameState(this));
        gameStates.put("TestWorld", new TestWorldGameState(this));
        gameStates.put("DebugControls", new DebugControlsGameState(this));
    }




    public void loadAssets()
    {
        String link = "/Sprites/Old/Pro_Test_Darker.png";
        InputStream stream = getClass().getResourceAsStream(link);
        Image image = null;
        if("true".equals(System.getProperty("isJar"))) {

            /*
            System.out.println("Load Assets: Jar Asset Loading");
            image = new Image(getClass().getResourceAsStream(link));
            sprites.put("IMAGE",image);

            Media media = new Media(getClass().getResource("/Sounds/BackgroundMusic/CaveWaterDrops.mp3").toExternalForm());
            //Media media = new Media(getClass().getResource("Sounds/BackgroundMusic/CaveWaterDrops.mp3").toString());
            player = new MediaPlayer(media);

            player.play();
            */
            InputStream inputSound = getClass().getResourceAsStream("/Sounds/SoundsAssetList.txt");
            Scanner soundScanner = new Scanner(new InputStreamReader(inputSound));
            while(soundScanner.hasNextLine())
            {
                String soundPath = soundScanner.nextLine();
                String[] parsed = soundPath.split("/");
                //System.out.println("Adding Sound " + parsed[parsed.length-1].replace(".mp3","") + " " + (new Media(getClass().getResource("/Sounds" + soundPath).toString()) == null) );
                sounds.put(parsed[parsed.length-1].replace(".mp3",""),new Media(getClass().getResource("/Sounds" + soundPath).toString()));
            }

            InputStream inputSprites = getClass().getResourceAsStream("/Sprites/SpriteAssetList.txt");
            Scanner spriteScanner = new Scanner(new InputStreamReader(inputSprites));
            while(spriteScanner.hasNextLine())
            {
                String spritePath = spriteScanner.nextLine();
                String[] parsed = spritePath.split("/");
                sprites.put(parsed[parsed.length-1].replace(".png",""),new Image(getClass().getResourceAsStream("/Sprites" + spritePath)));
            }

        }
        else
        {
            System.out.println("Load Assets: Project Asset Loading");
            System.out.println("Load Assets: UpdatingAssetList");

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
                            Image newSprite = new Image(getClass().getResource("/Sprites/" + innerFolders.getName() + "/" + sprite.getName()).toString());
                                System.out.println(sprite.getName().replace(".png","")+"*");
                                sprites.put(sprite.getName().replace(".png",""), ImageRenderHelper.resample(newSprite,2,false));
                                writer.println("/" + innerFolders.getName() + "/" + sprite.getName());
                        }
                    }
                }

                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Load Sounds
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
                            System.out.println(sound.getName().replace(".mp3","")+"*");
                            Media media = new Media( getClass().getResource("/Sounds/" + innerFolders.getName() + "/" + sound.getName()).toString());
                            sounds.put(sound.getName().replace(".mp3",""),media);
                            writer.println("/" + innerFolders.getName() + "/" + sound.getName());
                        }
                    }
                }

                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }


    }

    /*
    private void loadAssets() {
        System.out.println("Load Assets: Loading all Registered Folders");
        ArrayList<String> registeredSpriteFolders = new ArrayList<>(Arrays.asList(
                "/Sprites/TestSprites",
                "/Sprites/Pro",
                "/Sprites/Terrain",
                "/Sprites/Skull_Entity"
        ));
        ArrayList<String> resizeException = new ArrayList<>(Arrays.asList(
                "InsertExemptions"
        ));
        System.out.println("Load Assets: Processing Sprites");
        System.out.println("Load Assets: Added Sprites");
        for (String currentFolder : registeredSpriteFolders) {


            File startingFolder = new File("GameAssets" + currentFolder + "/");

            System.out.println("Load Assets: Checking Folder " + startingFolder.getName() + startingFolder.exists());
            for (File file : startingFolder.listFiles()) {
                System.out.println("Found " + file.getName());
                if (file.isFile()) {
                    if(file.getName().contains(".png")) {
                        String temp = file.getName().replace(".png", "");


                        boolean doResize = true;
                        for (String exception : resizeException) {
                            if (temp.contains(exception)) {
                                doResize = false;
                            }
                        }
                        if (doResize) {
                            int size = 2;

                            sprites.put(temp, ImageRenderHelper.resample(new Image(getClass().getResource(currentFolder + "/" + file.getName()).toExternalForm()), size, false));
                            System.out.println("  - " + temp);
                        } else {
                            sprites.put(temp, new Image("file:" + currentFolder + file.getName()));
                            System.out.println("  ~ " + temp);
                        }
                    }
                }
            }
        }
        ArrayList<String> registeredSoundFolders = new ArrayList<>(Arrays.asList(
                "GameAssets/Sounds/"

        ));
        System.out.println("Load Assets: Processing Sounds");
        System.out.println("Load Assets: Added Sounds");
        for (String currentFolder : registeredSoundFolders) {
            File startingFolder = new File(currentFolder);
            System.out.println("Load Assets: Checking Folder " + startingFolder.getName());
            for (File file : startingFolder.listFiles()) {
                if (file.isFile()) {
                    String temp = file.getName().replace("mp3","");
                    temp = temp.replace("MP3","");
                    temp = temp.replace(".","");
                    sounds.put(temp, new Media(new File(currentFolder + file.getName()).toURI().toString()));
                    System.out.println("  - " + temp);
                }
            }
        }
        System.out.println("Load Assets: Completed Loading");
    }

*/
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
        gameStates.remove("TestWorld");
        gameStates.put("TestWorld",new TestWorldGameState(this));
        getGameState("TestWorld").enterState(GameStateEnum.TestWorld);
    }

    public Image getSprite(String sprite)
    {
        return sprites.get(sprite);
    }

    public Media getSound(String sound)
    {
        return sounds.get(sound);
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
