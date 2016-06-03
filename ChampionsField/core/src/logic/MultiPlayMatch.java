package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.PriorityQueue;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import utils.Statistics;

public class MultiPlayMatch extends Match {
    public enum matchState {
        KickOff,
        Pause,
        Score,
        Play;
    }

    public MultiPlayMatch() {
        super(0);

        Random r = new Random();
        int aux = r.nextInt(2);     //MUDAR AQUI, SO PARA QUESTOES DE DEBUG! :D
        aux = 0;
        if (aux == 0) {
            homeTeam = new Team(numberOfPlayers, playerSize, "Benfica", Team.TeamState.Attacking, w);
            visitorTeam = new Team(numberOfPlayers, playerSize, "Porto", Team.TeamState.Defending, w);
        } else {
            homeTeam = new Team(numberOfPlayers, playerSize, "Benfica", Team.TeamState.Defending, w);
            visitorTeam = new Team(numberOfPlayers, playerSize, "Porto", Team.TeamState.Attacking, w);
        }
        homeTeam.controlPlayer(0);
    }

    public void addPlayerToMatch(ArrayList<Player> players) {
        for (Player player : players) {
            if (player.team == 0)
                homeTeam.addPlayer(player);
            else
                visitorTeam.addPlayer(player);
        }
    }

    @Override
    public void teamScored(Team defendingTeam, Team attackingTeam, String lastTouch) {
        ArrayList<String> attackingTeamNames = attackingTeam.getPlayerNames();

        //auto goal
        if (attackingTeamNames.contains(lastTouch)) {
            attackingTeam.autoGoal(lastTouch);
        } else defendingTeam.goalScored(lastTouch);
        defendingTeam.teamState = Team.TeamState.Defending;
        attackingTeam.teamState = Team.TeamState.Attacking;
        currentState = Match.matchState.Score;
    }

    @Override
    public void updateMatch(float x, float y, Rain rain, float dt) {

    }

    @Override
    public void endScoreState() {

    }

    @Override
    public void endGame() {
        ArrayList<Statistics> stats = new ArrayList<Statistics>();
        String name;
        int score, matches;

        FileHandle globalStatistics = Gdx.files.local("Statistics.txt");
        String info;
        boolean exist;
        exist=Gdx.files.local("Statistics.txt").exists();
        if(!exist){
            System.out.println("Error opening file!");
            return;
        } else{
            info = globalStatistics.readString();
            stats = parseStatistics(info);
        }

        for (int i = 0; i < homeTeam.getNumberPlayers(); i++) {
            homeTeam.players.get(i).addMatchPlayed();
            name = homeTeam.players.get(i).name;
            score = homeTeam.players.get(i).score;
            matches = homeTeam.players.get(i).matchesPlayed;
            Statistics s = new Statistics(name, score, matches);
            if(stats.contains(s)){
                stats.remove(s);
                stats.add(s);
            } else stats.add(s);
        }

        for (int i = 0; i < visitorTeam.getNumberPlayers(); i++) {
            visitorTeam.players.get(i).addMatchPlayed();
            name = visitorTeam.players.get(i).name;
            score = visitorTeam.players.get(i).score;
            matches = visitorTeam.players.get(i).matchesPlayed;
            Statistics s = new Statistics(name, score, matches);
            if(stats.contains(s)){
                stats.remove(s);
                stats.add(s);
            } else stats.add(s);
        }

        globalStatistics.writer(false);
        String output = "";
        for(int i = 0; i < stats.size(); i++){
            output = stats.get(i).stringToFile();
            globalStatistics.writeString(output, true);
            if(i != stats.size() - 1)
                globalStatistics.writeString("\n", true);
        }
    }

    public ArrayList<Statistics> parseStatistics(String info) {
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
}