package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Rain {
    ArrayList<Vector2> position;
    float fallingSpeed;
    float width;
    float height;
    float deltaTime;
    boolean isRaining;
    int nextRainingPeriod;
    Random random;

    public Rain(float width, float height) {
        this.width = width;
        this.height = height;
        position = new ArrayList<Vector2>();

        position.add(new Vector2(0, 0));
        position.add(new Vector2(width / 3, 0));
        position.add(new Vector2(width - width / 3, 0));

        position.add(new Vector2(width / 6, 0));
        position.add(new Vector2(width / 3 + width / 6, 0));
        position.add(new Vector2(width - width / 3 + width / 6, 0));

        position.add(new Vector2(0, height / 3));
        position.add(new Vector2(width / 3, height / 3));
        position.add(new Vector2(width - width / 3, height / 3));

        position.add(new Vector2(width / 6, height / 3));
        position.add(new Vector2(width / 3 + width / 6, height / 3));
        position.add(new Vector2(width - width / 3 + width / 6, height / 3));

        position.add(new Vector2(0, height - height / 3));
        position.add(new Vector2(width / 3, height - height / 3));
        position.add(new Vector2(width - width / 3, height - height / 3));

        position.add(new Vector2(width / 6, height - height / 3));
        position.add(new Vector2(width / 3 + width / 6, height - height / 3));
        position.add(new Vector2(width - width / 3 + width / 6, height - height / 3));

        position.add(new Vector2(0, height));
        position.add(new Vector2(width / 3, height));
        position.add(new Vector2(width - width / 3, height));

        fallingSpeed = 14f;
        deltaTime = 0;
        isRaining = false;
        random = new Random();
        nextRainingPeriod = random.nextInt(50) + 1;
    }

    public int getRainSize() {
        if(isRaining) {
            if(deltaTime >= 10)  return position.size();
            else  return Math.round(position.size() * deltaTime * 0.1f);
        } else {
            if(deltaTime >= 10)  return 0;
            else return Math.round(position.size() - (position.size() * deltaTime * 0.1f));
        }
    }

    public Vector2 getPosition(int index) {
        return position.get(index);
    }

    public void update() {
        if(deltaTime >= nextRainingPeriod) {
            if(isRaining) isRaining = false;
            else isRaining = true;

            deltaTime = 0;
            nextRainingPeriod = random.nextInt(50) + 11;
        }

        if(isRaining || (!isRaining && deltaTime <= 10)) {
            for (int i = 0; i < position.size(); i++) {
                if (position.get(i).y <= 0 - height / 3)
                    position.get(i).y = height;
                position.get(i).y -= fallingSpeed;
            }
        }
        deltaTime += Gdx.graphics.getDeltaTime();
    }
}
