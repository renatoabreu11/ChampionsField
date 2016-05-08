package logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    private Texture ballTex;
    Vector2 position;
    int size;

    public Ball(float width, float height, int size){
        ballTex = new Texture("ball.png");
        position = new Vector2(width/2 - size / 2, height/2 - size / 2);
        this.size = size;
    }

    public Texture getTexture() {
        return ballTex;
    }
}
