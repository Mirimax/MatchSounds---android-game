package com.mygdx.pianogame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.*;
import com.mygdx.pianogame.screens.LoadingScreen;
import com.mygdx.pianogame.screens.Menu;


public class GameClass extends Game {
	public OrthographicCamera camera;
	public AssetManager assetManager;
	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(true);
		assetManager = new AssetManager();
		this.setScreen(new LoadingScreen(this));
		//storing sounds of single notes
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		this.getScreen().dispose();
	}

}
