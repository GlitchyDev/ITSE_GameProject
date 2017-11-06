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
    private boolean blinking = false;
    private long lastBlinkStartTime = 0;
    private final String uuid = RenewJarUUID.getJarUUID();
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

        primaryStage.setTitle("~Endless Day~");
        primaryStage.setResizable(true);

        globalGameData = new GlobalGameData(GameStateEnum.MainMenu,primaryStage,canvas);
        primaryStage.getIcons().add(globalGameData.getSprite("WindowIcon"));

        primaryStage.show();

        canvas.setOnMouseClicked(event -> {
            int random = (int)(Math.random()* 10);
            if(random == 0)
            {
                if(!blinking) {
                    blinking = true;
                    lastBlinkStartTime = System.currentTimeMillis();
                }
            }
        });

        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                if(!primaryStage.isIconified()) {
                    globalGameData.getGameState(globalGameData.getCurrentGameState().toString()).runLogic(canvas, gc);
                    globalGameData.getGameState(globalGameData.getCurrentGameState().toString()).render(canvas, gc);
                    doWindowBlink(primaryStage);
                }
            }
        }.start();


    }

    private void doWindowBlink(Stage primaryStage) {
        int random = (int)(Math.random()* 1000);
        if(random == 0)
        {
            if(!blinking) {
                blinking = true;
                lastBlinkStartTime = System.currentTimeMillis();
            }
        }
        if(blinking)
        {
            double duration = (System.currentTimeMillis() - lastBlinkStartTime)/1000.0;
            if(duration < 0.2)
            {
                if(!primaryStage.getIcons().contains(globalGameData.getSprite("WindowIcon_Blink_1"))) {
                    primaryStage.getIcons().clear();
                    primaryStage.getIcons().add(globalGameData.getSprite("WindowIcon_Blink_1"));
                }
            }
            else
            {
                if(duration < 0.4)
                {
                    if(!primaryStage.getIcons().contains(globalGameData.getSprite("WindowIcon_Blink_2"))) {
                        primaryStage.getIcons().clear();
                        primaryStage.getIcons().add(globalGameData.getSprite("WindowIcon_Blink_2"));
                    }

                }
                else
                {
                    if(duration < 0.6) {
                        if(!primaryStage.getIcons().contains(globalGameData.getSprite("WindowIcon_Blink_1"))) {
                            primaryStage.getIcons().clear();
                            primaryStage.getIcons().add(globalGameData.getSprite("WindowIcon_Blink_1"));
                        }
                    }
                    else
                    {
                        blinking = false;
                        primaryStage.getIcons().clear();
                        primaryStage.getIcons().add(globalGameData.getSprite("WindowIcon"));

                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
