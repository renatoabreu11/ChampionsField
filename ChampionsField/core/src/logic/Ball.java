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

    public Ball(float xPosition, float yPosition, float size, World w){
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
        fixtureDef.filter.maskBits = (short)(Constants.entityMasks.PlayerMask.getMask() | Constants.entityMasks.GoalMask.getMask() |
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

    public void update(float dt){
        if(steeringBehavior != null){
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(dt);
        }
    }

    private void applySteering(float dt){
        boolean anyAccelerations = false;

        if(!steeringOutput.linear.isZero()){
            Vector2 force = steeringOutput.linear.scl(dt);
            body.applyForceToCenter(force, true);
            anyAccelerations = true;
        }

        if(steeringOutput.angular != 0){
            body.applyTorque(steeringOutput.angular * dt, true);
            anyAccelerations = true;
        }

        if(anyAccelerations){
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            if(currentSpeedSquare > maxLinearSpeed * maxLinearSpeed){
                body.setLinearVelocity(velocity.scl(maxLinearSpeed/(float)Math.sqrt((currentSpeedSquare))));
            }

            if(body.getAngularVelocity() > maxAngularSpeed){
                body.setAngularVelocity(maxAngularSpeed);
            }

        }
    }

    public void setPosition(Vector2 position) {
        this.position = position;
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

    public void reposition(){
        body.setTransform(0, 0, 0);
        setPositionToBody();
        body.setLinearVelocity(0, 0);
    }

    /*
    * BEGIN OF MULTIPLAYER FUNCTIONS
    * */

    public void updatePosition(float x, float y) {
        body.setTransform(x, y, 0);
        setPositionToBody();
        body.setLinearVelocity(0, 0);
    }

    /*
    * END OF MULTIPLAYER FUNCTIONS
    * */

    /**
     * Coordinates methods
     */
    @Override
    public Vector2 getScreenCoordinates() {
        float x = getPosition().x * 100f + Gdx.graphics.getWidth()/2 - radius*100f;
        float y = getPosition().y * 100f + Gdx.graphics.getHeight()/2 - radius*100f;
        return new Vector2(x, y);
    }

    /**
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
        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
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

    public SteeringBehavior<Vector2> getSteeringBehavior(){
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> behavior){
        steeringBehavior = behavior;
    }
}
