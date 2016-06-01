package logic;

import java.util.ArrayList;
import java.util.Random;

public class MultiPlayMatch extends Match{
    public enum matchState{
        KickOff,
        Pause,
        Score,
        Play;
    }

    public MultiPlayMatch(){
        super(0);

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

    public void addPlayerToMatch(ArrayList<Player> players) {
        for(Player player : players) {
            if(player.team == 0)
                homeTeam.addPlayer(player);
            else
                visitorTeam.addPlayer(player);
        }
    }

    @Override
    public void teamScored(Team defendingTeam, Team attackingTeam ){
        defendingTeam.goalScored();
        defendingTeam.teamState = Team.TeamState.Defending;
        attackingTeam.teamState = Team.TeamState.Attacking;
        currentState = Match.matchState.Score;
    }

    @Override
    public void updateMatch(float x, float y, Rain rain, float dt) {

    }

    @Override
    public void endScoreState() {

    }
}
