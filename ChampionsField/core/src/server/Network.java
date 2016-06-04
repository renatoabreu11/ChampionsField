package server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
    static public final int PORT = 54555;

    public static void registerPackets(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Login.class);
        kryo.register(AddPlayer.class);
        kryo.register(UpdatePlayer.class);
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
    }

    static public class UpdatePlayer {
        public String name;
        float x, y;
        int team;
    }

    static public class RemovePlayer {

    }
}
