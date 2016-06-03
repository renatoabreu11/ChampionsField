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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import utils.Constants;
import utils.Statistics;

public abstract class Match{
    public enum matchState{
        KickOff,
        Pause,
        Score,
        Play;
    }

    Field field;
    Team homeTeam;
    Team visitorTeam;
    Goal homeTeamGoal;
    Goal visitorTeamGoal;
    Ball ball;
    World w;

    int numberOfPlayers;
    float playerSize;
    matchState currentState;
    long elapsedTime;
    long startTime;
    String time;

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

        currentState = matchState.KickOff;
        startTime = System.currentTimeMillis();
        elapsedTime = ((System.currentTimeMillis() - startTime) / 1000);
        LocalTime timeOfDay = LocalTime.ofSecondOfDay(elapsedTime);
        time = timeOfDay.toString();
    }

    private void createCollisionListener() {
        w.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture f1 = contact.getFixtureA();
                Fixture f2 = contact.getFixtureB();

                if((f1.getUserData() == "HomeGoal" || f1.getUserData() == "Ball") && (f2.getUserData() == "HomeGoal" || f2.getUserData() == "Ball")){
                    teamScored(visitorTeam, homeTeam, ball.lastTouch);
                }
                else if((f1.getUserData() == "VisitorGoal" || f1.getUserData() == "Ball") && (f2.getUserData() == "VisitorGoal" || f2.getUserData() == "Ball")){
                    teamScored(homeTeam, visitorTeam, ball.lastTouch);
                }
                else{
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
                    }
                }
            }

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

    public World getWorld() {
        return w;
    }

    public String getTime() {
        return time;
    }

    public abstract void teamScored(Team defendingTeam, Team attackingTeam, String lastTouch);

    public abstract void updateMatch(float x, float y, Rain rain, float dt);

    public abstract void endScoreState();

    public matchState getCurrentState() {
        return currentState;
    }

    public void erasePlayers() {
        homeTeam.erasePlayers();
    }

    public Ball getBall(){
        return this.ball;
    }

    public Field getField() {
        return field;
    }

    public Team getHomeTeam(){
        return this.homeTeam;
    }

    public Team getVisitorTeam(){
        return this.visitorTeam;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public int getScoreHomeTeam() {
        return homeTeam.score;
    }

    public int getScoreVisitorTeam() {
        return visitorTeam.score;
    }

    public Goal getHomeTeamGoal() {
        return homeTeamGoal;
    }

    public void setHomeTeamGoal(Goal homeTeamGoal) {
        this.homeTeamGoal = homeTeamGoal;
    }

    public Goal getVisitorTeamGoal() {
        return visitorTeamGoal;
    }

    public void setVisitorTeamGoal(Goal visitorTeamGoal) {
        this.visitorTeamGoal = visitorTeamGoal;
    }

    public abstract void endGame();

    public long getElapsedTime() {
        return elapsedTime;
    }

}
