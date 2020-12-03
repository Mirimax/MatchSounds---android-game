package com.mygdx.pianogame.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class PianoTile extends Tile {
    public float originX,originY;
    public int occupiedAnswerTile;

    public PianoTile(String text, Skin skin, String style, int id, float x, float y, float width, float height){
        super(text,skin,style,id,x,y,width,height);
        originX = x;
        originY = y;
        this.setColor(0.302f,0.302f,0.302f,1f);
    }

}
