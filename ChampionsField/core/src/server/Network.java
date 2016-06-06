package server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
    static public final int PORT = 54555;
    static public final String LOCAL_IP = "127.0.0.1";
    static public final String IPV4_ALPENDORADA = "192.168.1.105";
    static public final String IPV4_PORTO = "192.168.0.104";

    public static void registerPackets(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Login.class);
        kryo.register(AddPlayer.class);
        kryo.register(UpdatePlayer.class);
        kryo.register(UpdateBall.class);
        kryo.register(RemovePlayer.class);
    }

    static public class Login {
        String name;
        int team;
    }

    static public class AddPlayer {
        String name;
        int team;
        boolean controlledPlayer;
        boolean barrierSide;
    }

    static public class UpdatePlayer {
        public String name;
        float x, y;
        int team;
    }

    static public class UpdateBall {
        float x, y;
        String name;
    }

    static public class RemovePlayer {
        int team;
        String name;
    }
}
