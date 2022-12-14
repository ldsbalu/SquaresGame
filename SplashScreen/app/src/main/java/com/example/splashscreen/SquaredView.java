package com.example.splashscreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * @author Jackson Pike
 * A subclass view for NumberedSquare project. Overrides onDraw to create and draw 5 <code>NumberedSquare</code>'s on the canvas.
 */
public class SquaredView extends View implements TickListener {

    private List<NumberedSquare> squares;
    private boolean initialized = false;
    private MoveHandler hndlr;
    private float dr, dl, db, dt, smallest;
    private int offsetX, offsetY;
    private Bitmap backgroundBitmap;
    private int level = 1;
    private int sequentialID = 1;
    private MediaPlayer soundtrack, iceEffect;



    /**
     * Constructor. Only makes call to <code>super()</code> No additional code.
     * @param c - Context
     */
    public SquaredView(Context c) {
        super(c);

        squares = new ArrayList<NumberedSquare>();
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper);
        offsetX = 0;
        offsetY = 0;

        if(SquarePreference.soundOn(c)) {
            soundtrack = MediaPlayer.create(c, R.raw.soundtrack);
            soundtrack.setLooping(true);
            soundtrack.start();
        }

        iceEffect = MediaPlayer.create(c, R.raw.icecrack);
        iceEffect.setLooping(false);

