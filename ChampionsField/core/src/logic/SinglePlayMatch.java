package logic;

import com.badlogic.gdx.math.Circle;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import utils.Constants;

public class SinglePlayMatch extends Match{

    PowerUp powerUp;

    public SinglePlayMatch(int numberOfPlayers) {
        super(numberOfPlayers);

        Random r = new Random();
        int aux = r.nextInt(2);
        aux = 0;
        if(aux == 0){
            homeTeam = new Team(numberOfPlayers, playerSize, "Benfica", Team.TeamState.Attacking, w);
            visitorTeam = new Team(numberOfPlayers, playerSize, "Porto", Team.TeamState.Defending, w);
        } else{
            homeTeam = new Team(numberOfPlayers, playerSize, "Benfica", Team.TeamState.Defending, w);
            visitorTeam = new Team(numberOfPlayers, playerSize, "Porto", Team.TeamState.Attacking, w);
        }
        homeTeam.controlPlayer(0);

        powerUp = new PowerUp();
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

    public PowerUp getPowerUp() {
        return powerUp;
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
        if(powerUp.isActive()){
            Circle c = null;
            int team = -1;
            int playerIndex = -1;
            boolean caught = false;
            for(int i = 0; i < homeTeam.getNumberPlayers(); i++){
                Player p = homeTeam.players.get(i);
                c = new Circle(p.getPosition(), p.radius);
                if(c.contains(powerUp.getPosition())){
                    playerIndex = i;
                    team = 0;
                    caught = true;
                }
            }

            if(!caught) {
                for (int i = 0; i < visitorTeam.getNumberPlayers(); i++) {
                    Player p = visitorTeam.players.get(i);
                    c = new Circle(p.getPosition(), p.radius);
                    if (c.contains(powerUp.getPosition())) {
                        playerIndex = i;
                        team = 1;
                    }
                }
            }
            if(caught) {
                Constants.powerUpType type = powerUp.getType();
                switch (type) {
                    case TeamSpeedInc:
                        if (team == 0)
                            homeTeam.applyPowerUp(1.01f);
                        else visitorTeam.applyPowerUp(1.01f);
                        break;
                    case TeamSpeedDec:
                        if (team == 0)
                            visitorTeam.applyPowerUp(0.95f);
                        else homeTeam.applyPowerUp(0.95f);
                        break;
                    case PlayerSpeedInc:
                        if (team == 0) {
                            homeTeam.getPlayers().get(playerIndex).speedMultiplier = 1.05f;
                            homeTeam.getPlayers().get(playerIndex).powerActivated = true;
                            homeTeam.getPlayers().get(playerIndex).activeTime = 0;
                        } else {
                            visitorTeam.getPlayers().get(playerIndex).speedMultiplier = 1.05f;
                            visitorTeam.getPlayers().get(playerIndex).powerActivated = true;
                            visitorTeam.getPlayers().get(playerIndex).activeTime = 0;
                        }
                        break;
                }
                powerUp.setActive(false);
            }
        }

        switch (currentState) {
            case KickOff: {
                ball.body.setAwake(true);
                homeTeam.removePowerUps();
                visitorTeam.removePowerUps();
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

        if(!powerUp.isActive()){
            powerUp.checkPowerUpAppearance(elapsedTime);
        }

        rain.update();
        w.step(Constants.GAME_SIMULATION_SPEED, 6, 2);

        elapsedTime = ((System.currentTimeMillis() - startTime) / 1000);
        time = Constants.formatter.format(new Date(elapsedTime * 1000L));
    }
}
