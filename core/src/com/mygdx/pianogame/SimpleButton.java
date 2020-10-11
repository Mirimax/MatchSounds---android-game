package com.mygdx.pianogame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class SimpleButton extends Actor {
    Texture texture;
    float posX,posY;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture,posX,posY);
    }

    public SimpleButton(Texture texture, float posX, float posY){
        this.texture = texture;
        this.posX = posX;
        this.posY = posY;
        setBounds(posX,posY,texture.getWidth(),texture.getHeight());
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

    }

}
