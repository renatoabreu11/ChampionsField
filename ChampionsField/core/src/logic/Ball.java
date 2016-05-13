package logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Ball{
    Vector2 position;
    float speed;
    float radius;
    Body body;

    public Ball(float xPosition, float yPosition, int size, World w){
        position = new Vector2(xPosition * 0.01f, yPosition * 0.01f);
        speed = 0;
        radius = size / 2;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);
        body = w.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius * 0.01f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.5f;
        fixtureDef.friction = 0.9f;
        fixtureDef.restitution = 0.8f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Ball");
        shape.dispose();
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Body getBody() {
        return body;
    }

    public void setPositionToBody(){
        setPosition(body.getPosition());
    }
}
