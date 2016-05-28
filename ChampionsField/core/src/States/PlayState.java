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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;

import logic.Ball;
import logic.Goal;
import logic.Match;
import logic.Player;
import logic.Rain;

public class PlayState extends State implements ApplicationListener{
    //Objects textures
    private TextureAtlas explosionAtlas;
    private Animation explosionAnimation;
    private Texture rainTexture;
    private TextureAtlas ballTexture;
    private Animation ballAnimation;
    private Texture fieldTexture;
    private Texture homeTeamTexture;
    private Texture visitorTeamTexture;
    private Texture leftGoalTexture;
    private Texture rightGoalTexture;
    private BitmapFont font;

    private Rain rain;

    private float deltaTime = 0;
    private float startController;
    public boolean scored;
    private float scoreAnimationTime;

    static final float TIME_TO_START = 3;
    static final float GAME_SIMULATION_SPEED = 1 / 60f;
    static final float PLAYERS_SPEED = 5;
    static final float EXPLOSION_SPEED = 5f;
    static final float EXPLOSION_DURATION = 2.4f;

    //Match class init
    private Match match;

    //Physics World
    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer debugRenderer;

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
        explosionAtlas = new TextureAtlas("Explosion.atlas");
        explosionAnimation = new Animation(1 / 4f, explosionAtlas.getRegions());
        ballTexture = new TextureAtlas("SoccerBall.atlas");
        ballAnimation = new Animation(1 / 15f, ballTexture.getRegions());
        fieldTexture = new Texture("Field.jpg");
        homeTeamTexture = new Texture("Player.png");
        visitorTeamTexture = new Texture("Player.png");
        leftGoalTexture = new Texture("LeftGoal.png");
        rightGoalTexture = new Texture("RightGoal.png");
        rainTexture = new Texture("Rain.png");
        rain = new Rain(width, height);
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        //Physics World
        Vector2 gravity = new Vector2(0, 0f);
        world = new World(gravity, true);

        camera = new OrthographicCamera(Gdx.graphics.getWidth() * 0.01f, Gdx.graphics.getHeight() * 0.01f);
        cam.setToOrtho(false);
        camera.update();
        debugRenderer = new Box2DDebugRenderer();

        match = new Match(2, world);
        createCollisionListener();

        startController = 0;
        scored = false;
        scoreAnimationTime = 0;
    }

    private void createCollisionListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture f1 = contact.getFixtureA();
                Fixture f2 = contact.getFixtureB();

                if((f1.getUserData() == "HomeGoal" || f1.getUserData() == "Ball") && (f2.getUserData() == "HomeGoal" || f2.getUserData() == "Ball")) {
                    match.teamScored(match.getVisitorTeam(), world);
                    scored = true;
                }  else if((f1.getUserData() == "VisitorGoal" || f1.getUserData() == "Ball") && (f2.getUserData() == "VisitorGoal" || f2.getUserData() == "Ball")) {
                    match.teamScored(match.getHomeTeam(), world);
                    scored = true;
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
        if(startController >= TIME_TO_START)
            match.deactivateBarriers();
        else
            startController += dt;

        if(gameplayController == 1)
            match.updateMatch(dt);
        else
            match.updateMatch(touchpad.getKnobPercentX() * PLAYERS_SPEED, touchpad.getKnobPercentY() * PLAYERS_SPEED);

        rain.update();
        world.step(GAME_SIMULATION_SPEED, 6, 2);

        deltaTime += dt;
    }


    @Override
    public void render(SpriteBatch sb) {
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

        /*float goalX = -Gdx.graphics.getWidth()/2 + 30f * (Gdx.graphics.getWidth() / Match.FIELD_TEXTURE_WIDTH);
        float goalY = 500f * (Gdx.graphics.getHeight() / Match.FIELD_TEXTURE_HEIGHT);
        sb.draw(leftGoalTexture, width / 2 + goalX, height / 2 - goalY / 2, width / 2 + goalX + 300, (height / 2 - goalY / 2) * 2);*/

        if(scored) {
            if(scoreAnimationTime >= EXPLOSION_DURATION) {
                scored = false;
                scoreAnimationTime = 0;
                match.stopAllPlayersMotion();
                startController = 0;
                match.activateBarriers();
            } else {
                sb.draw(explosionAnimation.getKeyFrame(scoreAnimationTime * EXPLOSION_SPEED, true), 10, height / 2, width / 4, height / 3);
                scoreAnimationTime += Gdx.graphics.getDeltaTime();
            }
        }

        for(int i = 0; i < rain.getRainSize(); i++)
            sb.draw(rainTexture, rain.getPosition(i).x, rain.getPosition(i).y, width / 3, height / 3);

        sb.end();

        if(gameplayController == 2) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }

        debugRenderer.render(world, camera.combined);
    }

    private Vector2 convertToScreenCoordinates(Goal g) {
        float x = g.getPosition().x * 100f + Gdx.graphics.getWidth()/2;
        float y = g.getPosition().y * 100f + Gdx.graphics.getHeight()/2 + g.getVerticalLength()/2;
        return new Vector2(x, y);
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
