package edu.byuh.cis.cs203.solidsquares;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by draperg on 8/21/17.
 */

public class GameView extends View {

    private boolean firstRun;
    private float w,h;
    private List<NumberedSquare> squares;

    /**
     * GameView constructor. Just initializes a few variables
     * @param context - required by API
     */
    public GameView(Context context) {
        super(context);
        firstRun = true;
        squares = new ArrayList<>();
    }

    /**
     * All the rendering happens here. The first time onDraw is called, we also do some
     * one-time initialization of the graphics objects (since the width and height of
     * the screen are not known until onDraw is called).
     * @param c
     */
    @Override
    public void onDraw(Canvas c) {
        if (firstRun) {
            w = c.getWidth();
            h = c.getHeight();
            createSquares();
            firstRun = false;
        }
        c.drawColor(Color.CYAN);
        for (NumberedSquare ns : squares) {
            ns.draw(c);
        }
    }

    /**
     * Handle touch events
     * @param me - the MotionEvent object that encapsulates all the data about this
     *           particular touch event. Supplied by OS.
     * @return true, always.
     */
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {
            createSquares();
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
        while (squares.size() < 5) {
            float candidateX = (float)(Math.random() * (w-size));
            float candidateY = (float)(Math.random() * (h-size));
            RectF candidate = new RectF(candidateX, candidateY, candidateX+size, candidateY+size);
            boolean legal = true;
            for (NumberedSquare other : squares) {
                if (other.overlaps(candidate)) {
                    legal = false;
                    break;
                }
            }
            if (legal) {
                squares.add(new NumberedSquare(w, h, candidate));
            }
        }
    }
}
