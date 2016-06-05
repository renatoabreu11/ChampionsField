package logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import utils.Constants;

public class Field {
    Body northBorder;
    Body southBorder;
    Body eastBorder;
    Body westBorder;

    Body topBorder;
    Body bottomBorder;

    Body rightHalfMoon;
    Body leftHalfMoon;

    Body test;

    public Field(World w) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(0, 0);

        northBorder = w.createBody(bodyDef);
        southBorder = w.createBody(bodyDef);
        eastBorder = w.createBody(bodyDef);
        westBorder = w.createBody(bodyDef);

        topBorder = w.createBody(bodyDef);
        bottomBorder = w.createBody(bodyDef);

        rightHalfMoon = w.createBody(bodyDef);
        leftHalfMoon = w.createBody(bodyDef);

        createBorders(w);
    }

    private void createBorders(World w) {
        float width =  Gdx.graphics.getWidth() * 0.01f;
        float height = Gdx.graphics.getHeight() * 0.01f;
        float widthScale =  Gdx.graphics.getWidth() / Constants.FIELD_TEXTURE_WIDTH;
        float heightScale = Gdx.graphics.getHeight() / Constants.FIELD_TEXTURE_HEIGHT;

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
        fixtureDef1.filter.categoryBits = Constants.entityMasks.ScreenBordersMask.getMask();
        fixtureDef1.filter.maskBits = Constants.entityMasks.PlayerMask.getMask();

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

        float x = 135 * 0.01f * widthScale;
        float y = 35 * 0.01f * heightScale;
        float goalY = 250 * 0.01f * heightScale;

        //Edges of the field
        ChainShape topFieldBorder = new ChainShape();
        Vector2[] topBorderVertices = new Vector2[4];
        topBorderVertices[0] = new Vector2(-width / 2 + x, goalY);
        topBorderVertices[1] = new Vector2(-width / 2 + x, height / 2 - y);
        topBorderVertices[2] = new Vector2(width / 2 - x, height / 2 - y);
        topBorderVertices[3] = new Vector2(width / 2 - x, goalY);
        topFieldBorder.createChain(topBorderVertices);

        ChainShape bottomFieldBorder = new ChainShape();
        Vector2[] bottomBorderVertices = new Vector2[4];
        bottomBorderVertices[0] = new Vector2(-width / 2 + x, -goalY);
        bottomBorderVertices[1] = new Vector2(-width / 2 + x, -height / 2 + y);
        bottomBorderVertices[2] = new Vector2(width / 2 - x, -height / 2 + y);
        bottomBorderVertices[3] = new Vector2(width / 2 - x, -goalY);
        bottomFieldBorder.createChain(bottomBorderVertices);

        FixtureDef innerLinesFixture = new FixtureDef();
        innerLinesFixture.filter.categoryBits = Constants.entityMasks.FieldBordersMask.getMask();
        innerLinesFixture.filter.maskBits = Constants.entityMasks.BallMask.getMask();

        innerLinesFixture.shape = topFieldBorder;
        topBorder.createFixture(innerLinesFixture);
        topFieldBorder.dispose();

        innerLinesFixture.shape = bottomFieldBorder;
        bottomBorder.createFixture(innerLinesFixture);
        bottomFieldBorder.dispose();

        //Initial barriers
        FixtureDef centerFixture = new FixtureDef();
        centerFixture.filter.categoryBits = Constants.entityMasks.CenterMask.getMask();
        centerFixture.filter.maskBits = Constants.entityMasks.PlayerMask.getMask();

        ChainShape leftMoon = new ChainShape();
        ChainShape rightMoon = new ChainShape();

        Vector2[] vertices = new Vector2[7];
        vertices[0] = new Vector2(0, height  /2 - y);
        vertices[1] = new Vector2(0, 250 * 0.01f * heightScale);
        vertices[2] = new Vector2(-175 * 0.01f * widthScale, 175 * 0.01f * heightScale);
        vertices[3] = new Vector2(- 250 * 0.01f * widthScale, 0);
        vertices[4] = new Vector2(-175 * 0.01f * widthScale, -175 * 0.01f * heightScale);
        vertices[5] = new Vector2(0, -250 * 0.01f * heightScale);
        vertices[6] = new Vector2(0, -height  /2 - y);

        leftMoon.createChain(vertices);

        centerFixture.shape = leftMoon;
        leftHalfMoon.createFixture(centerFixture);
        leftMoon.dispose();

        Vector2[] verts = new Vector2[7];
        verts[0] = new Vector2(0, -height  /2 - y);
        verts[1] = new Vector2(0, -250 * 0.01f * heightScale);
        verts[2] = new Vector2(175 * 0.01f * widthScale, -175 * 0.01f * heightScale);
        verts[3] = new Vector2(250 * 0.01f * widthScale, 0);
        verts[4] = new Vector2(175 * 0.01f * widthScale, 175 * 0.01f * heightScale);
        verts[5] = new Vector2(0, 250 * 0.01f * heightScale);
        verts[6] = new Vector2(0, height  /2 - y);

        rightMoon.createChain(verts);
        centerFixture.shape = rightMoon;
        rightHalfMoon.createFixture(centerFixture);
        rightMoon.dispose();
    }

    public void deactivateBarriers() {
        rightHalfMoon.setActive(false);
        leftHalfMoon.setActive(false);
    }

    public void activateBarriers(boolean teamFlag) {
        rightHalfMoon.setActive(teamFlag);
        leftHalfMoon.setActive(!teamFlag);
    }
}
