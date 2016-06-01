package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Random;

public class Match{

    static final float BALL_SIZE = 48;
    public static final float PLAYER_SIZE = 60;
    static final float FIELD_TEXTURE_WIDTH = 2560;
    static final float FIELD_TEXTURE_HEIGHT = 1600;
    static final float GAME_SIMULATION_SPEED = 1 / 60f;

    public enum entityMasks{
        BallMask(1),
        PlayerMask(2),
        FieldBordersMask(4),
        GoalMask(8),
        ScreenBordersMask(16),
        FootballGoalMask(32),
        CenterMask(64);

        private final short mask;
        entityMasks(int mask){
            this.mask = (short)mask;
        }
        public short getMask(){
            return this.mask;
        }
    };

    public enum matchState{
        KickOff,
        Pause,
        Score,
        Play;
    }

    private Field field;
    private Team homeTeam;
    private Team visitorTeam;
    private Goal homeTeamGoal;
    private Goal visitorTeamGoal;
    private Ball ball;
    private World w;


    private int numberOfPlayers;
    private matchState currentState;

    public Match(int numberOfPlayers){
        //Physics World
        Vector2 gravity = new Vector2(0, 0f);
        w = new World(gravity, true);
        createCollisionListener();

        float playerSize = (PLAYER_SIZE * 100 / 1920) * Gdx.graphics.getWidth() / 100;

        Random r = new Random();
        int aux = r.nextInt(2);     //MUDAR AQUI, SO PARA QUESTOES DE DEBUG! :D
        aux = 0;
        if(aux == 0){
            homeTeam = new Team(numberOfPlayers, playerSize, "Benfica", Team.TeamState.Attacking, w);
            visitorTeam = new Team(numberOfPlayers, playerSize, "Porto", Team.TeamState.Defending, w);
        } else{
            homeTeam = new Team(numberOfPlayers, playerSize, "Benfica", Team.TeamState.Defending, w);
            visitorTeam = new Team(numberOfPlayers, playerSize, "Porto", Team.TeamState.Attacking, w);
        }
        float widthScale = Gdx.graphics.getWidth() / FIELD_TEXTURE_WIDTH;
        float heightScale = Gdx.graphics.getHeight() / FIELD_TEXTURE_HEIGHT;
        this.numberOfPlayers = numberOfPlayers;
        ball = new Ball(0, 0, (BALL_SIZE * 100 / 1920) * Gdx.graphics.getWidth() / 100, w);
        field = new Field(w);
        homeTeam.controlPlayer(0);
        homeTeamGoal = new Goal(-Gdx.graphics.getWidth()/2 + 30f * widthScale, 0, 500f * heightScale, 100f * widthScale,  w, "HomeGoal");
        visitorTeamGoal = new Goal(Gdx.graphics.getWidth()/2 - 30f * widthScale, 0, 500f * heightScale, 100f * widthScale,  w, "VisitorGoal");
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

    public void teamScored(Team defendingTeam, Team attackingTeam ){
        defendingTeam.goalScored();
        defendingTeam.teamState = Team.TeamState.Defending;
        attackingTeam.teamState = Team.TeamState.Attacking;
        currentState = matchState.Score;
    }


    public matchState getCurrentState() {
        return currentState;
    }

    public void updateMatch(float x, float y, Rain rain){
        switch (currentState) {
            case KickOff: {
                if(homeTeam.getTeamState() == Team.TeamState.Attacking)
                    field.activateBarriers(true);
                else field.activateBarriers(false);
                if (ball.body.getPosition().x != 0 || ball.body.getPosition().y != 0) {
                    field.deactivateBarriers();
                    homeTeam.teamState = Team.TeamState.Playing;
                    visitorTeam.teamState = Team.TeamState.Playing;
                    currentState = matchState.Play;
                }
                homeTeam.updateControlledPlayer(x, y);
                break;
            }
            case Play: {
                homeTeam.updateControlledPlayer(x, y);
                break;
            }
            case Score:{
                ball.body.setAwake(false);
                break;
            }
        }

        rain.update();
        w.step(GAME_SIMULATION_SPEED, 6, 2);
    }

    public void endScoreState(){
        currentState = matchState.KickOff;
        homeTeam.repositionTeam();
        visitorTeam.repositionTeam();
        ball.reposition();
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

    /*
    * BEGIN OF THE MULTIPLAYER FUNCTIONS
    * */

    public Match(){
        //Physics World
        Vector2 gravity = new Vector2(0, 0f);
        w = new World(gravity, true);
        createCollisionListener();

        float playerSize = (PLAYER_SIZE * 100 / 1920) * Gdx.graphics.getWidth() / 100;

        Random r = new Random();
        int aux = r.nextInt(2);     //MUDAR AQUI, SO PARA QUESTOES DE DEBUG! :D
        aux = 0;
        if(aux == 0){
            homeTeam = new Team(numberOfPlayers, playerSize, "Benfica", Team.TeamState.Attacking, w);
            visitorTeam = new Team(numberOfPlayers, playerSize, "Porto", Team.TeamState.Defending, w);
        } else{
            homeTeam = new Team(numberOfPlayers, playerSize, "Benfica", Team.TeamState.Defending, w);
            visitorTeam = new Team(numberOfPlayers, playerSize, "Porto", Team.TeamState.Attacking, w);
        }
        float widthScale = Gdx.graphics.getWidth() / FIELD_TEXTURE_WIDTH;
        float heightScale = Gdx.graphics.getHeight() / FIELD_TEXTURE_HEIGHT;

        ball = new Ball(0, 0, (BALL_SIZE * 100 / 1920) * Gdx.graphics.getWidth() / 100, w);
        field = new Field(w);
        homeTeam.controlPlayer(0);
        homeTeamGoal = new Goal(-Gdx.graphics.getWidth()/2 + 30f * widthScale, 0, 500f * heightScale, 100f * widthScale,  w, "HomeGoal");
        visitorTeamGoal = new Goal(Gdx.graphics.getWidth()/2 - 30f * widthScale, 0, 500f * heightScale, 100f * widthScale,  w, "VisitorGoal");
        currentState = matchState.KickOff;
    }

    public void addPlayerToMatch(ArrayList<Player> players) {
        for(Player player : players) {
            if(player.team == 0)
                homeTeam.addPlayer(player);
            else
                visitorTeam.addPlayer(player);
        }
    }

    /*
    * END OF THE MULTIPLAYER FUNCTIONS
    * */
}
