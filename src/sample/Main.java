package sample;

import GameStates.DebugControlsGameState;
import GameStates.TestWorldGameState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main extends Application {
    private HashMap<String, Image> sprites;
    private Controller xboxController;
    private XBoxController X;



    @Override
    public void start(Stage primaryStage) throws Exception{
        //new Thread(new ServerConnection()).start();
       // new Thread(new ClientConnection()).start();

        primaryStage.setTitle("Endless Days");
        primaryStage.setResizable(true);


        primaryStage.setWidth(486);
        primaryStage.setHeight(349);
        Group root = new Group();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);
        Canvas canvas = new Canvas(480, 320);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setCache(true);
        // *****
        primaryStage.show();
        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        System.out.println("Controller #" + controllers.length);

        for(Controller c: controllers)
        {
            System.out.println(c.getName() + " " + c.getType());
            if(c.getType().equals(Controller.Type.GAMEPAD))
            {
                xboxController = c;
                System.out.println("Found our Controller!");
            }
        }

        X = new XBoxController(xboxController,1);


        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth(newVal.intValue()-6);
        });

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight(newVal.intValue()-29);
        });

        sprites = new HashMap<>();
        loadAssets();


        //DebugControlsGameState debugState = new DebugControlsGameState(X,sprites);
        TestWorldGameState debugState = new TestWorldGameState(X);
        //xboxController.poll();
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                debugState.runLogic(canvas,gc);
                debugState.render(canvas,gc);
            }
        }.start();


    }

    public void loadAssets() {

        System.out.println("Loading all Registered Folders");
        System.out.println("Processing Sprites");
        ArrayList<String> registeredFolders = new ArrayList<>(Arrays.asList(
                "GameAssets/Sprites/TestSprites/"
        ));
        for (String currentFolder : registeredFolders) {
            File startingFolder = new File(currentFolder);
            for (File file : startingFolder.listFiles()) {
                if (file.isFile()) {

                    String temp = file.getName().substring(0, file.getName().length() - 4);
                    sprites.put(temp, new Image("file:" + currentFolder + file.getName()));
                    System.out.println("  - " + temp);
                }
            }
        }
        System.out.println("Processing Complete!");
    }





    public static void main(String[] args) {
        launch(args);
    }
}
