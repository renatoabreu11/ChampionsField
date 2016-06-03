package logic;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

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
    Array<Rectangle> regions;

    //ONLINE VARIABLES
    World world;

    public Team(int numPlayers, float size, String name, TeamState initialState, World w) {
        score = 0;
        teamState = initialState;
        players = new ArrayList<Player>();

        regions = new Array<Rectangle>(5);
        if(teamState == TeamState.Attacking){
            regions.add(Constants.AttackGR);
            regions.add(Constants.AttackCD);
            regions.add(Constants.AttackDM);
            regions.add(Constants.AttackAM);
            regions.add(Constants.AttackST);
        } else{
            regions.add(Constants.DefendGR);
            regions.add(Constants.DefendCD);
            regions.add(Constants.DefendDM);
            regions.add(Constants.DefendAM);
            regions.add(Constants.DefendST);
        }

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
                players.add(new Player(position.x, position.y, n+ i, size, w));
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
                        auxDistance = distanceBetweenPoints(ballPosition, players.get(j).position);
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
                if(!regions.get(i).contains(players.get(i).getPosition())){
                    players.get(i).stateMachine.handleMessage(new Telegram());
                } else if(regions.get(i).contains(b.getPosition())){
                    players.get(i).wayPoint.setPosition(b.getPosition());
                    players.get(i).stateMachine.changeState(PlayerState.ChaseBall);
                }
                players.get(i).update(dt);
            }
        }
    }

    private float distanceBetweenPoints(Vector2 p1, Vector2 p2){
        float xDiff = (float)Math.pow(p1.x - p2.x, 2);
        float yDiff = (float)Math.pow(p1.y - p2.y, 2);
        return (float)Math.sqrt(xDiff + yDiff);
    }

    public void goalScored(String scorer) {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).name == scorer)
                players.get(i).score++;
        }
        score++;
    }

    public void autoGoal(String scorer) {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).name == scorer)
                players.get(i).score--;
        }
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

    public ArrayList<String> getPlayerNames(){
        ArrayList<String> names;
        names = new ArrayList<String>();
        for(int i = 0; i < players.size(); i++) {
            names.add(players.get(i).name);
        }
        return names;
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
