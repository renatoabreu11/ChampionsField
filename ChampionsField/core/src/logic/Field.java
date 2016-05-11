package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Vector;

public class Field {
    Body northBorder;
    Body southBorder;
    Body eastBorder;
    Body westBorder;

    public Field(World w) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        northBorder = w.createBody(bodyDef);
        southBorder = w.createBody(bodyDef);
        eastBorder = w.createBody(bodyDef);
        westBorder = w.createBody(bodyDef);

        createBorders(w);
    }

    private void createBorders(World w) {
        float width =  Gdx.graphics.getWidth() * 0.01f;
        float height = Gdx.graphics.getHeight() * 0.01f;
        EdgeShape shape1 = new EdgeShape();
        shape1.set(- width / 2, height/2, width/2, height/2);
        EdgeShape shape2 = new EdgeShape();
        shape2.set(width / 2, -height/2, width/2, height/2);
        EdgeShape shape3 = new EdgeShape();
        shape3.set(- width / 2, -height/2, -width/2, height/2);
        EdgeShape shape4 = new EdgeShape();
        shape4.set(- width / 2, -height/2, width/2, -height/2);

        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.shape = shape1;
        fixtureDef1.friction = 0;
        fixtureDef1.restitution = 0.5f;

        Fixture fixture = southBorder.createFixture(fixtureDef1);
        shape1.dispose();

        fixtureDef1.shape = shape2;
        northBorder.createFixture(fixtureDef1);
        shape2.dispose();

        fixtureDef1.shape = shape3;
        eastBorder.createFixture(fixtureDef1);
        shape3.dispose();

        fixtureDef1.shape = shape4;
        southBorder.createFixture(fixtureDef1);
        shape4.dispose();
    }

    public Vector<Body> getBodies(){
        Vector<Body> bodies = new Vector<Body>();
        bodies.add(northBorder);
        bodies.add(southBorder);
        bodies.add(eastBorder);
        bodies.add(southBorder);
        return bodies;
    }
}
