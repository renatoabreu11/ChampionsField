package States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;

import logic.Player;
import logic.Team;

/**
 * Created by Evenilink on 02/05/2016.
 */
public class PlayState extends State {

    private Texture field;
    private Team team1;
    private Team team2;

    private Stage stage;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        field = new Texture("MenuBackground.jpg");
        team1 = new Team(2);
        //team2 = new Team(1);

        /***********Touchpad construction************/
        //Create a touchpad skin
        touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));
        //Create TouchPad Style
        touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(15, 15, 200, 200);

        //Create a Stage and add TouchPad
        stage = new Stage();
        //stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true, batch);
        stage.addActor(touchpad);
        Gdx.input.setInputProcessor(stage);
        /***********End of Touchpad construction************/

    }

    @Override
    public void dispose() {

    }

    @Override
    protected void handleInput() {

    }

    public void checkWithFieldCollision(Player player) {
        double fieldLeft = player.getCollider().x - player.getRadius();
        double fieldRight = width - player.getCollider().x - player.getRadius();        //width of the field
        double fieldTop = player.getCollider().y - player.getRadius();
        double fieldBottom = height - player.getCollider().y - player.getRadius();      //height of the field

        if (fieldLeft < 0)
            player.setPositionX(0);
        if(fieldRight < 0)
            player.setPositionX(width - player.getRadius() * 2);
        if(fieldTop < 0)
            player.setPositionY(0);
        if(fieldBottom < 0)
            player.setPositionY(height - player.getRadius() * 2);
    }

    public void checkWithAnotherPlayer(Player player, ArrayList<Player> players) {
        for(int i = 0; i < players.size(); i++) {
            if(player.equals(players.get(i)))
                continue;
            if (player.getCollider().overlaps(players.get(i).getCollider())) {
                player.setPosition(player.getLastPosition());
                players.get(i).setPosition(players.get(i).getLastPosition());
            }
        }
    }

    @Override
    public void update(float dt) {
        //Updates the main player's position
        team1.getPlayers().get(0).setLastPosition(team1.getPlayers().get(0).getPosition());
        float lastPositionX = team1.getPlayers().get(0).getPosition().x;
        float lastPositionY = team1.getPlayers().get(0).getPosition().y;
        team1.getPlayers().get(0).setPosition(lastPositionX + touchpad.getKnobPercentX() * 5, lastPositionY + touchpad.getKnobPercentY() * 5 );

        team1.updatePlayers();

        for(int i = 0; i < team1.getNumberPlayers(); i++) {
            checkWithFieldCollision(team1.getPlayers().get(i));
            checkWithAnotherPlayer(team1.getPlayers().get(i), team1.getPlayers());
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(field, 0, 0, width, height);
        team1.render(sb);
        sb.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
