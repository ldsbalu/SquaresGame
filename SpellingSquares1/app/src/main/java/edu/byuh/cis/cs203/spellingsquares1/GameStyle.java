package edu.byuh.cis.cs203.spellingsquares1;

import java.util.List;

/**
 * Created by draperg on 10/25/17.
 */

public interface GameStyle {

    public enum TouchStatus {
        CONTINUE,
        LEVEL_COMPLETE,
        TRY_AGAIN
    }

    String getNextLevelLabel();
    String getTryAgainLabel();
    List<String> getSquareLabels();
    TouchStatus getTouchStatus(NumberedSquare c);
}
