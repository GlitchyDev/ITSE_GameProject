package GameInfo;

import GameStates.DebugControlsGameState;
import GameStates.GameStateBase;
import GameStates.MainMenuGameState;
import GameStates.TestWorldGameState;
import javafx.scene.image.Image;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import sample.DebugController;
import sample.XBoxController;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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
    private GameStateEnum gameStateEnum;
    private ArrayList<XBoxController> connectedControllers;

    public GlobalGameData(GameStateEnum startingState)
    {
        System.out.println("Global Gamestate: Creating Global GameState");
        gameStates = new HashMap<>();
        sprites = new HashMap<>();
        this.gameStateEnum = startingState;
        connectedControllers = new ArrayList<>();

        createGameStates();
        connectedControllers.addAll(scanForControllers());
        loadAssets();
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
                "GameAssets/Sprites/TestSprites/"
        ));
        System.out.println("Load Assets: Processing Sprites");
        System.out.println("Load Assets: Added Sprites");
        for (String currentFolder : registeredFolders) {
            File startingFolder = new File(currentFolder);
            System.out.println("Load Assets: Checking Folder " + startingFolder.getName());
            for (File file : startingFolder.listFiles()) {
                if (file.isFile()) {
                    String temp = file.getName().substring(0, file.getName().length() - 4);
                    sprites.put(temp, new Image("file:" + currentFolder + file.getName()));
                    System.out.println("  - " + temp);
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

    private static ControllerEnvironment createDefaultEnvironment() throws ReflectiveOperationException {
        // Find constructor (class is package private, so we can't access it directly)
        Constructor<ControllerEnvironment> constructor = (Constructor<ControllerEnvironment>)
                Class.forName("net.java.games.input.DefaultControllerEnvironment").getDeclaredConstructors()[0];
        // Constructor is package private, so we have to deactivate access control checks
        constructor.setAccessible(true);
        // Create object with default constructor
        return constructor.newInstance();
    }

    // Getters and Setters

    public GameStateBase getGameState(String state)
    {
        return gameStates.get(state);
    }

    public Image getSprite(String sprite)
    {
        return sprites.get(sprite);
    }

    public GameStateEnum getGameStateEnum() {
        return gameStateEnum;
    }

    public void setGameStateEnum(GameStateEnum gameStateEnum) {
        this.gameStateEnum = gameStateEnum;
    }

    public ArrayList<XBoxController> getConnectedControllers()
    {
        return connectedControllers;
    }
}
