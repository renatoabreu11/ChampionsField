package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Circle;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import utils.Constants;
import utils.Statistics;

public class MultiPlayMatch extends Match {
    public volatile boolean controlledPlayerMoved;
    public volatile boolean ballMoved;
    public boolean everyPlayersConnected;
    public int controlledPlayerTeam;
    Player controlledPlayer;

    public MultiPlayMatch(int controlledPlayerTeam){
        super(0);

        if(controlledPlayerTeam == 0){
            homeTeam = new Team("Benfica", Team.TeamState.Attacking, w);
            visitorTeam = new Team("Porto", Team.TeamState.Defending, w);
        } else{
            homeTeam = new Team("Benfica", Team.TeamState.Defending, w);
            visitorTeam = new Team("Porto", Team.TeamState.Attacking, w);
        }

        this.controlledPlayerTeam = controlledPlayerTeam;
        numberOfPlayers = 0;
        controlledPlayerMoved = false;
        ballMoved = false;
        everyPlayersConnected = false;
    }

    public void addPlayerToMatch(String name, int team, boolean controlledPlayer, boolean barrierSide) {
        if(team == 0)
            homeTeam.addPlayer(name, team, playerSize, controlledPlayer, w, controlledPlayerTeam, this);
        else
            visitorTeam.addPlayer(name, team, playerSize, controlledPlayer, w, controlledPlayerTeam, this);

        numberOfPlayers++;
    }

    public void removePlayerFromMatch(String name, int team) {
        if(team == 0)
            homeTeam.removePlayer(name);
        else
            visitorTeam.removePlayer(name);

        numberOfPlayers--;
    }

    public boolean everyPlayerConnected() {
        if(homeTeam.getPlayers().size() == Constants.NUMBER_PLAYER_ONLINE && visitorTeam.getPlayers().size() == Constants.NUMBER_PLAYER_ONLINE){
            startTime = System.currentTimeMillis();
            return true;
        }

        return false;
    }

    public void setControlledPlayer(Player player) {
        controlledPlayer = player;
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
        switch (currentState) {
            case KickOff: {
                ball.body.setAwake(true);
                if(homeTeam.getTeamState() == Team.TeamState.Attacking)
                    field.activateBarriers(false);
                else field.activateBarriers(true);
                if (ball.body.getPosition().x != 0 || ball.body.getPosition().y != 0) {
                    field.deactivateBarriers();
                    homeTeam.teamState = Team.TeamState.Playing;
                    visitorTeam.teamState = Team.TeamState.Playing;
                    currentState = matchState.Play;
                }
                controlledPlayer.getBody().setLinearVelocity(x, y);
                break;
            }
            case Play: {
                controlledPlayer.getBody().setLinearVelocity(x, y);
                break;
            }
            case Score:{
                break;
            }
        }
        elapsedTime = ((System.currentTimeMillis() - startTime) / 1000);
        time = Constants.formatter.format(new Date(elapsedTime * 1000L));

        rain.update();
        w.step(Constants.GAME_SIMULATION_SPEED, 6, 2);

        if(x != 0 || y != 0) controlledPlayerMoved = true;
        if(ballTouched){
            ballMoved = true;
            ballTouched = false;
        }
    }

    @Override
    public void endScoreState() {

    }

    @Override
    public void endGame() {
        ArrayList<Statistics> stats = new ArrayList<Statistics>();
        String name;
        int score, matches;

        Statistics parser = new Statistics("", 0, 0);
        FileHandle globalStatistics = Gdx.files.internal("Statistics.txt");
        String info;
        boolean exist;
        exist=Gdx.files.internal("Statistics.txt").exists();
        if(!exist){
            System.out.println("Error opening file!");
            return;
        } else{
            info = globalStatistics.readString();
            stats = parser.parseStatisticsToArray(info);
        }

        boolean found = false;
        for (int i = 0; i < homeTeam.getNumberPlayers(); i++) {
            homeTeam.players.get(i).addMatchPlayed();
            name = homeTeam.players.get(i).name;
            score = homeTeam.players.get(i).score;
            matches = homeTeam.players.get(i).matchesPlayed;
            Statistics s = new Statistics(name, score, matches);
            for(Statistics st : stats){
                if(st.equals(s)){
                    found = true;
                    st.setGoalsScored(s.getGoalsScored() + st.getGoalsScored());
                    st.setMatchesPlayed(s.getMatchesPlayed() + st.getMatchesPlayed());
                }
            }

            if(!found){
                stats.add(s);
            }
            found = false;
        }

        for (int i = 0; i < visitorTeam.getNumberPlayers(); i++) {
            visitorTeam.players.get(i).addMatchPlayed();
            name = visitorTeam.players.get(i).name;
            score = visitorTeam.players.get(i).score;
            matches = visitorTeam.players.get(i).matchesPlayed;
            Statistics s = new Statistics(name, score, matches);
            for(Statistics st : stats){
                if(st.equals(s)){
                    found = true;
                    st.setGoalsScored(s.getGoalsScored() + st.getGoalsScored());
                    st.setMatchesPlayed(s.getMatchesPlayed() + st.getMatchesPlayed());
                }
            }

            if(!found){
                stats.add(s);
            }
            found = false;
        }

        //Não sei se esta parte funciona devido a ter local. ´É preciso testar. A parte de cima funciona
        FileHandle statisticsWrite = Gdx.files.local("Statistics.txt");
        statisticsWrite.writer(false);
        String output = "";
        for(int i = 0; i < stats.size(); i++){
            output = stats.get(i).stringToFile();
            statisticsWrite.writeString(output, true);
            if(i != stats.size() - 1)
                statisticsWrite.writeString("\n", true);
        }
    }

    public float getClientPlayerX(int team) {
        if(team == 0) {
            for(Player player : homeTeam.getPlayers())
                if(player.getControlled())
                    return player.body.getTransform().getPosition().x;
        } else {
            for(Player player : visitorTeam.getPlayers())
                if(player.getControlled())
                    return player.body.getTransform().getPosition().x;
        }

        return -1;
    }

    public float getClientPlayerY(int team) {
        if(team == 0) {
            for(Player player : homeTeam.getPlayers())
                if(player.getControlled())
                    return player.body.getTransform().getPosition().y;
        } else {
            for(Player player : visitorTeam.getPlayers())
                if(player.getControlled())
                    return player.body.getTransform().getPosition().y;
        }

        return -1;
    }

    public void setPlayerPosition(float x, float y, String name, int team) {
        if(team == 0)
            homeTeam.changePlayerPosition(x, y, name);
        else
            visitorTeam.changePlayerPosition(x, y, name);
    }

    public void setBallPosition(float x, float y, float vx, float vy, String lastTouch) {
        ball.updatePosition(x, y, vx, vy);
        ball.lastTouch = lastTouch;
    }
}