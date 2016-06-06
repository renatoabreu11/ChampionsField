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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.ArrayList;

import logic.Ball;
import logic.Goal;
import logic.Match;
import logic.MultiPlayMatch;
import logic.Player;
import logic.Rain;
import server.MPClient;
import utils.Constants;

public class MultiPlayState extends State implements ApplicationListener {
    //Objects textures
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
    private Rain rain;
    private TextureAtlas teamSpeedIncAtlas;
    private TextureAtlas teamSpeedDecAtlas;
    private TextureAtlas playerSpeedIncAtlas;
    private Animation powerUpAnimation;

    private float deltaTime = 0;
    private float scoreAnimationTime;
    private float powerUpAnimationTime;

    private Vector2 explosionPos;
    static final float PowerUp_DURATION = 10f;
    Box2DDebugRenderer debugRenderer;

    //Match class init
    public MultiPlayMatch match;

    //Physics World
    private OrthographicCamera camera;

    //Touchpad
    private Stage stage;
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    private boolean readyToPlay;
        //Objects textures
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
        private Rain rain;
        private TextureAtlas teamSpeedIncAtlas;
        private TextureAtlas teamSpeedDecAtlas;
        private TextureAtlas playerSpeedIncAtlas;
        private Animation powerUpAnimation;

        private float deltaTime = 0;
        private float scoreAnimationTime;
        private float powerUpAnimationTime;

        private Vector2 explosionPos;
        Box2DDebugRenderer debugRenderer;

        //Match class init
        public MultiPlayMatch match;

        //Physics World
        private OrthographicCamera camera;

        //Touchpad
        private Stage stage;
        private Touchpad touchpad;
        private Touchpad.TouchpadStyle touchpadStyle;
        private Skin touchpadSkin;
        private Drawable touchBackground;
        private Drawable touchKnob;

