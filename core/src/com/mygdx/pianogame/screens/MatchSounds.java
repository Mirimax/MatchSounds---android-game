package com.mygdx.pianogame.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.pianogame.GameClass;
import com.mygdx.pianogame.modes.SurvivalMode;
import com.mygdx.pianogame.objects.AnswerTile;
import com.mygdx.pianogame.objects.PianoTile;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.util.ArrayList;

public class MatchSounds implements Screen {
    private final GameClass app;
    private Stage stage;
    private SurvivalMode survivalMode;

    private enum GAMESTATE {LISTEN,MATCH}
    private GAMESTATE state;

    private Sound[] singleNotes;
    private Sound emptyTileSound;
    //notes sequences
    private ArrayList<Integer> pianoTilesToShow;
    private ArrayList<Integer> correctSequence;
    //Count and check variables
    private float timer;
    private int cnt;
    private int counter;
    private boolean startPlaySequence;
    private boolean checkSequence;
    //Actors of stage
    private PianoTile[] tiles;
    private AnswerTile[] answerTiles;
    private TextButton backButton;
    private TextButton skipButton;
    private TextButton playButton;
    private TextButton playAgainButton;
    private TextButton checkButton;
    private TextButton resetSurvival;
    private TextButton nextLevel;
    private TextButton resetCustom;
    private TextButton changeSettingsCustom;
    private GlyphLayout text; //not quite an actor but let him be there
    private GlyphLayout levelText;
    private GlyphLayout livesLeftText;
    private Texture noteTex;
    private Image[] noteImg;

    public MatchSounds(final GameClass app){
        this.app = app;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        singleNotes = new Sound[88];
        text = new GlyphLayout();
        levelText = new GlyphLayout();
        livesLeftText = new GlyphLayout();
        survivalMode = new SurvivalMode();
    }

    @Override
    public void show() {
        stage.clear();

        for(int i = 0; i<88;i++) singleNotes[87-i] = app.assetManager.get("sounds/singlenotes/"+ (i+1) + ".mp3",Sound.class);
        emptyTileSound = app.assetManager.get("sounds/empty_tile.mp3",Sound.class);

        if (app.gamemode == GameClass.GAMEMODE.SURVIVAL) {
            pianoTilesToShow = survivalMode.getPianoTilesToShow();
            correctSequence = survivalMode.getCorrectSequence();
            counter = 3;
        }
        else if(app.gamemode == GameClass.GAMEMODE.CUSTOM){
            pianoTilesToShow = app.customMode.getPianoTilesToShow();
            correctSequence = app.customMode.getCorrectSequence();
            counter = app.customMode.getListenAgainCnt();
        }

        initListeningPart();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playSequence();
        playAndCheckSequence();
        displayText();

        stage.act();
        stage.draw();
    }

    private void displayText(){
        app.batch.begin();
        app.font.draw(app.batch, text,stage.getWidth()/2f - text.width/2,playAgainButton.getY()/2+playAgainButton.getHeight()/2+stage.getHeight()/2+text.height/2);
        app.font.draw(app.batch,levelText,stage.getWidth()-livesLeftText.width-100,backButton.getY() + levelText.height + 15);
        app.font.draw(app.batch,livesLeftText,stage.getWidth()-livesLeftText.width-100,backButton.getY() - levelText.height);
        app.batch.end();
    }

