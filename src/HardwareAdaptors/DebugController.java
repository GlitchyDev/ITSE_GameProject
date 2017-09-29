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
    public DirectionalPadEnum getDirectionalPad() {
        if(keyboard.isKeyDown(Component.Identifier.Key.UP))
        {
            if(keyboard.isKeyDown(Component.Identifier.Key.LEFT))
            {
                return DirectionalPadEnum.NORTH_WEST;
            }
            else {
                if (keyboard.isKeyDown(Component.Identifier.Key.RIGHT)) {
                    return DirectionalPadEnum.NORTH_EAST;
                }
                else
                {
                    return DirectionalPadEnum.NORTH;
                }
            }
        }
        if(keyboard.isKeyDown(Component.Identifier.Key.DOWN))
        {
            if(keyboard.isKeyDown(Component.Identifier.Key.LEFT))
            {
                return DirectionalPadEnum.SOUTH_WEST;
            }
            else {
                if (keyboard.isKeyDown(Component.Identifier.Key.RIGHT)) {
                    return DirectionalPadEnum.SOUTH_EAST;
                }
                else
                {
                    return DirectionalPadEnum.SOUTH;
                }
            }
        }

        if(keyboard.isKeyDown(Component.Identifier.Key.LEFT))
        {
            return DirectionalPadEnum.WEST;
        }
        if(keyboard.isKeyDown(Component.Identifier.Key.RIGHT))
        {
            return DirectionalPadEnum.EAST;
        }

        return DirectionalPadEnum.NONE;
    }

    @Override
    public boolean getBack() {
        return keyboard.isKeyDown(Component.Identifier.Key.BACK);
    }

    @Override
    public boolean getStart() {
        return keyboard.isKeyDown(Component.Identifier.Key.INSERT);
    }

    @Override
    public boolean getButtonB() {
        return keyboard.isKeyDown(Component.Identifier.Key.LSHIFT);
    }
    public boolean getButtonA() {
        return keyboard.isKeyDown(Component.Identifier.Key.LCONTROL);
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
}
