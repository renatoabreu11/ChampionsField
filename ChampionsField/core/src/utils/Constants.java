package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public final class Constants {
    public static final float ScreenWidth = Gdx.graphics.getWidth();
    public static final float ScreenHeight = Gdx.graphics.getHeight();
    public static final float BALL_SIZE = 48;
    public static final float PLAYER_SIZE = 60;
    public static final float FIELD_TEXTURE_WIDTH = 2560;
    public static final float FIELD_TEXTURE_HEIGHT = 1600;
    public static final float BOX_TO_WORLD = 100f;
    public static final float WORLD_TO_BOX = 0.01f;
    public static final float GAME_SIMULATION_SPEED = 1 / 60f;
    public static final float widthScale = ScreenWidth / FIELD_TEXTURE_WIDTH;
    public static final float heightScale = ScreenHeight / FIELD_TEXTURE_HEIGHT;
    public static final float regionWidth = 380 * widthScale * WORLD_TO_BOX;
    public static final float regionHeight = 1535 * heightScale * WORLD_TO_BOX;
    public static final long  GAME_TIME = 300;
    public static final float Switch_Height = 220 * heightScale;
    public static final float Switch_Width = 220 * widthScale;
    public static final float leaderboardWidthScale = ScreenWidth / 700;
    public static final float leaderboardHeightScale = ScreenHeight / 1000;

    //players positions
    public static final Vector2 Defender = new Vector2(ScreenWidth/2 - 430 * widthScale , 0);
    public static final Vector2 Midfielder = new Vector2(400 * widthScale, -300 * heightScale );
    public static final Vector2 Striker = new Vector2(200 * widthScale, 450 * heightScale);

    //Attacking team field regions
    public static final Rectangle AttackCentral = new Rectangle((-ScreenWidth/2 + 130 * widthScale) * WORLD_TO_BOX, - regionHeight/2, regionWidth, regionHeight);
    public static final Rectangle AttackMidfielder = new Rectangle((-ScreenWidth/2 + 510 * widthScale) * WORLD_TO_BOX, - regionHeight/2, regionWidth, regionHeight);
    public static final Rectangle AttackStriker = new Rectangle(-regionWidth, - regionHeight/2, regionWidth, regionHeight);

    //Defending team field regions
    public static final Rectangle DefendCentral = new Rectangle(760 * widthScale * WORLD_TO_BOX, - regionHeight/2, regionWidth, regionHeight);
    public static final Rectangle DefendMidfielder = new Rectangle(380 * widthScale * WORLD_TO_BOX, - regionHeight/2, regionWidth, regionHeight);
    public static final Rectangle DefendStriker =  new Rectangle(0, - regionHeight/2, regionWidth, regionHeight);
    public static long PowerfirstAppear = 2;
    public static long PowerLastAppear = 180;

    public enum entityMasks{
        BallMask(1),
        PlayerMask(2),
        FieldBordersMask(4),
        GoalMask(8),
        ScreenBordersMask(16),
        FootballGoalMask(32),
        CenterMask(64);

        private final short mask;
        entityMasks(int mask){
            this.mask = (short)mask;
        }
        public short getMask(){
            return this.mask;
        }
    };

    public enum powerUpType{
        TeamSpeedInc,
        TeamSpeedDec,
        PlayerSpeedInc,
    };

    private Constants(){
        //this prevents even the native class from
        //calling this :
        throw new AssertionError();
    }
}
