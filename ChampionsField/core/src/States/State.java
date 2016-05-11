package States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State {
    protected OrthographicCamera cam;
    protected GameStateManager gsm;
    protected float width;
    protected float height;

    protected State(GameStateManager gsm) {
        this.gsm = gsm;
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        cam.setToOrtho(true);       //top left corner is 0,0
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
