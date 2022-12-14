package com.example.spellingGameOne.gameStyles;

import android.content.res.Resources;

import com.example.spellingGameOne.sys.NumberedSquare;
import com.example.spellingGameOne.sys.TouchStatus;

import java.util.List;

/**
 * GameStyle interface, works with GameView class to supply the latter various game logic.
 */
public interface GameStyle {
    /**
     * Returns the string to be displayed on each new level of a NumberedSquare game
     * @param res - resources of the view object calling this method
     * @return - Returns the string to be displayed on each new level of a NumberedSquare game
     */
    String getNextLevelLabel(Resources res);

    /**
     * Returns the string to be displayed when the user taps on the wrong square
     * @param res - resources of the view object calling this method
     * @return  Returns the string to be displayed when the user taps on the wrong square
     */
    String getTryAgainLabel(Resources res);

    /**
     * Returns a list of strings, in which each element is a label for a NumberedSquare
     * @return - Returns a list of strings, in which each element is a label for a NumberedSquare
     */
    List<String> getSquareLabels();

    /**
     * Returns the <code>TouchStatus</code> given the NumberedSquare c passed (Is it the correct square in the sequence)
     * @param c - Tapped NumberSquare supplied by gameview
     * @return TouchStatus
     */
    TouchStatus getTouchStatus(NumberedSquare c);

    int numberOfSquares();
}
