package edu.byuh.cis.cs203.spellingsquares1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;
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
    //private int nextExpectedID;
    private Toast toasty;
    private GameStyle gs;

    /**
     * GameView constructor. Just initializes a few variables
     *
     * @param context - required by API
     */
    public GameView(Context context) {
        super(context);
        firstRun = true;
        squares = new ArrayList<>();
        gs = new CountingGame();
        //gs = new SpellingGame();
        timer = new Timer();
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
            createSquares();
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
                if (ns.contains(x, y) && !ns.isFrozen()) {
                    GameStyle.TouchStatus status = gs.getTouchStatus(ns);
                    if (status == GameStyle.TouchStatus.CONTINUE) {
                        ns.freeze();
                    } else if (status == GameStyle.TouchStatus.TRY_AGAIN) {
                        toasty = Toast.makeText(getContext(), gs.getTryAgainLabel(), Toast.LENGTH_LONG);
                        toasty.show();
                    } else {
                        //LEVEL_COMPLETE
                        createSquares();
                        break;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Helper method to unsubscribe all squares from timer,
     * then delete the squares.
     */
    private void deleteAllSquares() {
        for (NumberedSquare ns : squares) {
            timer.unsubscribe(ns);
        }
        squares.clear();
        NumberedSquare.resetCounter();
    }

    /**
     * Helper method for creating the NumberedSquare objects
     */
    private void createSquares() {
        deleteAllSquares();
        List<String> labels = gs.getSquareLabels();
        NumberedSquare first = new NumberedSquare(this, labels.get(0));
        float size = first.getSize();
        squares.add(first);
        timer.subscribe(first);
        for (int i=1; i<labels.size(); i++) {
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
                    NumberedSquare ns = new NumberedSquare(this, candidate, labels.get(i));
                    squares.add(ns);
                    timer.subscribe(ns);
                }
            }
        }
        toasty = Toast.makeText(getContext(), gs.getNextLevelLabel(), Toast.LENGTH_LONG);
        toasty.show();
    }

    @Override
    public void tick() {
        checkForCollisions();
        invalidate();
    }

    private void checkForCollisions() {
        for (NumberedSquare a : squares) {
            for (NumberedSquare b : squares) {
                a.checkForCollision(b);
            }
        }
    }

}
