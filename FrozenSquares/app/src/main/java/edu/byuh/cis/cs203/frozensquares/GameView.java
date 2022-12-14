package edu.byuh.cis.cs203.frozensquares;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by draperg on 8/21/17.
 */

public class GameView extends View implements TickListener {

    private boolean firstRun;
    private float w, h;
    private List<NumberedSquare> squares;
    private Timer timer;

    /**
     * GameView constructor. Just initializes a few variables
     *
     * @param context - required by API
     */
    public GameView(Context context) {
        super(context);
        firstRun = true;
        squares = new ArrayList<>();
        timer = new Timer();
    }

    /**
     * All the rendering happens here. The first time onDraw is called, we also do some
     * one-time initialization of the graphics objects (since the width and height of
     * the screen are not known until onDraw is called).
     *
     * @param c
     */
    @Override
    public void onDraw(Canvas c) {
        if (firstRun) {
            w = c.getWidth();
            h = c.getHeight();
            createSquares();
            timer.subscribe(this);
            firstRun = false;
        }
        c.drawColor(Color.CYAN);
        for (NumberedSquare ns : squares) {
            ns.draw(c);
        }
    }

    /**
     * Handle touch events
     *
     * @param me - the MotionEvent object that encapsulates all the data about this
     *           particular touch event. Supplied by OS.
     * @return true, always.
     */
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            float x = me.getX();
            float y = me.getY();
            for (NumberedSquare ns : squares) {
                if (ns.contains(x, y)) {
                    ns.freeze();
                }
            }
            invalidate();
        }
        return true;
    }

    /**
     * Helper method for creating five NumberedSquare objects
     */
    private void createSquares() {
        squares.clear();
        NumberedSquare.resetCounter();
        NumberedSquare first = new NumberedSquare(w, h);
        float size = first.getSize();
        squares.add(first);
        timer.subscribe(first);
        for (int i=1; i<5; i++) {
            boolean legal = false;
            while (!legal) {
                float candidateX = (float)(Math.random() * (w-size));
                float candidateY = (float)(Math.random() * (h-size));
                RectF candidate = new RectF(candidateX, candidateY, candidateX+size, candidateY+size);
                legal = true;
                for (NumberedSquare other : squares) {
                    if (other.overlaps(candidate)) {
                        legal = false;
                        break;
                    }
                }
                if (legal) {
                    NumberedSquare ns = new NumberedSquare(w, h, candidate);
                    squares.add(ns);
                    timer.subscribe(ns);
                }
            }
        }
    }

    private void checkForCollisions() {
        for (NumberedSquare a : squares) {
            for (NumberedSquare b : squares) {
                a.checkForCollision(b);
            }
        }
    }
    
    @Override
    public void tick() {
        checkForCollisions();
        invalidate();
    }
}
