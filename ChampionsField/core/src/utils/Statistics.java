package utils;

public class Statistics{
    private String name;
    private int goalsScored;
    private int matchesPlayed;

    public Statistics(String name, int goalsScored, int matchesPlayed) {
        this.name = name;
        this.goalsScored = goalsScored;
        this.matchesPlayed = matchesPlayed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Statistics that = (Statistics) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + goalsScored;
        result = 31 * result + matchesPlayed;
        return result;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "name='" + name + '\'' +
                ", goalsScored=" + goalsScored +
                ", matchesPlayed=" + matchesPlayed +
                '}';
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public String stringToFile() {
        return name + ';' + goalsScored + ';' + matchesPlayed;
    }
}
