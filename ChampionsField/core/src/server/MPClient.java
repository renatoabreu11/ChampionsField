package server;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

import States.PlayState;
import logic.Match;

public class MPClient {
    static final int TIME_OUT = 5000;
    Client client;

    //0 --> homeTeam, 1 --> visitorTeam
    public MPClient(PlayState playState, int team) {
        client = new Client();
        client.start();

        Network.registerPackets(client);

        Network.Login login = new Network.Login();
        Vector2 position;
        position = playState.match.getNextClientPosition(team);
        login.x = position.x;
        login.y = position.y;
        login.name = "1";
        login.size = Match.PLAYER_SIZE;
        login.controlledPlayer = true;

        addListeners(playState, login.name, team);

        try {
            client.connect(TIME_OUT, "127.0.0.1", Network.PORT);
        } catch (IOException e) {
            e.printStackTrace();
            client.stop();
        }

        client.sendTCP(login);

        while(true) {

        }
    }

    private void addListeners(final PlayState playState, final String playerName, final int team) {
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
                /*if(object instanceof Network.ActivatePlayerPhysics) {
                    playState.match.activateNextClientPhysics(playerName, team);
                }*/
            }
        }));
    }

    /*public static void main(String[] args) {
        Log.set(Log.LEVEL_DEBUG);
        new MPClient();
    }*/
}
