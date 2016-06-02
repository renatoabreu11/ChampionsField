package States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State {
    protected OrthographicCamera cam;
    protected GameStateManager gsm;
    protected float width;
    protected float height;
    protected int gameplayController;       //1 --> pan movement, 2 --> joystick
    protected SpriteBatch sb;

    protected State(GameStateManager gsm) {
        this.gsm = gsm;
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        cam.setToOrtho(true);
        sb = new SpriteBatch();
        gameplayController = 2;
    }

    protected State() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        sb = new SpriteBatch();
        gameplayController = 2;
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
