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

 /** The main class of the game that handles changes of the screen,
 *   creates fonts, skins and variables available for all Screens.
 *   @author Paweł Platta */
public class GameClass extends Game {
	private OrthographicCamera camera;
	/** Loads and keeps all assets. */
	public AssetManager assetManager;
	public GAMEMODE gamemode;
	/** Keeps track of chosen game mode. */
	 public enum GAMEMODE {SURVIVAL,CUSTOM}
	/** Store information locally on device. */
	public Preferences prefs;
	/** Used in order to draw things on screen. */
	public SpriteBatch batch;

	/** Font for buttons. */
	public BitmapFont font;
	/** Font for buttons. */
	public BitmapFont fontSmall;
	/** Font used for draw on the background. */
	public BitmapFont fontSmallBlack;
	 /** Font used for draw on the background. */
	public BitmapFont fontBig;
	 /** Font used for draw on the background. */
	public BitmapFont fontBlack;
    /** Skin for buttons and select fields. */
	public Skin skin;
	/** Skin for buttons ans select fields but with smaller font size. */
	public Skin skinSmall;
    /** Instance of {@link LoadingScreen} class. */
	public LoadingScreen loadingScreen;
	/** Instance of {@link Menu} class. */
	public Menu menu;
	/** Instance of {@link MatchSounds} class. */
	public MatchSounds matchSounds;
	/** Instance of {@link CustomMode} class. */
	public CustomMode customMode;

	/** Initialize all variables.
	 * Calls {@link #initFonts()} and {@link #initSkins()} function. */
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

	/** Called when the screen should render itself. */
	 @Override
	public void render () {
		super.render();
	}

    /** Initialize fonts */
	public void initFonts(){
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

    /** Initialize skins */
	public void initSkins(){
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
