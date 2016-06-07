package logic;

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

    /**
     * Constructor for the team
     * @param numPlayers team's number of player
     * @param size the size of the players in the physics world
     * @param name team's name
     * @param initialState team's initial state
     * @param w the world the team is added
     */
    public Team(int numPlayers, float size, String name, TeamState initialState, World w) {
        score = 0;
        teamState = initialState;
        players = new ArrayList<Player>();

        regions = new Array<Rectangle>(3);
        if(teamState == TeamState.Attacking){
            regions.add(Constants.AttackCentral);
            regions.add(Constants.AttackMidfielder);
            regions.add(Constants.AttackStriker);
        } else{
            regions.add(Constants.DefendCentral);
            regions.add(Constants.DefendMidfielder);
            regions.add(Constants.DefendStriker);
        }

        for(int i = 0; i < numPlayers; i++){
            Vector2 position = new Vector2(0, 0);
            String n = "";
            switch(i){
                case 0:
                    position = Constants.Defender;
                    n = "DC";
                    break;
                case 1:
                    position = Constants.Midfielder;
                    n = "DM";
                    break;
                case 2:
                    position = Constants.Striker;
                    n = "ST";
                    break;
            }
            if(teamState == TeamState.Attacking)
                players.add(new Player(- position.x , position.y, n + (i + 4), size, w, regions.get(i)));
            else
                players.add(new Player(position.x, position.y, n + (i + 1), size, w, regions.get(i)));
        }
    }

    /**
     * Removes the power ups for all the team's players
     */
    public void removePowerUps() {
        for(Player player : players) {
            player.speedMultiplier = 1;
            player.powerActivated = false;
            player.activeTime = 0;
        }
    }

    /**
     * Switches to the player that is closer to the ball
     * @param ballPosition the ball position
     * @return true if the controlled player is the team, false otherwise
     */
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

    /**
     * Repositions the whole team
     */
    public void repositionTeam() {
        for(int i = 0; i < players.size(); i++) {
            players.get(i).reposition();
        }
    }

    /**
     * Erase of all the player's bodies
     */
    public void erasePlayers() {
        for(int i = 0; i < players.size(); i++)
            players.get(i).getBody().getWorld().destroyBody(players.get(i).getBody());
    }

    /**
     * Sets the controlled player
     * @param index index of the players ArrayList
     */
    public void controlPlayer(int index){
        if(index >= 0 && index <= players.size() - 1)
            players.get(index).setControlled(true);
    }

    /**
     * Updates the controlled player
     * @param x controlled player's new x position
     * @param y controlled player's new y position
     */
    public void updateControlledPlayer(float x, float y) {
        for(Player player : players) {
            if (player.stateMachine.getCurrentState() == PlayerState.Controlled) {
                player.getBody().setLinearVelocity(x * player.speedMultiplier, y * player.speedMultiplier);
            }
        }
    }

    public void updatePlayers(float dt, Ball ball, Team adversaryTeam){
        for(int i = 0; i < players.size(); i++) {
            if (players.get(i).stateMachine.getCurrentState() != PlayerState.Controlled) {
                players.get(i).updateWayPoints(ball, adversaryTeam);
                players.get(i).update(dt);
            }

            if(players.get(i).powerActivated ){
                players.get(i).activeTime += dt;
                if(players.get(i).activeTime > Constants.powerTime){
                    players.get(i).speedMultiplier = 1;
                    players.get(i).powerActivated = false;
                    players.get(i).activeTime = 0;
                }
            }
        }
    }

    public void updatePlayers(float dt) {
        for(int i = 0; i < players.size(); i++) {
            if (players.get(i).stateMachine.getCurrentState() != PlayerState.Controlled) {
                players.get(i).update(dt);
            }
        }
    }

    /**
     * Applies the power up to all the team's players
     * @param i speed modifier
     */
    public void applyPowerUp(float i) {
        for(Player player : players) {
            player.speedMultiplier = i;
            player.powerActivated = true;
            player.activeTime = 0;
        }
    }

    private float distanceBetweenPoints(Vector2 p1, Vector2 p2){
        float xDiff = (float)Math.pow(p1.x - p2.x, 2);
        float yDiff = (float)Math.pow(p1.y - p2.y, 2);
        return (float)Math.sqrt(xDiff + yDiff);
    }

    /**
     * Updates the scoring information
     * @param scorer the name of the player who scored
     */
    public void goalScored(String scorer) {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).name.equals(scorer)){
                players.get(i).score++;
            }
        }
        score++;
    }

    /**
     * Updates the scoring information if there's an auto goal
     * @param scorer
     */
    public void autoGoal(String scorer) {
        for(int i = 0; i < players.size(); i++) {
            if(players.get(i).name == scorer)
                players.get(i).score--;
        }
    }

    public void initWayPoints(Ball ball, Team adversaryTeam) {
        for(int i = 0; i < players.size(); i++) {
            players.get(i).initWayPoints(ball, adversaryTeam);
        }
    }

    /**
     * Returns the team's players
     * @return team's players to return
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the number of player in the team
     * @return number of players to return
     */
    public int getNumberPlayers() {
        return players.size();
    }

    /**
     * Returns the team state
     * @return team state to return
     */
    public TeamState getTeamState() {
        return teamState;
    }

    /**
     * Returns the team's name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the team's name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return an ArrayList with all the player's names
     * @return
     */
    public ArrayList<String> getPlayerNames(){
        ArrayList<String> names;
        names = new ArrayList<String>();
        for(int i = 0; i < players.size(); i++) {
            names.add(players.get(i).name);
        }
        return names;
    }

    /**
     * Dispose of all the objects
     */
    public void dispose() {
        erasePlayers();
    }

    /*
    * BEGIN OF THE MULTPLAYER FUNCTIONS
    * */

    /**
     * ONLINE ONLY
     * Constructor for the team
     * @param name
     * @param initialState
     * @param w
     */
    public Team(String name, TeamState initialState, World w) {
        this.name = name;
        world = w;
        score = 0;
        teamState = initialState;
        players = new ArrayList<Player>();
    }

    /**
     * ONLINE ONLY
     * Adds a new player to the team, and if that player is the controlled one, sets it
     * @param name player's name
     * @param team player's team
     * @param size the size of the player in the physics world
     * @param controlledPlayer is controlled player?
     * @param w the world the player is added
     * @param controlledPlayerTeam the team the controlled player belongs to
     * @param match the client's match
     */
    public void addPlayer(String name, int team, float size, boolean controlledPlayer, World w, int controlledPlayerTeam, MultiPlayMatch match) {
        float x, y;
        int teamSide;
        boolean controlled;

        if(team == 0)
            teamSide = 1;
        else
            teamSide = -1;

        if(players.size() == 0) {
            x = (Constants.ScreenWidth / 4) * teamSide;
            y = 0;
        } else {
            x = ((Constants.ScreenWidth / 4) - (Constants.ScreenWidth / 6)) * teamSide;
            if(players.size() != 0)
                y = (Constants.ScreenHeight  / 4) * teamSide;
            else
                y = (-Constants.ScreenHeight  / 4) * teamSide;
        }

        if(playerAlreadyControlled() || !controlledPlayer)
            controlled = false;
        else
            controlled = true;

        Player player = new Player(x, y, name, team, controlled, size);
        player.addPhysics(w);
        players.add(player);

        if(controlled && player.team == controlledPlayerTeam)
            match.setControlledPlayer(player, player.body.getTransform().getPosition());
    }

    /**
     * ONLINE ONLY
     * Removes a player from the client's match
     * @param name player's name
     */
    public void removePlayer(String name) {
        for(Player player : players) {
            if(player.name.equals(name)) {
                player.getBody().getWorld().destroyBody(player.getBody());
                players.remove(player);
                break;
            }
        }
    }

    /**
     * ONLINE ONLY
     * Returns true if there's already a controlled player in the client's match
     * @return true if already a controlled player, false otherwise
     */
    private boolean playerAlreadyControlled() {
        for(Player player : players) {
            if(player.getControlled())
                return true;
        }

        return false;
    }

    /**
     * ONLINE ONLY
     * Updates a player position
     * @param x player's new x position
     * @param y player's new y position
     * @param name player's name
     */
    public void changePlayerPosition(float x, float y, String name) {
        for(Player player : players) {
            if(player.name.equals(name)) {
                player.updatePlayerPosition(x, y);
                break;
            }
        }
    }

    /*
    * END OF THE MULTPLAYER FUNCTIONS
    * */
}
