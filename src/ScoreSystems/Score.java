package ScoreSystems;

public class Score implements Comparable<Score> {
    private String name;
    private int score;

    public Score(String name, int score)
    {
        this.name = name;
        this.score = score;
    }


    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public void setScore(int score)
    {
        this.score = score;
    }

    @Override
    public String toString() {
        return name + ": " + score;
    }

    @Override
    public int compareTo(Score o) {
        return this.score - o.getScore();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Score(name,score);
    }
}
