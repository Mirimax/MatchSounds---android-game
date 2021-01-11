package com.mygdx.pianogame.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**Class that is extension of {@link TextButton} class.
 * Provides {@link Rectangle} object to handle collision between tiles
 * and identification number for every tile.
 *@author Paweł Platta */
public class Tile extends TextButton {
    /** Identification number of a tile. */
    public int id;
    /** Position of a tile. */
    public float x,y;
    /** {@link Rectangle} object to handle collisions. */
    public Rectangle rectangle;

    /**Sets all given arguments.
     * @param text text displayed on tile.
     * @param skin {@link Skin} object,
     * @param style name of a style.
     * @param id identification number.
     * @param x X coordinate of position of tile.
     * @param y Y coordinate of position of tile.
     * @param width width of tile.
     * @param height height of tile.*/
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
