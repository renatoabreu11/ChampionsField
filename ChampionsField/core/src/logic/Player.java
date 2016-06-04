package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import utils.Constants;

public class Player implements Coordinates, Steerable<Vector2>{
    Vector2 initialPosition;
    Vector2 position;
    Body body;

    int score;
    String name;
    int team;
    int matchesPlayed;

    boolean tagged;
    private float maxLinearSpeed, maxLinearAcceleration;
    private float maxAngularSpeed, maxAngularAcceleration;
    float radius;
    private SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
    private SteeringBehavior<Vector2> steeringBehavior;

    StateMachine<Player, PlayerState> stateMachine;
    WayPoint controlledPlayerWayPoint;
    WayPoint ballWayPoint;
    Rectangle region;

    //ONLINE VARIABLES ONLY
    boolean isControlledPlayer;

    public Player(float xPosition, float yPosition, String name, float size, World w,  Rectangle rectangle) {
        position = new Vector2(xPosition * 0.01f, yPosition* 0.01f);
        initialPosition = position;
        controlledPlayerWayPoint = null;
        ballWayPoint = null;
        this.radius = (size/2) * 0.01f;
        this.score = 0;
        this.name = name;
        matchesPlayed = 0;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);
        body = w.createBody(bodyDef);
        body.setAngularDamping(0.5f);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.5f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.75f;
        fixtureDef.filter.categoryBits = Constants.entityMasks.PlayerMask.getMask();
        fixtureDef.filter.maskBits = (short)(Constants.entityMasks.PlayerMask.getMask() | Constants.entityMasks.BallMask.getMask() |
                Constants.entityMasks.ScreenBordersMask.getMask() | Constants.entityMasks.GoalMask.getMask() | Constants.entityMasks.CenterMask.getMask());
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this.name);
        shape.dispose();

        maxLinearSpeed = 5;
        maxLinearAcceleration = 50;
        maxAngularSpeed = 3;
        maxAngularAcceleration = 0.5f;
        tagged = false;

        stateMachine = new DefaultStateMachine<Player, PlayerState>(this, PlayerState.Static);
        region = rectangle;
    }

    public void update(float dt){
        stateMachine.update();
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

    public void reposition() {
        body.setTransform(initialPosition.x, initialPosition.y, 0);
        setPositionToBody();
        body.setLinearVelocity(0, 0);
    }

    public void setControlled(boolean control) {
        if(control){
            this.stateMachine.changeState(PlayerState.Controlled);
        } else{
            this.stateMachine.changeState(PlayerState.toOriginalRegion);
        }
    }

    public float distanceBetweenPoints(Vector2 p1, Vector2 p2){
        float xDiff = (float)Math.pow(p1.x - p2.x, 2);
        float yDiff = (float)Math.pow(p1.y - p2.y, 2);
        return (float)Math.sqrt(xDiff + yDiff);
    }

    public void setPosition(Vector2 pos) {
        position = pos;
    }

    public Body getBody() {
        return body;
    }

    public void setPositionToBody(){
        setPosition(body.getPosition());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
    * BEGIN OF THE MULTIPLAYER FUNCTIONS
    * */

    public Player() {}

    public Player(float xPosition, float yPosition, String name, int team, boolean controlledPlayer, float size) {
        this.position = new Vector2();
        this.position.x = xPosition * 0.01f;
        this.position.y = yPosition * 0.01f;
        this.name = name;
        this.team = team;
        this.radius = (size/2) * 0.01f;
        this.score = 0;
        this.isControlledPlayer = controlledPlayer;
    }

    public void addPhysics(World w) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);
        body = w.createBody(bodyDef);
        body.setAngularDamping(0.5f);
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2.5f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.75f;
        fixtureDef.filter.categoryBits = Constants.entityMasks.PlayerMask.getMask();
        fixtureDef.filter.maskBits = (short)(Constants.entityMasks.PlayerMask.getMask() | Constants.entityMasks.BallMask.getMask() | Constants.entityMasks.ScreenBordersMask.getMask() | Constants.entityMasks.GoalMask.getMask() | Constants.entityMasks.CenterMask.getMask());
        Fixture fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }

    public boolean getControlled() {
        return isControlledPlayer;
    }

    public void updatePlayerPosition(float x, float y) {
        body.setTransform(x * 0.01f, y * 0.01f, 0);
        setPositionToBody();
        body.setLinearVelocity(0, 0);
    }

    /*
    * END OF THE MULTIPLAYER FUNCTIONS
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

    @Override
    public boolean equals(Object obj) {
        Player p = (Player) obj;
        if((this.position == p.position) && this.name == p.name && this.score == p.score) {
            return true;
        }
        return false;
    }

    public void addMatchPlayed(){
        matchesPlayed++;
    }

    public void updateWayPoints(Ball ball, Player p) {
        if(p != null)
            controlledPlayerWayPoint.setWayPoint(p.body, p.radius);
        ballWayPoint.setWayPoint(ball.body, ball.radius);
    }
}
