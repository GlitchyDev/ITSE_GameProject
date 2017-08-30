package sample;

import net.java.games.input.Controller;

/**
 * Created by Robert on 8/24/2017.
 *
 * This class is made to:
 * - Streamline and Provide Easy access to the JInput Controls
 * - Label each type of input as what button does, and translate it to proper types ( Boolean for Buttons, Enums for Directionals )
 * - ( Attempt ) to Implement Event Systems, so one can know when values are changed ( At least for buttons where implementation is absolute)
 *
 */
public class XBoxController {
    private Controller controllerDevice;
    private int id;

    public boolean hasController()
    {
        return controllerDevice != null;
    }
    public XBoxController(Controller controllerDevice, int id)
    {
        this.controllerDevice = controllerDevice;
        this.id = id;
    }

    public void poll() {controllerDevice.poll();}

    public double getLeftStickY()
    {
        return normalizeDirectionInput(controllerDevice.getComponents()[0].getPollData());
    }
    public double getLeftStickX()
    {
        return normalizeDirectionInput(controllerDevice.getComponents()[1].getPollData());
    }
    public double getRightStickY() {return normalizeDirectionInput(controllerDevice.getComponents()[2].getPollData());}
    public double getRightStickX() {return normalizeDirectionInput(controllerDevice.getComponents()[3].getPollData());}
    public DirectionalPadEnum getDirectionalPad() {
        return DirectionalPadEnum.translateRawDirection(controllerDevice.getComponents()[15].getPollData());
    }



    public String toString()
    {
        String information = "XBox Controller #" + id + "\n";
        information += "Left Stick Y " + normalizeDirectionInput(controllerDevice.getComponents()[0].getPollData()) + "\n";
        information += "Left Stick X " + normalizeDirectionInput(controllerDevice.getComponents()[1].getPollData()) + "\n";
        information += "Right Stick Y " + normalizeDirectionInput(controllerDevice.getComponents()[2].getPollData()) + "\n";
        information += "Right Stick X " + normalizeDirectionInput(controllerDevice.getComponents()[3].getPollData()) + "\n";
        information += "Right/Left Trigger " + normalizeDirectionInput(controllerDevice.getComponents()[4].getPollData()) + "\n";
        information += "Button A: " + isButtonPressed(controllerDevice.getComponents()[5].getPollData()) + "\n";
        information += "Button B: " + isButtonPressed(controllerDevice.getComponents()[6].getPollData()) + "\n";
        information += "Button X: " + isButtonPressed(controllerDevice.getComponents()[7].getPollData()) + "\n";
        information += "Button Y: " + isButtonPressed(controllerDevice.getComponents()[8].getPollData()) + "\n";
        information += "Left Shoulder: " + isButtonPressed(controllerDevice.getComponents()[9].getPollData()) + "\n";
        information += "Right Shoulder: " + isButtonPressed(controllerDevice.getComponents()[10].getPollData()) + "\n";
        information += "Back: " + isButtonPressed(controllerDevice.getComponents()[11].getPollData()) + "\n";
        information += "Start: " + isButtonPressed(controllerDevice.getComponents()[12].getPollData()) + "\n";
        information += "Left Stick Press: " + isButtonPressed(controllerDevice.getComponents()[13].getPollData()) + "\n";
        information += "Right Stick Press: " + isButtonPressed(controllerDevice.getComponents()[14].getPollData()) + "\n";
        information += "Directional Pad " + DirectionalPadEnum.translateRawDirection(controllerDevice.getComponents()[15].getPollData()) + "\n";
        information += "Rumblers " + controllerDevice.getRumblers().length + "\n";


        return information;
    }

    public double normalizeDirectionInput(double d)
    {
        if(d < 0.0001 && d > -0.0001)
        {
           // System.out.println("Normalized!");

            return 0.0;
        }
        return d;
    }
    public boolean isButtonPressed(double d)
    {
        return d==1;
    }
}
