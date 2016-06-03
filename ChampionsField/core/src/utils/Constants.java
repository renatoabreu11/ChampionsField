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
    public static final float regionWidth = 345 * widthScale * WORLD_TO_BOX;
    public static final float regionHeight = 770 * heightScale * WORLD_TO_BOX;

    //players positions
    public static final Vector2 Keeper = new Vector2(ScreenWidth/2 - 210 * widthScale , 0);
    public static final Vector2 CenterDefender = new Vector2(ScreenWidth/2 - 430 * widthScale , -225 * heightScale);
    public static final Vector2 DefensiveMidfielder = new Vector2(650 * widthScale, 225 * heightScale );
    public static final Vector2 AttackingMidfielder = new Vector2(325 * widthScale , - 375 * heightScale);
    public static final Vector2 Striker = new Vector2(200 * widthScale, 425 * heightScale);

    //Attacking team field regions
    public static final Rectangle AttackGR = new Rectangle((-ScreenWidth/2 + 130 * widthScale) * WORLD_TO_BOX, (-ScreenHeight/2 + 250 * heightScale) * WORLD_TO_BOX, 460 * widthScale * WORLD_TO_BOX, 1100 * heightScale * WORLD_TO_BOX);
    public static final Rectangle AttackCD = new Rectangle((-ScreenWidth/2 + 590 * widthScale) * WORLD_TO_BOX, 0, regionWidth, regionHeight);
    public static final Rectangle AttackDM = new Rectangle((-ScreenWidth/2 + 590 * widthScale) * WORLD_TO_BOX, -regionHeight, regionWidth, regionHeight);
    public static final Rectangle AttackAM = new Rectangle(-regionWidth, 0, regionWidth, regionHeight);
    public static final Rectangle AttackST = new Rectangle(-regionWidth, - regionHeight, regionWidth, regionHeight);

    //Defending team field regions
    public static final Rectangle DefendGR = new Rectangle((ScreenWidth/2 - 590 * widthScale) * WORLD_TO_BOX, (-ScreenHeight/2 + 250 * heightScale) * WORLD_TO_BOX, 460 * widthScale * WORLD_TO_BOX, 1100 * heightScale * WORLD_TO_BOX);
    public static final Rectangle DefendCD = new Rectangle(345 * widthScale * WORLD_TO_BOX, 0, 345 * widthScale * WORLD_TO_BOX, 770 * heightScale * WORLD_TO_BOX);
    public static final Rectangle DefendDM = new Rectangle(345 * widthScale * WORLD_TO_BOX, (- 770 * heightScale) * WORLD_TO_BOX, 345 * widthScale * WORLD_TO_BOX, 770 * heightScale * WORLD_TO_BOX);
    public static final Rectangle DefendAM = new Rectangle(0, 0, 345 * widthScale * WORLD_TO_BOX, 770 * heightScale * WORLD_TO_BOX);
    public static final Rectangle DefendST = new Rectangle(0, (- 770 * heightScale) * WORLD_TO_BOX, 345 * widthScale * WORLD_TO_BOX, 770 * heightScale * WORLD_TO_BOX);

    public static final int ReturnToRegion = 0;
    public static final int BallAtRegion = 1;


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
