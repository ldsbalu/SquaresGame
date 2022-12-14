package edu.byuh.cis.cs203.sequentialsquares;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by draperg on 8/21/17.
 */

@SuppressLint("AppCompatCustomView")
public class GameView extends ImageView implements TickListener {

    private boolean firstRun;
    private float w, h;
    private List<NumberedSquare> squares;
    private Timer timer;
    private int nextExpectedID;
    private Toast toasty;

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
        nextExpectedID = 1;
        setImageResource(R.drawable.sea_ice_terrain);
        setScaleType(ScaleType.FIT_XY);
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
        super.onDraw(c);
        if (firstRun) {
            w = c.getWidth();
            h = c.getHeight();
            createSquares(nextExpectedID);
            timer.subscribe(this);
            firstRun = false;
        }
        //c.drawColor(Color.CYAN);
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
                    if (ns.getID() == nextExpectedID) {
                        ns.freeze();
                        nextExpectedID++;
                        if (nextExpectedID > squares.size()) {
                            createSquares(nextExpectedID);
                            nextExpectedID = 1;
                        }
                    } else {
                        toasty = Toast.makeText(getContext(), "TRY AGAIN!", Toast.LENGTH_SHORT);
                        toasty.show();
                    }
                }
            }
        }
        return true;
    }

    /**
     * Helper method for creating five NumberedSquare objects
     */
    private void createSquares(int n) {
        squares.clear();
        NumberedSquare.resetCounter();
        NumberedSquare first = new NumberedSquare(this);
        float size = first.getSize();
        squares.add(first);
        timer.subscribe(first);
        for (int i=1; i<n; i++) {
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
                    NumberedSquare ns = new NumberedSquare(this, candidate);
                    squares.add(ns);
                    timer.subscribe(ns);
                }
            }
        }
        toasty = Toast.makeText(getContext(), "Level " + n, Toast.LENGTH_SHORT);
        toasty.show();
    }

    @Override
    public void tick() {
        for (NumberedSquare ns : squares) {
            ns.checkForCollisions(squares);
        }
        invalidate();
    }
}
