package GameStates;

import GameInfo.GameStateEnum;
import GameInfo.GlobalGameData;
import GameStates.Enums.MainMenuPhaseEnum;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.ControllerType;
import sample.XBoxController;

public class MainMenuGameState extends GameStateBase {
    // Phases within a State report "MiniStates" that don't require a seperate State
    private long phaseStartTime;
    private MainMenuPhaseEnum phaseEnum;
    private XBoxController controller;

    public MainMenuGameState(GlobalGameData globalGameData) {
        super(globalGameData);
        phaseStartTime = 0;
        phaseEnum = MainMenuPhaseEnum.MMGPE_Constructor;

        controller = globalGameData.getConnectedControllers().get(0);


    }


    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {
        switch(phaseEnum)
        {
            case MMGPE_Constructor:
                phaseEnum = MainMenuPhaseEnum.MMGSE_Startup;
                phaseStartTime = System.currentTimeMillis();
                break;
            case MMGSE_Startup:
                if(calculateSecondTime(phaseStartTime) >= 1.0 ) {
                    gc.setGlobalAlpha(1.0);
                    phaseEnum = MainMenuPhaseEnum.MMGSE_TitleScreen;
                    phaseStartTime = System.currentTimeMillis();
                }
                break;
            case MMGSE_TitleScreen:
                controller.poll();
                switch(controller.getDirectionalPad())
                {
                    case NORTH:
                        globalGameData.switchGameState(GameStateEnum.TestWorld);
                        break;
                    case WEST:
                        globalGameData.switchGameState(GameStateEnum.DebugControls);
                        break;
                }
                break;
        }
    }

    @Override
    protected void doRender(Canvas canvas, GraphicsContext gc) {

        switch(phaseEnum)
        {
            case MMGSE_Startup:
                gc.setFill(Color.BLACK);
                gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

                gc.setFill(Color.WHITE);
                gc.setGlobalAlpha(calculateSecondTime(phaseStartTime)/1.0);
                gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
                break;
            case MMGSE_TitleScreen:
                gc.setFill(Color.WHITE);
                gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

                if(!(calculateSecondTime(phaseStartTime)/1.0 > 1))
                {
                    gc.setGlobalAlpha(calculateSecondTime(phaseStartTime)/1.0);
                }
                else
                {
                    gc.setGlobalAlpha(1.0);
                }

                gc.setFill(Color.BLUE);
                gc.fillText("Press ^ for TestWorld", canvas.getWidth()/2.0 - 60,200);
                gc.fillText("Press < for DebugControls", canvas.getWidth()/2.0 - 60,canvas.getHeight() - 200);

                if(globalGameData.getConnectedControllers().get(0).getControllerType() == ControllerType.KeyboardController)
                {
                    gc.fillText("Using Keyboard", canvas.getWidth()/2.0 - 60,225);

                }
                break;
        }

    }


    @Override
    public void enterState(GameStateEnum previousState) {

    }

    @Override
    public void exitState(GameStateEnum lastState) {

    }
}