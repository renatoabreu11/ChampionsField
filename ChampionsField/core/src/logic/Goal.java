package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import utils.Constants;

public class Goal implements Coordinates{
    Vector2 position;
    float verticalLength;
    float horizontalLength;
    Body body;

    /**
     * Constructor for the Goal
     * @param xPosition the x position
     * @param yPosition the y position
     * @param verticalLength the vertical length of the goal
     * @param horizontalLength the horizontal length of the goal
     * @param w the world the goal is added
     * @param whichTeam the team this goal belongs to (basically to select a side)
     */
    public Goal(float xPosition, float yPosition, float verticalLength, float horizontalLength, World w, String whichTeam) {
        position = new Vector2(xPosition * 0.01f , yPosition * 0.01f );
        this.verticalLength = verticalLength * 0.01f;
        this.horizontalLength = horizontalLength * 0.01f;

        if(whichTeam.equals("VisitorGoal"))
            this.horizontalLength *= -1;

        //Football goal borders except goal line.
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x, position.y);
        body = w.createBody(bodyDef);

        EdgeShape vertical = new EdgeShape();
        vertical.set(0, this.verticalLength/2, 0, -this.verticalLength/2);
        EdgeShape horizontalUp = new EdgeShape();
        horizontalUp.set(0, this.verticalLength/2, this.horizontalLength, this.verticalLength/2);
        EdgeShape horizontalDown = new EdgeShape();
        horizontalDown.set(0, -this.verticalLength/2, this.horizontalLength, -this.verticalLength/2);

        //User data is to identify the goal collision
        EdgeShape goalTrigger = new EdgeShape();
        if(whichTeam.equals("VisitorGoal"))
            goalTrigger.set(this.horizontalLength + ((Constants.BALL_SIZE * 100 / 1920) * Gdx.graphics.getWidth() / 100) * 0.01f, this.verticalLength/2, this.horizontalLength +((Constants.BALL_SIZE * 100 / 1920) * Gdx.graphics.getWidth() / 100) * 0.01f, -this.verticalLength/2);
        else
            goalTrigger.set(this.horizontalLength - ((Constants.BALL_SIZE * 100 / 1920) * Gdx.graphics.getWidth() / 100) * 0.01f, this.verticalLength/2, this.horizontalLength - ((Constants.BALL_SIZE * 100 / 1920) * Gdx.graphics.getWidth() / 100) * 0.01f, -this.verticalLength/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = Constants.entityMasks.GoalMask.getMask();
        fixtureDef.filter.maskBits = (short) (Constants.entityMasks.BallMask.getMask() | Constants.entityMasks.PlayerMask.getMask());

        fixtureDef.shape = vertical;
        body.createFixture(fixtureDef);
        vertical.dispose();

        fixtureDef.shape = horizontalUp;
        body.createFixture(fixtureDef);
        horizontalUp.dispose();

        fixtureDef.shape = horizontalDown;
        body.createFixture(fixtureDef);
        horizontalDown.dispose();

        FixtureDef goalTriggerFixture = new FixtureDef();
        goalTriggerFixture.shape = goalTrigger;
        goalTriggerFixture.filter.categoryBits = Constants.entityMasks.FootballGoalMask.getMask();
        goalTriggerFixture.filter.maskBits = Constants.entityMasks.BallMask.getMask();

        Fixture fixture;
        fixture = body.createFixture(goalTriggerFixture);
        fixture.setUserData(whichTeam);
        goalTrigger.dispose();
    }

    /**
     * Return the goal's vertical length
     * @return vertical length to be returned
     */
    public float getVerticalLength() {
        return verticalLength;
    }

    /**
     * Return the goal's horizontal length
     * @return horizontal length to be returned
     */
    public float getHorizontalLength() {
        return horizontalLength;
    }

    /**
     * Return the goal's position
     * @return position to be returned
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Sets the goal's position
     * @param position new goal's position
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Calculates the "draw world" position based on the "physics world" ones
     * @return the "draw world" position
     */
    @Override
    public Vector2 getScreenCoordinates() {
        float x = getPosition().x * 100f + Gdx.graphics.getWidth()/2;
        float y = getPosition().y * 100f + Gdx.graphics.getHeight()/2 - (getVerticalLength() * 100f)/2;
        return new Vector2(x, y);
    }
}
