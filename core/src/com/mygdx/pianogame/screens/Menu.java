package com.mygdx.pianogame.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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


public class Menu implements Screen {
    private final GameClass app;
    private Stage stage;
    private Image matchSoundsImg;

    public Menu(final GameClass app){
        this.app = app;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);


        //Adding game mode nr.1 MatchSound
        Texture matchSoundsTex = app.assetManager.get("img/buttons/option1.png",Texture.class);
        matchSoundsImg = new Image(matchSoundsTex);
        matchSoundsImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ((GameClass)Gdx.app.getApplicationListener()).dispose();
                ((GameClass)Gdx.app.getApplicationListener()).setScreen(new MatchSounds(app));
                return true;
            }
        });
        stage.addActor(matchSoundsImg);


    }

    @Override
    public void show() {
        matchSoundsImg.setPosition(Gdx.graphics.getWidth()/2 - matchSoundsImg.getWidth()/2,Gdx.graphics.getHeight()*4/5 - matchSoundsImg.getHeight()/2);
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
