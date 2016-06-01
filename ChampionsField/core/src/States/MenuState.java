package States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;
import java.util.Random;

import logic.Ball;
import logic.Field;
import server.MPClient;

public class MenuState extends State  {
    enum CameraState {
        MovingLeft, MovingRight, MovingUp, MovingDown;
    }

    //Camera variables related
    private OrthographicCamera camera;
    private CameraState cameraState;
    private float cameraSpeed;

    private SpriteBatch sbUnique;
    private ArrayList<Texture> background;
    private int currBack;
    private Random random;
    private Stage stage;
    private BitmapFont font;
    private TextureAtlas buttonsAtlas;
    private Skin buttonSkin;
    private TextButton playBtn;
    private TextButton settingsBtn;
    private TextButton exitBtn;

    private World world;

    private Field field;
    private ArrayList<Ball> balls;
    private TextureAtlas ballTexture;
    private Animation ballAnimation;

    //Time variables
    private float displayFieldTime;
    private float timeBetweenDisplay;
    private float deltaTime;

    public MenuState(GameStateManager gsm) {
        super(gsm);

        //Camera initialization
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width / 2, height / 2);
        cameraState = CameraState.MovingRight;
        cameraSpeed = 90;

        //Load of all the available fields
        background = new ArrayList<Texture>();
        background.add(new Texture("Field.jpg"));
        background.add(new Texture("FieldGalaxy.jpg"));
        random = new Random();
        currBack = random.nextInt(background.size());
        displayFieldTime = 0;
        timeBetweenDisplay = 3;

        stage = new Stage();
        buttonsAtlas = new TextureAtlas("button.pack");
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas);
        font = new BitmapFont();

        //Creating the buttons style
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = buttonSkin.getDrawable("buttonOff");
        style.down = buttonSkin.getDrawable("buttonOn");
        style.font = font;

        //Creating buttons
        playBtn = new TextButton("Play", style);
        playBtn.setHeight(width / 9.6f);
        playBtn.setWidth(height / 5.4f);

        settingsBtn = new TextButton("Settings", style);
        settingsBtn.setHeight(width / 9.6f);
        settingsBtn.setWidth(height / 5.4f);

        exitBtn = new TextButton("Exit", style);
        exitBtn.setHeight(width / 9.6f);
        exitBtn.setWidth(height / 5.4f);

        //Setting the buttons position
        playBtn.setPosition(width / 2 - playBtn.getWidth() / 2, height - height / 4 - playBtn.getHeight() / 2);
        settingsBtn.setPosition(width / 2 - settingsBtn.getWidth() / 2, height / 2 - settingsBtn.getHeight() / 2);
        exitBtn.setPosition(width / 2 - exitBtn.getWidth() / 2, height / 4 - exitBtn.getHeight() / 2);

        addListeners();

        //Adding the buttons to the stage
        stage.addActor(playBtn);
        stage.addActor(settingsBtn);
        stage.addActor(exitBtn);

        Gdx.input.setInputProcessor(stage);
        sbUnique = new SpriteBatch();

        //Physic objects creation
        world = new World(new Vector2(0, 0), true);
        field = new Field(world);

        balls = new ArrayList<Ball>();
        balls.add(new Ball(-width / 4, height / 4, 32, world));
        balls.add(new Ball(-width / 4, -height / 4, 32, world));
        balls.add(new Ball(width / 4, height / 4, 32, world));
        balls.add(new Ball(width / 4, -height / 4, 32, world));
        balls.add(new Ball(0, 0, 32, world));

        for(int i = 0; i < balls.size(); i++)
            balls.get(i).getBody().setLinearVelocity(random.nextInt(5 + 5) - 5, random.nextInt(5 + 5) - 5);

        ballTexture = new TextureAtlas("SoccerBall.atlas");
        ballAnimation = new Animation(1 / 15f, ballTexture.getRegions());
        deltaTime = 0;
    }

    void addListeners() {
        playBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                changeState(0);
            }
        });

        settingsBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                changeState(1);
            }
        });

        exitBtn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                changeState(2);
            }
        });
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
        if(cameraState == CameraState.MovingRight) {
            camera.position.x += cameraSpeed * dt;
            if(camera.position.x + width / 4 >= width)
                cameraState = CameraState.MovingUp;
        } else if(cameraState == CameraState.MovingUp) {
            camera.position.y += cameraSpeed * dt;
            if(camera.position.y + height / 4 >= height)
                cameraState = CameraState.MovingLeft;
        } else if(cameraState == CameraState.MovingLeft) {
            camera.position.x -= cameraSpeed * dt;
            if(camera.position.x - width / 4 <= 0)
                cameraState = CameraState.MovingDown;
        } else if(cameraState == CameraState.MovingDown) {
            camera.position.y -= cameraSpeed * dt;
            if(camera.position.y - height / 4 <= 0)
                cameraState = CameraState.MovingRight;
        }

        if(displayFieldTime >= timeBetweenDisplay) {
            currBack = random.nextInt(background.size());
            displayFieldTime = 0;

            for(int i = 0; i < balls.size(); i++)
                balls.get(i).getBody().setLinearVelocity(random.nextInt(5 + 5) - 5, random.nextInt(5 + 5) - 5);
        }

        world.step(1f / 60f, 6, 2);
        camera.update();
        displayFieldTime += dt;
    }

    @Override
    public void render(SpriteBatch sb) {
        sbUnique.setProjectionMatrix(camera.combined);

        deltaTime += Gdx.graphics.getDeltaTime();

        sbUnique.begin();
        sbUnique.draw(background.get(currBack), 0, 0, width, height);
        for(int i = 0; i < balls.size(); i++) {
            balls.get(i).setPositionToBody();
            Vector2 screenPosition = convertToScreenCoordinates(balls.get(i));
            sbUnique.draw(ballAnimation.getKeyFrame(deltaTime, true), screenPosition.x, screenPosition.y, balls.get(0).getRadius()*2, balls.get(0).getRadius()*2);
        }
        sbUnique.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        for(int i = 0; i < background.size(); i++)
            background.get(i).dispose();
        stage.dispose();
        font.dispose();
        buttonsAtlas.dispose();
        buttonSkin.dispose();
    }

    private void changeState(int state) {
        switch(state) {
            case 0:
                gsm.set(new PlayState(gsm));
                //MPClient client = new MPClient("1", 0, gsm);
                break;
            case 1: gsm.set(new Options(gsm)); break;
            case 2: Gdx.app.exit(); break;
            default: break;
        }
    }

    private Vector2 convertToScreenCoordinates(Ball b) {
        float x = b.getPosition().x * 100f + Gdx.graphics.getWidth()/2 - b.getRadius();
        float y = b.getPosition().y * 100f + Gdx.graphics.getHeight()/2 - b.getRadius();
        return new Vector2(x, y);
    }
}
