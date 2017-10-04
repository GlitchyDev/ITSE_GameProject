package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.util.HashMap;

public class RenderingTest extends Application {
    private static final int CELL_SIZE = 2;
    private static final int BOARD_SIZE = 400;

    private static final int W = BOARD_SIZE * CELL_SIZE;
    private static final int H = BOARD_SIZE * CELL_SIZE;

    private static final double CYCLE_TIME_IN_MS = 5_000;

    private final WritablePixelFormat<IntBuffer> pixelFormat =
            PixelFormat.getIntArgbPreInstance();

    private Canvas canvas = new Canvas(W, H);
    private GraphicsContext gc = canvas.getGraphicsContext2D();
    private int[] buffer = new int[W * H];

    protected int lastFPS;
    // The amount of frames rendered this second
    protected int frameCount;
    // The Milisecond time of the last frame update
    protected long lastFPSUpdate;
    // --------- [Logic Percentage]
    protected double lastLogicFramePercentage;
    // --------- [Render Percentage]
    protected double lastRenderFramePercentage;

    @Override
    public void start(Stage stage) {
        final long start = System.nanoTime();
        lastFPS = 0;
        frameCount = 0;
        lastFPSUpdate = 0;
        lastLogicFramePercentage = 0;
        lastRenderFramePercentage = 0;

        Image image = new Image("file:GameAssets/Sprites/Terrain/Test.png");
        int[] convertedImageBuffer = new int[(int)(image.getWidth() + image.getHeight() * image.getWidth())];
        PixelReader reader = image.getPixelReader();
        for(int x = 0; x < image.getWidth(); x++)
        {
            for(int y = 0; y < image.getHeight(); y++)
            {
                convertedImageBuffer[(int)(x + y * image.getWidth())] = reader.getArgb(x,y);
            }
        }



        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long startRenderTime = System.nanoTime();
                gc.clearRect(0, 0, W, H);




                for (int x = 0; x < W; x++) {
                    for (int y = 0; y < H; y++) {
                            int argb = convertedImageBuffer[(int)(x%27 + y%27 * image.getWidth())];
                            buffer[x + y * W] = argb;
                    }
                }
                outerloop:
                for (int x = 0; x < W; x++) {
                    for (int y = 0; y < H; y++) {
                        int argb = buffer[x + y * W];
                        if(x == 500)
                        {
                           // break outerloop;
                        }

                        long old = System.nanoTime();
                        long neww = System.nanoTime();
                        int red = x%255;
                        int blue = y%255;

                        int green = (int)(neww - old);
                        int alpha = (0xFF & (argb >> 24));

                        buffer[x + y * W] = intsToARGB(red,blue,green,alpha);

                    }
                }

                /*
                for (int i = 0; i < W; i += CELL_SIZE) {
                    for (int j = 0; j < H; j += CELL_SIZE) {
                        int ci = (i/CELL_SIZE + j/CELL_SIZE) % 5 == 0 ? ci1 : ci2;
                        for (int dx = 0; dx < CELL_SIZE; dx++) {
                            for (int dy = 0 ; dy < CELL_SIZE; dy++) {
                                buffer[i + dx + W * (j + dy)] = ci;
                            }
                        }
                    }
                }
                */

                PixelWriter p = gc.getPixelWriter();
                p.setPixels(0, 0, W, H, pixelFormat, buffer, 0, W);


                if(System.currentTimeMillis() - lastFPSUpdate >= 1000 )
                {
                    lastFPS = frameCount;
                    frameCount = 1;
                    lastFPSUpdate = System.currentTimeMillis();
                }
                else {
                    frameCount++;
                    if(frameCount > 60)
                    {
                        frameCount = 60;
                    }
                }
                lastRenderFramePercentage = (100L/16666666.6667) * (System.nanoTime() - startRenderTime);

                gc.setFill(Color.RED);
                gc.fillText("FPS: " + lastFPS, 50,50);
                gc.fillText("Percentage: " + lastRenderFramePercentage, 50,60);
            }
        };
        timer.start();

        stage.setScene(new Scene(new Group(canvas)));
        stage.show();
    }

    /*
    private int toARGB(Color c) {
        return
                (                      255  << 24) |
                        ((int) (c.getRed()   * 255) << 16) |
                        ((int) (c.getGreen() * 255) << 8)  |
                        ((int) (c.getBlue()  * 255));
    }
    */

    public static int intsToARGB(int red, int green, int blue, int alpha) {
        return (alpha << 24) | (red << 16 ) | (green<<8) | blue;
}

    public static void main(String[] args) {
        launch(args);
    }
}