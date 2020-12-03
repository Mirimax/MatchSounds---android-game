package com.mygdx.pianogame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.pianogame.screens.LoadingScreen;
import com.mygdx.pianogame.screens.MatchSounds;
import com.mygdx.pianogame.screens.Menu;



public class GameClass extends Game {
	public OrthographicCamera camera;
	public AssetManager assetManager;

	public BitmapFont font;
	public BitmapFont fontBig;
	public SpriteBatch batch;
	public Skin skin;
	public LoadingScreen loadingScreen;
	public Menu menu;
	public MatchSounds matchSounds;


	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		assetManager = new AssetManager();
		batch = new SpriteBatch();

		initFonts();
		initSkins();

		loadingScreen = new LoadingScreen(this);
		menu = new Menu(this);
		matchSounds = new MatchSounds(this);
		this.setScreen(loadingScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		fontBig.dispose();
		skin.dispose();
		assetManager.dispose();
		loadingScreen.dispose();
		menu.dispose();
		matchSounds.dispose();
	}

	private void initFonts(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arcon.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 60;
		parameter.color = Color.BLACK;
		font = generator.generateFont(parameter);
		FreeTypeFontGenerator.FreeTypeFontParameter parameterBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameterBig.size = 120;
		parameterBig.color = Color.BLACK;
		fontBig = generator.generateFont(parameterBig);
	}
	private void initSkins(){
		skin = new Skin();
		skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
		skin.add("default-font",this.font);
		skin.load(Gdx.files.internal("ui/uiskin.json"));
	}

}
