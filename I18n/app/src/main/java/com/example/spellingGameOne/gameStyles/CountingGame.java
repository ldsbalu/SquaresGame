package com.example.spellingGameOne.gameStyles;

import android.content.res.Resources;

import com.example.spellingGameOne.R;
import com.example.spellingGameOne.sys.NumberedSquare;
import com.example.spellingGameOne.sys.TouchStatus;

import java.util.ArrayList;
import java.util.List;

public class CountingGame implements GameStyle {

    private int level = 1;
    private int expectedLabel = 1;
    private int numberOfSquares = 0;

    public CountingGame() {
        for (int h = 0; h < 5; h++) {
            for (int i = 0; i < h; i++) {
                numberOfSquares++;
            }
        }
    }

    /**
     * Returns the string to be displayed on each new level of a NumberedSquare game
     * @param res - resources of the view object calling this method
     * @return - Returns the string to be displayed on each new level of a NumberedSquare game
     */
    @Override
    public String getNextLevelLabel(Resources res) {
        return String.format("%s %s", res.getString(R.string.countingLevelLabel), level);
    }

    /**
     * Returns the string to be displayed when the user taps on the wrong square
     * @param res - resources of the view object calling this method
     * @return  Returns the string to be displayed when the user taps on the wrong square
     */
    @Override
    public String getTryAgainLabel(Resources res) {
        return res.getString(R.string.tryAgain);
    }
    /**
     * Returns a list of strings, in which each element is a label for a NumberedSquare
     * @return - Returns a list of strings, in which each element is a label for a NumberedSquare
     */
    @Override
    public List<String> getSquareLabels() {
        List<String> retVal = new ArrayList<>();
        for(int i = 0; i < level; i++) {
            retVal.add(String.valueOf(i+1));
        }

        return retVal;
    }
    /**
     * Returns the <code>TouchStatus</code> given the NumberedSquare c passed (Is it the correct square in the sequence)
     * @param c - Tapped NumberSquare supplied by gameview
     * @return TouchStatus
     */
    @Override
    public TouchStatus getTouchStatus(NumberedSquare c) {
        TouchStatus status = TouchStatus.TRY_AGAIN;
        if(c != null) {
            if(c.label.equals(String.valueOf(expectedLabel))){
                if(expectedLabel == level) {
                    level++;
                    expectedLabel = 1;
                    if(level < 5) {
                        return TouchStatus.LEVEL_COMPLETE;
                    } else {
                        return TouchStatus.GAME_OVER;
                    }
                }
                status = TouchStatus.CONTINUE;
                expectedLabel++;
            }
        }
        return status;
    }

    @Override
    public int numberOfSquares() {
        return numberOfSquares;
    }

    @Override
    public String toString() {
        return "CountingGame";
    }

}
