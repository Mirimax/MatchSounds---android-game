package com.mygdx.pianogame.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**Class that represents piano tiles which a player can move and play sound by clicking on them during play.
 * @author Paweł Platta*/
public class PianoTile extends Tile {
    /** Initial position of a piano tile. */
    public float originX,originY;
    /** A Number of an answer tile that piano tile is set on. */
    public int occupiedAnswerTile;

    /**Sets all given arguments.
     * @param text text displayed on tile.
     * @param skin {@link Skin} object,
     * @param style name of a style.
     * @param id identification number.
     * @param x X coordinate of position of tile.
     * @param y Y coordinate of position of tile.
     * @param width width of tile.
     * @param height height of tile.*/
    public PianoTile(String text, Skin skin, String style, int id, float x, float y, float width, float height){
        super(text,skin,style,id,x,y,width,height);
        originX = x;
        originY = y;
        this.setColor(0.302f,0.302f,0.302f,1f);
        occupiedAnswerTile = -1;
    }
}
