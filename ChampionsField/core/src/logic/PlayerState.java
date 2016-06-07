package logic;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.Interpose;
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

            boolean containsBall = false, containsPlayer = false;

            for(WayPoint waypoint : entity.adversaryTeamWayPoint)
                if(entity.region.contains(waypoint.getPosition())) {
                    containsPlayer = true;
                }

            if(entity.region.contains(entity.ballWayPoint.getPosition())) {
                containsBall = true;
            }

            if(containsBall)
                entity.stateMachine.changeState(PlayerState.InterceptBall);
            else if(containsPlayer)
                entity.stateMachine.changeState(PlayerState.Block);
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

            boolean containsBall = false, containsPlayer = false;

            for(WayPoint waypoint : entity.adversaryTeamWayPoint)
                if(entity.region.contains(waypoint.getPosition())) {
                    containsPlayer = true;
                }

            if(entity.region.contains(entity.ballWayPoint.getPosition())) {
                containsBall = true;
            }

            if(containsBall)
                entity.stateMachine.changeState(PlayerState.InterceptBall);
            else if(containsPlayer)
                entity.stateMachine.changeState(PlayerState.Block);

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
            WayPoint wp = null;
            for(WayPoint waypoint : entity.adversaryTeamWayPoint)
                if(entity.region.contains(waypoint.getPosition()))
                    wp = waypoint;
            Pursue<Vector2> pursue = new Pursue<Vector2>(entity, wp, 0.001f);
            entity.setSteeringBehavior(pursue);
        }

        @Override
        public void update(Player entity) {
            boolean contains = false;
            for(WayPoint waypoint : entity.adversaryTeamWayPoint)
                if(entity.region.contains(waypoint.getPosition()))
                    contains = true;

            boolean containsBall = false;
            if(entity.region.contains(entity.ballWayPoint.getPosition())) {
                containsBall = true;
            }

            if(contains == false)
                entity.stateMachine.changeState(PlayerState.toOriginalRegion);
            else if(containsBall)
                entity.stateMachine.changeState(PlayerState.InterceptBall);
        }
    },

    InterceptBall(){
        @Override
        public void enter(Player entity) {
            WayPoint wp = entity.ballWayPoint;
            Interpose<Vector2> interpose = new Interpose<Vector2>(entity, wp, entity);
            entity.setSteeringBehavior(interpose);
        }

        @Override
        public void update(Player entity) {
            if(!entity.region.contains(entity.ballWayPoint.getPosition())) {
                entity.stateMachine.changeState(PlayerState.toOriginalRegion);
                return;
            }
        }
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

    public float distanceBetweenPoints(Vector2 p1, Vector2 p2){
        float xDiff = (float)Math.pow(p1.x - p2.x, 2);
        float yDiff = (float)Math.pow(p1.y - p2.y, 2);
        return (float)Math.sqrt(xDiff + yDiff);
    }
}
