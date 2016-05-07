package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by Evenilink on 02/05/2016.
 */
public class Team {
    private ArrayList<Player> players;
    private Texture texture;

    public Team(int numPlayers) {
        texture = new Texture("Player.png");

        players = new ArrayList<Player>();
        players.add(new Player(Gdx.graphics.getWidth() / 2 - 100, 0 * 300, true));
        for(int i = 1; i < numPlayers; i++)
            players.add(new Player(Gdx.graphics.getWidth() / 2 - 100, i * 300, false));
    }

    public void render(SpriteBatch sb) {
        for(int i = 0; i < players.size(); i++) {
            sb.draw(texture, players.get(i).getPosition().x, players.get(i).getPosition().y, 32, 32);
            players.get(i).render(sb);
        }
    }

    public void updatePlayers() {
        for(int i = 0; i < players.size(); i++) {
            players.get(i).updateMovement();
            players.get(i).updateCollider();
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getNumberPlayers() {
        return players.size();
    }
}
