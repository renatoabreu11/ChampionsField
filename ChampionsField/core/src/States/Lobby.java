package States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;

import logic.MultiPlayMatch;
import server.MPClient;
import utils.Constants;

public class Lobby extends State{
    private Texture background;
    private BitmapFont font;
    private TextureAtlas buttonsAtlas;
    private Skin buttonSkin;
    private Stage stage;
    private ArrayList<TextButton> rooms;

    private MultiPlayMatch match;

    public Lobby(final GameStateManager gsm) {
        super(gsm);

        background = new Texture("Connecting.jpg");
        stage = new Stage();
        rooms = new ArrayList<TextButton>();

        //Buttons style
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        buttonsAtlas = new TextureAtlas("button.pack");
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = buttonSkin.getDrawable("buttonOff");
        style.down = buttonSkin.getDrawable("buttonOn");
        style.font = font;

        for(int i = 0; i < Constants.NUMBER_MATCHES_HOST_BY_SERVER; i++) {
            final TextButton textButton = new TextButton("Room " + Integer.toString(i + 1), style);
            textButton.setWidth(Constants.buttonWidth);
            textButton.setHeight(Constants.buttonHeight);

            if(i == 0)
                textButton.setPosition(Constants.ScreenWidth / 2 - Constants.buttonWidth / 2, Constants.ScreenHeight - Constants.ScreenHeight / 3);
            else
                textButton.setPosition(Constants.ScreenWidth / 2 - Constants.buttonWidth / 2, Constants.ScreenHeight / 3);

            final int roomNumber = i;
            textButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    final int clientTeam;
                    Preferences prefs = Gdx.app.getPreferences("My Preferences");
                    String team = prefs.getString("Starting Team", "Red");
                    if(team.equals("Red"))
                        clientTeam = 0;
                    else clientTeam = 1;
                    final String name = prefs.getString("Name", "AAA");
                    match = new MultiPlayMatch(clientTeam);

                    class MyClient implements Runnable {
                        @Override
                        public void run() {
                            MPClient client = new MPClient(name, clientTeam, match, roomNumber);
                        }
                    }
                    Thread newPlayer = new Thread(new MyClient());
                    newPlayer.start();
                    gsm.set(new MultiPlayState(gsm, match));
                }
            });
            stage.addActor(textButton);
        }

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, Constants.ScreenWidth, Constants.ScreenHeight);
        sb.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {

    }

    @Override
    protected void handleInput() {

    }
}
