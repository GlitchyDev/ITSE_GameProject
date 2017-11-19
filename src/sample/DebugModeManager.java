package sample;

public class DebugModeManager {
    public static boolean isDebugMode = false;


    public static void enableDebugMode()
    {
        isDebugMode = true;
    }
    public static boolean computerDebugModeDefault()
    {
        return false;
    }

}
