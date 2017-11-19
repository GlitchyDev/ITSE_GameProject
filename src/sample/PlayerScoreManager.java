package sample;

public class PlayerScoreManager {
    private static Score topScore = new Score("*TopScore",0);
    private static Score currentScore = new Score("*CurrentScore",0);


    /**
     * Run on game start
     */
    public static void resetScore()
    {
        if(currentScore.getScore() > topScore.getScore())
        {
            topScore.setScore(currentScore.getScore());
        }
        currentScore.setScore(0);
    }
    public static void updateScores()
    {
        if(currentScore.getScore() > topScore.getScore())
        {
            topScore.setScore(currentScore.getScore());
        }
    }
    public static void addToScore(int amount)
    {
        currentScore.setScore(currentScore.getScore()+amount);
    }

    public static Score getCurrentScore()
    {
        return currentScore;
    }
    public static Score getTopScore()
    {
        return topScore;
    }
}
