package com.mygdx.pianogame.objects;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class AnswerTile extends Tile {
    public float overlappingTileX, overlappingTileY;
    public boolean isFree;
    public int occupiedPianoTile;

    public AnswerTile(String text, Skin skin, String style, int id, float x, float y, float width, float height) {
        super(text, skin, style, id, x, y, width, height);
        overlappingTileX = x + (width - 100) / 2f;
        overlappingTileY = y + (height - 220) / 2f;
        setColor(0.8f, 0.8f, 0.8f, 1);
        getLabel().setAlignment(Align.top);
        isFree = true;
        occupiedPianoTile = -1;
    }
}
