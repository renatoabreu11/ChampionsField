package logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import utils.Constants;

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
            Vector2 position = new Vector2(0, 0);
            String n = "";
            switch(i){
                case 0:
                    position = Constants.Keeper;
                    n = "GK";
                    break;
                case 1:
                    position = Constants.CenterDefender;
                    n = "CD";
                    break;
                case 2:
                    position = Constants.DefensiveMidfielder;
                    n = "DM";
                    break;
                case 3:
                    position = Constants.AttackingMidfielder;
                    n = "AM";
                    break;
                case 4:
                    position = Constants.Striker;
                    n = "ST";
                    break;
            }
            if(teamState == TeamState.Attacking)
                players.add(new Player(- position.x , position.y, n, size, w));
            else
                players.add(new Player(position.x, position.y, n, size, w));
        }
    }

    public boolean switchPlayer(Vector2 ballPosition) {
        int playerIndex = -1;
        float minDistance = Integer.MAX_VALUE;
        float auxDistance = 0;

        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).stateMachine.getCurrentState() == PlayerState.Controlled){
                players.get(i).setControlled(false);
                for(int j = 0; j < players.size(); j++) {
                    if(j != i){
                        float xDiff = (float)Math.pow(ballPosition.x - players.get(j).position.x, 2);
                        float yDiff = (float)Math.pow(ballPosition.y - players.get(j).position.y, 2);
                        auxDistance = (float)Math.sqrt(xDiff + yDiff);
                        if(auxDistance < minDistance) {
                            minDistance = auxDistance;
                            playerIndex = j;
                        }
                    }
                }

                players.get(playerIndex).setControlled(true);
                return true;
            }
        }
        return false;
    }

    public void repositionTeam() {
        for(int i = 0; i < players.size(); i++) {
            players.get(i).reposition();
        }
    }

    public void erasePlayers() {
        for(int i = 0; i < players.size(); i++)
            players.get(i).getBody().getWorld().destroyBody(players.get(i).getBody());
    }

    public void controlPlayer(int index){
        if(index >= 0 && index <= players.size() - 1)
            players.get(index).setControlled(true);
    }

    public void updateControlledPlayer(float x, float y) {
        for(int i = 0; i < players.size(); i++) {
            if (players.get(i).stateMachine.getCurrentState() == PlayerState.Controlled) {
                players.get(i).getBody().setLinearVelocity(x, y);
            }
        }
    }

    public void updatePlayers(float dt, Ball b){
        for(int i = 0; i < players.size(); i++) {
            if (players.get(i).stateMachine.getCurrentState() != PlayerState.Controlled) {
                players.get(i).update(dt);
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
