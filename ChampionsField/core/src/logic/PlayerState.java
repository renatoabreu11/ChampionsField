package logic;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.math.Vector2;

import utils.Constants;

public enum PlayerState implements State<Player>{
    Static(){
        @Override
        public void enter(Player entity) {
            entity.setSteeringBehavior(null);
            entity.body.setLinearVelocity(0, 0);
        }

        @Override
        public void update(Player entity) {
            float distance = entity.distanceBetweenPoints(entity.position, entity.initialPosition);
            if(distance > Constants.regionWidth / 6) {
                entity.stateMachine.changeState(PlayerState.toOriginalRegion);
            }

            if(entity.controlledPlayerWayPoint != null){
                if(entity.region.contains(entity.controlledPlayerWayPoint.getPosition()))
                    entity.stateMachine.changeState(PlayerState.Block);
            }
        }
    },

    toOriginalRegion(){
        @Override
        public void enter(Player entity) {
            Arrive<Vector2> arrive = new Arrive<Vector2>(entity, new WayPoint(entity.initialPosition));
            arrive.setArrivalTolerance(0.001f);
            arrive.setDecelerationRadius(2);
            entity.setSteeringBehavior(arrive);
        }

        @Override
        public void update(Player entity) {
            float distance = entity.distanceBetweenPoints(entity.position, entity.initialPosition);
            if(distance < Constants.regionWidth / 6){
                entity.stateMachine.changeState(PlayerState.Static);
            }

            if(entity.controlledPlayerWayPoint != null){
                if(entity.region.contains(entity.controlledPlayerWayPoint.getPosition()))
                    entity.stateMachine.changeState(PlayerState.Block);
            }
        }
    },

    Controlled(){
        @Override
        public void enter(Player entity) {
            entity.setSteeringBehavior(null);
        }

        @Override
        public void update(Player entity) {
        }
    },

    Block(){
        @Override
        public void enter(Player entity) {
            Pursue<Vector2> pursue = new Pursue<Vector2>(entity, entity.controlledPlayerWayPoint, 0.001f);
            entity.setSteeringBehavior(pursue);
        }

        @Override
        public void update(Player entity) {
            if(!entity.region.contains(entity.controlledPlayerWayPoint.getPosition())){
                entity.stateMachine.changeState(PlayerState.toOriginalRegion);
            }

        }
    },

    Kick(){
        @Override
        public void enter(Player entity) {}

        @Override
        public void update(Player entity) {}
    },

    InterceptBall(){

    };

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
