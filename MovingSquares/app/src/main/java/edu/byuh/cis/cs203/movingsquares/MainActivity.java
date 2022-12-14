package edu.byuh.cis.cs203.movingsquares;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;

public class MainActivity extends Activity {

    private GameView gv;

    /**
     * It all starts here
     * @param savedInstanceState - passed in by OS. Ignore for now.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Numbered Squares 3");
        gv = new GameView(this);
        setContentView(gv);
    }

    /**
     * Helper method I made up one day, to work around the
     * lack of documentation about font sizes in Android.
     * This function is only "partially debugged" and I do not
     * guarantee its accuracy.
     * @param lowerThreshold how many pixels high the text should be
     * @return the font size that corresponds to the requested pixel height
     */
    public static float findThePerfectFontSize(float lowerThreshold) {
        float fontSize = 1;
        Paint p = new Paint();
        p.setTextSize(fontSize);
        while (true) {
            float asc = -p.getFontMetrics().ascent;
            if (asc > lowerThreshold) {
                break;
            }
            fontSize++;
            p.setTextSize(fontSize);
        }
        return fontSize;
    }

}
