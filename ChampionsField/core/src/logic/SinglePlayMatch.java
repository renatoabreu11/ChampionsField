package logic;

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

    public void switchPlayer(){
        if(homeTeam.switchPlayer(ball.position))
            return;
        else visitorTeam.switchPlayer(ball.position);
    }

    @Override
    public void teamScored(Team defendingTeam, Team attackingTeam ){
        defendingTeam.goalScored();
        defendingTeam.teamState = Team.TeamState.Defending;
        attackingTeam.teamState = Team.TeamState.Attacking;
        currentState = matchState.Score;
    }

    @Override
    public void updateMatch(float x, float y, Rain rain, float dt) {
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
                homeTeam.updatePlayers(dt, ball);
                visitorTeam.updatePlayers(dt, ball);
                break;
            }
            case Play: {
                homeTeam.updateControlledPlayer(x, y);
                homeTeam.updatePlayers(dt, ball);
                visitorTeam.updatePlayers(dt, ball);
                break;
            }
            case Score:{
                ball.body.setAwake(false);
                break;
            }
        }

        rain.update();
        w.step(Constants.GAME_SIMULATION_SPEED, 6, 2);
    }

}
