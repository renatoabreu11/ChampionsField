package States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.PriorityQueue;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.ArrayList;

import utils.Constants;
import utils.Statistics;

public class Leaderboard extends State {
    private Stage stage;
    private int back;
    private Texture background;
    private BitmapFont font;
    private TextureAtlas buttonsAtlas;
    private Skin buttonSkin;
    private TextButton button;

    private ArrayList<Statistics> highScores;

    public Leaderboard(GameStateManager gsm) {
        super(gsm);

        background = new Texture("Leaderboard.png");
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
        button.setHeight(125 * Constants.leaderboardHeightScale);
        button.setWidth(100 * Constants.leaderboardWidthScale);

        button.setPosition(300 * Constants.leaderboardWidthScale,  0);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return  true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                back  = 1;
            }
        }) ;

        stage.addActor(button);

        Gdx.input.setInputProcessor(stage);

        highScores = new ArrayList<Statistics>();
        Statistics parser = new Statistics("", 0, 0);
        PriorityQueue<Statistics> scores = new PriorityQueue<Statistics>();
        ArrayList<Statistics> stats = new ArrayList<Statistics>();

        FileHandle globalStatistics = Gdx.files.local("Statistics.txt");
        String info;
        boolean exist;
        exist=Gdx.files.local("Statistics.txt").exists();
        if(!exist){
            System.out.println("Error opening file!");
            return;
        } else{
            info = globalStatistics.readString();
            scores = parser.parseHighScores(info);
        }

        while(scores.size() != 0){
            highScores.add(scores.peek());
            scores.poll();
        }

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
       if(back == 1) {
           dispose();
           gsm.set(new MenuState(gsm));
       }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0, 0, width, height);
        for(int i = 0; i < highScores.size(); i++){
            font.draw(sb, highScores.get(i).getName(), 175 * Constants.leaderboardWidthScale, 140 * Constants.leaderboardHeightScale + i * 62 * Constants.leaderboardHeightScale);
            font.draw(sb, Integer.toString(highScores.get(i).getGoalsScored()), 385 * Constants.leaderboardWidthScale, 150 * Constants.leaderboardHeightScale + i * 62 * Constants.leaderboardHeightScale);
            font.draw(sb, Integer.toString(highScores.get(i).getMatchesPlayed()), 600 * Constants.leaderboardWidthScale, 150 * Constants.leaderboardHeightScale + i * 62 * Constants.leaderboardHeightScale);
        }
        sb.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
