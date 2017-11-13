package HardwareAdaptors;

import GameInfo.GlobalGameData;

/**
 * Created by Robert on 8/25/2017.
 */
public enum DirectionalEnum {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    NORTH_EAST,
    SOUTH_EAST,
    SOUTH_WEST,
    NORTH_WEST,
    NONE;



    public static DirectionalEnum translateRawDirection(double d)
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

    public static boolean isDiagnal(DirectionalEnum direction)
    {
        return (direction == NORTH_EAST || direction == NORTH_WEST || direction == SOUTH_EAST || direction == SOUTH_WEST);
    }

    public static DirectionalEnum randomDirection(GlobalGameData globalGameData)
    {
        switch(globalGameData.getRandom().nextInt(4))
        {
            case 0:
                return DirectionalEnum.NORTH;
            case 1:
                return DirectionalEnum.EAST;
            case 2:
                return DirectionalEnum.SOUTH;
            case 3:
                return DirectionalEnum.WEST;
        }
        return DirectionalEnum.NORTH;
    }


    public static DirectionalEnum determineDirection(int x1, int y1, int x2, int y2)
    {
        double delta_x = x2 - x1;
        double delta_y = y2 - y1;

        double theta = Math.atan2(delta_y, delta_x);

        double angle = theta*180/Math.PI + 180;



        if(angle >= 45 && angle < 135)
        {
            return SOUTH;
        }
        if(angle >= 135 && angle < 225)
        {
            return WEST;
        }
        if(angle >= 225 && angle < 315)
        {
            return NORTH;
        }
        if((angle >= 315 && angle <= 360) || (angle < 45 && angle >= 0))
        {
            return EAST;
        }


        System.out.println(angle);
        return NONE;
    }



}
