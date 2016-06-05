package logic;

import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import utils.Constants;

public class PowerUp {

    Vector2 position;

    Constants.powerUpType type;

    boolean active;

    public PowerUp() {
        Random r = new Random();
        active = false;
    }

    public void checkPowerUpAppearance(long timeElapsed){
        if(timeElapsed == Constants.PowerfirstAppear || timeElapsed == Constants.PowerLastAppear)
            active = true;

        Random r = new Random();
        int aux = r.nextInt(5) + 1;
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

        position = new Vector2(0, 0);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Constants.powerUpType getType() {
        return type;
    }

    public void setType(Constants.powerUpType type) {
        this.type = type;
    }
}
