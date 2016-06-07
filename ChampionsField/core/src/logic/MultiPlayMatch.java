package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Date;

import utils.Constants;
import utils.Statistics;

public class MultiPlayMatch extends Match {
    public volatile boolean controlledPlayerMoved;
    public volatile boolean ballMoved;
    public volatile boolean canRepositionAfterScore;
    public volatile Vector2 controlledPlayerInitialPosition;
    public volatile boolean canStepWorld;
    public boolean everyPlayersConnected;
    public int controlledPlayerTeam;
    Player controlledPlayer;
    private boolean barrierSide;
    public boolean isFull;


    /**
     * Constructor for the multiplayer match
     * @param controlledPlayerTeam team the client's player is controlling
     */
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
        canRepositionAfterScore = false;
        canStepWorld = true;
        isFull = false;
        controlledPlayerInitialPosition = new Vector2();
    }

    /**
     * Adds a player to the client's match
     * @param name player's name
     * @param team player's team
     * @param controlledPlayer is the controlled player?
     * @param barrierSide match's initial barrier side
     */
    public void addPlayerToMatch(String name, int team, boolean controlledPlayer, boolean barrierSide) {
        if(team == 0)
            homeTeam.addPlayer(name, team, playerSize, controlledPlayer, w, controlledPlayerTeam, this);
        else
            visitorTeam.addPlayer(name, team, playerSize, controlledPlayer, w, controlledPlayerTeam, this);

        this.barrierSide = barrierSide;
        field.activateBarriers(this.barrierSide);
        numberOfPlayers++;
    }

    /**
     * Removes a player from the cient's match
     * @param name player's name
     * @param team player's team
     */
    public void removePlayerFromMatch(String name, int team) {
        if(team == 0)
            homeTeam.removePlayer(name);
        else
            visitorTeam.removePlayer(name);

        numberOfPlayers--;
    }

    public Vector2 getControlledPlayerInitialPosition() {
        return controlledPlayer.initialPosition;
    }

    /**
     * Checks if every player is connected / the teams are filled
     * @return returns true if the game is ready to start, and false otherwise
     */
    public boolean everyPlayerConnected() {
        if(homeTeam.getPlayers().size() == Constants.NUMBER_PLAYER_ONLINE && visitorTeam.getPlayers().size() == Constants.NUMBER_PLAYER_ONLINE){
            startTime = System.currentTimeMillis();
            return true;
        }

        return false;
    }

    public void matchFull() {
        isFull = true;
    }

    /**
     * Sets the client's controlled player
     * @param player controlled player
     */
    public void setControlledPlayer(Player player) {
        controlledPlayer = player;
    }

    /**
     * Changes the team's states and sets the information about the goal scored
     * @param defendingTeam the defending team
     * @param attackingTeam the attacking team
     * @param lastTouch the last player who touched the ball
     */
    @Override
    public void teamScored(Team defendingTeam, Team attackingTeam, String lastTouch) {
        currentState = Match.matchState.Score;
        ArrayList<String> attackingTeamNames = attackingTeam.getPlayerNames();

        //auto goal
        if (attackingTeamNames.contains(lastTouch)) {
            attackingTeam.autoGoal(lastTouch);
            defendingTeam.score++;
        } else defendingTeam.goalScored(lastTouch);

        defendingTeam.teamState = Team.TeamState.Defending;
        attackingTeam.teamState = Team.TeamState.Attacking;
    }

    /**
     * Updates the multiplayer match, such as the state, time elapsed, rain and the world's
     * physics
     * @param x the new client's player x position
     * @param y the new client's player y position
     * @param dt the delta time
     */
    @Override
    public void updateMatch(float x, float y, float dt) {
        switch (currentState) {
            case KickOff: {
                ball.body.setAwake(true);
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

        canStepWorld = false;
        w.step(Constants.GAME_SIMULATION_SPEED, 6, 2);
        canStepWorld = true;

        if(x != 0 || y != 0) controlledPlayerMoved = true;
        if(ballTouched){
            ballMoved = true;
            ballTouched = false;
        }
    }

    /**
     * Called after a goal is scored
     * Repositions the players and the ball
     */
    @Override
    public void endScoreState() {
        currentState = matchState.KickOff;
        barrierSide = !barrierSide;
        field.activateBarriers(barrierSide);
        canRepositionAfterScore = true;
    }

    /**
     * Updates the highscores
     * If a player doesn't exist, adds it
     */
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

        //Não sei se esta parte funciona devido a ter local. ´É preciso testar. A parte de cima funciona!!!!!!!!!!!!!!!!!!!
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

    /**
     * Returns the client's player x position
     * @param team client's player team
     * @return client's player x position to return
     */
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

    /**
     * Returns the client's player y position
     * @param team client's player team
     * @return client's player y position to return
     */
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

    /**
     * Updates the player position
     * @param x the new x position
     * @param y the new y position
     * @param name player's name
     * @param team player's team
     */
    public void setPlayerPosition(float x, float y, String name, int team) {
        if(team == 0)
            homeTeam.changePlayerPosition(x, y, name);
        else
            visitorTeam.changePlayerPosition(x, y, name);
    }

    /**
     * Updates the ball position and velocity
     * @param x the new x position
     * @param y the new y position
     * @param vx the new x velocity position
     * @param vy the new y velocity position
     * @param lastTouch the last player's name who touched the ball
     */
    public void setBallPosition(float x, float y, float vx, float vy, String lastTouch) {
        ball.updatePosition(x, y, vx, vy);
        ball.lastTouch = lastTouch;
    }
}