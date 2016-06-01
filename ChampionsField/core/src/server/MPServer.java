package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.ArrayList;

import logic.Match;
import logic.MultiPlayMatch;
import logic.Player;

public class MPServer {
    Server server;
    Match match;

    ArrayList<Player> playersLoggedIn;
    int numNewPlayers;

    public MPServer() throws IOException {
        playersLoggedIn = new ArrayList<Player>();
        numNewPlayers = 0;
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

                if (object instanceof Network.Login) {
                    Network.Login login = (Network.Login) object;

                    player = new Player(login.x, login.y, login.name, login.size, login.team);
                    loggedIn(connection, player);

                    numNewPlayers++;
                    if (numNewPlayers == 2) {
                        match = new MultiPlayMatch();
                        //playState.match.addPlayerToMatch(playersLoggedIn);
                    }
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
