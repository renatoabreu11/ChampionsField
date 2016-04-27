package States;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Evenilink on 27/04/2016.
 */
public class MenuState extends State {
    private Texture background;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("MenuBackground.jpg");
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
        sb.draw(background, 0, 0, background.getWidth(), background.getHeight());
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
