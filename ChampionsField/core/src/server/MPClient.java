package server;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;

import States.GameStateManager;
import logic.Match;

public class MPClient {
    static final int TIME_OUT = 5000;
    Client client;

    //0 --> homeTeam, 1 --> visitorTeam
    public MPClient(String name, int team, GameStateManager gsm) {
        client = new Client();
        client.start();

        Network.registerPackets(client);
        addListeners();

        try {
            client.connect(TIME_OUT, "127.0.0.1", Network.PORT);
        } catch (IOException e) {
            e.printStackTrace();
            client.stop();
        }

        Network.Login login = new Network.Login();
        login.x = 1;
        login.y = 1;
        login.name = name;
        login.size = Match.PLAYER_SIZE;
        login.team = team;

        client.sendTCP(login);

        while(true) {

        }
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

            }
        }));
    }
}
