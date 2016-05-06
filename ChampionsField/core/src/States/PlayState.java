package States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    public PlayState(GameStateManager gsm) {
        super(gsm);
        field = new Texture("MenuBackground.jpg");
        cam.setToOrtho(false);
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

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        //sb.draw(field, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        team1.render(sb);
        sb.end();
    }
}
