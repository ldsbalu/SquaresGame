package edu.byuh.cis.cs203.frozensquares;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.List;

/**
 * Created by draperg on 8/21/17.
 */

public class NumberedSquare implements TickListener{
    private float size;
    private int id;
    private static int counter = 1;
    private RectF bounds;
    private float screenWidth, screenHeight;
    private Paint textPaint;
    private Paint backgroundPaint;
    private PointF velocity;
    private boolean frozen;

    @Override
    public void tick() {
        move();
    }

    private enum HitSide {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        NONE
    }

    /**
     * Constructor for NumberedSquare. This constructor accepts two parameters,
     * representing the width and height of the display. The constructor finds the
     * smaller of these two values (typically, width is smaller for portrait orientation;
     * height is smaller for landscape) and bases the size of the square on that.
     *
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
        if (!frozen) {
            if (bounds.left < 0 || bounds.right > screenWidth) {
                velocity.x *= -1;
                if (bounds.left < 0) {
                    setLeft(1);
                } else {
                    setRight(screenWidth - 1);
                }
            }
            if (bounds.top < 0 || bounds.bottom > screenHeight) {
                velocity.y *= -1;
                if (bounds.top < 0) {
                    setTop(1);
                } else {
                    setBottom(screenHeight - 1);
                }
            }
            bounds.offset(velocity.x, velocity.y);
        }
    }

    /**
     * Render the square and its label into the display
     *
     * @param c the Canvas object, representing the dispaly area
     */
    public void draw(Canvas c) {
        c.drawRect(bounds, backgroundPaint);
        c.drawText("" + id, bounds.centerX(), bounds.centerY() + size / 7, textPaint);
    }

    /**
     * Simply returns the width (which is the same as the height) of this square.
     * @return the width of the square
     */
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

    /**
     * Checks whether this square has collided with the passed-in square. If so, both
     * square's velocities are adjusted.
     * @param other another square to compare against this one.
     */
    public void checkForCollision(NumberedSquare other) {
        if (other.id > this.id) {
            if (this.overlaps(other)) {
                HitSide hitSide = HitSide.NONE;
                float dtop = Math.abs(other.bounds.bottom - this.bounds.top);
                float dbot = Math.abs(other.bounds.top - this.bounds.bottom);
                float dleft = Math.abs(other.bounds.right - this.bounds.left);
                float drt = Math.abs(other.bounds.left - this.bounds.right);
                float min = Math.min(Math.min(dtop, dbot), Math.min(drt, dleft));
                if (min == dtop) {
                    hitSide = HitSide.TOP;
                }
                if (min == dbot) {
                    hitSide = HitSide.BOTTOM;
                }
                if (min == dleft) {
                    hitSide = HitSide.LEFT;
                }
                if (min == drt) {
                    hitSide = HitSide.RIGHT;
                }
                exchangeMomentum(other, hitSide);
            }
        }
    }

    private void exchangeMomentum(NumberedSquare other, HitSide hitside) {
        float tmp;
        forceApart(other, hitside);
        if (hitside == HitSide.TOP || hitside == HitSide.BOTTOM) {
            if (this.frozen) {
                other.velocity.y = -other.velocity.y;
            } else if (other.frozen) {
                this.velocity.y = -this.velocity.y;
            } else {
                tmp = this.velocity.y;
                this.velocity.y = other.velocity.y;
                other.velocity.y = tmp;
            }
        } else {
            if (this.frozen) {
                other.velocity.x = -other.velocity.x;
            } else if (other.frozen) {
                this.velocity.x = -this.velocity.x;
            } else {
                tmp = this.velocity.x;
                this.velocity.x = other.velocity.x;
                other.velocity.x = tmp;
            }
        }
    }

    private void forceApart(NumberedSquare other, HitSide hitside) {
        RectF myBounds = new RectF(this.bounds);
        RectF otherBounds = new RectF(other.bounds);
        switch (hitside) {
            case LEFT:
                this.setLeft(otherBounds.right + 1);
                other.setRight(myBounds.left - 1);
                break;
            case RIGHT:
                this.setRight(otherBounds.left - 1);
                other.setLeft(myBounds.right + 1);
                break;
            case TOP:
                this.setTop(otherBounds.bottom + 1);
                other.setBottom(myBounds.top - 1);
                break;
            case BOTTOM:
                this.setBottom(otherBounds.top - 1);
                other.setTop(myBounds.bottom + 1);
        }
    }

    private void setBottom(float b) {
        if (!frozen) {
            float dy = b - bounds.bottom;
            bounds.offset(0, dy);
        }
    }

    private void setRight(float r) {
        if (!frozen) {
            float dx = r - bounds.right;
            bounds.offset(dx, 0);
        }
    }

    private void setLeft(float lf) {
        if (!frozen) {
            bounds.offsetTo(lf, bounds.top);
        }
    }

    private void setTop(float t) {
        if (!frozen) {
            bounds.offsetTo(bounds.left, t);
        }
    }

    /**
     * Prevent this square from moving. Change its appearance too.
     */
    public void freeze() {
        frozen = true;
        backgroundPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.CYAN);
    }

    /**
     * Perform an inside/outside test on this square
     * @param x the X coordinate of the point to test
     * @param y the Y coordinate of the point to test
     * @return true if (x,y) is inside the square, false otherwise
     */
    public boolean contains(float x, float y) {
        return bounds.contains(x,y);
    }


}
