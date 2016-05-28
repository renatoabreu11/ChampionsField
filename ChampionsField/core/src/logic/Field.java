package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Vector;

import static logic.Match.entityMasks.BallMask;
import static logic.Match.entityMasks.FieldBordersMask;
import static logic.Match.entityMasks.FootballGoalMask;
import static logic.Match.entityMasks.PlayerMask;
import static logic.Match.entityMasks.ScreenBordersMask;

public class Field {
    Body northBorder;
    Body southBorder;
    Body eastBorder;
    Body westBorder;

    Body topLine;
    Body bottomLine;
    Body topLeftLine;
    Body bottomLeftLine;
    Body topRightLine;
    Body bottomRightLine;

    Body leftBarrier;
    Body rightBarrier;

    public Field(World w) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        northBorder = w.createBody(bodyDef);
        southBorder = w.createBody(bodyDef);
        eastBorder = w.createBody(bodyDef);
        westBorder = w.createBody(bodyDef);

        topLine = w.createBody(bodyDef);
        bottomLine = w.createBody(bodyDef);
        topLeftLine = w.createBody(bodyDef);
        bottomLeftLine = w.createBody(bodyDef);
        topRightLine = w.createBody(bodyDef);
        bottomRightLine = w.createBody(bodyDef);

        leftBarrier = w.createBody(bodyDef);
        rightBarrier = w.createBody(bodyDef);

        createBorders(w);
    }

    private void createBorders(World w) {
        float width =  Gdx.graphics.getWidth() * 0.01f;
        float height = Gdx.graphics.getHeight() * 0.01f;

        //Edges of the screen
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
        fixtureDef1.filter.categoryBits = ScreenBordersMask.getMask();
        fixtureDef1.filter.maskBits = PlayerMask.getMask();

        southBorder.createFixture(fixtureDef1);
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

        float percentageFieldX = 137 * 100 / 2560;
        float x = percentageFieldX * Gdx.graphics.getWidth() / 100;
        x = x / 100;

        float percentageFieldY = 35 * 100 / 1600;
        float y = percentageFieldY * Gdx.graphics.getHeight() / 100;
        y = y / 100;

        float percentageGoalY = 550 * 100 / 1600;
        float goalY = percentageGoalY * Gdx.graphics.getHeight() / 100;
        goalY = goalY / 100;

        //Edges of the field
        EdgeShape topFieldLine = new EdgeShape();
        topFieldLine.set(-width / 2 + x, height / 2 - y, width / 2 - x, height  /2 - y);
        EdgeShape bottomFieldLine = new EdgeShape();
        bottomFieldLine.set(-width / 2 + x, -height / 2 + y, width / 2 - x, -height  /2 + y);
        EdgeShape topFieldLeft = new EdgeShape();
        topFieldLeft.set(-width / 2 + x, height / 2 - y, -width / 2 + x, height / 2 - goalY);
        EdgeShape bottomFieldLeft = new EdgeShape();
        bottomFieldLeft.set(-width / 2 + x, -height / 2 + goalY, -width / 2 + x, -height / 2 + y);
        EdgeShape topFieldRight = new EdgeShape();
        topFieldRight.set(width / 2 - x, height / 2 - y, width / 2 - x, height / 2 - goalY);
        EdgeShape bottomFieldRight = new EdgeShape();
        bottomFieldRight.set(width / 2 - x, -height / 2 + goalY, width / 2 - x, -height / 2 + y);

        FixtureDef innerLinesFixture = new FixtureDef();
        innerLinesFixture.filter.categoryBits = FieldBordersMask.getMask();
        innerLinesFixture.filter.maskBits = BallMask.getMask();

        innerLinesFixture.shape = topFieldLine;
        topLine.createFixture(innerLinesFixture);
        topFieldLine.dispose();

        innerLinesFixture.shape = bottomFieldLine;
        bottomLine.createFixture(innerLinesFixture);
        bottomFieldLine.dispose();

        innerLinesFixture.shape = topFieldLeft;
        topLeftLine.createFixture(innerLinesFixture);
        topFieldLeft.dispose();

        innerLinesFixture.shape = bottomFieldLeft;
        bottomLeftLine.createFixture(innerLinesFixture);
        bottomFieldLeft.dispose();

        innerLinesFixture.shape = topFieldRight;
        topRightLine.createFixture(innerLinesFixture);
        topFieldRight.dispose();

        innerLinesFixture.shape = bottomFieldRight;
        bottomRightLine.createFixture(innerLinesFixture);
        bottomFieldRight.dispose();

        //Initial barriers
        percentageFieldX = 1030 * 100 / 2560;
        x = percentageFieldX * Gdx.graphics.getWidth() / 100;
        x /= 100;

        EdgeShape leftEdge = new EdgeShape();
        leftEdge.set(- width / 2 + x, - height / 2, - width / 2 + x,  height / 2);
        EdgeShape rightEdge = new EdgeShape();
        rightEdge.set(width / 2 - x, - height / 2, width / 2 - x,  height / 2);

        fixtureDef1.shape = leftEdge;
        leftBarrier.createFixture(fixtureDef1);
        leftEdge.dispose();

        fixtureDef1.shape = rightEdge;
        rightBarrier.createFixture(fixtureDef1);
        rightEdge.dispose();
    }

    public void deactivateBarriers() {
        leftBarrier.setActive(false);
        rightBarrier.setActive(false);
    }

    public void activateBarriers() {
        leftBarrier.setActive(true);
        rightBarrier.setActive(true);
    }
}
