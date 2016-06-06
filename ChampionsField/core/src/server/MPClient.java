package server;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

import logic.*;
import utils.Constants;

public class MPClient {
    static final int TIME_OUT = 5000;
    Client client;
    MultiPlayMatch match;
    private String name;
    private float INTERVAL_BETWEEN_PACKETS = 5000f;
    private float dt = INTERVAL_BETWEEN_PACKETS;

    /*
    * A player can have the same name only if in different teams!
    * */
    public MPClient(String name, int team, MultiPlayMatch match) {
        this.match = match;
        client = new Client();
        client.start();
        this.name = name;

        Network.registerPackets(client);
        addListeners();

        try {
            client.connect(TIME_OUT, Network.IPV4_ESPOSENDE, Network.PORT);
        } catch (IOException e) {
            e.printStackTrace();
            client.stop();
        }

        Network.Login login = new Network.Login();
        login.name = name;
        login.team = team;
        client.sendTCP(login);

        PlayerInfo playerInfo = new PlayerInfo();
        Network.UpdatePlayer updatePlayer = new Network.UpdatePlayer();
        updatePlayer.name = name;
        updatePlayer.team = team;
        Network.UpdateBall updateBall = new Network.UpdateBall();

        //Test when game ends!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        while(true) {
            //Checks if game ended
            if(match.getElapsedTime() >= Constants.GAME_TIME)
                break;

            //Updates ball
            if(match.ballMoved) {
                match.ballMoved = false;
                updateBall.x = match.getBall().getBody().getTransform().getPosition().x;
                updateBall.y = match.getBall().getBody().getTransform().getPosition().y;
                updateBall.vx = match.getBall().getBody().getLinearVelocity().x;
                updateBall.vy = match.getBall().getBody().getLinearVelocity().y;
                updateBall.lastTouch = match.getBall().getLastTouch();
                client.sendTCP(updateBall);
            }

            //Updates player
            if(match.controlledPlayerMoved) {
                match.controlledPlayerMoved = false;
                playerInfo.x = match.getClientPlayerX(team);
                playerInfo.y = match.getClientPlayerY(team);

                updatePlayer.x = playerInfo.x;
                updatePlayer.y = playerInfo.y;
                client.sendTCP(updatePlayer);
            }
        }

        Network.RemovePlayer removePlayer = new Network.RemovePlayer();
        removePlayer.team = team;
        removePlayer.name = name;
        client.sendTCP(removePlayer);
    }

    private void addListeners() {
        client.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                Log.info("[MPClient]: You are connected");
            }

            @Override
            public void disconnected(Connection connection) {
                Log.info("[MPClient]: You are trying to disconnect...");
            }

            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof Network.AddPlayer) {
                    Network.AddPlayer addPlayer = (Network.AddPlayer) object;
                    match.addPlayerToMatch(addPlayer.name, addPlayer.team, addPlayer.controlledPlayer, addPlayer.barrierSide);
                }

                if(object instanceof Network.UpdatePlayer) {
                    Network.UpdatePlayer updatePlayer = (Network.UpdatePlayer) object;
                    match.setPlayerPosition(updatePlayer.x, updatePlayer.y, updatePlayer.name, updatePlayer.team);
                }

                if(object instanceof Network.RemovePlayer) {
                    Network.RemovePlayer removePlayer = (Network.RemovePlayer) object;
                    match.removePlayerFromMatch(removePlayer.name, removePlayer.team);
                }

                if(object instanceof Network.UpdateBall) {
                    Network.UpdateBall updateBall = (Network.UpdateBall) object;
                    match.setBallPosition(updateBall.x, updateBall.y, updateBall.vx, updateBall.vy, updateBall.lastTouch);
                }

            }
        }));
    }
}