package States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import utils.Constants;

public class Options extends State {
    private Stage stage;
    private int selectedOption;
    private Texture background;
    private BitmapFont font;
    private TextureAtlas buttonsAtlas;
    private Skin buttonSkin;
    private TextButton button;
    private TextField nameField;
    private TextField playersPerTeam;
    private Skin skin;
    private Dialog diag;

    public Options(GameStateManager gsm) {
        super(gsm);

        background = new Texture("Field.jpg");
        buttonsAtlas = new TextureAtlas("button.pack");
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas);
        font = new BitmapFont();

        stage = new Stage();

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = buttonSkin.getDrawable("buttonOff");
        style.down = buttonSkin.getDrawable("buttonOn");
        style.font = font;

        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        String name = prefs.getString("Name", "Default value");
        if(name.equals("Default value"))
            name = "";

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        nameField = new TextField(name, skin);
        nameField.setPosition(Constants.ScreenWidth/2 + nameField.getWidth()/2, Constants.ScreenHeight/2 + nameField.getHeight()*6);
        nameField.setMaxLength(3);
        nameField.setAlignment(1);
        nameField.setFocusTraversal(true);

        playersPerTeam = new TextField("3", skin);
        playersPerTeam.setPosition(Constants.ScreenWidth/2 + playersPerTeam.getWidth()/2, Constants.ScreenHeight/2 + playersPerTeam.getHeight()*3);
        playersPerTeam.setAlignment(1);
        playersPerTeam.setFocusTraversal(true);

        button = new TextButton("Back", style);
        button.setHeight(Constants.buttonHeight);
        button.setWidth(Constants.buttonWidth);

        button.setPosition(Constants.ScreenWidth/2 - button.getWidth()/2,  button.getHeight()/2);

        addListeners();

        stage.addActor(button);
        stage.addActor(nameField);
        stage.addActor(playersPerTeam);

        Gdx.input.setInputProcessor(stage);
    }

    private void addListeners() {
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
                String n = nameField.getText();
                if(n.length() < 3){
                    diag = new Dialog("Warning", skin) {
                        {
                            text("The username you choose needs to have 3 letter. Please provide it!");
                            button("Ok");
                        }

                        @Override
                        public void result(Object obj) {
                            diag.hide();
                        }
                    }.show(stage);
                    selectedOption = 0;
                } else
                    selectedOption = 2;
                break;
            case 2:
                gsm.set(new MenuState(gsm));
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
