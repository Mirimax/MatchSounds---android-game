package com.mygdx.pianogame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.pianogame.GameClass;


public class Menu implements Screen {
    private final GameClass app;
    private Stage stage;

    private Boolean showInfo;

    private GlyphLayout mathSoundText;
    private GlyphLayout additionalInfoText;
    private TextButton survivalMode;
    private TextButton customMode;
    private TextButton exit;
    private TextButton info;

    public Menu(final GameClass app){
        this.app = app;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
    }

    @Override
    public void show() {
        stage.clear();

        showInfo = false;

        //Text setup
        mathSoundText = new GlyphLayout(app.fontBig,"Match sounds");
        additionalInfoText = new GlyphLayout(app.fontSmallBlack,"Survival - best score: " + Gdx.app.getPreferences("Prefs").getInteger("survivalBest") +
                "\nSurvival games played: " + app.prefs.getInteger("survivalGamesPlayed",0) +
                "\nTotal rounds played: " + app.prefs.getInteger("roundsPlayed",0));

        //Settings background image.
        Texture backgroundTex = app.assetManager.get("img/background.png",Texture.class);
        Image backgroundImage = new Image(backgroundTex);
        backgroundImage.setSize(stage.getWidth(),stage.getHeight());
        stage.addActor(backgroundImage);

        initButtons();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        displayText();

        stage.act(delta);
        stage.draw();
    }

    private void displayText(){
        app.batch.begin();
        if(showInfo){
            app.fontSmallBlack.draw(app.batch,additionalInfoText,stage.getWidth() - additionalInfoText.width - 50,stage.getHeight()- 100 - additionalInfoText.height);
        }
        app.fontBig.draw(app.batch,mathSoundText,stage.getWidth()/2f - mathSoundText.width/2,survivalMode.getY()/2+survivalMode.getHeight()/2+stage.getHeight()/2 + mathSoundText.height/2);
        app.batch.end();
    }

    private void initButtons(){
        //Survival mode button
        survivalMode = new TextButton("Survival", app.skin);
        survivalMode.setSize(600f,200f);
        survivalMode.setPosition(stage.getWidth()/2 - survivalMode.getWidth()/2,stage.getHeight()/2);
        survivalMode.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                app.gamemode = GameClass.GAMEMODE.SURVIVAL;
                app.setScreen(app.matchSounds);
            }
        });
        stage.addActor(survivalMode);

        //Custom mode button
        customMode = new TextButton("Custom", app.skin, "default");
        customMode.setSize(600,200);
        customMode.setPosition(stage.getWidth()/2 - customMode.getWidth()/2,survivalMode.getY()-25-customMode.getHeight());
        customMode.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                app.gamemode = GameClass.GAMEMODE.CUSTOM;
                app.setScreen(app.customMode);
            }
        });
        stage.addActor(customMode);

        //Exit from game button
        exit = new TextButton("Exit", app.skin, "default");
        exit.setSize(600,200);
        exit.setPosition(stage.getWidth()/2 - exit.getWidth()/2,customMode.getY()-25-exit.getHeight());
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

        //Info button for information stored in local memory
        info = new TextButton("info", app.skinSmall, "default");
        info.setSize(250,100);
        info.setPosition(stage.getWidth() - info.getWidth() - 100,stage.getHeight() - info.getHeight() - 50);
        info.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                showInfo = !showInfo;
            }
        });
        stage.addActor(info);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
