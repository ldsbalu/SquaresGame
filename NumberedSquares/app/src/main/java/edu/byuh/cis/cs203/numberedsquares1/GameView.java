package edu.byuh.cis.cs203.numberedsquares1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by draperg on 8/21/17.
 */

public class GameView extends View {

    private boolean init;
    private float w,h;
    private List<NumberedSquare> squares;

    /**
     * GameView constructor. Just initializes a few variables
     * @param context - required by API
     */
    public GameView(Context context) {
        super(context);
        init = false;
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
        if (!init) {
            w = c.getWidth();
            h = c.getHeight();
            createSquares();
            init = true;
        }
        c.drawColor(Color.CYAN);
        for (NumberedSquare ns : squares) {
            ns.draw(c);
        }
    }

    /**
     * Helper method for creating five NumberedSquare objects
     */
    private void createSquares() {
        squares.clear();
        NumberedSquare.resetCounter();
        for (int i=0; i<5; i++) {
            NumberedSquare ns = new NumberedSquare(w,h);
            squares.add(ns);
        }

    }
}
