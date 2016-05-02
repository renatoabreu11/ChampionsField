package States;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Evenilink on 27/04/2016.
 */
public class MenuState extends State implements ApplicationListener, GestureDetector.GestureListener {
    private SpriteBatch sb;
    private Texture background;
    private float width;
    private float height;

    class Button {
        float x;
        float y;
        float width;
        float height;
        Texture texture;
    }

    Button playBtn;
    Button exitBtn;

    public MenuState(GameStateManager gsm) {
        super(gsm);

        sb = new SpriteBatch();
        background = new Texture("MenuBackground.jpg");
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        playBtn = new Button();
        exitBtn = new Button();

        playBtn.texture = new Texture("PlayButton.png");
        playBtn.x = Gdx.graphics.getWidth() / 2 - playBtn.texture.getWidth() / 2;
        playBtn.y = Gdx.graphics.getHeight() / 2 - playBtn.texture.getHeight() / 2;
        playBtn.width = 128;
        playBtn.height = 128;

        exitBtn.texture = new Texture("ExitButton.png");
        exitBtn.x = Gdx.graphics.getWidth() / 2 - exitBtn.texture.getWidth() / 2;
        exitBtn.y = Gdx.graphics.getHeight() / 2 - 300 - exitBtn.texture.getHeight() / 2;
        exitBtn.width = 128;
        exitBtn.height = 128;

        GestureDetector gd = new GestureDetector(this);
        Gdx.input.setInputProcessor(gd);
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        render();
    }

    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        sb.begin();
        sb.draw(background, 0, 0, width, height);
        sb.draw(playBtn.texture, playBtn.x, playBtn.y, playBtn.width, playBtn.height);
        sb.draw(exitBtn.texture, exitBtn.x, exitBtn.y, exitBtn.width, exitBtn.height);
        sb.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(x >= playBtn.x && x <= playBtn.x + playBtn.width && y >= playBtn.y && y <= playBtn.y + playBtn.height)
            gsm.set(new PlayState(gsm));
        else if(x >= exitBtn.x && x <= exitBtn.x + exitBtn.width && y >= exitBtn.y && y <= exitBtn.y + exitBtn.height)
            Gdx.app.exit();

        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
