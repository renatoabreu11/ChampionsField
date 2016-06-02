package logic;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;

public enum PlayerState implements State<Player>{
    Static(){
        @Override
        public void enter(Player entity) {
            entity.setSteeringBehavior(null);
        }

        @Override
        public void update(Player entity) {
            if(entity.position != entity.initialPosition){
                entity.stateMachine.changeState(PlayerState.toOriginalRegion);
            }
        }
    },

    toOriginalRegion(){
        @Override
        public void enter(Player entity) {
            WayPoint wp = new WayPoint(entity.initialPosition);
            Arrive<Vector2> arrive = new Arrive<Vector2>(entity, wp);
            arrive.setArrivalTolerance(0.2f);
            arrive.setDecelerationRadius(10);
            entity.setSteeringBehavior(arrive);
        }

        @Override
        public void update(Player entity) {
            if(entity.position == entity.initialPosition){
                entity.stateMachine.changeState(PlayerState.Static);
            }
        }
    },

    Controlled(){},

    ChaseBall(){},

    InterceptBall(){};

    @Override
    public void enter(Player entity) {}

    @Override
    public void update(Player entity) {}

    @Override
    public void exit(Player entity) {}

    @Override
    public boolean onMessage(Player entity, Telegram telegram) {
        return false;
    }
}
