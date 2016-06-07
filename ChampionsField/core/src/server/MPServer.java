package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.io.IOException;
import java.util.ArrayList;

import utils.Constants;

public class MPServer {
    Server server;
    ArrayList<MatchInfo> matches;

    public MPServer() throws IOException {
        matches = new ArrayList<MatchInfo>();
        for(int i = 0; i < Constants.NUMBER_MATCHES_HOST_BY_SERVER; i++)
            matches.add(new MatchInfo());
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
                    MatchInfo match = matches.get(login.room);

                    //Checks to see if every team has the right number of players
                    int numPlayerHome = 0;
                    int numPlayersVisitor = 0;
                    for(PlayerInfo playerInfo : match.playersInfo) {
                        if(playerInfo.team == 0)
                            numPlayerHome++;
                        else
                            numPlayersVisitor++;
                    }

                    if((login.team == 0 && numPlayerHome < Constants.NUMBER_PLAYER_ONLINE)
                            || (login.team == 1 && numPlayersVisitor < Constants.NUMBER_PLAYER_ONLINE)) {

                        match.playersInfo.add(new PlayerInfo(login.team, login.name));
                        match.connections.add(c);

                        //Sends the player's info to the new created match, so that match will have the existing players already
                        for (int i = 0; i < match.numPlayers; i++) {
                            Network.AddPlayer addPlayer = new Network.AddPlayer();
                            addPlayer.team = match.playersInfo.get(i).team;
                            addPlayer.name = match.playersInfo.get(i).name;
                            addPlayer.controlledPlayer = false;
                            addPlayer.barrierSide = match.barrierSide;
                            addPlayer.room = login.room;
                            c.sendTCP(addPlayer);
                        }

                        match.numPlayers++;

                        //Adds the new player to the match in all devices
                        Network.AddPlayer addPlayer = new Network.AddPlayer();
                        addPlayer.name = login.name;
                        addPlayer.team = login.team;
                        addPlayer.controlledPlayer = true;
                        addPlayer.barrierSide = match.barrierSide;
                        addPlayer.room = login.room;
                        for(Connection connection : match.connections)
                            connection.sendTCP(addPlayer);
                    } else {
                        c.sendTCP(new Network.MatchFull());
                    }
                }

                if(object instanceof Network.UpdatePlayer) {
                    Network.UpdatePlayer updatePlayer = (Network.UpdatePlayer) object;
                    MatchInfo match = matches.get(updatePlayer.room);

                    for(Connection connection : match.connections)
                        connection.sendTCP(updatePlayer);
                }

                if(object instanceof Network.RemovePlayer) {
                    Network.RemovePlayer removePlayer = (Network.RemovePlayer) object;
                    MatchInfo match = matches.get(removePlayer.room);

                    for(PlayerInfo playerInfo : match.playersInfo) {
                        if(playerInfo.team == removePlayer.team && playerInfo.name.equals(removePlayer.name)) {
                            match.playersInfo.remove(playerInfo);
                            break;
                        }
                    }

                    //Removes the player from the other client's matches
                    for(Connection connection : match.connections)
                        connection.sendTCP(removePlayer);
                    match.numPlayers--;
                }

                if(object instanceof Network.UpdateBall) {
                    Network.UpdateBall updateBall = (Network.UpdateBall) object;
                    MatchInfo match = matches.get(updateBall.room);

                    for(Connection connection : match.connections)
                        connection.sendTCP(updateBall);
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
