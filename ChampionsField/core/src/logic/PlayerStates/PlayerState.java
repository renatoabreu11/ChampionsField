package logic.PlayerStates;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

import logic.Player;

public enum PlayerState implements State<Player>{
    ;

    @Override
    public void enter(Player entity) {
    }

    @Override
    public void update(Player entity) {

    }

    @Override
    public void exit(Player entity) {

    }

    @Override
    public boolean onMessage(Player entity, Telegram telegram) {
        return false;
    }
}