    private void initListeningPart(){
        text.setText(app.fontBlack,"Left times to play again: " + counter + ".");
        if(app.gamemode == GameClass.GAMEMODE.SURVIVAL){
            levelText.setText(app.fontBlack,"Level: " + survivalMode.currentLevel);
            livesLeftText.setText(app.fontBlack,"Lives left: " + survivalMode.livesLeft);
        }
        else{
            levelText.setText(app.fontBlack,"");
            livesLeftText.setText(app.fontBlack,"");
        }

        state = GAMESTATE.LISTEN;
        cnt = 0;
        timer = 0;
        startPlaySequence = false;
        checkSequence = false;

        Texture backgroundTex = app.assetManager.get("img/background2.png",Texture.class);
        Image backgroundImage = new Image(backgroundTex);
        backgroundImage.setSize(stage.getWidth(),stage.getHeight());
        stage.addActor(backgroundImage);

        //button for back to the menu at any time
        backButton = new TextButton("back", app.skinSmall, "default");
        backButton.setSize(300,100);
        backButton.setPosition(100,stage.getHeight()-backButton.getHeight()-50);
        backButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                survivalMode.resetLevels();
                ((GameClass)Gdx.app.getApplicationListener()).setScreen(app.menu);
            }
        });
        stage.addActor(backButton);

        //button for skip the listening part and start matching sounds
        skipButton = new TextButton("skip", app.skinSmall, "default");
        skipButton.setSize(400,150);
        skipButton.setPosition(stage.getWidth()-skipButton.getWidth()-100,stage.getHeight()/2-skipButton.getHeight()/2);
        skipButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                initMatchingPart();
            }
        });
        stage.addActor(skipButton);

        //button for re-playing the sequence of sounds in the listening part
        playAgainButton = new TextButton("play again", app.skinSmall, "default");
        playAgainButton.setSize(400,150);
        playAgainButton.setPosition(Gdx.graphics.getWidth()/2f - 200,stage.getHeight()*2/3-170/2f);
        playAgainButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(counter>0) {
                    startPlaySequence = true;
                    cnt = 0;
                    counter--;
                    text.setText(app.font,"Left times to play again: " + counter);
                    timer = 50;
                }
            }
        });
        stage.addActor(playAgainButton);

        noteTex = app.assetManager.get("img/note.png", Texture.class);
        noteImg = new Image[correctSequence.size()];
        for(int i=0;i<noteImg.length;i++){
            noteImg[i] = new Image(noteTex);
            float tempX = stage.getWidth()/2f-(int)(correctSequence.size()/2f)*(noteTex.getWidth() + 100)+ i*(noteTex.getWidth()+ 100);
            if(correctSequence.size()%2 == 1) tempX -= noteTex.getWidth()/2f;
            noteImg[i].setOrigin(noteTex.getWidth()/2f,noteTex.getHeight()/2f);
            noteImg[i].setPosition(tempX+300,stage.getHeight() + noteTex.getHeight()+200);
            noteImg[i].addAction(Actions.sequence(scaleTo(4f,4f),
                    parallel(moveTo(tempX,playAgainButton.getY()/2-noteTex.getHeight()/2f,0.9f, Interpolation.bounce),scaleTo(1f,1f,0.5f))
            ));
            stage.addActor(noteImg[i]);
        }
    }

    private void initMatchingPart(){
        state = GAMESTATE.MATCH;

        initTiles();

        skipButton.setVisible(false);
        playAgainButton.setVisible(false);

        for(int i=0;i<noteImg.length;i++) {
            noteImg[i].setVisible(false);
        }

        text.setText(app.font,"");

        playButton = new TextButton("play", app.skinSmall, "default");
        playButton.setSize(400,150);
        playButton.setPosition(stage.getWidth() - playButton.getWidth()-100,stage.getHeight()/2 - playButton.getHeight()-25);
        playButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                startPlaySequence = true;
                cnt = 0;
                timer = 50;
            }
        });
        stage.addActor(playButton);

        checkButton = new TextButton("check\nanswer", app.skinSmall, "default");
        checkButton.setSize(400,150);
        checkButton.setPosition(stage.getWidth() - checkButton.getWidth()-100,stage.getHeight()/2 + 25);
        checkButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                checkButton.setDisabled(true);
                checkSequence = true;
                startPlaySequence = false;
                cnt = 0;
                timer = 50;
            }
        });
        stage.addActor(checkButton);
    }

    //Initialize both the black playable tiles and the white answer tiles
    private void initTiles() {
        //initialization of answer tiles (these at the bottom of the game screen)
        answerTiles = new AnswerTile[correctSequence.size()];
        int tempWidth = 180, tempHeight = 230;
        for(int i = 0; i < answerTiles.length; i++){
            float tempX = stage.getWidth()/2f-(int)(answerTiles.length/2f)*(tempWidth + 25)+ i*(tempWidth + 25);
            if(answerTiles.length%2 == 1) tempX -= tempWidth/2f;
            answerTiles[i] = new AnswerTile("",app.skin,"mine",pianoTilesToShow.get(i),tempX,stage.getHeight()/3-tempHeight/2f,tempWidth,tempHeight);
            answerTiles[i].setDisabled(true);
            stage.addActor(answerTiles[i]);
        }

        //initialization of piano tiles (these at the top of the game screen)
        tiles = new PianoTile[pianoTilesToShow.size()];
        tempWidth = 100;
        tempHeight = 170;
        for(int i = 0; i < tiles.length; i++){
            final int finalI = i;
            float tempX = stage.getWidth()/2-(int)(tiles.length/2f)*(tempWidth+25) + i*tempWidth + i*25;
            if(tiles.length%2 == 1) tempX -= tempWidth/2f;
            tiles[i] = new PianoTile(Integer.toString(i+1),app.skin,"default",pianoTilesToShow.get(i),tempX,stage.getHeight()*2/3-tempHeight/2f,tempWidth,tempHeight);
            tiles[i].addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    singleNotes[tiles[finalI].id].play();
                    return true;
                }
                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    tiles[finalI].moveBy(x - tiles[finalI].getWidth()/2,y - tiles[finalI].getHeight()/2);
                    tiles[finalI].rectangle.x = tiles[finalI].getX();
                    tiles[finalI].rectangle.y = tiles[finalI].getY();
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    //looking for a collision of piano tile with answer tile
                    for (int j = 0; j < answerTiles.length; j++) {
                        if (tiles[finalI].rectangle.overlaps(answerTiles[j].rectangle)) {
                            if(tiles[finalI].occupiedAnswerTile != -1) {
                                answerTiles[tiles[finalI].occupiedAnswerTile].isFree = true;
                                answerTiles[tiles[finalI].occupiedAnswerTile].occupiedPianoTile = -1;
                                answerTiles[tiles[finalI].occupiedAnswerTile].occupiedNote = -1;
                            }
                            if (!answerTiles[j].isFree) {
                                int temp = answerTiles[j].occupiedPianoTile;
                                tiles[temp].setPosition(tiles[temp].originX, tiles[temp].originY);
                                tiles[temp].occupiedAnswerTile = -1;
                            }
                            tiles[finalI].setPosition(answerTiles[j].overlappingTileX, answerTiles[j].overlappingTileY);
                            answerTiles[j].isFree = false;
                            answerTiles[j].occupiedNote = pianoTilesToShow.get(finalI);
                            answerTiles[j].occupiedPianoTile = finalI;
                            tiles[finalI].occupiedAnswerTile = j;
                            return;
                        }
                    }
                    //back to starting position if no collision was detected
                    tiles[finalI].setPosition(tiles[finalI].originX,tiles[finalI].originY);
                    if(tiles[finalI].occupiedAnswerTile != -1){
                        answerTiles[tiles[finalI].occupiedAnswerTile].isFree = true;
                        answerTiles[tiles[finalI].occupiedAnswerTile].occupiedPianoTile = -1;
                        tiles[finalI].occupiedAnswerTile = -1;
                    }
                }
            });
            stage.addActor(tiles[i]);
        }

    }

    private void checkAnswer(){
        //checks if answer is correct and saves result in variable
        boolean correct = true;
        for (int i = 0; i < answerTiles.length; i++) {
            if(answerTiles[i].occupiedNote != correctSequence.get(i)){
                correct = false;
                break;
            }
        }

        playButton.setVisible(false);
        checkButton.setVisible(false);
        initResultButtons();

        //decide the next action that depends on the chosen game mode and correctness of the answer
        if (app.gamemode == GameClass.GAMEMODE.SURVIVAL) {
            if(correct){
                nextLevel.setVisible(true);
                text.setText(app.font,"Correct!");
            }
            else if(survivalMode.livesLeft > 1){
                nextLevel.setVisible(true);
                survivalMode.livesLeft--;
                text.setText(app.font,"Wrong!");
                livesLeftText.setText(app.fontBlack,"Lives left: " + survivalMode.livesLeft);
            }
            else{
                resetSurvival.setVisible(true);
                text.setText(app.font,"Wrong!.\nYour final score is " + (survivalMode.currentLevel - 1) + ".");
                int tmp = app.prefs.getInteger("survivalBest",survivalMode.currentLevel-1);
                app.prefs.putInteger("survivalBest",Math.max(tmp,survivalMode.currentLevel-1));
                app.prefs.putInteger("survivalGamesPlayed",app.prefs.getInteger("survivalGamesPlayed",0) + 1);
            }
        }
        else if(app.gamemode == GameClass.GAMEMODE.CUSTOM){
            if(correct){
                text.setText(app.font,"Correct!");
            }
            else{
                text.setText(app.font,"Wrong!");
            }
            resetCustom.setVisible(true);
            changeSettingsCustom.setVisible(true);
        }
        app.prefs.putInteger("roundsPlayed",app.prefs.getInteger("roundsPlayed",0) + 1);
        app.prefs.flush();
    }

    private void initResultButtons(){
        nextLevel = new TextButton("Next Level", app.skinSmall, "default");
        nextLevel.setSize(400,150);
        nextLevel.setPosition(stage.getWidth() - nextLevel.getWidth()-100,stage.getHeight()/2- nextLevel.getHeight()/2);
        nextLevel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                survivalMode.generateNextLevel();
                show();
            }
        });
        stage.addActor(nextLevel);
        nextLevel.setVisible(false);

        resetSurvival = new TextButton("Reset", app.skinSmall, "default");
        resetSurvival.setSize(400,150);
        resetSurvival.setPosition(stage.getWidth() - resetSurvival.getWidth()-100,stage.getHeight()/2- resetSurvival.getHeight()/2);
        resetSurvival.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                survivalMode.resetLevels();
                show();
            }
        });
        stage.addActor(resetSurvival);
        resetSurvival.setVisible(false);

        resetCustom = new TextButton("Reset", app.skinSmall, "default");
        resetCustom.setSize(400,150);
        resetCustom.setPosition(stage.getWidth() - resetCustom.getWidth()-100,stage.getHeight()/2- resetCustom.getHeight()-50);
        resetCustom.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                app.customMode.generateSequence();
                show();
            }
        });
        stage.addActor(resetCustom);
        resetCustom.setVisible(false);

        changeSettingsCustom = new TextButton("Change\nSettings", app.skinSmall, "default");
        changeSettingsCustom.setSize(400,150);
        changeSettingsCustom.setPosition(stage.getWidth() - changeSettingsCustom.getWidth()-100,stage.getHeight()/2 + 50);
        changeSettingsCustom.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                app.setScreen(app.customMode);
            }
        });
        stage.addActor(changeSettingsCustom);
        changeSettingsCustom.setVisible(false);
    }

    //Plays the sequence in the listening part and the matching part depending on the game state
    private void playSequence(){
        if(startPlaySequence){
            timer++;
            if(state == GAMESTATE.LISTEN){
                if(cnt < correctSequence.size() && timer >= 35){
                    singleNotes[correctSequence.get(cnt)].play();
                    noteImg[cnt].addAction(sequence(scaleTo(1.5f,1.5f,0.3f),scaleTo(1f,1f,0.3f)));
                    cnt++;
                    timer = 0;
                }
                else if(cnt == correctSequence.size() && timer > 60) {
                    startPlaySequence = false;
                    if(counter==0) initMatchingPart();
                }
            }
            else if(state == GAMESTATE.MATCH){
                if(cnt < answerTiles.length && timer >= 35){
                    if(answerTiles[cnt].occupiedPianoTile != -1){
                        singleNotes[answerTiles[cnt].occupiedNote].play();
                        timer = 0;
                    }
                    cnt++;
                    if(cnt == answerTiles.length) startPlaySequence = false;
                }
            }
        }
    }

    private void playAndCheckSequence(){
        if(checkSequence){
            timer++;
            if(cnt < answerTiles.length && timer >= 35){
                if(answerTiles[cnt].occupiedPianoTile != -1){
                    singleNotes[answerTiles[cnt].occupiedNote].play();
                }
                else{
                    emptyTileSound.play();
                }
                if (answerTiles[cnt].occupiedNote == correctSequence.get(cnt)) {
                    answerTiles[cnt].setColor(Color.GREEN);
                }
                else{
                    answerTiles[cnt].setColor(Color.RED);
                }
                cnt++;
                if(cnt == answerTiles.length) {
                    checkSequence = false;
                    checkAnswer();
                }
                timer = 0;
            }
        }
    }

    @Override
    public void resize(int width, int height) { }

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
