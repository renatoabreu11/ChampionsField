package States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

/**
 * Created by Evenilink on 02/05/2016.
 */
public class Options extends State {
    private Stage stage;
    private int selectedOption;
    private Texture background;
    private BitmapFont font;
    private TextureAtlas buttonsAtlas;
    private Skin buttonSkin;
    private TextButton button;

    public Options(GameStateManager gsm) {
        super(gsm);

        background = new Texture("Field.png");
        buttonsAtlas = new TextureAtlas("button.pack");
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas);
        font = new BitmapFont();

        stage = new Stage();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = buttonSkin.getDrawable("buttonOff");
        style.down = buttonSkin.getDrawable("buttonOn");
        style.font = font;

        button = new TextButton("Back", style);
        button.setHeight(width / 9.6f);
        button.setWidth(height / 5.4f);

        button.setPosition(width / 2 - button.getWidth() / 2, height / 2 - button.getHeight() / 2);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //selectedOption = 1;
                return  true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                selectedOption  = 1;
            }
        }) ;

        stage.addActor(button);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void dispose() {
        background.dispose();
        font.dispose();
        buttonsAtlas.dispose();
        buttonSkin.dispose();
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        switch (selectedOption) {
            case 0:
                break;
            case 1:
                dispose();
                gsm.set(new MenuState(gsm));
                break;
            case 2:
                gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, width, height);
        sb.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
