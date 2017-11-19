package ScoreSystems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SQLHelper {

    public static void main(String[] args)
    {

        updatScore("DEV",1);
        try {
            System.out.println(getScore("DEV"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void deletePlayer (String uuid){

        try{
            URL url = new URL("http://www.classprojectfall.com/updateScore.php");
            URLConnection conn = url.openConnection();

            // activate the output
            conn.setDoOutput(true);
            PrintStream ps = new PrintStream(conn.getOutputStream());

            // send your parameters to the php file
            ps.print("&uuid=" + uuid);

            // w input stream to send the request
            conn.getInputStream();

            // to get respond
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String l = null;
            while ((l=br.readLine())!=null) {
                System.out.println(l);
            }

            br.close();
            ps.close();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static boolean hasScore(String name)
    {
        try {
            return getScore(name) != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void insert (String uuid, int PlayerScore){

    try{
        URL url = new URL("http://www.classprojectfall.com/insertDataNew.php");
        URLConnection conn = url.openConnection();

        // activate the output
        conn.setDoOutput(true);
        PrintStream ps = new PrintStream(conn.getOutputStream());

        // send your parameters to the php file
        ps.print("&uuid=" + uuid);
        ps.print("&PlayerScoreManager=" + PlayerScore);

        // w input stream to send the request
        conn.getInputStream();

        // to get respond
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String l = null;
        while ((l=br.readLine())!=null) {
           System.out.println(l);
        }

        br.close();
        ps.close();
    }catch (MalformedURLException e){
        e.printStackTrace();
    }catch (IOException e){
        e.printStackTrace();
    }

    }


    public static void updatScore (String uuid, int PlayerScore){

        try{
            URL url = new URL("http://www.classprojectfall.com/updateScore.php");
            URLConnection conn = url.openConnection();

            // activate the output
            conn.setDoOutput(true);
            PrintStream ps = new PrintStream(conn.getOutputStream());

            // send your parameters to the php file
            ps.print("&uuid=" + uuid);
            ps.print("&PlayerScoreManager=" + PlayerScore);

            // w input stream to send the request
            conn.getInputStream();

            // to get respond
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String l = null;
            while ((l=br.readLine())!=null) {
                System.out.println(l);
            }

            br.close();
            ps.close();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }



    public static String getScore(String uuid){
         String Playerscore = "";

        URL oracle = null;
        try {
            oracle = new URL("http://www.classprojectfall.com/getDataNew.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection conn = null;
        try {
            conn = oracle.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.setDoOutput(true);
        PrintStream ps = null;
        try {
            ps = new PrintStream(conn.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // send your parameters to the php file
        ps.print("&uuid=" + uuid);

        // w input stream to send the request
        try {
            conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
               // System.out.println(inputLine);
                Playerscore = inputLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ps.close();

        return Playerscore;

    }

    
    public static String getTopPlayers(){
        String Playerscore = "";

        URL oracle = null;
        try {
            oracle = new URL("http://www.classprojectfall.com/TopPlayers.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection conn = null;
        try {
            conn = oracle.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.setDoOutput(true);
        PrintStream ps = null;
        try {
            ps = new PrintStream(conn.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }


        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                Playerscore = inputLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ps.close();

        return Playerscore;

    }


}
