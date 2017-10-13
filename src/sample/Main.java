package sample;

import GameInfo.Environment.World;
import GameInfo.GameStateEnum;
import GameInfo.GlobalGameData;
import GameStates.TestWorldGameState;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.java.games.input.Version;

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
        Screen screen = Screen.getPrimary();
        //Rectangle2D bounds = screen.getVisualBounds();

        //primaryStage.setX(bounds.getMinX());
       // primaryStage.setY(bounds.getMinY());
        //primaryStage.setWidth(bounds.getWidth());
        //primaryStage.setHeight(bounds.getHeight());

       //primaryStage.setMaximized(true);
        //primaryStage.initStyle(StageStyle.UNDECORATED);

        Group root = new Group();
        Scene theScene = new Scene(root);
        primaryStage.setScene(theScene);
        Canvas canvas = new Canvas(primaryStage.getWidth() - 6, primaryStage.getHeight() - 39);

        canvas.heightProperty().bind(primaryStage.heightProperty().subtract(39));
        canvas.widthProperty().bind(primaryStage.widthProperty().subtract(6));

        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.setCache(true);

        primaryStage.setTitle("Endless Days " + Version.getVersion());
        primaryStage.setResizable(true);

        globalGameData = new GlobalGameData(GameStateEnum.MainMenu,primaryStage,canvas);

        // Create the initial window size
        primaryStage.setWidth(((TestWorldGameState)globalGameData.getGameState("TestWorld")).getViewport().getViewWidthX() * World.getScaledUpSquareSize() + 6);
        primaryStage.setHeight(((TestWorldGameState)globalGameData.getGameState("TestWorld")).getViewport().getViewHeightY() * World.getScaledUpSquareSize() + 39);

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
