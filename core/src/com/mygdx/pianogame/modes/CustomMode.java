package com.mygdx.pianogame.modes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.pianogame.GameClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/** Class that represents the custom game mode. It generates selects buttons by which is possible to adjust the difficulty of a level.
 *  @author Paweł Platta */
public class CustomMode implements Screen {
    private final GameClass app;
    private Stage stage;
    private Random random;

    private ArrayList<Integer> correctSequence;
    private ArrayList<Integer> pianoTilesToShow;
    private int listenAgainCnt;

    /** {@link Slider} of number of tiles in the answer. */
    public Slider numberOfTilesSlider;
    /** {@link Slider} of number of additional tiles not included in the answer. */
    public Slider numberOfAdditionalTilesSlider;
    /** {@link Slider} of number of times to listen again. */
    public Slider listenAgainSlider;
    private GlyphLayout numberOfTilesText;
    private GlyphLayout numberOfAdditionalTilesText;
    private GlyphLayout listenAgainText;

    /** @param app Main instance of {@link com.badlogic.gdx.Game}. */
    public CustomMode(final GameClass app){
       this.app = app;
       stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
       random = new Random();
       correctSequence = new ArrayList<Integer>();
       pianoTilesToShow = new ArrayList<Integer>();
    }

    /** Called when this screen becomes the current screen for a Game.
     * Sets the default values for variables and calls {@link #initSelectBoxes()} and {@link #initButtons()} function. */
    @Override
    public void show() {
        stage.clear();

        Texture backgroundTex = app.assetManager.get("img/background.png",Texture.class);
        Image backgroundImage = new Image(backgroundTex);
        backgroundImage.setSize(stage.getWidth(),stage.getHeight());
        stage.addActor(backgroundImage);

        initSelectBoxes();
        initButtons();

        Gdx.input.setInputProcessor(stage);
    }

    /** Called when the screen should render itself.
     *  Calls {@link #displayText()} function. */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        displayText();

        stage.draw();
        stage.act();
    }

    /** Displays text. */
    public void displayText(){
        numberOfTilesText.setText(app.font,"Number of answer tiles: " + (int)numberOfTilesSlider.getValue());
        numberOfAdditionalTilesText.setText(app.font,"Number of additional tiles: " + (int)numberOfAdditionalTilesSlider.getValue());
        listenAgainText.setText(app.font,"Times to listen again: " + (int)listenAgainSlider.getValue());
        app.batch.begin();
        app.font.draw(app.batch,numberOfTilesText,numberOfTilesSlider.getX(),numberOfTilesSlider.getY() + 150);
        app.font.draw(app.batch,numberOfAdditionalTilesText,numberOfAdditionalTilesSlider.getX(),numberOfAdditionalTilesSlider.getY() + 150);
        app.font.draw(app.batch,listenAgainText,listenAgainSlider.getX(),listenAgainSlider.getY() + 150);
        app.batch.end();
    }

    /** Initialize select boxes. */
    public void initSelectBoxes(){
        numberOfTilesSlider = new Slider(1f,6f,1f,false,app.skinSmall);
        numberOfTilesSlider.setWidth(stage.getWidth()/3);
        numberOfTilesSlider.setPosition(stage.getWidth()/2 - numberOfTilesSlider.getWidth()/2,stage.getHeight()/5*4);
        numberOfTilesSlider.getStyle().knob.setMinHeight(75);
        numberOfTilesSlider.getStyle().knob.setMinWidth(30);
        stage.addActor(numberOfTilesSlider);

        numberOfTilesText = new GlyphLayout(app.font,"Number of answer tiles: ");

        numberOfAdditionalTilesSlider = new Slider(0f,8f,1f,false,app.skinSmall);
        numberOfAdditionalTilesSlider.setWidth(stage.getWidth()/3);
        numberOfAdditionalTilesSlider.setPosition(stage.getWidth()/2 - numberOfAdditionalTilesSlider.getWidth()/2,stage.getHeight()/5*3);
        numberOfAdditionalTilesSlider.getStyle().knob.setTopHeight(75);
        numberOfAdditionalTilesSlider.getStyle().knob.setMinWidth(30);
        stage.addActor(numberOfAdditionalTilesSlider);

        numberOfAdditionalTilesText = new GlyphLayout(app.font,"Number of additional tiles: ");

        listenAgainSlider = new Slider(1f,5f,1f,false,app.skinSmall);
        listenAgainSlider.setWidth(stage.getWidth()/3);
        listenAgainSlider.setPosition(stage.getWidth()/2 - listenAgainSlider.getWidth()/2,stage.getHeight()/5*2);
        listenAgainSlider.getStyle().knob.setMinHeight(75);
        listenAgainSlider.getStyle().knob.setMinWidth(30);
        stage.addActor(listenAgainSlider);

        listenAgainText = new GlyphLayout(app.font,"Times to listen again: ");
    }

    /** Generate sequences according to the current state of select buttons. */
    public void generateSequence(){
        correctSequence.clear();
        pianoTilesToShow.clear();
        for(int i=0;i<numberOfTilesSlider.getValue();i++){
            correctSequence.add(random.nextInt(88));
            pianoTilesToShow.add(correctSequence.get(i));
        }
        for(int i=0;i<numberOfAdditionalTilesSlider.getValue();i++){
            pianoTilesToShow.add(random.nextInt(88));
        }
        Collections.shuffle(pianoTilesToShow);
    }

    /** Initialize navigation buttons. */
    public void initButtons(){
        TextButton backButton = new TextButton("back", app.skinSmall, "default");
        backButton.setSize(300,100);
        backButton.setPosition(50,stage.getHeight()-150);
        backButton.setName("backButton");
        backButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ((GameClass)Gdx.app.getApplicationListener()).setScreen(app.menu);
            }
        });
        stage.addActor(backButton);

        TextButton startButton = new TextButton("start", app.skinSmall, "default");
        startButton.setSize(300,100);
        startButton.setPosition(stage.getWidth()/2-startButton.getWidth()/2,150);
        startButton.setName("startButton");
        startButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                generateSequence();
                listenAgainCnt = (int)listenAgainSlider.getValue();
                app.setScreen(app.matchSounds);
            }
        });
        stage.addActor(startButton);
    }

    /** Return a sequence that is not included in the answer. */
    public ArrayList<Integer> getPianoTilesToShow() {
        return pianoTilesToShow;
    }

    /** Return a sequence that represents the answer. */
    public ArrayList<Integer> getCorrectSequence() {
        return correctSequence;
    }

    /** Return the number of times a player can listen again to the sequence during the listening part. */
    public int getListenAgainCnt(){
        return listenAgainCnt;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }
}
