package server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
    static public final int PORT = 54555;
    static public final String LOCAL_IP = "127.0.0.1";
    static public final String IPV4_ALPENDORADA = "192.168.1.105";
    static public final String IPV4_PORTO = "192.168.0.105";
    static public final String IPV4_ESPOSENDE = "192.168.56.1";

    public static void registerPackets(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Login.class);
        kryo.register(AddPlayer.class);
        kryo.register(UpdatePlayer.class);
        kryo.register(UpdateBall.class);
        kryo.register(RemovePlayer.class);
        kryo.register(MatchFull.class);
    }

    static public class Login {
        String name;
        int team;
        int room;
    }

    static public class AddPlayer {
        String name;
        int team;
        boolean controlledPlayer;
        boolean barrierSide;
        int room;
    }

    static public class UpdatePlayer {
        public String name;
        float x, y;
        int team;
        int room;
    }

    static public class UpdateBall {
        float x, y, vx, vy;
        String lastTouch;
        int room;
    }

    static public class RemovePlayer {
        int team;
        String name;
        int room;
    }

    static public class MatchFull {
        int room;
    }
}
