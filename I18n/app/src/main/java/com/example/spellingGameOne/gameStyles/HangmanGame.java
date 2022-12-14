package com.example.spellingGameOne.gameStyles;

import android.content.Context;
import android.content.res.Resources;

import com.example.spellingGameOne.R;
import com.example.spellingGameOne.sys.NumberedSquare;
import com.example.spellingGameOne.sys.TouchStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * GameStyle that creates a HangMan like play style. (Although, it is sans wrong guess penalty)
 */
public class HangmanGame implements GameStyle {

    private List<String> WORDS;
    private List<String> splitWord;
    private List<String> userProgress;
    private int listIndex = 0;
    private int numberOfChoices = 8;
    private int numberOfSquares = 0;
    private String currentWord;
    private Context c;

    /**
     * Constructor. Instantiates list of words
     */
    public HangmanGame() {
        WORDS = new ArrayList<>();
        Collections.addAll(WORDS,  "TIGER", "LION", "GRAPE", "YOLK");
        splitNextWord();
        for(int i = 0; i < WORDS.size(); i++) {
            for(String letter:WORDS.get(i).split("")) {
                numberOfSquares++;
            }
        }
    }

    private void splitNextWord() {
        splitWord = new ArrayList<>();
        userProgress = new ArrayList<>();
        currentWord = WORDS.get(listIndex);
        Collections.addAll(splitWord, currentWord.split(""));
    }


    /**
     * Returns the string to be displayed on each new level of a NumberedSquare game
     *
     * @param res - resources of the view object calling this method
     * @return - Returns the string to be displayed on each new level of a NumberedSquare game
     */
    @Override
    public String getNextLevelLabel(Resources res) {

        String underline = "";
        for(int i = 0; i < WORDS.get(listIndex).length(); i++) {
            underline += "_ ";
        }
        return String.format("%s %s", res.getString(R.string.guessWordHangman), underline);

    }

    /**
     * Returns the string to be displayed when the user taps on the wrong square
     *
     * @param res - resources of the view object calling this method
     * @return Returns the string to be displayed when the user taps on the wrong square
     */
    @Override
    public String getTryAgainLabel(Resources res) {
        String hint = "";
        switch(currentWord) {
            case "LION":
                hint = "King of Jungle";
                break;
            case "TIGER":
                hint = "Indian National Animal";
                break;
            case "GRAPE":
                hint = "Purple Fruit";
                break;
            case "YOLK":
                hint = "Egg";
                break;
        }
        String progress = "";
        if(!userProgress.isEmpty()) {
            for (String s : userProgress) {
                progress += s;
            }
        }
        int len = progress.length();
        for(int i = 0; i < WORDS.get(listIndex).length()-len; i++) {
            progress += "_ ";
        }
        return String.format("Try Again: %s | (Hint: %s)", progress, hint);
    }

    /**
     * Returns a list of strings, in which each element is a label for a NumberedSquare
     *
     * @return - Returns a list of strings, in which each element is a label for a NumberedSquare
     */
    @Override
    public List<String> getSquareLabels() {
        splitNextWord();
        List<String> retVal = new ArrayList<>();
        Random r = new Random();
        for(int i = 0; i < numberOfChoices-currentWord.length(); i++) {
            char c = (char) (r.nextInt(26) + 'A');
            retVal.add(Character.toString(c));
        }
        for (String s : currentWord.split("")) {
          if(!retVal.contains(s)) {
              retVal.add(s);
          }
        }
        return retVal;
    }


    /**
     * Returns the <code>TouchStatus</code> given the NumberedSquare c passed (Is it the correct square in the sequence)
     *
     * @param c - Tapped NumberSquare supplied by gameview
     * @return TouchStatus
     */
    @Override
    public TouchStatus getTouchStatus(NumberedSquare c) {
        TouchStatus retVal = TouchStatus.TRY_AGAIN;
        String receivedLabel = c.label;
        String expectedLabel = splitWord.get(userProgress.size());

        if(receivedLabel.equals(expectedLabel)) {
            userProgress.add(receivedLabel);
            if(userProgress.size() == currentWord.length()) {
                listIndex++;
                if(listIndex > WORDS.size()-1) {
                    return TouchStatus.GAME_OVER;
                }
                return TouchStatus.LEVEL_COMPLETE;
            }
            retVal = TouchStatus.CONTINUE;

        }
        return retVal;
    }

    @Override
    public int numberOfSquares() {
        return numberOfSquares;
    }

    @Override
    public String toString() {
        return "Hangman";
    }
}
