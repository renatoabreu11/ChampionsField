package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import static logic.Match.entityMasks.BallMask;
import static logic.Match.entityMasks.FootballGoalMask;
import static logic.Match.entityMasks.GoalMask;
import static logic.Match.entityMasks.PlayerMask;

public class Goal {
    Vector2 position;
    float verticalLength;
    float horizontalLength;
    Body body;

    public Goal(float xPosition, float yPosition, float verticalLength, float horizontalLength, World w, String whichTeam) {
        position = new Vector2(xPosition * 0.01f , yPosition * 0.01f );
        this.verticalLength = verticalLength * 0.01f;
        this.horizontalLength = horizontalLength * 0.01f;
        float width = Gdx.graphics.getWidth() * 0.01f;
        float height = Gdx.graphics.getHeight() * 0.01f;

        if(whichTeam.equals("VisitorGoal")){
            this.horizontalLength *= -1;
        }

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
        goalTrigger.set(this.horizontalLength, this.verticalLength/2, this.horizontalLength, -this.verticalLength/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = GoalMask.getMask();
        fixtureDef.filter.maskBits = (short) (BallMask.getMask() | PlayerMask.getMask());

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
        goalTriggerFixture.filter.categoryBits = FootballGoalMask.getMask();
        goalTriggerFixture.filter.maskBits = BallMask.getMask();

        Fixture fixture;
        fixture = body.createFixture(goalTriggerFixture);
        fixture.setUserData(whichTeam);
        goalTrigger.dispose();
    }

    public Body getBody() {
        return body;
    }

    public float getVerticalLength() {
        return verticalLength;
    }

    public void setVerticalLength(float verticalLength) {
        this.verticalLength = verticalLength;
    }

    public float getHorizontalLength() {
        return horizontalLength;
    }

    public void setHorizontalLength(float horizontalLength) {
        this.horizontalLength = horizontalLength;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

}
