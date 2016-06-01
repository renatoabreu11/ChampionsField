package server;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import logic.Player;

public class Network {
    static public final int PORT = 54555;

    public static void registerPackets(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(Login.class);
        kryo.register(ActivatePlayerPhysics.class);
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
        boolean controlledPlayer;
    }

    static public class ActivatePlayerPhysics {

    }

    static public class UpdatePlayer {
        public String name;
        public Vector2 updatedPosition;
    }

    static public class AddPlayer {
        public Player player;
    }

    static public class RemovePlayer {

    }
}
