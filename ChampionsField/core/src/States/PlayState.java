package States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import logic.Player;
import logic.Team;

/**
 * Created by Evenilink on 02/05/2016.
 */
public class PlayState extends State {

    private Texture field;
    private Team team1;
    private Team team2;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        field = new Texture("MenuBackground.jpg");
        team1 = new Team(1);
        //team2 = new Team(1);
    }

    @Override
    public void dispose() {

    }

    @Override
    protected void handleInput() {

    }

    public void checkWithFieldCollision(Player player) {
        double fieldLeft = player.getCollider().x - player.getRadius();
        double fieldRight = width - player.getCollider().x - player.getRadius();        //width of the field
        double fieldTop = player.getCollider().y - player.getRadius();
        double fieldBottom = height - player.getCollider().y - player.getRadius();      //height of the field

        if (fieldLeft < 0)
            player.setPositionX(0);
        if(fieldRight < 0)
            player.setPositionX(width - player.getRadius() * 2);
        if(fieldTop < 0)
            player.setPositionY(0);
        if(fieldBottom < 0)
            player.setPositionY(height - player.getRadius() * 2);
    }

    @Override
    public void update(float dt) {
        team1.updatePlayers();

        for(int i = 0; i < team1.getNumberPlayers(); i++)
            checkWithFieldCollision(team1.getPlayers().get(i));
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(field, 0, 0, width, height);
        team1.render(sb);
        sb.end();
    }
}
