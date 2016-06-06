package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.ArrayList;

import utils.Constants;

public class MPServer {
    Server server;
    ArrayList<PlayerInfo> playersInfo;
    int numPlayers;

    public MPServer() throws IOException {
        numPlayers = 0;
        playersInfo = new ArrayList<PlayerInfo>();
        server = new Server();

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
                if (object instanceof Network.Login) {
                    Network.Login login = (Network.Login) object;

                    //Checks to see if every team has the right number of players
                    int numPlayerHome = 0;
                    int numPlayersVisitor = 0;
                    for(PlayerInfo playerInfo : playersInfo) {
                        if(playerInfo.team == 0)
                            numPlayerHome++;
                        else
                            numPlayersVisitor++;
                    }

                    if((login.team == 0 && numPlayerHome < Constants.NUMBER_PLAYER_ONLINE)
                            || (login.team == 1 && numPlayersVisitor < Constants.NUMBER_PLAYER_ONLINE)) {

                        playersInfo.add(new PlayerInfo(login.team, login.name));

                        //Sends the player's info to the new created match, so that match will have the existing players already
                        for (int i = 0; i < numPlayers; i++) {
                            Network.AddPlayer addPlayer = new Network.AddPlayer();
                            addPlayer.team = playersInfo.get(i).team;
                            addPlayer.name = playersInfo.get(i).name;
                            addPlayer.controlledPlayer = false;
                            c.sendTCP(addPlayer);
                        }

                        numPlayers++;

                        //Adds the new player to the match in all devices
                        Network.AddPlayer addPlayer = new Network.AddPlayer();
                        addPlayer.name = login.name;
                        addPlayer.team = login.team;
                        addPlayer.controlledPlayer = true;
                        server.sendToAllTCP(addPlayer);
                    } else {
                        //Match is full!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    }
                }

                if(object instanceof Network.UpdatePlayer) {
                    Network.UpdatePlayer updatePlayer = (Network.UpdatePlayer) object;
                    server.sendToAllTCP(updatePlayer);
                }

                if(object instanceof Network.RemovePlayer) {
                    Network.RemovePlayer removePlayer = (Network.RemovePlayer) object;

                    for(PlayerInfo playerInfo : playersInfo) {
                        if(playerInfo.team == removePlayer.team && playerInfo.name.equals(removePlayer.name)) {
                            playersInfo.remove(playerInfo);
                            break;
                        }
                    }

                    //Removes the player from the other client's matches
                    server.sendToAllTCP(removePlayer);
                    numPlayers--;
                }
            }
        });
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
