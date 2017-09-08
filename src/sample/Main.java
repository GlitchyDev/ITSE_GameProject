package sample;

import GameInfo.Environment.World;
import GameInfo.GameStateEnum;
import GameInfo.GlobalGameData;
import GameStates.TestWorldGameState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;

/**
 * This class is meant to
 * - Host the Main Application
 * - Run the Rendering
 * - Initiate all the required parts
 * - Scan for controllers
 */
public class Main extends Application {
    private GlobalGameData globalGameData;



    @Override
    public void start(Stage primaryStage) throws Exception{
        //new Thread(new ServerConnection()).start();
       // new Thread(new ClientConnection()).start();


        // *****



        /*
        TestWorldGameState debugState = new TestWorldGameState(g, g.scanForControllers().get(0));
        primaryStage.setWidth(debugState.getViewport().getViewWidthX() * World.getUnitRatio() + 6);
        primaryStage.setHeight(debugState.getViewport().getViewHeightY() * World.getUnitRatio() + 39);
        */

        primaryStage.setTitle("Endless Days");
        primaryStage.setResizable(true);

        globalGameData = new GlobalGameData(GameStateEnum.MainMenu);

        // Create the initial window size
        primaryStage.setWidth(((TestWorldGameState)globalGameData.getGameState("TestWorld")).getViewport().getViewWidthX() * World.getUnitRatio() + 6);
        primaryStage.setHeight(((TestWorldGameState)globalGameData.getGameState("TestWorld")).getViewport().getViewHeightY() * World.getUnitRatio() + 39);

        Group root = new Group();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);
        Canvas canvas = new Canvas(primaryStage.getWidth() - 6, primaryStage.getHeight() - 39);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setCache(true);


        primaryStage.show();




        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                globalGameData.getGameState(globalGameData.getCurrentGameState().toString()).runLogic(canvas,gc);
                globalGameData.getGameState(globalGameData.getCurrentGameState().toString()).render(canvas,gc);
            }
        }.start();


    }







    public static void main(String[] args) {
        launch(args);
    }
}
