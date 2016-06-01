package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class Team {
    enum TeamState{
        Playing, Attacking, Defending
    }

    int score;
    String name;
    ArrayList<Player> players;
    TeamState teamState;

    //ONLINE VARIABLES
    World world;

    public Team(int numPlayers, float size, String name, TeamState initialState, World w) {
        score = 0;
        teamState = initialState;
        players = new ArrayList<Player>();

        for(int i = 0; i < numPlayers; i++){
            if(teamState == TeamState.Attacking)
                players.add(new Player(- Gdx.graphics.getWidth() / 4 + i * 50 , 0, i+"", size, w));
            else
                players.add(new Player(Gdx.graphics.getWidth() / 4 - i * 50 ,0, i+"", size, w));
        }
    }

    public void repositionTeam() {
        for(int i = 0; i < players.size(); i++) {
            if(teamState == TeamState.Attacking)
                players.get(i).reposition(- Gdx.graphics.getWidth() / 4 + i * 50 , 0);
            else
                players.get(i).reposition(Gdx.graphics.getWidth() / 4 - i * 50 ,0);
        }
    }

    public void erasePlayers() {
        for(int i = 0; i < players.size(); i++)
            players.get(i).getBody().getWorld().destroyBody(players.get(i).getBody());
    }

    public void controlPlayer(int index){
        if(index >= 0 && index <= players.size() - 1)
            players.get(index).setControlledPlayer(true);
    }

    public void updateControlledPlayer(float x, float y, float dt) {
        for(int i = 0; i < players.size(); i++) {
            if (players.get(i).isControlledPlayer()) {
                players.get(i).getBody().setLinearVelocity(x, y);
            }
            players.get(i).update(dt);
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

    /*
    * BEGIN OF THE MULTPLAYER FUNCTIONS
    * */

    public Team(String name, TeamState initialState, World w) {
        this.name = name;
        world = w;
        score = 0;
        teamState = initialState;
        players = new ArrayList<Player>();
    }

    public void addPlayer(Player player) {
        player.addPhysics(world);
        players.add(player);
    }

    /*
    * END OF THE MULTPLAYER FUNCTIONS
    * */
}
