package States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
    private Texture homeTeamTexture;
    private Texture visitorTeamTexture;
    private BitmapFont font;
    private TextureAtlas buttonsAtlas;
    private Skin buttonSkin;
    private Stage stage;
    private ArrayList<TextButton> rooms;

    private MultiPlayMatch match;

    public Lobby(final GameStateManager gsm) {
        super(gsm);

        background = new Texture("Connecting.jpg");
        homeTeamTexture = new Texture("RedPlayer.png");
        visitorTeamTexture = new Texture("BluePlayer.png");
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        stage = new Stage();
        rooms = new ArrayList<TextButton>();

        //Buttons style
        buttonsAtlas = new TextureAtlas("button.pack");
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = buttonSkin.getDrawable("buttonOff");
        style.down = buttonSkin.getDrawable("buttonOn");
        style.font = font;

        for(int i = 0; i < Constants.NUMBER_MATCHES_HOST_BY_SERVER; i++) {
            final TextButton textButton = new TextButton("Room " + Integer.toString(i + 1), style);
            //System.out.println(Character.getNumericValue(textButton.getName().charAt(5)));

            textButton.setWidth(Constants.buttonWidth);
            textButton.setHeight(Constants.buttonHeight);

            if(i < Constants.NUMBER_MATCHES_HOST_BY_SERVER / 2)
                textButton.setPosition(Constants.ScreenWidth / 4 - Constants.buttonWidth / 2 + (Constants.ScreenWidth / 8 * i), Constants.ScreenHeight / 2 + Constants.ScreenHeight / 4);
            else
                textButton.setPosition(Constants.buttonWidth + Constants.buttonWidth * i - 4, Constants.ScreenHeight / 4);

            final int roomNumber = i+1;
            textButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    final int clientTeam;
                    Preferences prefs = Gdx.app.getPreferences("My Preferences");
                    String team = prefs.getString("Starting Team", "Red");
                    if(team.equals("Red"))
                        clientTeam = 0;
                    else clientTeam = 1;
                    final String name = prefs.getString("Name");
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
        font.draw(sb, "Waiting for more players...", Constants.ScreenWidth / 2, Constants.ScreenHeight - Constants.ScreenHeight / 4);
        font.draw(sb, "Red Team", Constants.ScreenWidth /4, Constants.ScreenHeight / 2);
        font.draw(sb, "Blue Team", Constants.ScreenWidth  - Constants.ScreenWidth /4, Constants.ScreenHeight / 2);

        /*float radius = 0;
        boolean canDrawHomePlayers = false;
        boolean canDrawVisitorPlayers = false;
        if(match.getHomeTeam().getPlayers().size() > 0) {
            radius = match.getHomeTeam().getPlayers().get(0).getBoundingRadius() * 100f;
            canDrawHomePlayers = true;
        }

        if(match.getVisitorTeam().getPlayers().size() > 0 && radius == 0) {
            radius = match.getVisitorTeam().getPlayers().get(0).getBoundingRadius() * 100f;
            canDrawVisitorPlayers = true;
        }

        if(canDrawHomePlayers)
            for(int i = 0; i < match.getHomeTeam().getPlayers().size(); i++) {
                sb.draw(homeTeamTexture, Constants.ScreenWidth / 8 + radius * i, Constants.ScreenHeight / 2 - Constants.ScreenHeight / 4, radius, radius);
                font.draw(sb, match.getHomeTeam().getPlayers().get(i).getName(), Constants.ScreenWidth / 8 + radius * i, Constants.ScreenHeight / 2 - Constants.ScreenHeight / 4);
            }

        if(canDrawVisitorPlayers)
            for(int i = 0; i < match.getVisitorTeam().getPlayers().size(); i++) {
                sb.draw(visitorTeamTexture, Constants.ScreenWidth - Constants.ScreenWidth / 8 + radius * i, Constants.ScreenHeight / 2 - Constants.ScreenHeight / 4, radius, radius);
                font.draw(sb, match.getVisitorTeam().getPlayers().get(i).getName(), Constants.ScreenWidth - Constants.ScreenWidth / 8 + radius * i, Constants.ScreenHeight / 2 - Constants.ScreenHeight / 4);
            }*/

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
