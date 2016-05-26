package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

public class Match{

    public enum entityMasks{
        BallMask(1),
        PlayerMask(2),
        FieldBordersMask(4),
        GoalMask(8),
        ScreenBordersMask(16),
        FootballGoalMask(32);

        private final short mask;
        entityMasks(int mask){
            this.mask = (short)mask;
        }
        public short getMask(){
            return this.mask;
        }
    };

    private Field field;
    private Team homeTeam;
    private Team visitorTeam;
    private Goal homeTeamGoal;
    private Goal visitorTeamGoal;

    private Ball ball;
    private int numberOfPlayers;

    public Match(int playersSize, int numberOfPlayers, World w){
        Random r = new Random();
        int aux = r.nextInt(2);
        if(aux == 0){
            homeTeam = new Team(numberOfPlayers, playersSize, "Benfica", Team.TeamState.Attacking, w);
            visitorTeam = new Team(numberOfPlayers, playersSize, "Porto", Team.TeamState.Defending, w);
        } else{
            homeTeam = new Team(numberOfPlayers, playersSize, "Benfica", Team.TeamState.Defending, w);
            visitorTeam = new Team(numberOfPlayers, playersSize, "Porto", Team.TeamState.Attacking, w);
        }
        float widthScale = Gdx.graphics.getWidth() / 2560f;
        float heightScale = Gdx.graphics.getHeight() /  1600f;
        this.numberOfPlayers = numberOfPlayers;
        ball = new Ball(0, 0, 24, w);
        field = new Field(w);
        homeTeam.controlPlayer(0);
        homeTeamGoal = new Goal(-Gdx.graphics.getWidth()/2 + 30f * widthScale, 0, 500f * heightScale, 100f * widthScale,  w, "HomeGoal");
        visitorTeamGoal = new Goal(Gdx.graphics.getWidth()/2 - 30f * widthScale, 0, 500f * heightScale, 100f * widthScale,  w, "VisitorGoal");
    }

    public void updateMatch(float dt) {
        homeTeam.updateControlledPlayer();
    }

    public void updateMatch(float x, float y){
        homeTeam.updateControlledPlayer(x, y);
        visitorTeam.updateControlledPlayer(x, y);
        homeTeam.updatePlayers();
        visitorTeam.updatePlayers();
    }

    public void teamScored(Team t) {
        t.goalScored();
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
