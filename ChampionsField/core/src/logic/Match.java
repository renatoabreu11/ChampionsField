package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.PriorityQueue;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import utils.Constants;
import utils.Statistics;

public abstract class Match{
    public enum matchState{
        KickOff,
        Score,
        Play
    }

    Field field;
    Team homeTeam;
    Team visitorTeam;
    Goal homeTeamGoal;
    Goal visitorTeamGoal;
    Ball ball;
    World w;
    public Rain rain;

    int numberOfPlayers;
    float playerSize;
    matchState currentState;
    volatile long elapsedTime;
    long startTime;
    String time;

    //Online only
    public boolean ballTouched;

    /**
     * Constructor for the match
     * Creates the "physics world" and his listener, and everything related to the match logic,
     * such as the goals, the field, the rain...
     * @param numberOfPlayers number of player to participate in the match
     */
    public Match(int numberOfPlayers){
        Vector2 gravity = new Vector2(0, 0f);
        w = new World(gravity, true);
        createCollisionListener();

        this.numberOfPlayers = numberOfPlayers;
        this.playerSize = (Constants.PLAYER_SIZE * 100 / 1920) * Gdx.graphics.getWidth() / 100;

        //Objects
        field = new Field(w);
        homeTeamGoal = new Goal(-Gdx.graphics.getWidth()/2 + 30f * Constants.widthScale, 0, 500f * Constants.heightScale, 100f * Constants.widthScale,  w, "HomeGoal");
        visitorTeamGoal = new Goal(Gdx.graphics.getWidth()/2 - 30f * Constants.widthScale, 0, 500f * Constants.heightScale, 100f * Constants.widthScale,  w, "VisitorGoal");
        ball = new Ball(0, 0, (Constants.BALL_SIZE * 100 / 1920) * Gdx.graphics.getWidth() / 100, w);
        rain = new Rain(Constants.ScreenWidth, Constants.ScreenHeight);

        currentState = matchState.KickOff;
        startTime = System.currentTimeMillis();
        elapsedTime = ((System.currentTimeMillis() - startTime) / 1000);
        time = Constants.formatter.format(new Date(elapsedTime * 1000L));
        ballTouched = false;
    }

    /**
     * Starts the match's time
     */
    public void startTimer(){
        startTime = System.currentTimeMillis();
    }

    /**
     * Creates the "physics world" collision listener, which is used to detect collision between
     * physic's objects
     */
    private void createCollisionListener() {
        w.setContactListener(new ContactListener() {
            /**
             * Runs every time a collision is detected
             * When that happens, it verifies the objects that collided and handles each one
             * differently
             * @param contact the "shock" between 2 physic's objects
             */
            @Override
            public void beginContact(Contact contact) {
                Fixture f1 = contact.getFixtureA();
                Fixture f2 = contact.getFixtureB();

                if((f1.getUserData() == "HomeGoal" || f1.getUserData() == "Ball") && (f2.getUserData() == "HomeGoal" || f2.getUserData() == "Ball")){
                    teamScored(visitorTeam, homeTeam, ball.lastTouch);
                    ball.body.setAwake(false);
                }
                else if((f1.getUserData() == "VisitorGoal" || f1.getUserData() == "Ball") && (f2.getUserData() == "VisitorGoal" || f2.getUserData() == "Ball")){
                    teamScored(homeTeam, visitorTeam, ball.lastTouch);
                    ball.body.setAwake(false);
                } else{
                    if((f1.getUserData() != "Ball" && f2.getUserData() != "Ball") || (f1.getUserData() == null || f2.getUserData() == null)){
                        return;
                    } else{
                        String data1 = f1.getUserData().toString();
                        String data2 = f2.getUserData().toString();

                        ArrayList<String> homeTeamNames = homeTeam.getPlayerNames();
                        ArrayList<String> visitorTeamNames = visitorTeam.getPlayerNames();

                        if(homeTeamNames.contains(data1) || visitorTeamNames.contains(data1)){
                            ball.lastTouch = data1;
                        } else if(homeTeamNames.contains(data2) || visitorTeamNames.contains(data2)){
                            ball.lastTouch = data2;
                        }

                        ballTouched = true;
                    }
                }
            }

            /**
             * Runs every time 2 object that collided are no longer colliding
             * @param contact the "shock" between 2 physic's objects
             */
            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    /**
     * Returns the time elapsed as a String
     * @return time elapsed
     */
    public String getTime() {
        return time;
    }

    /**
     * OVERRIDEABLE FUNCTION
     *
     * @param defendingTeam
     * @param attackingTeam
     * @param lastTouch
     */
    public abstract void teamScored(Team defendingTeam, Team attackingTeam, String lastTouch);

    /**
     * OVERRIDEABLE FUNCTION
     * Updates the match
     * @param x
     * @param y
     * @param dt
     */
    public abstract void updateMatch(float x, float y, float dt);

    /**
     * OVERRIDEABLE FUNCTION
     * Called after a goal is scored
     * */
    public abstract void endScoreState();

    /**
     * OVERRIDEABLE FUNCTION
     * Returns the current state of the match
     * @return state to return
     */
    public matchState getCurrentState() {
        return currentState;
    }

    /**
     * Returns the match's ball
     * @return ball to return
     */
    public Ball getBall(){
        return this.ball;
    }

    /**
     * Returns the home team
     * @return team to return
     */
    public Team getHomeTeam(){
        return this.homeTeam;
    }

    /**
     * Returns the visitor team
     * @return team to return
     */
    public Team getVisitorTeam(){
        return this.visitorTeam;
    }

    /**
     * Returns the number of player in the match
     * @return number of player to return
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Returns the home team's score
     * @return score to return
     */
    public int getScoreHomeTeam() {
        return homeTeam.score;
    }

    /**
     * Return the visitor team's score
     * @return score to return
     */
    public int getScoreVisitorTeam() {
        return visitorTeam.score;
    }

    /**
     * Returns the home team's goal
     * @return goal to return
     */
    public Goal getHomeTeamGoal() {
        return homeTeamGoal;
    }

    /**
     * Return the visitor team's goal
     * @return goal to return
     */
    public Goal getVisitorTeamGoal() {
        return visitorTeamGoal;
    }

    /**
     * OVERRIDEABLE FUNCTION
     *
     */
    public abstract void endGame();

    /**
     * Returns the elapsed time as a long
     * @return time to return
     */
    public long getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Returns the world
     * @return world to return
     */
    public World getWorld() {
        return w;
    }

    /**
     * OVERRIDEABLE FUNCTION
     * Disposes of all the objects
     */
    public abstract void dispose();

}
