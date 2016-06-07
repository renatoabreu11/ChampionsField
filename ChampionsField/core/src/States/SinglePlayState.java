package States;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;
import java.util.Random;

import logic.Ball;
import logic.Goal;
import logic.Match;
import logic.Player;
import logic.Rain;
import logic.SinglePlayMatch;
import utils.Constants;

public class SinglePlayState extends State implements ApplicationListener {
        //Objects textures
        private Texture loadingBackground;
        private TextureAtlas explosionAtlas;
        private Animation explosionAnimation;
        private Texture rainTexture;
        private TextureAtlas ballTexture;
        private Animation ballAnimation;
        private Texture fieldTexture;
        private Texture homeTeamTexture;
        private Texture visitorTeamTexture;
        private Texture goalTexture;
        private BitmapFont font;
        private Animation powerUpAnimation;
        private TextureAtlas teamSpeedIncAtlas;
        private TextureAtlas teamSpeedDecAtlas;
        private TextureAtlas playerSpeedIncAtlas;
        private TextureAtlas loadingAtlas;
        private Animation loadingAnimation;

        private float deltaTime = 0;
        private float scoreAnimationTime;
        private float powerUpAnimationTime;
        private float loadingAnimationTime = 0;

        private Vector2 explosionPos;
        Box2DDebugRenderer debugRenderer;

        //Match class init
        public SinglePlayMatch match;

        //Physics World
        private OrthographicCamera camera;

        //Touchpad
        private Stage stage;
        private Touchpad touchpad;
        private Touchpad.TouchpadStyle touchpadStyle;
        private Skin touchpadSkin;
        private Drawable touchBackground;
        private Drawable touchKnob;

        private TextureAtlas switchTexture;
        private Skin switchSkin;
        private Button switchBtn;

        private boolean readyToPlay;

    public SinglePlayState(GameStateManager gsm) {
        super(gsm);

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

        switchTexture = new TextureAtlas("Switch.pack");
        switchSkin = new Skin();
        switchSkin.addRegions(switchTexture);

        Button.ButtonStyle btnStyle = new Button.ButtonStyle();
        btnStyle.up = switchSkin.getDrawable("Switch");
        btnStyle.down = switchSkin.getDrawable("SwitchPressed");

        switchBtn = new Button(btnStyle);
        switchBtn.setRound(true);
        switchBtn.setHeight(Constants.Switch_Height);
        switchBtn.setWidth(Constants.Switch_Width);
        switchBtn.setPosition(width - Constants.Switch_Width * 2, Constants.Switch_Height/2);
        addListeners();
        stage.addActor(switchBtn);

        //Textures definition
        explosionAtlas = new TextureAtlas("Explosion.atlas");
        explosionAnimation = new Animation(1 / 4f, explosionAtlas.getRegions());
        ballTexture = new TextureAtlas("SoccerBall.atlas");
        ballAnimation = new Animation(1 / 15f, ballTexture.getRegions());
        Random r = new Random();
        int ground = r.nextInt(2);
        if(ground == 0)
            fieldTexture = new Texture("Field.jpg");
        else fieldTexture = new Texture("FieldGalaxy.jpg");
        homeTeamTexture = new Texture("BluePlayer.png");
        visitorTeamTexture = new Texture("RedPlayer.png");
        goalTexture = new Texture("FootballGoal.png");
        rainTexture = new Texture("Rain.png");
        loadingBackground = new Texture("Connecting.jpg");

        teamSpeedIncAtlas = new TextureAtlas("SpeedInc.atlas");
        teamSpeedDecAtlas = new TextureAtlas("SpeedDec.atlas");
        playerSpeedIncAtlas = new TextureAtlas("Density.atlas");

        loadingAtlas = new TextureAtlas("Loading.atlas");
        loadingAnimation = new Animation(1/1.5f, loadingAtlas.getRegions());

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        //Camera definition
        camera = new OrthographicCamera(Gdx.graphics.getWidth() * 0.01f, Gdx.graphics.getHeight() * 0.01f);
        camera.update();
        debugRenderer = new Box2DDebugRenderer();
        scoreAnimationTime = 0;

        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        int numberOfPlayers = prefs.getInteger("Number Of Players", 3);
        match = new SinglePlayMatch(numberOfPlayers);
        readyToPlay = false;
    }

