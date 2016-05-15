package States;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;

import logic.Ball;
import logic.Match;
import logic.Player;

public class PlayState extends State implements ApplicationListener{
    //Objects textures
    private TextureAtlas ballTexture;
    private Animation ballAnimation;
    private Texture fieldTexture;
    private Texture homeTeamTexture;
    private Texture visitorTeamTexture;
    private BitmapFont font;

    private float deltaTime = 0;
    static final float WORLD_TO_BOX = 0.01f;
    static final float BOX_TO_WORLD = 100f;

    //Match class init
    private Match match;

    //Physics World
    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Matrix4 debugMatrix;

    //Touchpad
    private Stage stage;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;


    public PlayState(GameStateManager gsm){
        super(gsm);

        if(gameplayController == 2) {
            touchpadSkin = new Skin();
            touchpadSkin.add("touchBackground", new Texture("touchBackground.png"));
            touchpadSkin.add("touchKnob", new Texture("touchKnob.png"));
            touchpadStyle = new Touchpad.TouchpadStyle();
            //Create Drawable's from TouchPad skin
            touchBackground = touchpadSkin.getDrawable("touchBackground");
            touchKnob = touchpadSkin.getDrawable("touchKnob");
            //Apply the Drawables to the TouchPad Style
            touchpadStyle.background = touchBackground;
            touchpadStyle.knob = touchKnob;
            //Create new TouchPad with the created style
            touchpad = new Touchpad(10, touchpadStyle);
            touchpad.setBounds(15, 15, 200, 200);

            //Create a Stage and add TouchPad
            stage = new Stage();
            stage.addActor(touchpad);
            Gdx.input.setInputProcessor(stage);
        }

        //Textures definition
        ballTexture = new TextureAtlas("SoccerBall.atlas");
        ballAnimation = new Animation(1 / 15f, ballTexture.getRegions());
        fieldTexture = new Texture("Field.png");
        homeTeamTexture = new Texture("Player.png");
        visitorTeamTexture = new Texture("Player.png");
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        //Physics World
        Vector2 gravity = new Vector2(0, 0f);
        world = new World(gravity, true);

        camera = new OrthographicCamera(Gdx.graphics.getWidth() * 0.01f, Gdx.graphics.getHeight() * 0.01f);
        cam.setToOrtho(false);
        camera.update();
        debugRenderer = new Box2DDebugRenderer();

        match = new Match(32, 4, world);
        createCollisionListener();
    }


    private void createCollisionListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture f1 = contact.getFixtureA();
                Fixture f2 = contact.getFixtureB();

                if((f1.getUserData() == "HomeGoal" || f1.getUserData() == "Ball") && (f2.getUserData() == "HomeGoal" || f2.getUserData() == "Ball")) {
                    match.teamScored(match.getVisitorTeam());
                }
                else if((f1.getUserData() == "VisitorGoal" || f1.getUserData() == "Ball") && (f2.getUserData() == "VisitorGoal" || f2.getUserData() == "Ball")) {
                    match.teamScored(match.getHomeTeam());
                }

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }


    @Override
    public void create() {
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = (Gdx.graphics.getWidth() * 0.01f / width) * height;
        camera.update();
    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        if(gameplayController == 1)
            match.updateMatch(dt);
        else
            match.updateMatch(touchpad.getKnobPercentX() * 5, touchpad.getKnobPercentY() * 5);

        world.step(1f / 60f, 6, 2);
    }


    @Override
    public void render(SpriteBatch sb) {
        deltaTime += Gdx.graphics.getDeltaTime();

        sb.begin();
        sb.draw(fieldTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Ball b = match.getBall();
        b.setPositionToBody();
        Vector2 screenPosition = convertToScreenCoordinates(b);
        sb.draw(ballAnimation.getKeyFrame(deltaTime, true), screenPosition.x, screenPosition.y, b.getRadius()*2, b.getRadius()*2);

        //Teams
        ArrayList<Player> homeTeamPlayers = match.getHomeTeam().getPlayers();
        ArrayList<Player> visitorTeamPlayers = match.getVisitorTeam().getPlayers();
        float radius = homeTeamPlayers.get(0).getRadius();

        for(int i = 0; i < match.getNumberOfPlayers(); i++){

            homeTeamPlayers.get(i).setPositionToBody();
            screenPosition = convertToScreenCoordinates(homeTeamPlayers.get(i));
            sb.draw(homeTeamTexture, screenPosition.x, screenPosition.y, homeTeamPlayers.get(i).getRadius()*2, homeTeamPlayers.get(i).getRadius()*2);
            font.draw(sb, homeTeamPlayers.get(i).getName(), screenPosition.x + radius - 15/2, screenPosition.y + radius + 15/2);

            visitorTeamPlayers.get(i).setPositionToBody();
            screenPosition = convertToScreenCoordinates(visitorTeamPlayers.get(i));
            sb.draw(visitorTeamTexture, screenPosition.x, screenPosition.y, visitorTeamPlayers.get(i).getRadius()*2, visitorTeamPlayers.get(i).getRadius()*2);
            font.draw(sb, visitorTeamPlayers.get(i).getName(), screenPosition.x + radius - 15/2, screenPosition.y + radius+ 15/2);
        }

        Player controPlayer = null;
        for(int i = 0; i < homeTeamPlayers.size(); i++) {
            if(homeTeamPlayers.get(i).isControlledPlayer()) {
                controPlayer = homeTeamPlayers.get(i);
                break;
            }
        }

        for(int i = 0; i < controPlayer.getPath().size(); i++)
            sb.draw(homeTeamTexture, controPlayer.getPath().get(i).x, controPlayer.getPath().get(i).y, controPlayer.getRadius() * 2, controPlayer.getRadius() * 2);

        font.draw(sb, Integer.toString(match.getScoreHomeTeam()), width / 4, height - height / 6);
        font.draw(sb, Integer.toString(match.getScoreVisitorTeam()), width - width / 4, height - height / 6);

        sb.end();

        if(gameplayController == 2) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }

        debugRenderer.render(world, camera.combined);
    }

    private Vector2 convertToScreenCoordinates(Player player) {
        float x = player.getPosition().x * 100f + Gdx.graphics.getWidth()/2 - player.getRadius();
        float y = player.getPosition().y * 100f + Gdx.graphics.getHeight()/2 - player.getRadius();
        return new Vector2(x, y);
    }

    private Vector2 convertToScreenCoordinates(Ball b) {
        float x = b.getPosition().x * 100f + Gdx.graphics.getWidth()/2 - b.getRadius();
        float y = b.getPosition().y * 100f + Gdx.graphics.getHeight()/2 - b.getRadius();
        return new Vector2(x, y);
    }
}
