package com.mygdx.pianogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class PianoTile extends Actor {
    public Texture texture = new Texture("img/buttons/play.png");
    Sound sound;
    public Boolean start = false;
    float x = Gdx.graphics.getWidth()/2, y = Gdx.graphics.getHeight()/2;
    PianoTile(){
        setBounds(x,y,texture.getWidth(),texture.getHeight());
        sound = Gdx.audio.newSound(Gdx.files.internal("sounds/singlenotes/32.mp3"));
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ((PianoTile)event.getTarget()).start = true;
                return true;
            }
        });

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,x,y);
    }
        @Override
    public void act(float delta) {
        if(start){
            sound.play();
            start = false;
        }
    }

}
