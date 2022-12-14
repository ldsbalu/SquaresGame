package edu.byuh.cis.cs203.movingsquares;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by draperg on 8/21/17.
 */

public class NumberedSquare {
    private int id;
    private static int counter = 1;
    private RectF bounds;
    private float screenWidth, screenHeight;
    private Paint textPaint;
    private Paint backgroundPaint;
    private PointF velocity;

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
        float lesser = Math.min(w, h);
        float size = lesser / 8f;
        float x = (float) (Math.random() * (screenWidth - size));
        float y = (float) (Math.random() * (screenHeight - size));
        bounds = new RectF(x, y, x + size, y + size);
        id = counter;
        counter++;
        backgroundPaint = new Paint();
        textPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(size / 12f);
        backgroundPaint.setColor(Color.BLUE);
        textPaint.setTextSize(MainActivity.findThePerfectFontSize(size * 0.4f));
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        float randomX = (float) (size * 0.08 - Math.random() * size * 0.16);
        float randomY = (float) (size * 0.08 - Math.random() * size * 0.16);
        velocity = new PointF(randomX, randomY);
    }

    /**
     * This is just like the other constructor, but instead of using a randomly-generated
     * rectangle as its bounds, it uses the pre-instantiated rectangle that's passed in.
     * This is a little bit wasteful, as we're just throwing away the random rectangle,
     * but it was easier to write. I'm saving my time rather than the CPU's. :-)
     * @param w
     * @param h
     * @param rect
     */
    public NumberedSquare(float w, float h, RectF rect) {
        this(w,h);
        bounds.set(rect);
    }

    /**
     * Move the square by its velocity. If we're near the edges, move
     * in the opposite direction.
     */
    public void move() {
        if (bounds.left < 0 || bounds.right > screenWidth) {
            velocity.x = -velocity.x;
        }
        if (bounds.top < 0 || bounds.bottom > screenHeight) {
            velocity.y = -velocity.y;
        }
        bounds.offset(velocity.x, velocity.y);
    }

    /**
     * Render the square and its label into the display
     * @param c the Canvas object, representing the dispaly area
     */
    public void draw(Canvas c) {
        c.drawRect(bounds, backgroundPaint);
        c.drawText(""+id, bounds.centerX(), bounds.centerY()+getSize()/7, textPaint);
    }

    public float getSize() {
        return bounds.width();
    }

    /**
     * Tests whether "this" square intersects the "other" square which is passed
     * into this method as an input parameter
     *
     * @param other the other square to compare with
     * @return true if the two squares overlap; false otherwise
     */
    public boolean overlaps(NumberedSquare other) {
        return overlaps(other.bounds);
    }

    /**
     * Tests whether "this" square intersects the "other" square which is passed
     * into this method as an input parameter
     *
     * @param other the other square to compare with
     * @return true if the two squares overlap; false otherwise
     */
    public boolean overlaps(RectF other) {
        return RectF.intersects(this.bounds, other);
    }

    /**
     * Static convenience method to ensure that the ID numbers don't grow too large.
     */
    public static void resetCounter() {
        counter = 1;
    }
}
