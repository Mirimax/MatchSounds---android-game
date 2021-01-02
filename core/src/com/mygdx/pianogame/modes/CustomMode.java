package com.mygdx.pianogame.modes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.pianogame.GameClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CustomMode implements Screen {
    private final GameClass app;
    private Stage stage;
    private Random random;

    private ArrayList<Integer> correctSequence;
    private ArrayList<Integer> pianoTilesToShow;

    private Slider numberOfTilesSlider;
    private GlyphLayout numberOfTilesText;
    private Slider numberOfAdditionalTilesSlider;
    private GlyphLayout numberOfAdditionalTilesText;

    public CustomMode(final GameClass app){
       this.app = app;
       stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
       random = new Random();
       correctSequence = new ArrayList<Integer>();
       pianoTilesToShow = new ArrayList<Integer>();
    }
    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        initSelectBoxes();
        initButtons();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //text update
        numberOfTilesText.setText(app.font,"Number of answer tiles: " + (int)numberOfTilesSlider.getValue());
        numberOfAdditionalTilesText.setText(app.font,"Number of additional tiles: " + (int)numberOfAdditionalTilesSlider.getValue());
        app.batch.begin();
        app.font.draw(app.batch,numberOfTilesText,numberOfTilesSlider.getX(),numberOfTilesSlider.getY() + numberOfTilesSlider.getHeight() + numberOfTilesText.height + 10);
        app.font.draw(app.batch,numberOfAdditionalTilesText,numberOfAdditionalTilesSlider.getX(),numberOfAdditionalTilesSlider.getY() + numberOfAdditionalTilesSlider.getHeight() + numberOfAdditionalTilesText.height + 10);
        app.batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void initSelectBoxes(){
        numberOfTilesSlider = new Slider(1f,10f,1f,false,app.skinSmall);
        numberOfTilesSlider.setWidth(stage.getWidth()/3);
        numberOfTilesSlider.setPosition(stage.getWidth()/2 - numberOfTilesSlider.getWidth()/2,stage.getHeight()/5*4);
        stage.addActor(numberOfTilesSlider);

        numberOfTilesText = new GlyphLayout(app.font,"Number of answer tiles: ");

        numberOfAdditionalTilesSlider = new Slider(1f,5f,1f,false,app.skinSmall);
        numberOfAdditionalTilesSlider.setWidth(stage.getWidth()/3);
        numberOfAdditionalTilesSlider.setPosition(stage.getWidth()/2 - numberOfAdditionalTilesSlider.getWidth()/2,stage.getHeight()/5*3);
        stage.addActor(numberOfAdditionalTilesSlider);

        numberOfAdditionalTilesText = new GlyphLayout(app.font,"Number of additional tiles: ");
    }

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
    public ArrayList<Integer> getPianoTilesToShow() {
        return pianoTilesToShow;
    }

    public ArrayList<Integer> getCorrectSequence() {
        return correctSequence;
    }

    private void initButtons(){
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
                app.setScreen(app.matchSounds);
            }
        });
        stage.addActor(startButton);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
