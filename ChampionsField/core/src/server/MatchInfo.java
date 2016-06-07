package server;

import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.Random;

public class MatchInfo {
    int numPlayers;
    boolean barrierSide;
    ArrayList<PlayerInfo> playersInfo;
    ArrayList<Connection> connections;

    public MatchInfo() {
        playersInfo = new ArrayList<PlayerInfo>();
        connections = new ArrayList<Connection>();
        numPlayers = 0;

        Random r = new Random();
        if(r.nextInt(2) == 0)
            barrierSide = true;
        else
            barrierSide = false;
    }
}
