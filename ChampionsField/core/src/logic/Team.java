package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class Team {
    enum TeamState{
        Playing, Attacking, Defending, Scoring
    }

    int score;
    String name;
    ArrayList<Player> players;
    TeamState teamState;

    public Team(int numPlayers, int size, String name, TeamState initialState, World w) {
        score = 0;
        teamState = initialState;
        players = new ArrayList<Player>();

        for(int i = 0; i < numPlayers; i++){
            if(teamState == TeamState.Attacking)
                players.add(new Player(- Gdx.graphics.getWidth() / 4 + i * 50 , 0, i+"", size, false, w));
            else
                players.add(new Player(Gdx.graphics.getWidth() / 4 - i * 50 ,0, i+"", size, false, w));
        }
    }

    public void repositionTeam(int numPlayers, int size, World w) {
        for(int i = 0; i < numPlayers; i++)
            players.get(i).reposition(- Gdx.graphics.getWidth() / 4 + i * 50 , 0, w);
    }

    public void controlPlayer(int index){
        if(index >= 0 && index <= players.size() - 1)
            players.get(index).setControlledPlayer(true);
    }

    public void updateControlledPlayer() {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).isControlledPlayer()) {
                players.get(i).updatePosition();
                break;
            }
        }
    }

    public void updateControlledPlayer(float x, float y) {
        for(int i = 0; i < players.size(); i++) {
            if (players.get(i).isControlledPlayer()) {
                players.get(i).getBody().setLinearVelocity(x, y);
                /*players.get(i).updatePosition(x, y);
                for (int j = 0; j < players.size(); j++) {
                }*/
            }
        }
    }

    public void updatePlayers(){
        for(int i = 0; i < players.size(); i++) {
            if(!players.get(i).isControlledPlayer()){
                for(int j = 0; j < players.size(); j++){
                 }
            }
        }
    }

    public void goalScored() {
        score++;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getNumberPlayers() {
        return players.size();
    }

    public TeamState getTeamState() {
        return teamState;
    }

    public void setTeamState(TeamState team) {
        this.teamState = team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
