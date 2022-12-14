package com.example.spellingGameOne.gameStyles;

import android.content.res.Resources;

import com.example.spellingGameOne.R;
import com.example.spellingGameOne.sys.NumberedSquare;
import com.example.spellingGameOne.sys.Score;
import com.example.spellingGameOne.sys.TouchStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Another numberedSquare game! This one generates random letters in the alphabet. The user must tap the squares in alphabetical order.
 */
public class AlphabeticalGame implements GameStyle {

    private List<String> labels = new ArrayList<>();
    private int numberOfChoices = 7;
    private int iterationCount = 1;
    private final int MAX_LEVEL = 3;
    private int alphabeticalIndex;
    private Random r = new Random();



    private void setValues() {
        labels.clear();
        while (labels.size() < numberOfChoices) {
            char a = (char) (r.nextInt(26) + 'A');
            if (!labels.contains(Character.toString(a))) {
                labels.add(Character.toString(a));
            }
        }
        alphabeticalIndex = 0;
        Collections.sort(labels);

    }
    /**
     * Returns the string to be displayed on each new level of a NumberedSquare game
     *
     * @param res - resources of the view object calling this method
     * @return - Returns the string to be displayed on each new level of a NumberedSquare game
     */
    @Override
    public String getNextLevelLabel(Resources res) {
        return res.getString(R.string.alphabetInstruction);
    }

    /**
     * Returns the string to be displayed when the user taps on the wrong square
     *
     * @param res - resources of the view object calling this method
     * @return Returns the string to be displayed when the user taps on the wrong square
     */
    @Override
    public String getTryAgainLabel(Resources res) {
        String letter = labels.get(alphabeticalIndex);
        int charValue = letter.charAt(0);
        String hint = String.valueOf( (char) (charValue -1));
        return String.format("%s %s", res.getString(R.string.alphabetHint), hint);
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
        String expectedLabel = labels.get(alphabeticalIndex);
        if(receivedLabel.equals(expectedLabel)) {
            alphabeticalIndex++;
            if(alphabeticalIndex == labels.size()) {
                iterationCount++;
                switch(iterationCount) {
                    case MAX_LEVEL: return TouchStatus.GAME_OVER;
                    default: return TouchStatus.LEVEL_COMPLETE;
                }
            }
            return TouchStatus.CONTINUE;
        }
        return TouchStatus.TRY_AGAIN;
    }

    @Override
    public String toString() {
        return "Alphabetic";
    }

    public int numberOfSquares() {
        return MAX_LEVEL * numberOfChoices;
    }

}
