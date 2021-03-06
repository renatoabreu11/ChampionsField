package logic;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import utils.Constants;

public class PowerUp implements Coordinates{

    Vector2 position;
    Constants.powerUpType type;
    boolean active;

    /**
     * Constructor for the power up
     */
    public PowerUp() {
        Random r = new Random();
        active = false;
    }

    /**
     * Determines when a power up should appear
     * @param timeElapsed the time elapsed in the match
     */
    public void checkPowerUpAppearance(long timeElapsed){
        if(timeElapsed == Constants.PowerfirstAppear || timeElapsed == Constants.PowerLastAppear)
            active = true;

        Random r = new Random();
        int aux = r.nextInt(3) + 1;
        switch (aux){
            case 1:
                type = Constants.powerUpType.TeamSpeedInc;
                break;
            case 2:
                type = Constants.powerUpType.TeamSpeedDec;
                break;
            case 3:
                type = Constants.powerUpType.PlayerSpeedInc;
                break;
        }

        float fieldWidth = Constants.ScreenWidth - 260*Constants.widthScale;
        float fieldHeight = Constants.ScreenHeight - 60*Constants.heightScale;
        float x = r.nextInt((int)(fieldWidth));
        float y = r.nextInt((int)(fieldHeight));
        x -= fieldWidth/2;
        y -= fieldHeight/2;
        position = new Vector2(x * Constants.WORLD_TO_BOX, y * Constants.WORLD_TO_BOX);
    }

    /**
     * Returns true if is active, false otherwise
     * @return value to return
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets if a power up is active or not
     * @param active the flag that controlls the decision
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the power up's position
     * @return position to return
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Updates the power up's position
     * @param position the new position
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Returns the power up type
     * @return type to return
     */
    public Constants.powerUpType getType() {
        return type;
    }

    /**
     * Calculates the "draw world" position based on the "physics world" ones
     * @return the "draw world" position
     */
    @Override
    public Vector2 getScreenCoordinates() {
        float x = getPosition().x * Constants.BOX_TO_WORLD + Constants.ScreenWidth/2;
        float y = getPosition().y * Constants.BOX_TO_WORLD + Constants.ScreenHeight/2;
        return new Vector2(x, y);
    }
}
