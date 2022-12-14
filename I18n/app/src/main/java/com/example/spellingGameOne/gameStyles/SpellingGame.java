package com.example.spellingGameOne.gameStyles;

import android.content.res.Resources;

import com.example.spellingGameOne.R;
import com.example.spellingGameOne.sys.NumberedSquare;
import com.example.spellingGameOne.sys.TouchStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  SpellingGame Class :)
 * @author Jackson Pike
 * Implements the GameStyle interface
 *
 * User is given a word to spell, with floating squares with each letter of the word. Tap them all
 * to move to the next word.
 *
 */
public class SpellingGame implements GameStyle{

    private final List<String> WORD_LIST;
    private String[] currentLevelSplit;
    private int numberOfSquares = 0;
    private int levelIndex = 0;
    private int nextLetterIndex = 0;
    private int winningLetterIndex;

    private String expectedLetter;
    private String currentLevel;

    /**
     * Constructor, defines the list of words to be used, then starts the program.
     */
    public SpellingGame() {
        String[] wds = {"LEG", "FOOT", "KIDNEY", "LAMB", "SLEEP", "DEMON", "PARALYSIS"};
        WORD_LIST = new ArrayList<>(Arrays.asList(wds));
        levelStart();

        for(int i = 0; i < WORD_LIST.size(); i++) {
            for(String letter:WORD_LIST.get(i).split("")) {
                numberOfSquares++;
            }
        }

    }

    private void levelStart() {

        nextLetterIndex = 0;
        currentLevel = WORD_LIST.get(levelIndex);
        currentLevelSplit = currentLevel.split("");
        winningLetterIndex = currentLevelSplit.length-1;
        expectedLetter = currentLevelSplit[0];
    }
    private void nextLetter() {
        nextLetterIndex++;
        expectedLetter = currentLevelSplit[nextLetterIndex];
    }
    /**
     * Returns the string to be displayed on each new level of a NumberedSquare game
     * @param res - resources of the view object calling this method
     * @return - Returns the string to be displayed on each new level of a NumberedSquare game
     */
    @Override
    public String getNextLevelLabel(Resources res) {
        return String.format("%s '%s'", res.getString(R.string.hangmanSpellPrompt), currentLevel);
    }
    /**
     * Returns the string to be displayed when the user taps on the wrong square
     * @param res - resources of the view object calling this method
     * @return  Returns the string to be displayed when the user taps on the wrong square
     */
    @Override
    public String getTryAgainLabel(Resources res) {
        return String.format("%s %s", res.getString(R.string.spellingGameHint), expectedLetter);
    }
    /**
     * Returns a list of strings, in which each element is a label for a NumberedSquare
     * @return - Returns a list of strings, in which each element is a label for a NumberedSquare
     */
    @Override
    public List<String> getSquareLabels() {
        List<String> labels = new ArrayList<>();
        Collections.addAll(labels, currentLevelSplit);
        return labels;
    }
    /**
     * Returns the <code>TouchStatus</code> given the NumberedSquare c passed (Is it the correct square in the sequence)
     * @param tappedSquare - Tapped NumberSquare supplied by gameview
     * @return TouchStatus
     */
    @Override
    public TouchStatus getTouchStatus(NumberedSquare tappedSquare) {
        TouchStatus retVal = TouchStatus.TRY_AGAIN;

        String receivedLetter = tappedSquare.label;
        if(receivedLetter.equals(expectedLetter)){
            if(receivedLetter.equals(currentLevelSplit[winningLetterIndex]) && nextLetterIndex == winningLetterIndex){

                levelIndex++;

                if(levelIndex == WORD_LIST.size()) {
                    return TouchStatus.GAME_OVER;
                }
                levelStart();
                return TouchStatus.LEVEL_COMPLETE;
            }
            retVal = TouchStatus.CONTINUE;
            nextLetter();
        }
        return retVal;
    }

    @Override
    public int numberOfSquares() {
        return numberOfSquares;
    }

    @Override
    public String toString() {
        return "Spelling";
    }
}
