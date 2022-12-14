package com.example.splashscreen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Jackson Pike
 * A relatively simple class, which draws a rectangle (Square) on given canvas, at a random position, within supplied canvas' view screen.
 * Additionally, a ID number is attached and displayed in the center of a given square, which is dynamically created sequentially through the use of a 'seed' static value.
 */
public class NumberedSquare implements TickListener{

    public int identifier; //ID value unique to the square
    private static int seed = 0; // Counter value to assist in unique ID'ing
    private RectF bounds; //RectF to hold the position of the square on the screen.
    private Paint tPaint; //Two paint objects to supply color to the square. tPaint is for Text, bPaint is for the color of the square
    private float canvasWidth, canvasHeight; /* Canvas width and height are fields and assigned at construction, in order to be accessed throughout the class.
                                                           * Square size is self explanatory (The width & height of square) */
    private float squareSize;
    private boolean frozen = false;
    public PointF velocity = new PointF();
    private Random rand = new Random();
    public static Canvas c;
    private Bitmap nonFrozenImage, frozenImage;
    private static ArrayList<Integer> colorOptions;
    private int nFrozenInt, frozenInt;


    /**
     * The constructor! Horray. Sets the ID, using the seed, then assigns paint fields
     * @param w - A supplied width of a canvas, in order to dynamically scale the size of the square.
     * @param h - A supplied height of a canvas,in order to dynamically scale the size of the square.
     */

    public NumberedSquare(float w, float h, Resources res, Context context) {

        identifier = ++seed;
        canvasWidth = w;
        canvasHeight = h;
        squareSize = canvasWidth * 0.15f;
        bounds = genBounds();
        velocity.x = (float) (Math.random()*20)-5;
        velocity.y = (float) (Math.random()*20)-5;

        tPaint = new Paint();
        tPaint.setTextSize(80);
        tPaint.setColor(Color.rgb(208, 236, 231 ));

        frozenInt = SquarePreference.getBackground(context);

        switch(frozenInt) {
            case R.drawable.frozen_red:
                nFrozenInt = R.drawable.nonfrozen_red;
                break;
            case R.drawable.frozen_green:
                nFrozenInt = R.drawable.nonfrozen_green;
                break;
            case R.drawable.frozen_blue:
                nFrozenInt = R.drawable.nonfrozen_blue;
                break;
        }
        nonFrozenImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, nFrozenInt), (int)squareSize, (int)squareSize, false);
        frozenImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, frozenInt), (int)squareSize, (int)squareSize, false);


    }

    public static void decrementSeed() {
        seed--;
    }

    /**
     * A method to contain the math of randomly finding position of square on screen, and creating a RectF storing the resulting values.
     * @return RectF containing square position on screen (Four float values, left, top, right, bottom.
     */
    public RectF genBounds() {
        Random r = new Random();
        int startingX, startingY;
        RectF potential;

        startingX = r.nextInt((int) canvasWidth - (int) squareSize);
        startingY = r.nextInt((int) canvasHeight - (int) squareSize);
        potential = new RectF(startingX, startingY, startingX+squareSize, startingY+squareSize);

        return potential;
    }

    /**
     * Method that draws the resulting rectangle and ID label on a supplied canvas, unless the square is frozen, in which case it'll draw an ice cube
     * @param c - The canvas in which to draw the resulting "Numbered Square" on.
     */
    public void draw(Canvas c) {
        if(frozen) {
            c.drawBitmap(frozenImage, bounds.left, bounds.top, tPaint);
        } else {
            c.drawBitmap(nonFrozenImage, bounds.left, bounds.top, tPaint);
        }
        c.drawText(Integer.toString(identifier), bounds.centerX() - squareSize / 7, bounds.centerY() + squareSize / 7, tPaint);
    }

    /**
     * Standard getter to return the RectF representation of the NumberedSquare
     * @return
     */
    public RectF getRectF() {
        return bounds;
    }

    /**
     * Move the square by the velocity, as well as bounce squares off wall, if it exceeds the canvas
     */
    public void move() {
        if(!frozen) {
            bounds.offset(velocity.x, velocity.y);
            if (bounds.right >= canvasWidth) {
                velocity.x *= -1;
                bounds.offsetTo(canvasWidth - squareSize - 1, bounds.top);
            } else if (bounds.left <= 0) {
                velocity.x *= -1;
                bounds.offsetTo(1, bounds.top);
            } else if (bounds.top <= 0) {
                velocity.y *= -1;
                bounds.offsetTo(bounds.left, 1);
            } else if (bounds.bottom > canvasHeight) {
                velocity.y *= -1;
                bounds.offsetTo(bounds.left, canvasHeight - squareSize - 1);
            }
        }
    }

    /**
     * Toggle the state of <code>{@link #frozen}</code>
     */
    public void toggleFreeze() {
        if(frozen) {
            frozen = false;
        } else {
            frozen = true;
        }
    }

    /**
     *  Set the state of <code>{@link #frozen}</code> to TRUE
     */
    public void freeze() {
        frozen = true;
    }

    /**
     * Self explanatory getter for <code>frozen</code>
     * @return
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Implementation of {@link TickListener#tick()}
     *
     * Calls the {@link #move()} method every time.
     */
    @Override
    public void tick() {
        move();
    }

    /**
     *  Set seed to 0 in order to start square creation at ID 1 when needed.
     */
    public static void resetSeed() {
        seed = 0;
    }

}
