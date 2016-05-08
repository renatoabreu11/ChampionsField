package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    private TextureAtlas ballTex;
    private Animation ballAnim;
    Vector2 position;
    int size;
    float speed;
    private float deltaTime = 0;
    private Circle collider;
    private float radius;

    public Ball(float width, float height, int size){
        ballTex = new TextureAtlas("SoccerBall.atlas");
        speed = 0;
        ballAnim = new Animation(1/15f, ballTex.getRegions());
        position = new Vector2(width/2 - size/2, height/2 - size);
        this.size = size;
        radius = size / 2;
        collider = new Circle(position.x + radius / 2, position.y + radius / 2, radius);
    }

    public TextureAtlas getTexture() {
        return ballTex;
    }

    public Circle getCollider() {
        return collider;
    }

    public void render(SpriteBatch sb) {
        deltaTime += Gdx.graphics.getDeltaTime();
        sb.draw(ballAnim.getKeyFrame(deltaTime, true), position.x, position.y, size, size);
    }


}
