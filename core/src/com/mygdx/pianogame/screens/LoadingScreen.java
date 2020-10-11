package com.mygdx.pianogame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.pianogame.GameClass;

public class LoadingScreen implements Screen {
    private final GameClass app;
    private ShapeRenderer shapeRenderer;
    private float progress;
    public LoadingScreen(final GameClass app){
        this.app = app;
        this.shapeRenderer = new ShapeRenderer();
        this.progress = 0f;

        loadingAssets();
    }
    @Override
    public void show() {

    }

    private void update(float delta){
        if(app.assetManager.update()){
            app.setScreen(new Menu(app));
        }
    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);
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
        shapeRenderer.dispose();
    }

    //loading all assets at launch
    private void loadingAssets(){
        app.assetManager.load("img/buttons/play.png", Texture.class);
        app.assetManager.load("img/buttons/option1.png", Texture.class);
        for(int i = 0; i<88;i++) app.assetManager.load("sounds/singlenotes/"+ (i+1) + ".mp3", Sound.class);
    }
}
