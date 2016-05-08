package logic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class Match {

    private Field field;
    private Team homeTeam;
    private Team visitorTeam;
    private Ball ball;

    public Match(float fieldWidth, float fieldHeight, float playersSize, int numberOfPlayers){
        field = new Field("Field.png", fieldWidth, fieldHeight);
        Random r = new Random();
        int aux = r.nextInt(2);
        if(aux == 0){
            homeTeam = new Team(numberOfPlayers, playersSize, "Benfica", Team.TeamState.Attacking, fieldHeight, fieldWidth);
            visitorTeam = new Team(numberOfPlayers, playersSize, "Porto", Team.TeamState.Defending, fieldHeight, fieldWidth);
        } else{
            homeTeam = new Team(numberOfPlayers, playersSize, "Benfica", Team.TeamState.Defending, fieldHeight, fieldWidth);
            visitorTeam = new Team(numberOfPlayers, playersSize, "Porto", Team.TeamState.Attacking, fieldHeight, fieldWidth);
        }
        ball = new Ball(fieldWidth, fieldHeight, 20);
        homeTeam.controlPlayer(0);
    }

    public void updateMatch(float x, float y){
        homeTeam.updateControlledPlayer(x, y);
        visitorTeam.updateControlledPlayer(x, y);
        homeTeam.updatePlayers();
        visitorTeam.updatePlayers();
        checkFieldCollisions(homeTeam);
        checkFieldCollisions(visitorTeam);
        checkHomeTeamCollisions();
        checkVisitorTeamCollisions();
        checkBallCollisions();
    }

    void checkFieldCollisions(Team t) {
        Player player = null;
        double fieldLeft, fieldRight, fieldTop, fieldBottom;
        for(int i = 0; i < t.getNumberPlayers(); i++){
            player = t.players.get(i);
            fieldLeft = player.getCollider().x - player.getRadius();
            fieldRight = field.width - player.getCollider().x - player.getRadius();
            fieldTop = player.getCollider().y - player.getRadius();
            fieldBottom = field.height - player.getCollider().y - player.getRadius();

            if (fieldLeft < 0)
                player.setPosition(0, player.position.y);
            if(fieldRight < 0)
                player.setPosition(field.width - player.getRadius() * 2, player.position.y);
            if(fieldTop < 0)
                player.setPosition(player.position.x, 0);
            if(fieldBottom < 0)
                player.setPosition(player.position.x,field.height - player.getRadius() * 2);
        }
    }

    void checkHomeTeamCollisions() {
        for(int i = 0; i < homeTeam.players.size(); i++) {
            for(int j = 0; j < visitorTeam.players.size(); j++){
                if (homeTeam.players.get(i).getCollider().overlaps(visitorTeam.players.get(j).getCollider())){
                    homeTeam.players.get(i).setLastPosition();
                    visitorTeam.players.get(j).setLastPosition();
                }
            }
        }
    }

    void checkVisitorTeamCollisions() {
        for(int i = 0; i < visitorTeam.players.size(); i++) {
            for(int j = 0; j < homeTeam.players.size(); j++){
                if (visitorTeam.players.get(i).getCollider().overlaps(homeTeam.players.get(j).getCollider())){
                    visitorTeam.players.get(j).setLastPosition();
                }
            }
        }
    }

    void checkBallCollisions() {
        for(int i = 0; i < homeTeam.players.size(); i++) {
            if(homeTeam.players.get(i).getCollider().overlaps(ball.getCollider())) {
                System.out.println("Posicao inicial: x = " + homeTeam.players.get(i).position.x + ", y = " + homeTeam.players.get(i).position.y + "\n");
                homeTeam.players.get(i).setLastPosition();
                System.out.println("Posicao actual: x = " + homeTeam.players.get(i).position.x + ", y = " + homeTeam.players.get(i).position.y + "\n\n");
            }
        }

        for(int i = 0; i < visitorTeam.players.size(); i++) {
            if(visitorTeam.players.get(i).getCollider().overlaps(ball.getCollider()))
                visitorTeam.players.get(i).setLastPosition();
        }
    }

    public void render(SpriteBatch sb) {
        sb.draw(field.getTexture(), 0, 0, field.width, field.height);
        homeTeam.render(sb);
        visitorTeam.render(sb);
        ball.render(sb);
    }

}
