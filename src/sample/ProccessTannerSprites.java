package sample;

import java.io.File;

public class ProccessTannerSprites {
    public static void main(String[] args)
    {
        File startingFolder = new File("C:/Users/Robert/DeskTop/P2Custom");
        for(File file: startingFolder.listFiles())
        {
            String color = file.getName().split("_")[file.getName().split("_").length - 1];
            color = color.replace(".png","");
            String oldName = file.getName();

            File newFile = new File("C:/Users/Robert/DeskTop/P2Custom/" + oldName.replace("P1",color).replace("_" + color,""));
            file.renameTo(newFile);
        }
        //C:\Users\Robert\Desktop\P2Custom
    }
}


//