    private void addListeners() {
        switchBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                match.switchPlayer();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

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
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        if(readyToPlay){
            match.updateMatch(touchpad.getKnobPercentX() * Constants.PLAYERS_SPEED, touchpad.getKnobPercentY() * Constants.PLAYERS_SPEED, dt);

            if(match.getCurrentState() == Match.matchState.Score  && scoreAnimationTime == 0) {
                explosionPos = match.getBall().getScreenCoordinates();
            }

            if(scoreAnimationTime >= Constants.EXPLOSION_DURATION) {
                scoreAnimationTime = 0;
                match.endScoreState();
            }else{
                if (match.getPowerUp().isActive() && powerUpAnimationTime == 0f) {
                    Constants.powerUpType type = match.getPowerUp().getType();
                    switch (type) {
                        case TeamSpeedInc:
                            powerUpAnimation = new Animation(1 / 2f, teamSpeedIncAtlas.getRegions());
                            break;
                        case TeamSpeedDec:
                            powerUpAnimation = new Animation(1 / 2f, teamSpeedDecAtlas.getRegions());
                            break;
                        case PlayerSpeedInc:
                            powerUpAnimation = new Animation(1 / 2f, playerSpeedIncAtlas.getRegions());
                            break;
                    }
                } else if (powerUpAnimationTime > 0) {
                    if (powerUpAnimationTime >= Constants.powerAnimationDuration) {
                        match.getPowerUp().setActive(false);
                        powerUpAnimationTime = 0;
                    }
                }
            }

            if(match.getElapsedTime() >= Constants.GAME_TIME){
                match.endGame();
                dispose();
                gsm.set(new MenuState(gsm));
            }
        } else {
            if(deltaTime > Constants.LoadingTime){
                readyToPlay = true;
                match.startTimer();
            }
        }
        deltaTime += dt;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();

        //Field draw
        if(readyToPlay){
            sb.draw(fieldTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Vector2 screenPosition;
            Ball b = match.getBall();
            b.setPositionToBody();
            screenPosition = b.getScreenCoordinates();

            if(match.getCurrentState() == Match.matchState.Score) {
                sb.draw(explosionAnimation.getKeyFrame(scoreAnimationTime * Constants.EXPLOSION_SPEED, true), explosionPos.x - Constants.EXPLOSION_WIDTH/2, explosionPos.y - Constants.EXPLOSION_HEIGHT/2, Constants.EXPLOSION_WIDTH, Constants.EXPLOSION_HEIGHT);
                scoreAnimationTime += Gdx.graphics.getDeltaTime();
            } else{
                sb.draw(ballAnimation.getKeyFrame(deltaTime, true), screenPosition.x, screenPosition.y, b.getRadius()*2 * 100f, b.getRadius()*2* 100f);
            }

            //Teams
            ArrayList<Player> homeTeamPlayers = match.getHomeTeam().getPlayers();
            ArrayList<Player> visitorTeamPlayers = match.getVisitorTeam().getPlayers();

            if(homeTeamPlayers.size() != 0) {
                float radius = homeTeamPlayers.get(0).getBoundingRadius();

                for (int i = 0; i < match.getNumberOfPlayers(); i++) {
                    homeTeamPlayers.get(i).setPositionToBody();
                    screenPosition = homeTeamPlayers.get(i).getScreenCoordinates();
                    sb.draw(homeTeamTexture, screenPosition.x, screenPosition.y, homeTeamPlayers.get(i).getBoundingRadius() * 2 * 100f, homeTeamPlayers.get(i).getBoundingRadius() * 2 * 100f);
                    font.draw(sb, homeTeamPlayers.get(i).getName(), screenPosition.x + radius * 100f / 2, screenPosition.y + radius * 100f + radius/5 * 100f);

                    visitorTeamPlayers.get(i).setPositionToBody();
                    screenPosition = visitorTeamPlayers.get(i).getScreenCoordinates();
                    sb.draw(visitorTeamTexture, screenPosition.x, screenPosition.y, visitorTeamPlayers.get(i).getBoundingRadius()*2* 100f, visitorTeamPlayers.get(i).getBoundingRadius()*2* 100f);
                    font.draw(sb, visitorTeamPlayers.get(i).getName(), screenPosition.x + radius * 100f / 2, screenPosition.y + radius * 100f + radius/5 * 100f);
                }
            }

            font.draw(sb, Integer.toString(match.getScoreHomeTeam()), width / 4, height - height / 6);
            font.draw(sb, Integer.toString(match.getScoreVisitorTeam()), width - width / 4, height - height / 6);

            font.draw(sb, match.getTime(), width / 2  - 28, height - height / 6);

            Goal g = match.getHomeTeamGoal();
            screenPosition = g.getScreenCoordinates();
            float vertLength = match.getHomeTeamGoal().getVerticalLength() * Constants.BOX_TO_WORLD;
            float horLength = match.getHomeTeamGoal().getHorizontalLength() * Constants.BOX_TO_WORLD;
            sb.draw(goalTexture, screenPosition.x, screenPosition.y, horLength, vertLength);

            g = match.getVisitorTeamGoal();
            screenPosition = g.getScreenCoordinates();
            sb.draw(goalTexture, screenPosition.x , screenPosition.y, -horLength, vertLength);

            if (match.getPowerUp().isActive()) {
                Vector2 powerUpPos = match.getPowerUp().getScreenCoordinates();
                sb.draw(powerUpAnimation.getKeyFrame(powerUpAnimationTime * Constants.PowerUpSpeed, true), powerUpPos.x, powerUpPos.y, Constants.PowerUpWidth, Constants.PowerUpHeight);
                powerUpAnimationTime += Gdx.graphics.getDeltaTime();
            }

            for(int i = 0; i < match.rain.getRainSize(); i++)
                sb.draw(rainTexture, match.rain.getPosition(i).x, match.rain.getPosition(i).y, width / 3, height / 3);

            if(gameplayController == 2) {
                stage.act(Gdx.graphics.getDeltaTime());
                stage.draw();
            }
        } else{
            sb.draw(loadingBackground, 0, 0, Constants.ScreenWidth, Constants.ScreenHeight);
            sb.draw(loadingAnimation.getKeyFrame(loadingAnimationTime * 5f, true), Constants.ScreenWidth/2 - Constants.loadingWidth/2, Constants.ScreenHeight/2 - Constants.loadingHeight/2, Constants.loadingWidth, Constants.loadingHeight);
            loadingAnimationTime += Gdx.graphics.getDeltaTime();
        }

        sb.end();
    }

    @Override
    public void dispose() {
        explosionAtlas.dispose();
        rainTexture.dispose();
        ballTexture.dispose();
        fieldTexture.dispose();
        homeTeamTexture.dispose();
        visitorTeamTexture.dispose();
        goalTexture.dispose();
        font.dispose();
        switchTexture.dispose();
        match.dispose();
    }
}
