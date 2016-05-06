package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by Evenilink on 02/05/2016.
 */
public class Team {
    private SpriteBatch sb;
    private ArrayList<Player> players;
    private Texture texture;

    public Team(int numPlayers) {
        sb = new SpriteBatch();
        players = new ArrayList<Player>();
        for(int i = 0; i < numPlayers; i++)
            players.add(new Player(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2));
        texture = new Texture("Player.png");
    }

    public int getNumPlayers() {
        return players.size();
    }

    public void render(SpriteBatch sb) {
        for(int i = 0; i < players.size(); i++) {
            sb.draw(texture, players.get(i).getPosition().x, players.get(i).getPosition().y, 32, 32);
            players.get(i).render(sb);
        }
    }
}
