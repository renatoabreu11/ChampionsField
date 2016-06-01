package utils;

import com.badlogic.gdx.Gdx;

public final class Constants {
    public static final float BALL_SIZE = 48;
    public static final float PLAYER_SIZE = 60;
    public static final float FIELD_TEXTURE_WIDTH = 2560;
    public static final float FIELD_TEXTURE_HEIGHT = 1600;
    public static final float GAME_SIMULATION_SPEED = 1 / 60f;
    public static final float widthScale = Gdx.graphics.getWidth() / FIELD_TEXTURE_WIDTH;
    public static final float heightScale = Gdx.graphics.getHeight() / FIELD_TEXTURE_HEIGHT;

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
