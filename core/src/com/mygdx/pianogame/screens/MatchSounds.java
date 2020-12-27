package com.mygdx.pianogame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
    private float timer = 100;
    private int cnt = 100;

    private PianoTile[] tiles;
    private com.mygdx.pianogame.objects.AnswerTile[] answerTiles;

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
        initNavButtons();
        initBoard();
        initTiles();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        matchSound();
        collision();

        update(delta);
        stage.draw();
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

    private void collision() {

    }

    private void gameSettings(){
        sequence = new int[4];
        for (int i = 0; i < sequence.length; i++) {
            sequence[i] = random.nextInt(88);
        }
    }

    private void matchSound(){
        timer++;
        if(cnt < sequence.length && timer >= 20){
            singleNotes[sequence[cnt]].play();
            cnt++;
            timer = 0;
        }
    }

    private void initNavButtons(){
        final TextButton playButton = new TextButton("play actual\n sequence", app.skin, "default");
        playButton.setSize(400,150);
        playButton.setName("playButton");
        playButton.setPosition(100,stage.getHeight()/2- playButton.getHeight()/2);
        playButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                cnt = 0;
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

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //checkButton.moveBy(x - checkButton.getWidth()/2,y - checkButton.getHeight()/2);
            }
        });
        stage.addActor(checkButton);

        TextButton backButton = new TextButton("back", app.skin, "default");
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




    }

    private void initTiles() {
        tiles = new PianoTile[sequence.length];
        int tempWidth = 100, tempHeight = 170;
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
                    for (int j = 0; j < answerTiles.length; j++) {
                        if (tiles[finalI].rectangle.overlaps(answerTiles[j].rectangle)) {
                            if(tiles[finalI].occupiedAnswerTile != -1) {
                                answerTiles[tiles[finalI].occupiedAnswerTile].isFree = true;
                                tiles[finalI].occupiedAnswerTile = -1;
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
                    tiles[finalI].setPosition(tiles[finalI].originX,tiles[finalI].originY);
                    if(tiles[finalI].occupiedAnswerTile != -1){
                        answerTiles[tiles[finalI].occupiedAnswerTile].isFree = true;
                        tiles[finalI].occupiedAnswerTile = -1;
                    }
                }
            });
            stage.addActor(tiles[i]);
        }
    }

    private void initBoard(){
        answerTiles = new AnswerTile[sequence.length];
        int tempWidth = 180, tempHeight = 230;
        for(int i = 0; i < answerTiles.length; i++){
            float tempX = stage.getWidth()/2f-(int)(answerTiles.length/2f)*(tempWidth + 25)+ i*(tempWidth + 25);
            if(answerTiles.length%2 == 1) tempX -= tempWidth/2f;
            answerTiles[i] = new AnswerTile(Integer.toString(i+1),app.skin,"default",sequence[i],tempX,stage.getHeight()/5-125,tempWidth,tempHeight);
            answerTiles[i].setName("answerTile" + i);
            stage.addActor(answerTiles[i]);
        }
    }



}
