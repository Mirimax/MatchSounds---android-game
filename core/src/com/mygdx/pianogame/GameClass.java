package com.mygdx.pianogame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.pianogame.modes.CustomMode;
import com.mygdx.pianogame.screens.LoadingScreen;
import com.mygdx.pianogame.screens.MatchSounds;
import com.mygdx.pianogame.screens.Menu;



public class GameClass extends Game {
	public OrthographicCamera camera;
	public AssetManager assetManager;

	public enum GAMEMODE {SURVIVAL,CUSTOM}
	public GAMEMODE gamemode;

	public Preferences prefs;
	public SpriteBatch batch;

	public BitmapFont font;
	public BitmapFont fontSmall;
	public BitmapFont fontSmallBlack;
	public BitmapFont fontBig;
	public BitmapFont fontBlack;
	public Skin skin;
	public Skin skinSmall;

	public LoadingScreen loadingScreen;
	public Menu menu;
	public MatchSounds matchSounds;

	public CustomMode customMode;

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		assetManager = new AssetManager();
		batch = new SpriteBatch();
		prefs = Gdx.app.getPreferences("Prefs");

		initFonts();
		initSkins();

		loadingScreen = new LoadingScreen(this);
		menu = new Menu(this);
		matchSounds = new MatchSounds(this);
		customMode = new CustomMode(this);

		this.setScreen(loadingScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	private void initFonts(){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arcon.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 80;
		parameter.color = Color.WHITE;
		font = generator.generateFont(parameter);

		FreeTypeFontGenerator.FreeTypeFontParameter parameterBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameterBig.size = 120;
		parameterBig.color = Color.BLACK;
		fontBig = generator.generateFont(parameterBig);

		FreeTypeFontGenerator.FreeTypeFontParameter parameterSmall = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameterSmall.size = 60;
		parameterSmall.color = Color.WHITE;
		fontSmall = generator.generateFont(parameterSmall);

		fontBlack = font;
		fontBlack.setColor(Color.BLACK);

		fontSmallBlack = fontSmall;
		fontSmallBlack.setColor(Color.BLACK);
	}

	private void initSkins(){
		skin = new Skin();
		skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
		skin.add("default-font",this.font);
		skin.load(Gdx.files.internal("ui/uiskin.json"));

		skinSmall = new Skin();
		skinSmall.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
		skinSmall.add("default-font",this.fontSmall);
		skinSmall.load(Gdx.files.internal("ui/uiskin.json"));
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		fontSmall.dispose();
		fontSmallBlack.dispose();
		fontBig.dispose();
		fontBlack.dispose();
		skin.dispose();
		skinSmall.dispose();
		assetManager.dispose();
		loadingScreen.dispose();
		menu.dispose();
		matchSounds.dispose();
	}
}
