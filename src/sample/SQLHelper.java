package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SQLHelper {
    public static void main(String [] args) throws Exception{

       // insert(4788721,"KSU50", 5050);


        ScoreManager.getTopScores();
        int i = 0;
        for(Score score: ScoreManager.getTopScores())
        {
            if(i < 10)
            {
                System.out.println((i+1) + "# " + score);
            }
            i++;
        }
        System.out.println();
        ScoreManager.logScore("AAA",5);
        i = 0;
        for(Score score: ScoreManager.getTopScores())
        {
            if(i < 10)
            {
                System.out.println((i+1) + "# " + score);
            }
            i++;
        }
        System.out.println();
        ScoreManager.logScore("AAA",5000000);
        i = 0;
        for(Score score: ScoreManager.getTopScores())
        {
            if(i < 10)
            {
                System.out.println((i+1) + "# " + score);
            }
            i++;
        }
        ScoreManager.submitScore("AAA",999999999);
        System.out.println(getScore("AAA"));

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
        ps.print("&PlayerScore=" + PlayerScore);

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
            ps.print("&PlayerScore=" + PlayerScore);

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


    public static String getScore(String uuid) throws Exception{
         String Playerscore = "";

        URL oracle = new URL("http://www.classprojectfall.com/getDataNew.php");
        URLConnection conn = oracle.openConnection();

        conn.setDoOutput(true);
        PrintStream ps = new PrintStream(conn.getOutputStream());

        // send your parameters to the php file
        ps.print("&uuid=" + uuid);

        // w input stream to send the request
        conn.getInputStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
           // System.out.println(inputLine);
            Playerscore = inputLine;
        }

        in.close();
        ps.close();

        return Playerscore;

    }

    
    public static String getTopPlayers() throws Exception{
        String Playerscore = "";

        URL oracle = new URL("http://www.classprojectfall.com/TopPlayers.php");
        URLConnection conn = oracle.openConnection();

        conn.setDoOutput(true);
        PrintStream ps = new PrintStream(conn.getOutputStream());


        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            Playerscore = inputLine;
        }
        in.close();
        ps.close();

        return Playerscore;

    }


}
