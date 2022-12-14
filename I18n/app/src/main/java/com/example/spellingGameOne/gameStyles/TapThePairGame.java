package com.example.spellingGameOne.gameStyles;

import android.content.res.Resources;

import com.example.spellingGameOne.R;
import com.example.spellingGameOne.sys.NumberedSquare;
import com.example.spellingGameOne.sys.TouchStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Another NumberedSquare game. In this game, you are given random letters (squares) on the screen. One letter occurs on two squares. These
 * are the squares you must tap to move onto the next level.
 */
public class TapThePairGame implements GameStyle {

    private List<String> labels = new ArrayList<>();
    private int numberOfChoices = 7;
    private int iterationCount = 0;
    private final int MAX_LEVEL = 2;
    private Random r = new Random();
    private char duplicate;
    private int duplicateTapped = 0;



    private void setValues() {
        labels.clear();
        duplicateTapped = 0;
        while(labels.size() < numberOfChoices) {
            char a = (char) (r.nextInt(26) + 'A');
            if(!labels.contains(Character.toString(a))) {
                labels.add(Character.toString(a));
            }
        }

        while(true) {
            duplicate = (char) (r.nextInt(26) + 'A');
            if(!labels.contains(Character.toString(duplicate))){
                labels.add(Character.toString(duplicate));
                labels.add(Character.toString(duplicate));
                break;
            }
        }
    }
    /**
     * Returns the string to be displayed on each new level of a NumberedSquare game
     *
     * @param res - resources of the view object calling this method
     * @return - Returns the string to be displayed on each new level of a NumberedSquare game
     */
    @Override
    public String getNextLevelLabel(Resources res) {
        return res.getString(R.string.levelLabelTapPAir);
    }
    
    /**
     * Returns the string to be displayed when the user taps on the wrong square
     *
     * @param res - resources of the view object calling this method
     * @return Returns the string to be displayed when the user taps on the wrong square
     */
    @Override
    public String getTryAgainLabel(Resources res) {
        return res.getString(R.string.tapPairHint);
    }

    /**
     * Returns a list of strings, in which each element is a label for a NumberedSquare
     *
     * @return - Returns a list of strings, in which each element is a label for a NumberedSquare
     */
    @Override
    public List<String> getSquareLabels() {
        setValues();
        return labels;
    }


    /**
     * Returns the <code>TouchStatus</code> given the NumberedSquare c passed (Is it the correct square in the sequence)
     *
     * @param tappedSquare - Tapped NumberSquare supplied by gameview
     * @return TouchStatus
     */
    @Override
    public TouchStatus getTouchStatus(NumberedSquare tappedSquare) {
        String receivedLabel = tappedSquare.label;
        String expectedLabel = Character.toString(duplicate);
        if(receivedLabel.equals(expectedLabel)) {
            duplicateTapped++;
            if(duplicateTapped == 2) {
                iterationCount++;
                if(iterationCount > MAX_LEVEL) {
                    return TouchStatus.GAME_OVER;
                }
                return TouchStatus.LEVEL_COMPLETE;
            }
            return TouchStatus.CONTINUE;
        }
        return TouchStatus.TRY_AGAIN;
    }

    @Override
    public int numberOfSquares() {
        return MAX_LEVEL * 2;
    }

    @Override
    public String toString() {
        return "TapThePair";
    }
}
