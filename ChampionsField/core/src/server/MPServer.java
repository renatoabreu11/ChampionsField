package server;

import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import logic.Player;

public class MPServer {
    ArrayList<Player> playersLoggedIn;
    Server server;

    public MPServer() throws IOException {
        playersLoggedIn = new ArrayList<Player>();
        server = new Server() {
            //Since we're implementing our own connection, we can store per
            //connection state without a connection ID to look up
            protected Connection newConnection() {
                return new PlayerConnection();
            }
        };

        Network.registerPackets(server);
        addListeners();
        server.bind(Network.PORT);
        server.start();
    }

    private void addListeners() {
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                Log.info("[SERVER]: Someone is trying to connect...");
            }

            @Override
            public void disconnected(Connection connection) {
                Log.info("[SERVER]: Someone is trying to disconnect...");
            }

            @Override
            public void received(Connection c, Object object) {
                //All connection for this server are PlayerConnections
                PlayerConnection connection = (PlayerConnection) c;
                Player player = connection.player;

                if(object instanceof Network.Login) {
                    Network.Login login = (Network.Login) object;

                    player = new Player(login.x, login.y, login.name, login.size, true);
                    loggedIn(connection, player);
                }
            }
        });
    }

    void loggedIn(PlayerConnection c, Player player) {
        c.player = player;

        //Adds all the current existing players to the new player
        for(int i = 0; i < playersLoggedIn.size(); i++) {
            Network.AddPlayer addPlayer = new Network.AddPlayer();
            addPlayer.player = playersLoggedIn.get(i);
            c.sendTCP(addPlayer);
        }

        playersLoggedIn.add(player);

        //Adds the new player to the current existing players
        Network.AddPlayer addPlayer = new Network.AddPlayer();
        addPlayer.player = player;
        server.sendToAllTCP(addPlayer);
    }

    static class PlayerConnection extends Connection {
        public Player player;
    }

    public static void main(String[] args) {
        try {
            new MPServer();
            Log.set(Log.LEVEL_DEBUG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
