package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.text.SimpleDateFormat;

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
    public static final int NUMBER_PLAYER_ONLINE = 1;
    public static final int NUMBER_MATCHES_HOST_BY_SERVER = 2;
    public static final float Switch_Height = 220 * heightScale;
    public static final float Switch_Width = 220 * widthScale;
    public static final float leaderboardWidthScale = ScreenWidth / 700;
    public static final float leaderboardHeightScale = ScreenHeight / 1000;
    public static final float buttonHeight = ScreenHeight/16;
    public static final float buttonWidth = ScreenWidth/10;
    public static final SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
    public static final float PLAYERS_SPEED = 3;
    public static final float EXPLOSION_SPEED = 5f;
    public static final float EXPLOSION_DURATION = 2.4f;
    public static final float EXPLOSION_WIDTH = ScreenWidth / 5;
    public static final float EXPLOSION_HEIGHT = ScreenHeight / 3;
    public static final float loadingHeight = ScreenHeight/8;
    public static final float loadingWidth = ScreenWidth/8;

    public static long PowerfirstAppear = 70;
    public static long PowerLastAppear = 215;
    public static final float powerAnimationDuration = 10f;
    public static final float powerTime = 10f;
    public static final float PowerUpSpeed = 5.1f;
    public static final float PowerUpWidth = 50f * widthScale;
    public static final float PowerUpHeight = 50f * heightScale;

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
    public static float LoadingTime = 0f;

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
