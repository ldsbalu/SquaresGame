package edu.byuh.cis.cs203.numberedsquares1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by draperg on 8/21/17.
 */

public class NumberedSquare {
    private float size;
    private int id;
    private static int counter = 1;
    private RectF bounds;
    private float screenWidth, screenHeight;
    private Paint textPaint;
    private Paint backgroundPaint;

    /**
     * Constructor for NumberedSquare. This constructor accepts two parameters,
     * representing the width and height of the display. The constructor finds the
     * smaller of these two values (typically, width is smaller for portrait orientation;
     * height is smaller for landscape) and bases the size of the square on that.
     * @param w - the width of the screen
     * @param h - the height of the screen
     */
    public NumberedSquare(float w, float h) {
        screenWidth = w;
        screenHeight = h;
        float lesser = Math.min(w,h);
        size = lesser/8f;
        id = counter;
        counter++;
        float x = (float)(Math.random()*(screenWidth-size));
        float y = (float)(Math.random()*(screenHeight-size));
        bounds = new RectF(x, y, x+size, y+size);
        backgroundPaint = new Paint();
        textPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(lesser/100f);
        backgroundPaint.setColor(Color.BLUE);
        textPaint.setTextSize(Main.findThePerfectFontSize(lesser/20f));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * Render the square and its label into the display
     * @param c the Canvas object, representing the dispaly area
     */
    public void draw(Canvas c) {
        c.drawRect(bounds, backgroundPaint);
        c.drawText(""+id, bounds.centerX(), bounds.centerY()+size/7, textPaint);
    }

    /**
     * Static convenience method to ensure that the ID numbers don't grow too large.
     */
    public static void resetCounter() {
        counter = 1;
    }
}
