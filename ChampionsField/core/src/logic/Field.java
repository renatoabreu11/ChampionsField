package logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Field {
    private Texture fieldTex;
    float width;
    float height;

    public Field(String s, float width, float height) {
        fieldTex = new Texture(s);
        this.width= width;
        this.height = height;
    }

    public void render(SpriteBatch sb) {
        sb.draw(fieldTex, 0, 0, width, height);
    }

    public Texture getTexture() {
        return fieldTex;
    }
}
