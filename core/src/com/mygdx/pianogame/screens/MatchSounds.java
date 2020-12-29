package com.mygdx.pianogame.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.pianogame.GameClass;
import com.mygdx.pianogame.objects.AnswerTile;
import com.mygdx.pianogame.objects.PianoTile;

import java.util.Random;

public class MatchSounds implements Screen {
    private final GameClass app;
    private Stage stage;
    private Random random;
    private int[] sequence;
    private Sound[] singleNotes;
    private float timer;
    private int counter;
    private int cnt;
    private boolean startPlaySequence;
    private GlyphLayout text;
    private enum GAMESTATE {LISTEN,MATCH};
    private GAMESTATE state;
    //Actors of stage
    private PianoTile[] tiles;
    private AnswerTile[] answerTiles;
    private TextButton backButton;
    private TextButton skipButton;
    private TextButton playButton;
    private TextButton replayButton;

    public MatchSounds(final GameClass app){
        this.app = app;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        random = new Random();
        singleNotes = new Sound[88];
    }

    @Override
    public void show() {
        stage.clear();
        for(int i = 0; i<88;i++) singleNotes[87-i] = app.assetManager.get("sounds/singlenotes/"+ (i+1) + ".mp3",Sound.class);
        Gdx.input.setInputProcessor(stage);
        gameSettings();
        initFirstStage();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        playSequence();

        textDisplay();


        update(delta);
        stage.draw();
    }

    private void gameSettings(){
        counter = 3;
        cnt = 0;
        timer = 0;
        startPlaySequence = false;
        state = GAMESTATE.LISTEN;
        sequence = new int[4];
        for (int i = 0; i < sequence.length; i++) {
            sequence[i] = random.nextInt(88);
        }
    }

    private void textDisplay(){
        if(state == GAMESTATE.LISTEN){
            text.setText(app.font,"Times to re-listen: " + counter);
        }
        else if(state == GAMESTATE.MATCH){
            text.setText(app.font,"costam");
        }
        app.batch.begin();
        app.font.draw(app.batch,text,Gdx.graphics.getWidth()/2f - text.width/2,Gdx.graphics.getHeight()*9/10f);
        app.batch.end();
    }

    private void initFirstStage(){
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
                switchStage();
            }
        });
        stage.addActor(skipButton);

        //button for re-listen sounds sequence
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
                    timer = 50;
                }
            }
        });
        stage.addActor(replayButton);

        text = new GlyphLayout();
    }

    private void playSequence(){
        if(startPlaySequence){
            timer++;
            if(state == GAMESTATE.LISTEN){
                if(cnt < sequence.length && timer >= 35){
                    singleNotes[sequence[cnt]].play();
                    cnt++;
                    timer = 0;
                }
                else if(cnt == sequence.length && timer > 60) {
                    startPlaySequence = false;
                    if(counter==0) switchStage();
                }
            }
            else if(state == GAMESTATE.MATCH){
                if(cnt < answerTiles.length && timer >= 35){
                    if(answerTiles[cnt].occupiedPianoTile != -1){
                        singleNotes[sequence[answerTiles[cnt].occupiedPianoTile]].play();
                        timer = 0;
                    }
                    cnt++;
                }
            }
        }
    }

    private void switchStage(){
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

        TextButton checkButton = new TextButton("check", app.skin, "default");
        checkButton.setSize(400,150);
        checkButton.setName("checkButton");
        checkButton.setPosition(stage.getWidth() - checkButton.getWidth()-100,stage.getHeight()/2- checkButton.getHeight()/2);
        checkButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

        });
        stage.addActor(checkButton);

        initTiles();
        state = GAMESTATE.MATCH;
    }

    private void initTiles() {
        //initialization of answer tiles (these at the bottom of the game screen)
        answerTiles = new AnswerTile[sequence.length];
        int tempWidth = 180, tempHeight = 230;
        for(int i = 0; i < answerTiles.length; i++){
            float tempX = stage.getWidth()/2f-(int)(answerTiles.length/2f)*(tempWidth + 25)+ i*(tempWidth + 25);
            if(answerTiles.length%2 == 1) tempX -= tempWidth/2f;
            answerTiles[i] = new AnswerTile(Integer.toString(i+1),app.skin,"default",sequence[i],tempX,stage.getHeight()/5-125,tempWidth,tempHeight);
            answerTiles[i].setName("answerTile" + i);
            stage.addActor(answerTiles[i]);
        }

        //initialization of piano tiles (these at the top of the game screen)
        tiles = new PianoTile[sequence.length];
        tempWidth = 100;
        tempHeight = 170;
        for(int i = 0; i < tiles.length; i++){
            final int finalI = i;
            float tempX = stage.getWidth()/2-(int)(tiles.length/2f)*(tempWidth+25) + i*tempWidth + i*25;
            if(tiles.length%2 == 1) tempX -= tempWidth/2f;
            tiles[i] = new PianoTile(Integer.toString(i+1),app.skin,"default",sequence[i],tempX,stage.getHeight()/2-85,tempWidth,tempHeight);
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
                            }
                            if (!answerTiles[j].isFree) {
                                int temp = answerTiles[j].occupiedPianoTile;
                                tiles[temp].setPosition(tiles[temp].originX, tiles[temp].originY);
                                tiles[temp].occupiedAnswerTile = -1;
                            }
                            tiles[finalI].setPosition(answerTiles[j].overlappingTileX, answerTiles[j].overlappingTileY);
                            answerTiles[j].isFree = false;
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
