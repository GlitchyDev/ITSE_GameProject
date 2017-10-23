package sample;

import com.sun.org.apache.regexp.internal.RE;

import java.io.*;
import java.util.Scanner;
import java.util.UUID;

public class RenewJarUUID {

    public static void main(String[] args)
    {
        renewJarUUID();
    }


    public static String getJarUUID()
    {
        System.out.println("JarUUID: Obtaining Jar UUID");
        if(System.getProperty("isJar").equals("true")) {
            InputStream jarUUIDFile = RenewJarUUID.class.getClassLoader().getResourceAsStream("JarUUID.txt");
            System.out.println(jarUUIDFile == null);
            Scanner jarUUIDScanner = new Scanner(new InputStreamReader(jarUUIDFile));
            return jarUUIDScanner.nextLine();
        }
        else
        {
            // Loading Sprites
            File jarUUIDFile = new File("GameAssets/JarUUID.txt");
            if(jarUUIDFile.exists())
            {
                Scanner jarUUIDScanner = null;
                try {
                    jarUUIDScanner = new Scanner(jarUUIDFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String retrivedString = jarUUIDScanner.nextLine();
                System.out.println("*" + retrivedString);
                return retrivedString;
            }
            else
            {
                return renewJarUUID();
            }

        }
    }


    private static String renewJarUUID()
    {
        System.out.println("JarUUID: Renewing JarUUID");
        File jarUUIDFile = new File("GameAssets/JarUUID.txt");
        UUID uuid = UUID.randomUUID();
        if(jarUUIDFile.exists())
        {
            jarUUIDFile.delete();
        }
        try {
            PrintWriter writer = new PrintWriter(jarUUIDFile);
            writer.println(uuid.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return uuid.toString();
    }
}
