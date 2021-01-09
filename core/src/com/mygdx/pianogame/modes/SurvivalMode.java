package com.mygdx.pianogame.modes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SurvivalMode {
    private Random random;
    private ArrayList<Integer> correctSequence;
    private ArrayList<Integer> pianoTilesToShow;
    public int currentLevel;
    private int additionalTiles;
    private int answerTiles;
    private int repeatLevel;
    public int livesLeft;

    public SurvivalMode(){
       random = new Random();
       correctSequence = new ArrayList<Integer>();
       pianoTilesToShow = new ArrayList<Integer>();
       resetLevels();
    }

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

    public ArrayList<Integer> getCorrectSequence(){
        return correctSequence;
    }
    public ArrayList<Integer> getPianoTilesToShow(){
        return pianoTilesToShow;
    }
}
