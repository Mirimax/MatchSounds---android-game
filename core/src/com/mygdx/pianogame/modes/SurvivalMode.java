package com.mygdx.pianogame.modes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

 /** Class that generates levels for the survival game mode and keeps track of all variables during play.
 * @author Paweł Platta */
public class SurvivalMode {
    private Random random;
    private ArrayList<Integer> correctSequence;
    private ArrayList<Integer> pianoTilesToShow;
    /** Number of additional tiles that are not included in the answer. */
    public int additionalTiles;
    /** Number of all tiles in the answer. */
    public int answerTiles;
    /** Number of times the level is repeated with the same settings. */
    public int repeatLevel;
    /** Keep track of current level. */
    public int currentLevel;
    /** Keep track of current lives. */
    public int livesLeft;

    /** Initialize all variables calls {@link #resetLevels()}. */
    public SurvivalMode(){
       random = new Random();
       correctSequence = new ArrayList<Integer>();
       pianoTilesToShow = new ArrayList<Integer>();
       resetLevels();
    }

    /** Generates the next level dependable on the current state of the game. */
    public void generateNextLevel(){
        currentLevel++;
        if(additionalTiles%2 == 0){
            if(repeatLevel == 0){
                additionalTiles = 1;
                answerTiles++;
                repeatLevel = 3;
            }
            else{
                repeatLevel--;
            }
        }
        else{
            additionalTiles++;
        }
        correctSequence.clear();
        pianoTilesToShow.clear();
        for(int i=0;i<answerTiles;i++){
           correctSequence.add(random.nextInt(88));
           pianoTilesToShow.add(correctSequence.get(i));
        }
        for(int i=0;i<additionalTiles;i++){
            pianoTilesToShow.add(random.nextInt(88));
        }
        Collections.shuffle(pianoTilesToShow);
    }

    /** Resets all levels and sets the game at first level. */
    public void resetLevels(){
        correctSequence.clear();
        pianoTilesToShow.clear();
        currentLevel = 1;
        additionalTiles = 1;
        answerTiles = 1;
        repeatLevel = 3;
        livesLeft = 3;
        correctSequence.add(random.nextInt(88));
        pianoTilesToShow.add(correctSequence.get(0));
        pianoTilesToShow.add(random.nextInt(88));
    }
    /** Return a sequence that represents the answer. */
    public ArrayList<Integer> getCorrectSequence(){
        return correctSequence;
    }
    /** Return a sequence that is not included in the answer. */
    public ArrayList<Integer> getPianoTilesToShow(){
        return pianoTilesToShow;
    }
}
