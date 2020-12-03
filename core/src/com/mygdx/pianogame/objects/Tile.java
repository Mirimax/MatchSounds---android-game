package com.mygdx.pianogame.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Tile extends TextButton {
    public int id;
    public float x,y;
    public Rectangle rectangle;

    public Tile(String text, Skin skin, String style, int id, float x, float y, float width, float height){
        super(text, skin, style);
        this.id = id;
        this.x = x;
        this.y = y;
        this.setPosition(x,y);
        this.setBounds(x,y,width,height);
        this.setSize(width,height);
        rectangle = new Rectangle((int)x,(int)y,width,height);
    }
}
