package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Goal {
    Vector2 position;
    float length;
    Body body;

    public Goal(float xPosition, float yPosition, float length, World w, String whichTeam) {
        position = new Vector2(xPosition * 0.01f, yPosition * 0.01f);
        this.length = length * 0.01f;

        float width = Gdx.graphics.getWidth() * 0.01f;
        float height = Gdx.graphics.getHeight() * 0.01f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position.x, position.y);
        body = w.createBody(bodyDef);

        EdgeShape vertical = new EdgeShape();
        vertical.set(-width / 2 + 0.5f, 0.7f, -width / 2 + 0.5f, -0.7f);
        EdgeShape horizontalUp = new EdgeShape();
        horizontalUp.set(-width / 2 + 0.5f, 0.7f, -width / 2 + 0.9f, 0.7f);
        EdgeShape horizontalDown = new EdgeShape();
        horizontalDown.set(-width / 2 + 0.5f, -0.7f, -width / 2 + 0.9f, -0.7f);

        //User data is to identify the goal collision
        EdgeShape goalTrigger = new EdgeShape();
        goalTrigger.set(-width / 2 + 0.55f, 0.7f, -width / 2 + 0.55f, -0.7f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = goalTrigger;
        fixtureDef.density = 1;
        Fixture fixture;
        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(whichTeam);

        body.createFixture(vertical, 1);
        body.createFixture(horizontalUp, 1);
        body.createFixture(horizontalDown, 1);
    }

    public Body getBody() {
        return body;
    }
}
