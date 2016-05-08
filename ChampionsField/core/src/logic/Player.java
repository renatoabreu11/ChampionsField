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

import java.util.Random;

public class Player implements GestureDetector.GestureListener{

    enum PlayerState{
        Moving, Shooting, Idle, Spectating
    }
    Vector2 lastPosition;
    Vector2 position;
    private Texture texture;
    float speed;
    private float radius;
    private Circle collider;
    private boolean controlledPlayer;
    int score;
    String name;
    private PlayerState state;

    public Player(float xStartPosition, float yStartPosition, String name, float radius, boolean controlledPlayer) {
        lastPosition = new Vector2(xStartPosition, yStartPosition);
        position = new Vector2(xStartPosition, yStartPosition);
        texture = new Texture("Player.png");
        speed = 5f;
        this.controlledPlayer = controlledPlayer;
        this.radius = radius / 2;
        collider = new Circle(xStartPosition, yStartPosition, radius);
        this.score = 0;
        this.name = name;
        this.state = PlayerState.Idle;
    }

    public boolean isControlledPlayer() {
        return controlledPlayer;
    }

    public void setControlledPlayer(boolean controlledPlayer) {
        this.controlledPlayer = controlledPlayer;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
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
        lastPosition = position;
        position.x = x;
        position.y = y;
        updateCollider();
    }

    public void updatePosition(float x, float y) {
        System.out.println("Posicao antiga: x = " + position.x + ", y = " + position.y + "\n");
        lastPosition = position;
        position.x += x;
        position.y += y;
        System.out.println("Posicao actual: x = " + position.x + ", y = " + position.y + "\n\n");
        updateCollider();
    }

    public void setPosition(Vector2 pos) {
        lastPosition = position;
        position = pos;
        updateCollider();
    }

    public void move() {
        Random r = new Random();
        int x = r.nextInt(5);
        int y = r.nextInt(5);
        updatePosition(x - 2, y - 2);
    }

    public void setLastPosition() {
        position = lastPosition;
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
        if((this.position == p.position) && this.name == p.name && this.score == p.score) {
            return true;
        }
        return false;
    }
}
