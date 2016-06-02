package utils;

import com.badlogic.gdx.Gdx;
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
    public static final Vector2 Keeper = new Vector2(ScreenWidth/2 - 210 * widthScale , 0);
    public static final Vector2 CenterDefender = new Vector2(ScreenWidth/2 - 430 * widthScale , -225 * heightScale);
    public static final Vector2 DefensiveMidfielder = new Vector2(650 * widthScale, 225 * heightScale );
    public static final Vector2 AttackingMidfielder = new Vector2(325 * widthScale , - 375 * heightScale);
    public static final Vector2 Striker = new Vector2(200 * widthScale, 425 * heightScale);

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

    private Constants(){
        //this prevents even the native class from
        //calling this :
        throw new AssertionError();
    }
}
