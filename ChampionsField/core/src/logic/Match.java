package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import utils.Constants;

public abstract class Match{
    public World getWorld() {
        return w;
    }

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
    }

    private void createCollisionListener() {
        w.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture f1 = contact.getFixtureA();
                Fixture f2 = contact.getFixtureB();

                if((f1.getUserData() == "HomeGoal" || f1.getUserData() == "Ball") && (f2.getUserData() == "HomeGoal" || f2.getUserData() == "Ball"))
                    teamScored(visitorTeam, homeTeam);
                else if((f1.getUserData() == "VisitorGoal" || f1.getUserData() == "Ball") && (f2.getUserData() == "VisitorGoal" || f2.getUserData() == "Ball"))
                    teamScored(homeTeam, visitorTeam);
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

    public abstract void teamScored(Team defendingTeam, Team attackingTeam);

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
}
