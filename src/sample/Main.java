package sample;

import GameInfo.Environment.World;
import GameInfo.WorldViewport;
import GameStates.Enums.GameStateEnum;
import GameInfo.GlobalGameData;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

/**
 * This class is meant to
 * - Host the Main Application
 * - Run the Rendering
 * - Initiate all the required parts
 * - Scan for controllers
 */
public class Main extends Application {
    private GlobalGameData globalGameData;

    //private static boolean cache = false;


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setWidth(World.getScaledUpSquareSize() * WorldViewport.widthBuffer + 16);
        primaryStage.setHeight(World.getScaledUpSquareSize() * WorldViewport.heightBuffer + 39);

        primaryStage.setMinWidth(World.getScaledUpSquareSize() * 15 + 16);
        primaryStage.setMinHeight(World.getScaledUpSquareSize() * 15 + 39);

        Group root = new Group();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);
        Canvas canvas = new Canvas(primaryStage.getWidth(), primaryStage.getHeight());

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setCache(true);

        RenewJarUUID.renewJarUUID();
        primaryStage.setTitle("Endless Days " + RenewJarUUID.getJarUUID());
        primaryStage.setResizable(true);

        globalGameData = new GlobalGameData(GameStateEnum.MainMenu,primaryStage,canvas);
        primaryStage.show();



        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                if(!primaryStage.isIconified()) {

                    globalGameData.getGameState(globalGameData.getCurrentGameState().toString()).runLogic(canvas, gc);
                    globalGameData.getGameState(globalGameData.getCurrentGameState().toString()).render(canvas, gc);

                }
            }
        }.start();


    }







    public static void main(String[] args) {
        launch(args);
    }
}
