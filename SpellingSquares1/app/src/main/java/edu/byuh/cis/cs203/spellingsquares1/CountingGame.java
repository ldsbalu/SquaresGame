package edu.byuh.cis.cs203.spellingsquares1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by draperg on 10/25/17.
 */

public class CountingGame implements GameStyle {

    private int level;
    private List<String> labels;
    private int nextExpectedID;

    public CountingGame() {
        level = 1;
        nextExpectedID = 1;
        labels = new ArrayList<>();
        prepare();
    }

    private void prepare() {
        nextExpectedID = 1;
        labels.clear();
        for (int i=1; i<=level; ++i) {
            labels.add(""+i);
        }
    }


    @Override
    public String getNextLevelLabel() {
        if (level == 1) {
            return "Tap the 1";
        } else if (level == 2) {
            return "Tap the 1, then the 2";
        } else {
            return "Tap the cubes from 1 to " + level;
        }
    }

    @Override
    public String getTryAgainLabel() {
        return "Oops, tap the " + nextExpectedID;
    }

    @Override
    public List<String> getSquareLabels() {
        return labels;
    }

    @Override
    public TouchStatus getTouchStatus(NumberedSquare c) {
        int id = c.getID();
        if (id <= nextExpectedID) {
            if (id == nextExpectedID) {
                nextExpectedID++;
                if (id == labels.size()) {
                    level++;
                    prepare();
                    return TouchStatus.LEVEL_COMPLETE;
                }
            }
            return TouchStatus.CONTINUE;
        } else {
            return TouchStatus.TRY_AGAIN;
        }
    }
}
