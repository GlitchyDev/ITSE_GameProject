package sample;

import java.util.ArrayList;
import java.util.Arrays;

public class ScoreManager {
    private static ArrayList<Score> topScores = new ArrayList<>();



    public static void updateScores()
    {

        Arrays.sort(new ArrayList[]{topScores});

    }

    /**
     * Submits top score to SQL if its notable
     * @param name
     * @param score
     */
    public static void submitScore(String name, int score)
    {
        // Detect if score exists yet
        try {
            if(!SQLHelper.hasScore(name))
            {
                SQLHelper.insert(name,score);
            }
            else
            {
                SQLHelper.updatScore(name, score);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Grabs the top 10 scores from SQL if they are not already loaded
     * @return
     */
    public static ArrayList<Score> getTopScores()
    {
        if(topScores.size() == 0)
        {
            try {
                String unformatedScores = SQLHelper.getTopPlayers();
                unformatedScores = unformatedScores.replace("[","");
                unformatedScores = unformatedScores.replace("]","");
                unformatedScores = unformatedScores.replace("\"","");
                unformatedScores = unformatedScores.replace("{","");
                unformatedScores = unformatedScores.replace("},","}");
                String[] splitScores = unformatedScores.split("}");

                for(String s: splitScores)
                {
                    System.out.println(s);
                    String[] parsingScore = s.split(",");
                    String name = parsingScore[0].replace("uuid:","");
                    int score = Integer.valueOf(parsingScore[1].replace("Score:",""));
                    Score highScore = new Score(name,score);
                    topScores.add(highScore);
                }
                topScores.add(PlayerScoreManager.getTopScore());
                topScores.add(PlayerScoreManager.getCurrentScore());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return topScores;
    }
}
