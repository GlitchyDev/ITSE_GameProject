package GameStates;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Robert on 8/26/2017.
 * This class is made to:
 * - Store information unique to each Game State
 * - Render the Gamestate to the screen
 * - Perform or recieve logic for that Gamestate
 */
public abstract class GameStateBase {
    // ---------[FPS Information]
    // The last Seconds FPS
    public int lastFPS;
    // The amount of frames rendered this second
    public int frameCount;
    // The Milisecond time of the last frame update
    public long lastFPSUpdate;
    // --------- [Logic Percentage]
    public double lastLogicFramePercentage;
    // --------- [Render Percentage]
    public double lastRenderFramePercentage;
    // Testing


    public GameStateBase()
    {
        lastFPS = 0;
        frameCount = 0;
        lastFPSUpdate = 0;
        lastLogicFramePercentage = 0;
        lastRenderFramePercentage = 0;
        //


    }


    /**
     Runs the Inherited Logic for the Gamestate, and also calculates the percentage of the Frame it takes to do
     */
    public void runLogic(Canvas canvas, GraphicsContext gc)
    {
        long startLogicTime = System.nanoTime();
        doLogic(canvas, gc);
        lastLogicFramePercentage = (100.0/16666666.6667) * (System.nanoTime() - startLogicTime);
    }

    /**
     * The Abstract overriden by classes that extend GameStateBase that runs the Logic
     */
    abstract protected void doLogic(Canvas canvas, GraphicsContext gc);

    /**
     Runs the Inherited Rendering for the Gamestate, and also calculates the percentage of the Frame it takes to do
     */
    public void render(Canvas canvas, GraphicsContext gc)
    {
        long startRenderTime = System.nanoTime();
        doRender(canvas, gc);
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
    }


    /**
     * The Abstract overriden by classes that extend GameStateBase that runs the Logic

     */
    abstract protected void doRender(Canvas canvas, GraphicsContext gc);

}
