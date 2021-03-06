package HardwareAdaptors;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Keyboard;

/**
 * The Purpose of this class is
 * - To Replace XBoxController, in the event we can not find valid controls ( AE: No conenected controller )
 * - Replace each "Get" for XBox Controllers with a
 */
public class DebugController extends XBoxController {
    private Keyboard keyboard;

    public DebugController(Controller controllerDevice, int id) {
        super(controllerDevice, id);
        keyboard = (Keyboard) controllerDevice;
        controllerType = ControllerType.KeyboardController;
    }


    @Override
    public DirectionalEnum getDirectionalPad() {
        if(keyboard.isKeyDown(Component.Identifier.Key.UP))
        {
            if(keyboard.isKeyDown(Component.Identifier.Key.LEFT))
            {
                return DirectionalEnum.NORTH_WEST;
            }
            else {
                if (keyboard.isKeyDown(Component.Identifier.Key.RIGHT)) {
                    return DirectionalEnum.NORTH_EAST;
                }
                else
                {
                    return DirectionalEnum.NORTH;
                }
            }
        }
        if(keyboard.isKeyDown(Component.Identifier.Key.DOWN))
        {
            if(keyboard.isKeyDown(Component.Identifier.Key.LEFT))
            {
                return DirectionalEnum.SOUTH_WEST;
            }
            else {
                if (keyboard.isKeyDown(Component.Identifier.Key.RIGHT)) {
                    return DirectionalEnum.SOUTH_EAST;
                }
                else
                {
                    return DirectionalEnum.SOUTH;
                }
            }
        }

        if(keyboard.isKeyDown(Component.Identifier.Key.LEFT))
        {
            return DirectionalEnum.WEST;
        }
        if(keyboard.isKeyDown(Component.Identifier.Key.RIGHT))
        {
            return DirectionalEnum.EAST;
        }

        return DirectionalEnum.NONE;
    }

    @Override
    public boolean getBack() {
        return keyboard.isKeyDown(Component.Identifier.Key.ESCAPE);
    }

    @Override
    public boolean getStart() {
        return keyboard.isKeyDown(Component.Identifier.Key.END);
    }

    @Override
    public boolean getButtonB() {
        return keyboard.isKeyDown(Component.Identifier.Key.LSHIFT);
    }
    public boolean getButtonA() {
        return keyboard.isKeyDown(Component.Identifier.Key.LCONTROL);
    }

    @Override
    public boolean getLeftShoulder() {
        return keyboard.isKeyDown(Component.Identifier.Key._1);
    }

    @Override
    public boolean getRightShoulder() {
        return keyboard.isKeyDown(Component.Identifier.Key._0);
    }

    @Override
    public String toString()
    {
        String information = "Keyboard Controller #" + id + "\n";
        information += "Up: " + keyboard.isKeyDown(Component.Identifier.Key.UP) + "\n";
        information += "Down: " + keyboard.isKeyDown(Component.Identifier.Key.DOWN) + "\n";
        information += "Left: " + keyboard.isKeyDown(Component.Identifier.Key.LEFT) + "\n";
        information += "Right: " + keyboard.isKeyDown(Component.Identifier.Key.RIGHT) + "\n";
        information += "Back: " + keyboard.isKeyDown(Component.Identifier.Key.BACK) + "\n";

        return information;
    }

    @Override
    public boolean isXBoxController() {
        return false;
    }

    @Override
    public boolean isAnyThingPressed()
    {
        return getDirectionalPad() != DirectionalEnum.NONE || getButtonA() || getButtonB() || getButtonX() || getButtonY() || getRightShoulder() || getRightShoulder() || getStart();
    }
}
