package utils;

import com.badlogic.gdx.ai.msg.PriorityQueue;

import java.util.ArrayList;
import java.util.Iterator;

public class Statistics implements Comparable<Statistics> {
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

    @Override
    public int compareTo(Statistics o) {
        if(this.getGoalsScored() > o.getGoalsScored())
            return 1;
        else return 0;
    }

    public ArrayList<Statistics> parseStatisticsToArray(String info) {
        ArrayList<Statistics> stats = new ArrayList<Statistics>();
        String name = "";
        int score = 0, matches = 0;

        String delims = "[;\\n\\r]+";
        String[] tokens = info.split(delims);
        int counter = 0;
        for(int i = 0; i < tokens.length; i++){
            switch(counter){
                case 0:
                    name = tokens[i];
                    break;
                case 1:
                    try {
                        score = Integer.parseInt(tokens[i]);
                    } catch(NumberFormatException nFE) {
                        System.out.println("Not an Integer");
                    }
                    break;
                case 2:
                    try {
                        matches = Integer.parseInt(tokens[i]);
                    } catch(NumberFormatException nFE) {
                        System.out.println("Not an Integer");
                    }
                    break;
            }
            counter++;
            if(counter == 3) {
                Statistics s = new Statistics(name, score, matches);
                stats.add(s);
                counter = 0;
            }
        }
        return stats;
    }

    public PriorityQueue<Statistics> parseHighScores(String info) {
        PriorityQueue<Statistics> highScores = new PriorityQueue<Statistics>();
        String name = "";
        int score = 0, matches = 0;

        String delims = "[;\\n\\r]+";
        String[] tokens = info.split(delims);
        int counter = 0;
        for(int i = 0; i < tokens.length; i++){
            switch(counter){
                case 0:
                    name = tokens[i];
                    break;
                case 1:
                    try {
                        score = Integer.parseInt(tokens[i]);
                    } catch(NumberFormatException nFE) {
                        System.out.println("Not an Integer");
                    }
                    break;
                case 2:
                    try {
                        matches = Integer.parseInt(tokens[i]);
                    } catch(NumberFormatException nFE) {
                        System.out.println("Not an Integer");
                    }
                    break;
            }
            counter++;
            if(counter == 3) {
                Statistics s = new Statistics(name, score, matches);
                if(highScores.size() < 10)
                    highScores.add(s);
                else{
                    if(s.getGoalsScored() > highScores.peek().getGoalsScored()){
                        highScores.poll();
                        highScores.add(s);
                    }
                }
                counter = 0;
            }
        }
        return highScores;
    }
}
