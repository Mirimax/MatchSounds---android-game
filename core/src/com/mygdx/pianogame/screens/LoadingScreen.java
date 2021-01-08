package com.mygdx.pianogame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
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
    private int timer;
    private int cnt;
    private String text;

    public LoadingScreen(final GameClass app){
        this.app = app;
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        loadingAssets();
        loadingText = new GlyphLayout(app.fontBig,"Loading...");
        timer=0;
        cnt = 1;
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

        //Printing text
        timer++;
        if(timer > 20){
            timer = 0;
            cnt++;
            if(cnt == 4) cnt = 1;
        }
        text = "Loading";
        for(int i=0; i<cnt; i++) text += ".";
        loadingText.setText(app.fontBig,text);
        app.batch.begin();
        app.fontBig.draw(app.batch,loadingText,Gdx.graphics.getWidth()/2f - loadingText.width/2,Gdx.graphics.getHeight()/5f);
        app.batch.end();

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
        app.assetManager.load("img/background.png", Texture.class);
        app.assetManager.load("img/background1.png", Texture.class);
        app.assetManager.load("img/background2.png", Texture.class);
        for(int i = +0; i<88;i++) app.assetManager.load("sounds/singlenotes/"+ (i+1) + ".mp3", Sound.class);
        app.assetManager.load("ui/uiskin.atlas", TextureAtlas.class);
        app.assetManager.load("img/note.png", Texture.class);
        app.assetManager.load("sounds/empty_tile.mp3", Sound.class);
    }
}
