package GameInfo;

import GameStates.DebugControlsGameState;
import GameStates.GameStateBase;
import GameStates.MainMenuGameState;
import GameStates.TestWorldGameState;
import javafx.scene.image.Image;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import sample.DebugController;
import sample.TestRenderHelper;
import sample.XBoxController;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

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
    private GameStateEnum currentGameState;
    private ArrayList<XBoxController> connectedControllers;
    private ArrayList<Player> connectedPlayers;
    private Random random;

    public GlobalGameData(GameStateEnum startingState)
    {
        System.out.println("Global Gamestate: Creating Global GameState");
        gameStates = new HashMap<>();
        sprites = new HashMap<>();
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

    private void loadAssets() {
        System.out.println("Load Assets: Loading all Registered Folders");
        ArrayList<String> registeredFolders = new ArrayList<>(Arrays.asList(
                "GameAssets/Sprites/TestSprites/",
                "GameAssets/Sprites/Pro/",
                "GameAssets/Sprites/Terrain/",
                "GameAssets/Sprites/Skull_Entity/"
        ));
        ArrayList<String> resizeException = new ArrayList<>(Arrays.asList(
                "Pro"
        ));
        System.out.println("Load Assets: Processing Sprites");
        System.out.println("Load Assets: Added Sprites");
        for (String currentFolder : registeredFolders) {
            File startingFolder = new File(currentFolder);
            System.out.println("Load Assets: Checking Folder " + startingFolder.getName());
            for (File file : startingFolder.listFiles()) {
                if (file.isFile()) {
                    String temp = file.getName().replace(".png","");

                    boolean doResize = true;
                    for(String exception: resizeException) {
                        if (temp.contains(exception))
                        {
                            doResize = false;
                        }
                    }
                    if(doResize)
                    {
                        int size = 2;
                        sprites.put(temp, TestRenderHelper.resample(new Image("file:" + currentFolder + file.getName()),size));
                        System.out.println("  - " + temp);
                    }
                    else
                    {
                        sprites.put(temp, new Image("file:" + currentFolder + file.getName()));
                        System.out.println("  ~ " + temp);
                    }
                }
            }
        }
        System.out.println("Load Assets: Completed Loading");
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
        gameStates.remove("TestWorld");
        gameStates.put("TestWorld",new TestWorldGameState(this));
        getGameState("TestWorld").enterState(GameStateEnum.TestWorld);
    }


    public Image getSprite(String sprite)
    {
        return sprites.get(sprite);
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
