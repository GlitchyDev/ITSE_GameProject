package HardwareAdaptors;

/**
 * Created by Robert on 8/25/2017.
 */
public enum DirectionalPadEnum {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    NORTH_EAST,
    SOUTH_EAST,
    SOUTH_WEST,
    NORTH_WEST,
    NONE;



    public static DirectionalPadEnum translateRawDirection(double d)
    {
        switch(String.valueOf(d))
        {
            case "0.125":
                return NORTH_WEST;
            case "0.25":
                return NORTH;
            case "0.375":
                return NORTH_EAST;
            case "0.5":
                return EAST;
            case "0.625":
                return SOUTH_EAST;
            case "0.75":
                return SOUTH;
            case "0.875":
                return SOUTH_WEST;
            case "1.0":
                return WEST;
            default:
                return NONE;
        }
    }

    public static boolean isDiagnal(DirectionalPadEnum direction)
    {
        return (direction == NORTH_EAST || direction == NORTH_WEST || direction == SOUTH_EAST || direction == SOUTH_WEST);
    }
}