    public MultiPlayState(GameStateManager gsm){
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

        //Textures definition
        explosionAtlas = new TextureAtlas("Explosion.atlas");
        explosionAnimation = new Animation(1 / 4f, explosionAtlas.getRegions());
        ballTexture = new TextureAtlas("SoccerBall.atlas");
        ballAnimation = new Animation(1 / 15f, ballTexture.getRegions());
        fieldTexture = new Texture("Field.jpg");
        homeTeamTexture = new Texture("Player.png");
        visitorTeamTexture = new Texture("Player.png");
        goalTexture = new Texture("FootballGoal.png");
        rainTexture = new Texture("Rain.png");
        teamSpeedIncAtlas = new TextureAtlas("SpeedInc.atlas");
        teamSpeedDecAtlas = new TextureAtlas("SpeedDec.atlas");
        playerSpeedIncAtlas = new TextureAtlas("Density.atlas");
        font = new BitmapFont();
        font.setColor(Color.WHITE);

        //Camera definition
        camera = new OrthographicCamera(Gdx.graphics.getWidth() * 0.01f, Gdx.graphics.getHeight() * 0.01f);
        cam.setToOrtho(false);
        camera.update();
        debugRenderer = new Box2DDebugRenderer();

        rain = new Rain(width, height);
        scoreAnimationTime = 0;
        readyToPlay = false;

        int clientTeam = 0;
        match = new MultiPlayMatch(clientTeam);

        class MyClient implements Runnable {
            @Override
            public void run() {
            }
        }
        Thread newPlayer = new Thread(new MyClient());
        newPlayer.start();
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
<<<<<<< HEAD
        if(!readyToPlay && match.everyPlayerConnected())
            readyToPlay = true;

        if(readyToPlay) {
            if (match.getElapsedTime() >= Constants.GAME_TIME) {
                match.endGame();
            }

            match.updateMatch(touchpad.getKnobPercentX() * Constants.PLAYERS_SPEED, touchpad.getKnobPercentY() * Constants.PLAYERS_SPEED, rain, dt);
=======
        if(match.getElapsedTime() >= Constants.GAME_TIME){
            match.endGame();
            dispose();
            gsm.set(new MenuState(gsm));
        }

        match.updateMatch(touchpad.getKnobPercentX() * Constants.PLAYERS_SPEED, touchpad.getKnobPercentY() * Constants.PLAYERS_SPEED, rain, dt);

        if(match.getCurrentState() == Match.matchState.Score  && scoreAnimationTime == 0) {
            explosionPos = match.getBall().getScreenCoordinates();
        }

        if(scoreAnimationTime >= Constants.EXPLOSION_DURATION) {
            scoreAnimationTime = 0;
            match.endScoreState();
        }
>>>>>>> origin/master

            if (match.getCurrentState() == Match.matchState.Score && scoreAnimationTime == 0) {
                explosionPos = match.getBall().getScreenCoordinates();
            }
<<<<<<< HEAD

            if (scoreAnimationTime >= Constants.EXPLOSION_DURATION) {
                scoreAnimationTime = 0;
                match.endScoreState();
=======
        } else if(powerUpAnimationTime > 0){
            if(powerUpAnimationTime >= Constants.powerAnimationDuration){
                match.getPowerUp().setActive(false);
                powerUpAnimationTime = 0;
>>>>>>> origin/master
            }

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
                if (powerUpAnimationTime >= PowerUp_DURATION) {
                    match.getPowerUp().setActive(false);
                    powerUpAnimationTime = 0;
                }
            }

            deltaTime += dt;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(readyToPlay) {
            sb.begin();

            sb.draw(fieldTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            Vector2 screenPosition;
            Ball b = match.getBall();
            b.setPositionToBody();
            screenPosition = b.getScreenCoordinates();

<<<<<<< HEAD
            if (match.getCurrentState() == Match.matchState.Score) {
                sb.draw(explosionAnimation.getKeyFrame(scoreAnimationTime * Constants.EXPLOSION_SPEED, true), explosionPos.x - Constants.EXPLOSION_WIDTH / 2, explosionPos.y - Constants.EXPLOSION_HEIGHT / 2, Constants.EXPLOSION_WIDTH, Constants.EXPLOSION_HEIGHT);
                scoreAnimationTime += Gdx.graphics.getDeltaTime();
            } else {
                sb.draw(ballAnimation.getKeyFrame(deltaTime, true), screenPosition.x, screenPosition.y, b.getRadius() * 2 * 100f, b.getRadius() * 2 * 100f);
            }
=======
        if(match.getCurrentState() == Match.matchState.Score) {
            sb.draw(explosionAnimation.getKeyFrame(scoreAnimationTime * Constants.EXPLOSION_SPEED, true), explosionPos.x - Constants.EXPLOSION_WIDTH/2, explosionPos.y - Constants.EXPLOSION_HEIGHT/2, Constants.EXPLOSION_WIDTH, Constants.EXPLOSION_HEIGHT);
            scoreAnimationTime += Gdx.graphics.getDeltaTime();
        } else{
            sb.draw(ballAnimation.getKeyFrame(deltaTime, true), screenPosition.x, screenPosition.y, b.getRadius()*2 * 100f, b.getRadius()*2* 100f);
        }
>>>>>>> origin/master

            //Teams
            ArrayList<Player> homeTeamPlayers = match.getHomeTeam().getPlayers();
            ArrayList<Player> visitorTeamPlayers = match.getVisitorTeam().getPlayers();

            float radius = homeTeamPlayers.get(0).getBoundingRadius();

            for (int i = 0; i < Constants.NUMBER_PLAYER_ONLINE; i++) {
                homeTeamPlayers.get(i).setPositionToBody();
                screenPosition = homeTeamPlayers.get(i).getScreenCoordinates();
                sb.draw(homeTeamTexture, screenPosition.x, screenPosition.y, homeTeamPlayers.get(i).getBoundingRadius() * 2 * 100f, homeTeamPlayers.get(i).getBoundingRadius() * 2 * 100f);
                font.draw(sb, homeTeamPlayers.get(i).getName(), screenPosition.x + radius * 100f / 2, screenPosition.y + radius * 100f);

                visitorTeamPlayers.get(i).setPositionToBody();
                screenPosition = visitorTeamPlayers.get(i).getScreenCoordinates();
                sb.draw(visitorTeamTexture, screenPosition.x, screenPosition.y, visitorTeamPlayers.get(i).getBoundingRadius() * 2 * 100f, visitorTeamPlayers.get(i).getBoundingRadius() * 2 * 100f);
                font.draw(sb, visitorTeamPlayers.get(i).getName(), screenPosition.x + radius * 100f / 2, screenPosition.y + radius * 100f);
            }

            font.draw(sb, Integer.toString(match.getScoreHomeTeam()), width / 4, height - height / 6);
            font.draw(sb, Integer.toString(match.getScoreVisitorTeam()), width - width / 4, height - height / 6);

            font.draw(sb, match.getTime(), width / 2 - 28, height - height / 6);

            Goal g = match.getHomeTeamGoal();
            screenPosition = g.getScreenCoordinates();
            float vertLength = match.getHomeTeamGoal().getVerticalLength() * Constants.BOX_TO_WORLD;
            float horLength = match.getHomeTeamGoal().getHorizontalLength() * Constants.BOX_TO_WORLD;
            sb.draw(goalTexture, screenPosition.x, screenPosition.y, horLength, vertLength);

            g = match.getVisitorTeamGoal();
            screenPosition = g.getScreenCoordinates();
            sb.draw(goalTexture, screenPosition.x, screenPosition.y, -horLength, vertLength);

<<<<<<< HEAD
            if (match.getPowerUp().isActive()) {
                Vector2 powerUpPos = match.getPowerUp().getPosition();
                sb.draw(powerUpAnimation.getKeyFrame(powerUpAnimationTime * Constants.EXPLOSION_SPEED, true), powerUpPos.x, powerUpPos.y, Constants.EXPLOSION_WIDTH, Constants.EXPLOSION_HEIGHT);
                powerUpAnimationTime += Gdx.graphics.getDeltaTime();
            }
=======
        if(match.getPowerUp().isActive()){
            Vector2 powerUpPos = match.getPowerUp().getScreenCoordinates();
            sb.draw(powerUpAnimation.getKeyFrame(powerUpAnimationTime * Constants.PowerUpSpeed, true), powerUpPos.x, powerUpPos.y, Constants.PowerUpWidth, Constants.PowerUpHeight);
            powerUpAnimationTime += Gdx.graphics.getDeltaTime();
        }
>>>>>>> origin/master

            for (int i = 0; i < rain.getRainSize(); i++)
                sb.draw(rainTexture, rain.getPosition(i).x, rain.getPosition(i).y, width / 3, height / 3);

            sb.end();

            if (gameplayController == 2) {
                stage.act(Gdx.graphics.getDeltaTime());
                stage.draw();
            }
            debugRenderer.render(match.getWorld(), camera.combined);
        }
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
    }
}
