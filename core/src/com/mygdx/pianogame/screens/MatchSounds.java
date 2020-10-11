package com.mygdx.pianogame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.pianogame.GameClass;
import com.mygdx.pianogame.SimpleButton;


public class MatchSounds implements Screen {
    private final GameClass app;
    private Stage stage;
    private Image playImg;
    Sound[] singlenotes;
    int cnt = 0;
    public MatchSounds(final GameClass app){
        this.app = app;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        //adding sounds to this game mode
        singlenotes = new Sound[88];
        for(int i = 0; i<88;i++) singlenotes[87-i] = app.assetManager.get("sounds/singlenotes/"+ (i+1) + ".mp3",Sound.class);

        //Adding option nr.1 of menu called MatchSound
        Texture playTex = app.assetManager.get("img/buttons/play.png",Texture.class);
        playImg = new Image(playTex);
        playImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                singlenotes[cnt++].play();
                if(cnt == 88) cnt = 0;
                return true;
            }
        });
        stage.addActor(playImg);

    }

    @Override
    public void show() {
        playImg.setPosition(Gdx.graphics.getWidth()/2 - playImg.getWidth()/2,Gdx.graphics.getHeight()/2 - playImg.getHeight()/2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
        stage.draw();
    }

    private void update(float delta) {
        stage.act(Gdx.graphics.getDeltaTime());
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
