package com.mygdx.pianogame.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.pianogame.GameClass;
import com.mygdx.pianogame.modes.SurvivalMode;
import com.mygdx.pianogame.objects.AnswerTile;
import com.mygdx.pianogame.objects.PianoTile;

import java.util.ArrayList;
import java.util.Random;

public class MatchSounds implements Screen {
    private final GameClass app;
    private Stage stage;
    private Random random;
    private enum GAMESTATE {LISTEN,MATCH}
    private GAMESTATE state;
    private Sound[] singleNotes;
    private SurvivalMode survivalMode;
    //notes sequences
    private ArrayList<Integer> pianoTilesToShow;
    private ArrayList<Integer> correctSequence;
    //Count and check variables
    private float timer;
    private int cnt;
    private int counter;
    private boolean startPlaySequence;
    //Actors of stage
    private PianoTile[] tiles;
    private AnswerTile[] answerTiles;
    private TextButton backButton;
    private TextButton skipButton;
    private TextButton playButton;
    private TextButton replayButton;
    private TextButton checkButton;
    private TextButton resetSurvival;
    private TextButton nextLevel;
    private GlyphLayout text; //not quite an actor but let him be there

    public MatchSounds(final GameClass app){
        this.app = app;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        random = new Random();
        singleNotes = new Sound[88];
        text = new GlyphLayout();
        survivalMode = new SurvivalMode();
    }

    @Override
    public void show() {
        stage.clear();
        for(int i = 0; i<88;i++) singleNotes[87-i] = app.assetManager.get("sounds/singlenotes/"+ (i+1) + ".mp3",Sound.class);
        Gdx.input.setInputProcessor(stage);

        initListeningPart();
        state = GAMESTATE.LISTEN;
        counter = 3;
        text.setText(app.font,"Times to re-play: " + counter);
        cnt = 0;
        timer = 0;
        startPlaySequence = false;

        if (app.gamemode == GameClass.GAMEMODE.SURVIVAL) {
            pianoTilesToShow = survivalMode.getPianoTilesToShow();
            correctSequence = survivalMode.getCorrectSequence();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playSequence();

        //Displaying text
        app.batch.begin();
        app.font.draw(app.batch,text,Gdx.graphics.getWidth()/2f - text.width/2,Gdx.graphics.getHeight()*9/10f);
        app.batch.end();

        update(delta);
        stage.draw();
    }

    private void checkAnswer(){
        //checks if answer is correct and saves result in variable
        boolean correct = true;
        for (int i = 0; i < answerTiles.length; i++) {
            Gdx.app.log("Info","Occupied Piano Tile: " + answerTiles[i].occupiedPianoTile + " Correct: " + correctSequence.get(i));
            if(answerTiles[i].occupiedNote != correctSequence.get(i)){
                correct = false;
                break;
            }
        }
        playButton.setVisible(false);
        checkButton.setVisible(false);
        initResultPart();
        //decide the next action that depends on the chosen game mode and correctness of the answer
        if (app.gamemode == GameClass.GAMEMODE.SURVIVAL) {
           if(correct){
              nextLevel.setVisible(true);
              text.setText(app.font,"Correct!\nYour actual score is: " + survivalMode.currentLevel + "\nContinue play or go back to menu.");
           }
           else{
               resetSurvival.setVisible(true);
               text.setText(app.font,"Your answer is not correct.\nYour final score is: " + survivalMode.currentLevel + "\nReset this game mode or go back to the menu.");
           }
        }
    }


    private void initListeningPart(){
        //button for back to the menu at any time
        backButton = new TextButton("back", app.skin, "default");
        backButton.setPosition(50,stage.getHeight()-150);
        backButton.setSize(300,100);
        backButton.setName("backButton");
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
        skipButton = new TextButton("skip", app.skin, "default");
        skipButton.setPosition(stage.getWidth()-350,stage.getHeight()-150);
        skipButton.setSize(300,100);
        skipButton.setName("skipButton");
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
        replayButton = new TextButton("Re-listen", app.skin, "default");
        replayButton.setSize(400,150);
        replayButton.setName("replayButton");
        replayButton.setPosition(Gdx.graphics.getWidth()/2f - 200,Gdx.graphics.getHeight()*7/10f);
        replayButton.addListener(new InputListener(){
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
                    text.setText(app.font,"Times to re-play: " + counter);
                    timer = 50;
                }
            }
        });
        stage.addActor(replayButton);

    }

    private void initMatchingPart(){
        text.setText(app.font,"Current level: " + survivalMode.currentLevel + "\n" + app.gamemode);

        skipButton.setVisible(false);
        replayButton.setVisible(false);

        playButton = new TextButton("Play actual\n sequence", app.skin, "default");
        playButton.setSize(400,150);
        playButton.setName("playButton");
        playButton.setPosition(100,stage.getHeight()/2 - playButton.getHeight()/2);
        playButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                for(int i=0;i<answerTiles.length;i++){
                    Gdx.app.log(i+1 + ": ",Integer.toString(answerTiles[i].occupiedPianoTile));
                    startPlaySequence = true;
                    cnt = 0;
                    timer = 50;
                }
            }
        });
        stage.addActor(playButton);

        checkButton = new TextButton("check", app.skin, "default");
        checkButton.setSize(400,150);
        checkButton.setName("checkButton");
        checkButton.setPosition(stage.getWidth() - checkButton.getWidth()-100,stage.getHeight()/2- checkButton.getHeight()/2);
        checkButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                checkAnswer();
            }
        });
        stage.addActor(checkButton);

        initTiles();
        state = GAMESTATE.MATCH;
    }

    private void initResultPart(){
        nextLevel = new TextButton("Next Level", app.skin, "default");
        nextLevel.setSize(400,150);
        nextLevel.setPosition(stage.getWidth() - nextLevel.getWidth()-100,stage.getHeight()/2- nextLevel.getHeight()/2);
        nextLevel.setName("nextLevel");
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

        resetSurvival = new TextButton("Reset", app.skin, "default");
        resetSurvival.setSize(400,150);
        resetSurvival.setPosition(stage.getWidth() - resetSurvival.getWidth()-100,stage.getHeight()/2- resetSurvival.getHeight()/2);
        resetSurvival.setName("resetSurvival");
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
    }

    //Initialize both the black playable tiles and the white answer tiles
    private void initTiles() {
        //initialization of answer tiles (these at the bottom of the game screen)
        answerTiles = new AnswerTile[correctSequence.size()];
        int tempWidth = 180, tempHeight = 230;
        for(int i = 0; i < answerTiles.length; i++){
            float tempX = stage.getWidth()/2f-(int)(answerTiles.length/2f)*(tempWidth + 25)+ i*(tempWidth + 25);
            if(answerTiles.length%2 == 1) tempX -= tempWidth/2f;
            answerTiles[i] = new AnswerTile(Integer.toString(i+1),app.skin,"default",pianoTilesToShow.get(i),tempX,stage.getHeight()/5-125,tempWidth,tempHeight);
            answerTiles[i].setName("answerTile" + i);
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
            tiles[i] = new PianoTile(Integer.toString(i+1),app.skin,"default",pianoTilesToShow.get(i),tempX,stage.getHeight()/2-85,tempWidth,tempHeight);
            tiles[i].setName("tile" + i);
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

    //Plays the sequence in the listening part and the matching part depending on the game state
    private void playSequence(){
        if(startPlaySequence){
            timer++;
            if(state == GAMESTATE.LISTEN){
                if(cnt < correctSequence.size() && timer >= 35){
                    singleNotes[correctSequence.get(cnt)].play();
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
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    private void update(float delta) {
        stage.act(delta);
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
