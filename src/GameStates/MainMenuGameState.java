package GameStates;

import GameInfo.GlobalGameData;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MainMenuGameState extends GameStateBase {
    private long phaseStartTime;

    public MainMenuGameState(GlobalGameData globalGameData) {
        super(globalGameData);
        phaseStartTime = System.currentTimeMillis();

    }


    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {

    }

    @Override
    protected void doRender(Canvas canvas, GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());


    }
}
