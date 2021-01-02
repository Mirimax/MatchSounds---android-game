package com.mygdx.pianogame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.pianogame.GameClass;


public class Menu implements Screen {
    private final GameClass app;
    private Stage stage;

    private TextButton.TextButtonStyle textButtonStyle;

    private GlyphLayout mathSoundText;

    public Menu(final GameClass app){
        this.app = app;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        //Text of the title of the game
        mathSoundText = new GlyphLayout(app.fontBig,"Match sounds");

        initButtons();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Displaying the title of game
        app.batch.begin();
        app.fontBig.draw(app.batch,mathSoundText,Gdx.graphics.getWidth()/2f - mathSoundText.width/2,Gdx.graphics.getHeight()- 100 - mathSoundText.height);
        app.batch.end();

        update(delta);
        stage.draw();
    }

    private void initButtons(){
        //Option nr.1 - Survival mode
        TextButton survivalMode = new TextButton("Survival", app.skin);
        survivalMode.setPosition(stage.getWidth()/2 - 300,stage.getHeight()/2);
        survivalMode.setSize(600f,200f);
//        survivalMode.setColor(0.302f,0.302f,0.302f,1f);
        survivalMode.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                app.gamemode = GameClass.GAMEMODE.SURVIVAL;
                ((GameClass)Gdx.app.getApplicationListener()).setScreen(app.matchSounds);
            }
        });
        stage.addActor(survivalMode);

        //Option nr.2 - Custom mode
        TextButton customMode = new TextButton("Custom", app.skin, "default");
        customMode.setPosition(stage.getWidth()/2 - 300,stage.getHeight()/2-225);
        customMode.setSize(600,200);
        customMode.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                app.gamemode = GameClass.GAMEMODE.CUSTOM;
                ((GameClass)Gdx.app.getApplicationListener()).setScreen(app.matchSounds);
            }
        });
        stage.addActor(customMode);

        //Option nr.3 - Exit from game
        TextButton exit = new TextButton("Exit", app.skin, "default");
        exit.setPosition(stage.getWidth()/2 - 300,stage.getHeight()/2 - 450);
        exit.setSize(600,200);
        exit.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });
        stage.addActor(exit);
    }

    @Override
    public void resize(int width, int height) {

    }

    private void update(float delta) {
        stage.act(delta);
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
