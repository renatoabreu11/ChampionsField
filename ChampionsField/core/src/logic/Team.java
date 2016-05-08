package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Team {
    //scoring corresponde à animação de golo. Playing é jogo corrido. Os outros dois é a respetiva situação no inicio do jogo ou após golos.
    enum TeamState{
        Playing, Attacking, Defending, Scoring
    }

    int score;
    String name;
    ArrayList<Player> players;
    private Texture texture;
    TeamState teamState;
    private float spriteSize;

    public Team(int numPlayers, float size, String name, TeamState initialState, float height, float width) {
        texture = new Texture("Player.png");
        score = 0;
        spriteSize = size;
        teamState = initialState;
        players = new ArrayList<Player>();
        for(int i = 0; i < numPlayers; i++){
            if(teamState == TeamState.Attacking){
                players.add(new Player(i*50 , height / 2, "B", size, false));
            } else{
                players.add(new Player(width - i*50, height / 2, "B", size, false));
            }
        }
    }

    public void controlPlayer(int index){
        for(int i = 0; i < players.size(); i++) {
            if(i == index)
                players.get(i).setControlledPlayer(true);
        }
    }

    public void render(SpriteBatch sb) {
        for(int i = 0; i < players.size(); i++) {
            sb.draw(texture, players.get(i).position.x, players.get(i).position.y, spriteSize, spriteSize);
            players.get(i).render(sb);
        }
    }

    public void updateControlledPlayer(float x, float y) {
        for(int i = 0; i < players.size(); i++) {
            if (players.get(i).isControlledPlayer()) {
                players.get(i).updatePosition(x, y);
                for (int j = 0; j < players.size(); j++) {
                    if (i != j && players.get(i).getCollider().overlaps(players.get(j).getCollider())) {
                        players.get(i).setLastPosition();
                        players.get(j).setLastPosition();
                    }
                }
            }
        }
    }

    public void updatePlayers(){
        for(int i = 0; i < players.size(); i++) {
            if(!players.get(i).isControlledPlayer()){
                players.get(i).move();
                for(int j = 0; j < players.size(); j++){
                    if (i != j && players.get(i).getCollider().overlaps(players.get(j).getCollider())) {
                        players.get(i).setLastPosition();
                        players.get(j).setLastPosition();
                    }
                 }
            }
            players.get(i).updateCollider();
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
}