        hndlr = new MoveHandler(SquarePreference.getSpeed(c));


    }

    /**
     * Overriden onDraw Method. Draws a color on the canvas for background, and then creates and invokes the draw method of 5 numberedsquares. s
     * @param c
     */
    @Override
    public void onDraw(Canvas c) {

        if(!initialized) {
            backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, c.getWidth(), c.getHeight(), true);
            createSquares(c, level);
            hndlr.register(this);
            initialized = true;
            Toast levelAnnouncer = Toast.makeText(getContext(), String.format("Level %d", level), Toast.LENGTH_SHORT);
            levelAnnouncer.show();


        }
        c.drawBitmap(backgroundBitmap, 0, 0, null);
        for (NumberedSquare square : squares) {
            square.draw(c);
        }

    }



    /**
     * Overriden onTouchEvent method which redraws the screen (creating a new set of NumberedSquares in the process)
     * @param e MotionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if(e.getAction( ) == MotionEvent.ACTION_UP) {
            isSquareTouched(new PointF(e.getX(), e.getY()));
        }
        return true;
    }

    public void isSquareTouched(PointF touchPoint) {
        NumberedSquare tappedSquare = null;
        for (NumberedSquare square : squares) {
            if (square.getRectF().contains(touchPoint.x, touchPoint.y)) {
                tappedSquare = square;
            }
        }
        if(tappedSquare != null) {
            if(tappedSquare.identifier == sequentialID) {
                tappedSquare.freeze();
                if(SquarePreference.effectEnabled(getContext()) && soundtrack != null) {
                    soundtrack.pause();

                    iceEffect.start();
                    iceEffect.setOnCompletionListener(
                            mediaPlayer -> soundtrack.start());
                }
                sequentialID++;
            } else {
                Toast outOfSequence = Toast.makeText(getContext(), "Please tap in order!", Toast.LENGTH_SHORT);
                outOfSequence.show();
            }

            if(tappedSquare.identifier == level && tappedSquare.identifier == sequentialID-1) {
                level++;
                sequentialID = 1;
                initialized = false;
                NumberedSquare.resetSeed();
                invalidate();
            }

        }
    }
    /**
     * A method to create <code>n</code> number of squares on a given canvas.
     * @param c - A supplied canvas to give height & width to NumberedSquare constructor
     * @param n - Number of squares you want created
     */
    public void createSquares(Canvas c, int n) {
        if(!squares.isEmpty()) {
            for (NumberedSquare square : squares) {
                hndlr.unregister(square);
            }
            squares.clear();
        }
        while(squares.size() < n) {
            NumberedSquare potential = new NumberedSquare(c.getWidth(), c.getHeight(), getResources(), getContext());
            boolean noIntersect = true;
            for(NumberedSquare square: squares) {
                if(potential.getRectF().intersect(square.getRectF())) {
                    noIntersect = false;
                    NumberedSquare.decrementSeed();
                    break;
                }
            }
            if(noIntersect) {
                squares.add(potential);
                hndlr.register(potential);
            }
        }
    }

    /**
     * Checks for collided squares within the <code>squares</code> ArrayList, and if a collision (intersection) is detected,
     * gives the collided squares to {@link #checkForCollision(NumberedSquare, NumberedSquare)}, to 'un' collide
     */
    public void detectCollision() {

        for(int i = 0; i < squares.size(); i++) {
            for(int j = 0; j < squares.size(); j++) {
                if(squares.get(i).identifier > squares.get(j).identifier) {
                    NumberedSquare iSquare = squares.get(i);
                    NumberedSquare jSquare = squares.get(j);
                    if (RectF.intersects(squares.get(i).getRectF(), squares.get(j).getRectF())) {
                        checkForCollision(iSquare, jSquare);
                    }
                }
            }
        }
    }

    /**
     * Takes in two NumberedSquares which intersect. This method swaps the x or y velocities of the squares
     * x, if the collision is on the left or right
     * y - if the collision is on the top or bottom
     * @param iSquare - one collided <code>NumberedSquare</code>
     * @param jSquare - other collided <code>NumberedSquare</code>
     */
    private void checkForCollision(NumberedSquare iSquare, NumberedSquare jSquare) {

        RectF lSquare = iSquare.getRectF();
        RectF rSquare = jSquare.getRectF();
        boolean iFrozen, jFrozen;
        iFrozen = iSquare.isFrozen();
        jFrozen = jSquare.isFrozen();
        boolean yTrue = false;

        dr = abs(lSquare.right - rSquare.left);
        dl = abs(lSquare.left - rSquare.right);
        db = abs(lSquare.bottom - rSquare.top);
        dt = abs(lSquare.top - rSquare.bottom);

        smallest = min(min(db, dt), min(dr, dl));

        if (smallest == dt) {
            yTrue = true;
            offsetY = -4;
        } else if (smallest == db) {
            yTrue = true;
            offsetY = 4;
        } else if(smallest == dr) {
            offsetX = 4;
        } else if(smallest == dl) {
            offsetX = -4;
        }

        if(!iFrozen && !jFrozen) {

            //Actually exchanging the velocities
            if (yTrue) {
                float tempY = iSquare.velocity.y;
                iSquare.velocity.y = jSquare.velocity.y;
                jSquare.velocity.y = tempY;
            } else {
                float tempX = iSquare.velocity.x;
                iSquare.velocity.x = jSquare.velocity.x;
                jSquare.velocity.x = tempX;
            }
            rSquare.offset(offsetX, offsetY);
        } else {
            frozenCollision(iSquare, jSquare);
        }
    }

    private void frozenCollision(NumberedSquare iSquare, NumberedSquare jSquare) {
        NumberedSquare nonFrozen = null;

        if(iSquare.isFrozen()) {
            nonFrozen = jSquare;
        } else {
            nonFrozen = iSquare;
        }


        if(smallest == db || smallest == dt) {
            nonFrozen.velocity.y *= -1;
        } else {
            nonFrozen.velocity.x *= -1;
        }
        nonFrozen.getRectF().offset(offsetX*-1, offsetY*-1);


    }

    /**
     * Implementation of <code>{@link TickListener#tick()}</code>
     */
    @Override
    public void tick(){
        for(NumberedSquare s:squares) {
            s.move();
            detectCollision();
        }
        invalidate();
    }

    public void pauseSoundtrack() {
        if(soundtrack != null) {
            if (soundtrack.isPlaying()) {
                soundtrack.pause();
            }
        }
    }

    public void resumeSoundtrack() {
        if(soundtrack != null) {
            if (!soundtrack.isPlaying()) {
                soundtrack.start();
            }
        }
    }

    public void destroySoundtrack() {
        if(soundtrack != null) {
            soundtrack.release();
        }
    }
}

