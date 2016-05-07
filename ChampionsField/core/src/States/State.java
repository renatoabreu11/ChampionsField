package States;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;

/**
 * Created by Evenilink on 27/04/2016.
 */
public abstract class State {
    protected OrthographicCamera cam;
    protected GameStateManager gsm;
    protected float width;
    protected float height;

    protected State(GameStateManager gsm) {
        this.gsm = gsm;
        cam = new OrthographicCamera();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        cam.setToOrtho(true);
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
