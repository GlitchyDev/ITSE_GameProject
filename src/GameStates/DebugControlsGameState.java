package GameStates;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import sample.XBoxController;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Robert on 8/26/2017.
 *
 * Modified by Charlie on 8/30 - this is a test, delete this comment later
 */
public class DebugControlsGameState extends GameStateBase {
    private HashMap<String, Image> sprites;
    private XBoxController controller;
    private Random random;

    public DebugControlsGameState(XBoxController controller,HashMap<String,Image> sprites)
    {
        this.controller = controller;
        this.sprites = sprites;
        random = new Random();
    }

    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {
        if(controller.hasController()) {
            controller.poll();
        }
    }

    @Override
    protected void doRender(Canvas canvas, GraphicsContext gc) {

        // Clear the Canvas
        gc.setFill(Color.GRAY);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        // Render Background
        for(int x = 0; x < 51; x++)
        {
            for(int y = 0; y < 26; y++)
            {
                int imagesize = 27;
                gc.drawImage(sprites.get("Test"),x * imagesize,y * imagesize,imagesize,imagesize);

            }
        }

        if(controller.hasController()) {
            // Render Controller Button Information
            int i = 0;
            for (String s : controller.toString().split("\n")) {
                gc.setFill(Color.BLACK);
                gc.fillText(s, 20, 30 + i * 10);
                i++;
            }

            // Render the Controller Sticks
            gc.setFill(Color.BLUE);
            gc.fillRect(200, 200, 10, 10);
            gc.setStroke(Color.GOLD);
            gc.strokeLine(205, 205, 205 + controller.getLeftStickX() * 20, 205 + controller.getLeftStickY() * 20);
            gc.setFill(Color.AQUA);
            gc.fillRect(202 + controller.getLeftStickX() * 20, 202 + controller.getLeftStickY() * 20, 6, 6);
            gc.setStroke(Color.RED);
            gc.strokeRect(180, 180, 50, 50);
            //
            gc.setFill(Color.BLUE);
            gc.fillRect(250, 200, 10, 10);
            gc.setStroke(Color.GOLD);
            gc.strokeLine(255, 205, 255 + controller.getRightStickX() * 20, 205 + controller.getRightStickY() * 20);
            gc.setFill(Color.AQUA);
            gc.fillRect(252 + controller.getRightStickX() * 20, 202 + controller.getRightStickY() * 20, 6, 6);
            gc.setStroke(Color.RED);
            gc.strokeRect(230, 180, 50, 50);
        }

        // Render the FPS and Logic and Render Percentages
        gc.setFill(Color.BLACK);
        gc.fillText("FPS: " + lastFPS,250,10);
        gc.fillText("LogicPercentage: " + lastLogicFramePercentage,250,20);
        gc.fillText("RenderPercentage: " + lastRenderFramePercentage,250,30);



    }
}
