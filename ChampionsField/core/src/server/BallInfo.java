package server;

/**
 * Created by Evenilink on 06/06/2016.
 */
public class BallInfo {
    float x, y;
    String lastTouchedPlayer;
    String semiLastTouchedPlayer;

    public BallInfo() {
        x = 0;
        y = 0;
        lastTouchedPlayer = "";
        semiLastTouchedPlayer = "";
    }
}
