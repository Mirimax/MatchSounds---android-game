package com.mygdx.pianogame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.pianogame.GameClass;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;

public class LoadingScreen implements Screen {
    private final GameClass app;
    private ShapeRenderer shapeRenderer;
    private GlyphLayout loadingText;

    public LoadingScreen(final GameClass app){
        this.app = app;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        loadingAssets();
        loadingText = new GlyphLayout(app.fontBig,"Loading...");
    }

    private void update(float delta){
        if(app.assetManager.update()){
            app.setScreen(app.menu);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);


        //Printing loading... text
        app.batch.begin();
        app.fontBig.draw(app.batch,loadingText,Gdx.graphics.getWidth()/2 - loadingText.width/2,Gdx.graphics.getHeight()/5);
        app.batch.end();
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
        for(int i =
            +0; i<88;i++) app.assetManager.load("sounds/singlenotes/"+ (i+1) + ".mp3", Sound.class);
        app.assetManager.load("ui/uiskin.atlas", TextureAtlas.class);
    }
}
