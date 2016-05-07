package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
    private Vector2 lastPosition;
    private Texture texture;
    private float speed;
    private float radius;
    private Circle collider;
    private boolean controlledPlayer;

    public Player(float xStartPosition, float yStartPosition, boolean controlledPlayer) {
        position = new Vector2();
        position.x = xStartPosition;
        position.y = yStartPosition;
        lastPosition = new Vector2();
        texture = new Texture("Player.png");
        speed = 5f;
        radius = 32 / 2;
        collider = new Circle(xStartPosition, yStartPosition, radius);
        this.controlledPlayer = controlledPlayer;
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

    public Vector2 getLastPosition() {
        return lastPosition;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        collider.setX(position.x + radius);
        collider.setY(position.y + radius);
    }

    public void setPosition(Vector2 pos) {
        position = pos;
        collider.setX(position.x + radius);
        collider.setY(position.y + radius);
    }

    public void setPositionX(float x) {
        position.x = x;
        collider.setX(x + radius);
    }

    public void setPositionY(float y) {
        position.y = y;
        collider.setY(y + radius);
    }

    public void setLastPosition(Vector2 lastPosition) {
        this.lastPosition = lastPosition;
    }

    public void updateMovement() {
        lastPosition = position;

        if(!controlledPlayer) {
            position.x++;
        }
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
        sb.draw(texture, position.x, position.y, 32, 32);
    }


    //This need to be redefined, it's not good
    @Override
    public boolean equals(Object obj) {
        Player p = (Player) obj;
        if(position.x == p.getPosition().x && position.y == p.getPosition().y)
            return true;
        return false;
    }
}
