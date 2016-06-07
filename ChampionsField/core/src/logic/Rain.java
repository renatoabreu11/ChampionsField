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

    /**
     * Constructor for the rain
     * Adds all the textures needed to fill the screen
     * @param width the screen width
     * @param height the screen height
     */
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

    /**
     * Returns the number of texture instances that should be draw on screen based on the elapsed time
     * @return number of textures to be draw
     */
    public int getRainSize() {
        if(isRaining) {
            if(deltaTime >= 10)  return position.size();
            else  return Math.round(position.size() * deltaTime * 0.1f);
        } else {
            if(deltaTime >= 10)  return 0;
            else return Math.round(position.size() - (position.size() * deltaTime * 0.1f));
        }
    }

    /**
     * Returns the position of the rain
     * @param index index of the rain position
     * @return position to return
     */
    public Vector2 getPosition(int index) {
        return position.get(index);
    }

    /**
     * Updates the rain
     */
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

    /**
     * Disposes of the objects
     */
    public void dispose() {
        position.clear();
    }
}
