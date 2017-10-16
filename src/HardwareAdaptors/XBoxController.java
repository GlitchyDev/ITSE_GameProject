package HardwareAdaptors;

import net.java.games.input.Controller;

import java.util.concurrent.*;

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
    protected Controller controllerDevice;
    protected int id;
    protected ControllerType controllerType;
    private final Runnable stuffToDo = new Thread() {
        @Override
        public void run() {
            controllerDevice.poll();
        }
    };


    public boolean hasController()
    {
        return controllerDevice != null;
    }
    public XBoxController(Controller controllerDevice, int id)
    {
        this.controllerDevice = controllerDevice;
        this.id = id;
        controllerType = ControllerType.XBoxController;
    }

    public void poll() {
        //controllerDevice.poll();
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Future future = executor.submit(stuffToDo);
        executor.shutdown(); // This does not cancel the already-scheduled task.
        try {
            future.get(500, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException ie) {
        }
        catch (ExecutionException ee) {
        }
        catch (TimeoutException te) {
        }
        if (!executor.isTerminated()) {
            executor.shutdownNow(); // If you want to stop the code that hasn't finished.
        }

    }

    public double getLeftStickY()
    {
        return normalizeDirectionInput(controllerDevice.getComponents()[0].getPollData());
    }
    public double getLeftStickX()
    {
        return normalizeDirectionInput(controllerDevice.getComponents()[1].getPollData());
    }
    public boolean getLeftStickPress() {
        return isButtonPressed(controllerDevice.getComponents()[13].getPollData());
    }

    public double getRightStickY() {return normalizeDirectionInput(controllerDevice.getComponents()[2].getPollData());}
    public double getRightStickX() {return normalizeDirectionInput(controllerDevice.getComponents()[3].getPollData());}
    public boolean getRightStickPress() {
        return isButtonPressed(controllerDevice.getComponents()[14].getPollData());
    }

    public double getTrigger()
    {
        return normalizeDirectionInput(controllerDevice.getComponents()[4].getPollData());
    }

    public DirectionalEnum getDirectionalPad() {
        return DirectionalEnum.translateRawDirection(controllerDevice.getComponents()[15].getPollData());
    }

    public boolean getButtonA()
    {
        return isButtonPressed(controllerDevice.getComponents()[5].getPollData());
    }
    public boolean getButtonB()
    {
        return isButtonPressed(controllerDevice.getComponents()[6].getPollData());
    }
    public boolean getButtonX()
    {
        return isButtonPressed(controllerDevice.getComponents()[7].getPollData());
    }
    public boolean getButtonY()
    {
        return isButtonPressed(controllerDevice.getComponents()[8].getPollData());
    }

    public boolean getLeftShoulder()
    {
        return isButtonPressed(controllerDevice.getComponents()[9].getPollData());
    }
    public boolean getRightShoulder()
    {
        return isButtonPressed(controllerDevice.getComponents()[10].getPollData());
    }

    public boolean getBack()
    {
        return isButtonPressed(controllerDevice.getComponents()[11].getPollData());
    }
    public boolean getStart()
    {
        return isButtonPressed(controllerDevice.getComponents()[12 ].getPollData());
    }

    public ControllerType getControllerType()
    {
        return controllerType;
    }



    public String toString()
    {
        String information = "XBox Controller #" + id + "\n";
        information += "Left Stick Y: " + normalizeDirectionInput(controllerDevice.getComponents()[0].getPollData()) + "\n";
        information += "Left Stick X: " + normalizeDirectionInput(controllerDevice.getComponents()[1].getPollData()) + "\n";
        information += "Right Stick Y: " + normalizeDirectionInput(controllerDevice.getComponents()[2].getPollData()) + "\n";
        information += "Right Stick X: " + normalizeDirectionInput(controllerDevice.getComponents()[3].getPollData()) + "\n";
        information += "Right/Left Trigger: " + normalizeDirectionInput(controllerDevice.getComponents()[4].getPollData()) + "\n";
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
        information += "Directional Pad: " + DirectionalEnum.translateRawDirection(controllerDevice.getComponents()[15].getPollData()) + "\n";
        information += "Rumblers: " + controllerDevice.getRumblers().length + "\n";
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
