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

import logic.Team;

/**
 * Created by Evenilink on 02/05/2016.
 */
public class PlayState extends State {

    private Texture field;
    private Team team1;
    private Rectangle fieldBounds;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        field = new Texture("MenuBackground.jpg");
        fieldBounds = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        team1 = new Team(1);
    }

    @Override
    public void dispose() {

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        team1.updatePlayers();

        for(int i = 0; i < team1.getNumberPlayers(); i++) {
            double fieldLeft = team1.getPlayers().get(i).getCollider().x - team1.getPlayers().get(i).getRadius() - fieldBounds.x;
            double fieldRight = Gdx.graphics.getWidth() - team1.getPlayers().get(i).getCollider().x - team1.getPlayers().get(i).getRadius();
            double fieldTop = team1.getPlayers().get(i).getCollider().y - team1.getPlayers().get(i).getRadius() - fieldBounds.y;
            double fieldBottom = Gdx.graphics.getHeight() - team1.getPlayers().get(i).getCollider().y - team1.getPlayers().get(i).getRadius();

            if (fieldLeft < 0)
                team1.getPlayers().get(i).setPositionX(0);
            if(fieldRight < 0)
                team1.getPlayers().get(i).setPositionX(Gdx.graphics.getWidth() - team1.getPlayers().get(i).getRadius() * 2);
            if(fieldTop < 0)
                team1.getPlayers().get(i).setPositionY(0);
            if(fieldBottom < 0)
                team1.getPlayers().get(i).setPositionY(Gdx.graphics.getHeight() - team1.getPlayers().get(i).getRadius() * 2);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(field, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        team1.render(sb);
        sb.end();
    }
}
