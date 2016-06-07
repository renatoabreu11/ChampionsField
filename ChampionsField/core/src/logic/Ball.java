package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import utils.Constants;

public class Ball implements Coordinates, Steerable<Vector2> {
    Vector2 position;
    Body body;

    boolean tagged;
    float maxLinearSpeed, maxLinearAcceleration;
    float maxAngularSpeed, maxAngularAcceleration;
    float radius;
    SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
    SteeringBehavior<Vector2> steeringBehavior;

    String lastTouch;

    /**
     * Constructor for the ball
     * @param xPosition starting x position
     * @param yPosition starting y position
     * @param size the size of the ball in the physics world
     * @param w the world the ball is added
     */
    public Ball(float xPosition, float yPosition, float size, World w) {
        position = new Vector2(xPosition * 0.01f, yPosition * 0.01f);
        radius = (size / 2) * 0.01f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);
        body = w.createBody(bodyDef);
        body.setLinearDamping(0.75f);
        body.setAngularDamping(0.75f);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.5f;
        fixtureDef.friction = 0.9f;
        fixtureDef.restitution = 0.8f;
        fixtureDef.filter.categoryBits = Constants.entityMasks.BallMask.getMask();
        fixtureDef.filter.maskBits = (short) (Constants.entityMasks.PlayerMask.getMask() | Constants.entityMasks.GoalMask.getMask() |
                Constants.entityMasks.FieldBordersMask.getMask() | Constants.entityMasks.GoalMask.getMask() | Constants.entityMasks.FootballGoalMask.getMask());

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Ball");
        shape.dispose();

        lastTouch = "";

        maxLinearSpeed = 5;
        maxLinearAcceleration = 50;
        maxAngularSpeed = 3;
        maxAngularAcceleration = 0.5f;
        tagged = false;
    }

    public void update(float dt) {
        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(dt);
        }
    }

    private void applySteering(float dt) {
        boolean anyAccelerations = false;

        if (!steeringOutput.linear.isZero()) {
            Vector2 force = steeringOutput.linear.scl(dt);
            body.applyForceToCenter(force, true);
            anyAccelerations = true;
        }

        if (steeringOutput.angular != 0) {
            body.applyTorque(steeringOutput.angular * dt, true);
            anyAccelerations = true;
        }

        if (anyAccelerations) {
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt((currentSpeedSquare))));
            }

            if (body.getAngularVelocity() > maxAngularSpeed) {
                body.setAngularVelocity(maxAngularSpeed);
            }

        }
    }

    /**
     * Sets the new position for the ball
     * @param position the new position for the ball
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Return the ball's radius
     * @return radius to be returned
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Returns the ball's body
     * @return body to be returned
     */
    public Body getBody() {
        return body;
    }

    /**
     * Sets the 'Vector2 position' to the same position as the ball's body
     */
    public void setPositionToBody() {
        setPosition(body.getPosition());
    }

    /**
     * Repositions the ball at the center of the field, and stops it from moving
     */
    public void reposition() {
        body.setTransform(0, 0, 0);
        setPositionToBody();
        body.setLinearVelocity(0, 0);
    }

    /*
    * BEGIN OF MULTIPLAYER FUNCTIONS
    * */

    /**
     * ONLINE ONLY
     * Updates the ball's position and velocity
     * @param x the new x position
     * @param y the new y position
     * @param vx the new x velocity
     * @param vy the new y velocity
     */
    public void updatePosition(float x, float y, float vx, float vy) {
        body.setTransform(x, y, 0);
        setPositionToBody();
        body.setLinearVelocity(vx, vy);
    }

    /*
    * END OF MULTIPLAYER FUNCTIONS
    * */

    /*
     * Coordinates methods
     */

    /**
     * Calculates the "draw world" position based on the "physics world" ones
     * @return the "draw world" position
     */
    @Override
    public Vector2 getScreenCoordinates() {
        float x = getPosition().x * 100f + Gdx.graphics.getWidth() / 2 - radius * 100f;
        float y = getPosition().y * 100f + Gdx.graphics.getHeight() / 2 - radius * 100f;
        return new Vector2(x, y);
    }

    /*
     * Steerable methods
     */
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float) Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float) Math.sin(angle);
        outVector.y = (float) Math.cos(angle);
        return outVector;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return radius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Location<Vector2> newLocation() {
        return null;
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> behavior) {
        steeringBehavior = behavior;
    }

    public String getLastTouch() {
        return lastTouch;
    }
}
