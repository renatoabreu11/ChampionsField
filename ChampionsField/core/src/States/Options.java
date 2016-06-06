package States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ai.msg.PriorityQueue;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

import utils.Constants;
import utils.Statistics;

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
    private SelectBox<String> initialTeam;

    private Label nameLabel;
    private Label playersPerTeamLabel;
    private Label startingTeamLabel;
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
        int numOfPlayers =  prefs.getInteger("Number Of Players", 3);
        if(name.equals("Default value"))
            name = "";

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        nameField = new TextField(name, skin);
        nameField.setPosition(Constants.ScreenWidth/2 + nameField.getWidth()/2, Constants.ScreenHeight/2 + nameField.getHeight()*6);
        nameField.setMaxLength(3);
        nameField.setAlignment(1);

        playersPerTeam = new TextField(Integer.toString(numOfPlayers), skin);
        playersPerTeam.setPosition(Constants.ScreenWidth/2 + playersPerTeam.getWidth()/2, Constants.ScreenHeight/2 + playersPerTeam.getHeight()*3);
        playersPerTeam.setAlignment(1);

        button = new TextButton("Back", style);
        button.setHeight(Constants.buttonHeight);
        button.setWidth(Constants.buttonWidth);

        button.setPosition(Constants.ScreenWidth/2 - button.getWidth()/2,  button.getHeight()/2);

        nameLabel = new Label("Username", skin);
        nameLabel.setAlignment(1);
        nameLabel.setPosition(Constants.ScreenWidth/2 - nameField.getWidth(), Constants.ScreenHeight/2 + nameField.getHeight()*6 + nameLabel.getHeight()/4);

        playersPerTeamLabel = new Label("Players per Team", skin);
        playersPerTeamLabel.setAlignment(1);
        playersPerTeamLabel.setPosition(Constants.ScreenWidth/2 - playersPerTeam.getWidth(), Constants.ScreenHeight/2 + playersPerTeamLabel.getHeight()*4);

        initialTeam = new SelectBox<String>(skin);
        String[] choices ={
            "Blue", "Red"
        };
        initialTeam.setItems(choices);
        String selectedTeam = prefs.getString("Starting Team", "Red");
        initialTeam.setSelected(selectedTeam);
        initialTeam.setWidth(nameField.getWidth());
        initialTeam.setHeight(nameField.getHeight());
        initialTeam.setPosition(Constants.ScreenWidth/2 + initialTeam.getWidth()/2, Constants.ScreenHeight/2 - initialTeam.getHeight());

        startingTeamLabel = new Label("Multiplayer Initial Team", skin);
        startingTeamLabel.setAlignment(1);
        startingTeamLabel.setPosition(Constants.ScreenWidth/2 - startingTeamLabel.getWidth(), Constants.ScreenHeight/2 - startingTeamLabel.getHeight());


        addListeners();

        stage.addActor(button);
        stage.addActor(nameField);
        stage.addActor(playersPerTeam);
        stage.addActor(nameLabel);
        stage.addActor(playersPerTeamLabel);
        stage.addActor(initialTeam);
        stage.addActor(startingTeamLabel);

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
                String numPlayers = playersPerTeam.getText();

                int players = 0;
                try {
                    players = Integer.parseInt(numPlayers);
                    if(players < 1 || players > 3){
                        diag = new Dialog("Warning", skin) {
                            {
                                text("Please provide a valid number of players, between 1 to 3.");
                                button("Ok");
                            }

                            @Override
                            public void result(Object obj) {
                                diag.hide();
                            }
                        }.show(stage);
                        selectedOption = 0;
                    }
                } catch (NumberFormatException e) {
                    diag = new Dialog("Warning", skin) {
                        {
                            text("Please provide a valid number of players, between 1 to 3.");
                            button("Ok");
                        }

                        @Override
                        public void result(Object obj) {
                            diag.hide();
                        }
                    }.show(stage);
                    selectedOption = 0;
                }

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
                }

                if(checkIfExists(n)){
                    System.out.println(n);
                        diag = new Dialog("Warning", skin) {
                            {
                                text("The username you chosen already exists. Please choose another one!");
                                button("Ok");
                            }

                            @Override
                            public void result(Object obj) {
                                diag.hide();
                            }
                        }.show(stage);
                        selectedOption = 0;
                }

                if(selectedOption != 0){
                    Preferences prefs = Gdx.app.getPreferences("My Preferences");
                    prefs.putString("Name", n);
                    prefs.putInteger("Number Of Players", players);
                    prefs.putString("Starting Team", initialTeam.getSelected());
                    selectedOption = 2;
                }
                break;
            case 2:
                gsm.set(new MenuState(gsm));
        }
    }

    private boolean checkIfExists(String n) {
        Statistics parser = new Statistics("", 0, 0);
        ArrayList<Statistics> stats = new ArrayList<Statistics>();

        FileHandle globalStatistics = Gdx.files.internal("Statistics.txt");
        String info;
        boolean exist;
        exist=Gdx.files.internal("Statistics.txt").exists();
        if(!exist){
            System.out.println("Error opening file!");
            return false;
        } else{
            info = globalStatistics.readString();
            stats = parser.parseStatisticsToArray(info);
        }

        for(Statistics stat : stats){
            if(stat.getName().equals(n))
                return true;
        }

        return false;
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
