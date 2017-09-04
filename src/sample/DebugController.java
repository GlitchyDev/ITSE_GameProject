package sample;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Keyboard;

public class DebugController extends XBoxController {
    private Keyboard keyboard;

    public DebugController(Controller controllerDevice, int id) {
        super(controllerDevice, id);
        keyboard = (Keyboard) controllerDevice;
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
}
