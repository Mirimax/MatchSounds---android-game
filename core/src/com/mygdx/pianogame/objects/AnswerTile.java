package com.mygdx.pianogame.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

/**Class that represents answer tiles on which a player can set piano tiles in order to guess the answer.
 * @author Paweł Platta */
public class AnswerTile extends Tile {
    /** Position of a piano tile that is set on this answer tile. */
    public float overlappingTileX, overlappingTileY;
    /** Tells if a piano tile stands on the answer tile. */
    public boolean isFree;
    /** A number of a piano tile that is set on this answer tile. */
    public int occupiedPianoTile;
    /** A number of a music note that is set on this answer tile. */
    public int occupiedNote;

    /**Sets all given arguments.
     * @param text text displayed on tile.
     * @param skin {@link Skin} object.
     * @param style name of a style.
     * @param id identification number.
     * @param x X coordinate of position of tile.
     * @param y Y coordinate of position of tile.
     * @param width width of tile.
     * @param height height of tile. */
    public AnswerTile(String text, Skin skin, String style, int id, float x, float y, float width, float height) {
        super(text, skin, style, id, x, y, width, height);
        overlappingTileX = x + (width - 100) / 2f;
        overlappingTileY = y + (height - 170) / 2f;
        setColor(0.8f, 0.8f, 0.8f, 1);
        getLabel().setAlignment(Align.top);
        isFree = true;
        occupiedPianoTile = -1;
        occupiedNote = -1;
    }
}
