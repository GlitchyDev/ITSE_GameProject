package GameStates;

import GameStates.Enums.GameStateEnum;
import GameInfo.GlobalGameData;
import GameStates.Enums.MainMenuPhaseEnum;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import HardwareAdaptors.ControllerType;
import HardwareAdaptors.XBoxController;
import Utility.DebugModeManager;

public class MainMenuGameState extends GameStateBase {
    // Phases within a State report "MiniStates" that don't require a seperate State
    private long phaseStartTime;
    private MainMenuPhaseEnum phaseEnum;
    private XBoxController controller;
    private boolean rePoll = false;

    public MainMenuGameState(GlobalGameData globalGameData) {
        super(globalGameData);
        phaseStartTime = 0;
        phaseEnum = MainMenuPhaseEnum.MMGPE_Constructor;

        controller = globalGameData.getConnectedControllers().get(0);


    }


    @Override
    protected void doLogic(Canvas canvas, GraphicsContext gc) {
        if(DebugModeManager.isDebugMode) {
            switch (phaseEnum) {
                case MMGPE_Constructor:
                    phaseEnum = MainMenuPhaseEnum.MMGSE_Startup;
                    phaseStartTime = System.currentTimeMillis();
                    break;
                case MMGSE_Startup:
                    if (calculateSecondTime(phaseStartTime) >= 1.0) {
                        gc.setGlobalAlpha(1.0);
                        phaseEnum = MainMenuPhaseEnum.MMGSE_TitleScreen;
                        phaseStartTime = System.currentTimeMillis();
                    }
                    break;
                case MMGSE_TitleScreen:
                    controller.poll();
                    switch (controller.getDirectionalPad()) {
                        case NORTH:
                            globalGameData.switchGameState(GameStateEnum.TestWorld);
                            break;
                        case SOUTH:
                            globalGameData.switchGameState(GameStateEnum.TutorialScreen);
                            break;
                        case EAST:
                            globalGameData.switchGameState(GameStateEnum.TitleScreen);
                            break;
                        case WEST:
                            globalGameData.switchGameState(GameStateEnum.DebugControls);
                            break;

                    }
                    break;
            }
        }
        else
        {
            controller.poll();
            controller.getStart();
            controller.poll();
            if(controller.getStart() || (DebugModeManager.computerDebugModeDefault() && !controller.isXBoxController()))
            {
                System.out.println("Debug Name Enabled");
                globalGameData.getPrimaryStage().show();
                DebugModeManager.enableDebugMode();
            }
            else
            {
                    globalGameData.getPrimaryStage().show();
                    globalGameData.switchGameState(GameStateEnum.TitleScreen);
            }
        }
    }

    @Override
    protected void doRender(Canvas canvas, GraphicsContext gc) {
        if(DebugModeManager.isDebugMode) {
            switch (phaseEnum) {
                case MMGSE_Startup:
                    gc.setFill(Color.BLACK);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    gc.setFill(Color.WHITE);
                    gc.setGlobalAlpha(calculateSecondTime(phaseStartTime) / 1.0);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    break;
                case MMGSE_TitleScreen:
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    if (!(calculateSecondTime(phaseStartTime) / 1.0 > 1)) {
                        gc.setGlobalAlpha(calculateSecondTime(phaseStartTime) / 1.0);
                    } else {
                        gc.setGlobalAlpha(1.0);
                    }

                    gc.setFill(Color.BLUE);
                    gc.fillText("Press /\\ for TestWorld", canvas.getWidth() / 3.0, canvas.getHeight() / 6.0 * 1);
                    gc.fillText("Press < for DebugControls", canvas.getWidth() / 3.0, canvas.getHeight() / 6.0 * 1.5);
                    gc.fillText("Press > for TitleScreen", canvas.getWidth() / 3.0, canvas.getHeight() / 6.0 * 2);
                    gc.fillText("Press \\/ for TitleScreen", canvas.getWidth() / 3.0, canvas.getHeight() / 6.0 * 2.5);


                    if (globalGameData.getConnectedControllers().get(0).getControllerType() == ControllerType.KeyboardController) {
                        gc.fillText("Using Keyboard", canvas.getWidth() / 3.0, canvas.getHeight() / 6.0 * 3);

                    }
                    break;
            }
            gc.setGlobalAlpha(1.0);

            Image sprite = globalGameData.getSprite("Gear");
            gc.save();
            gc.translate(50, 35);
            gc.translate(sprite.getWidth() / 2, sprite.getHeight() / 2);
            gc.rotate((((int) (System.currentTimeMillis() % 8000.0) / 500) % 16 + 1) * 45 / 2);
            gc.drawImage(sprite, -sprite.getWidth() / 2, -sprite.getHeight() / 2);
            gc.restore();

            gc.save();
            gc.translate(82, 35);
            gc.translate(sprite.getWidth() / 2, sprite.getHeight() / 2);
            gc.rotate(-(((int) (System.currentTimeMillis() % 8000.0) / 500 + 5) % 16 + 1) * 45 / 2);
            gc.drawImage(sprite, -sprite.getWidth() / 2, -sprite.getHeight() / 2);
            gc.restore();


            String text = "ABC 123 !?,.:_";
            int charNum = 0;
            for (char c : text.toUpperCase().toCharArray()) {
                if (c != ' ') {
                    Image let = globalGameData.getSprite(String.valueOf(c));
                    gc.drawImage(let, 10 + charNum * 8, 80);
                }
                charNum++;
            }

        }
        else
        {
            gc.setGlobalAlpha(1.0);
            gc.setFill(Color.BLACK);
            gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        }


    }


    @Override
    public void enterState(GameStateEnum previousState) {
    }

    @Override
    public void exitState(GameStateEnum lastState) {

    }
}
