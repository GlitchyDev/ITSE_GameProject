package sample;

public class PlayerScore {
    private static Score score = new Score("",0);


    /**
     * Run on game start
     */
    public void resetScore()
    {
        score.setScore(0);
        score.setName("");
    }
    public void addToScore(int amount)
    {
        score.setScore(score.getScore()+amount);
    }
    public void setName(int amount)
    {
        score.setScore(score.getScore()+amount);
    }

    public Score getScore()
    {
        try {
            return (Score) score.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
