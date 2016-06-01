package server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import logic.Player;

public class Network {
    static public final int PORT = 54555;

    public static void registerPackets(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Login.class);
        kryo.register(UpdatePlayer.class);
        kryo.register(AddPlayer.class);
        kryo.register(RemovePlayer.class);

        kryo.register(logic.Player.class);
        kryo.register(com.badlogic.gdx.math.Vector2.class);
    }

    static public class Login {
        float x, y;
        String name;
        float size;
        int team;
    }

    static public class UpdatePlayer {
        public String name;
        public float newX, newY;
    }

    static public class AddPlayer {
        public Player player;
    }

    static public class RemovePlayer {

    }
}
