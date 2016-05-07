package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by Evenilink on 02/05/2016.
 */
public class Player implements GestureDetector.GestureListener{
    private Vector2 position;
    private Texture texture;
    private float speed;
    private float radius;
    private Circle collider;

    private Stage stage;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    public Player(float xStartPosition, float yStartPosition) {
        position = new Vector2();
        position.x = xStartPosition;
        position.y = yStartPosition;
        texture = new Texture("Player.png");
        speed = 5f;
        radius = 32 / 2;
        collider = new Circle(xStartPosition, yStartPosition, radius);

        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, 200, 200);

        //Create a Stage and add TouchPad
        stage = new Stage();
        //stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    public Circle getCollider() {
        return collider;
    }

    public float getRadius() {
        return radius;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        collider.setX(position.x + radius);
        collider.setY(position.y + radius);    }

    public void setPositionX(float x) {
        position.x = x;
        collider.setX(x + radius);
    }

    public void setPositionY(float y) {
        position.y = y;
        collider.setY(y + radius);
    }

    public void updateCollider() {
        collider.setX(position.x + radius);
        collider.setY(position.y + radius);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
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

    public void render(SpriteBatch sb) {
        //Move blockSprite with TouchPad
        position.x = position.x + touchpad.getKnobPercentX() * speed;
        position.y = position.y + touchpad.getKnobPercentY() * speed;

        //Draw
        sb.draw(texture, position.x, position.y, 32, 32);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
