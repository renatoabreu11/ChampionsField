package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import utils.Constants;

public class SinglePlayMatch extends Match{

    public SinglePlayMatch(int numberOfPlayers) {
        super(numberOfPlayers);

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
        homeTeam.controlPlayer(0);
    }

    @Override
    public void endScoreState() {
        currentState = matchState.KickOff;
        homeTeam.repositionTeam();
        visitorTeam.repositionTeam();
        ball.reposition();
        switchPlayer();
    }

    @Override
    public void endGame() {

    }

    public void switchPlayer(){
        if(homeTeam.switchPlayer(ball.position))
            return;
        else visitorTeam.switchPlayer(ball.position);
    }

    @Override
    public void teamScored(Team defendingTeam, Team attackingTeam, String lastTouch){
        currentState = matchState.Score;
        ArrayList<String> attackingTeamNames = attackingTeam.getPlayerNames();

        //auto goal
        if(attackingTeamNames.contains(lastTouch)){
            attackingTeam.autoGoal(lastTouch);
            defendingTeam.score++;
        } else defendingTeam.goalScored(lastTouch);

        defendingTeam.teamState = Team.TeamState.Defending;
        attackingTeam.teamState = Team.TeamState.Attacking;
    }

    @Override
    public void updateMatch(float x, float y, Rain rain, float dt) {
        switch (currentState) {
            case KickOff: {
                ball.body.setAwake(true);
                if(homeTeam.getTeamState() == Team.TeamState.Attacking)
                    field.activateBarriers(true);
                else field.activateBarriers(false);
                if (ball.body.getPosition().x != 0 || ball.body.getPosition().y != 0) {
                    field.deactivateBarriers();
                homeTeam.teamState = Team.TeamState.Playing;
                visitorTeam.teamState = Team.TeamState.Playing;
                currentState = matchState.Play;
            }
            homeTeam.initWayPoints(ball, visitorTeam);
            visitorTeam.initWayPoints(ball, homeTeam);
            homeTeam.updateControlledPlayer(x, y);
            homeTeam.updatePlayers(dt);
            visitorTeam.updatePlayers(dt);
            break;
        }
        case Play: {
            homeTeam.updateControlledPlayer(x, y);
            homeTeam.updatePlayers(dt, ball, visitorTeam);
            visitorTeam.updatePlayers(dt, ball, homeTeam);
            break;
        }
        case Score:{
                break;
            }
        }

        rain.update();
        w.step(Constants.GAME_SIMULATION_SPEED, 6, 2);

        elapsedTime = ((System.currentTimeMillis() - startTime) / 1000);
        time = Constants.formatter.format(new Date(elapsedTime * 1000L));
    }
}
