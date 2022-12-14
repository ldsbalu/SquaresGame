package com.example.spellingGameOne.mainActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.abs;
import static java.lang.Math.min;
import com.example.spellingGameOne.gameStyles.AlphabeticalGame;
import com.example.spellingGameOne.gameStyles.TapThePairGame;
import com.example.spellingGameOne.gameStyles.CountingGame;
import com.example.spellingGameOne.gameStyles.GameStyle;
import com.example.spellingGameOne.gameStyles.HangmanGame;
import com.example.spellingGameOne.gameStyles.SpellingGame;
import com.example.spellingGameOne.sys.MoveHandler;
import com.example.spellingGameOne.sys.NumberedSquare;
import com.example.spellingGameOne.R;
import com.example.spellingGameOne.sys.Score;
import com.example.spellingGameOne.sys.SquarePreference;
import com.example.spellingGameOne.sys.TickListener;
import com.example.spellingGameOne.sys.TouchStatus;

/**
 * @author Jackson Pike
 * A subclass view for NumberedSquare project.
 */
public class SquaredView extends View implements TickListener {
    //Primitives
    private boolean levelInitialized = false;
    private float db;
    private float dt;
    private float smallest;
    private int offsetX, offsetY;

    //Non-Display Objects
    private MediaPlayer soundtrack;
    private final MediaPlayer iceEffect;
    private GameStyle currentGame;
    private final MoveHandler movingSquaresThread;
    private List<NumberedSquare> squares;
    private Score userScore;

    //Display Related
    private Bitmap backgroundBitmap;
    private Toast levelAnnouncerToast; // levelAnnouncerToast is initialized in onDraw levelInitialization
    private Canvas canvas;
    private Context context;
    private Resources resources;
    private Paint scoreTextColor;


    /**
     * Constructor. Only makes call to <code>super()</code> No additional code.
     * @param context - Context
     */
    public SquaredView(Context context) {
        //Setting primitives (and calling super)
        super(context);
        this.context = context;
        offsetY = 0;
        offsetX = 0;

        //Setting Non-Display Fields
        if(SquarePreference.soundOn(context)) {
            soundtrack = MediaPlayer.create(context, R.raw.soundtrack);
            soundtrack.setLooping(true);
            soundtrack.start();
        }
        iceEffect = MediaPlayer.create(context, R.raw.icecrack);
        iceEffect.setLooping(false);

        newGame();
        userScore = new Score(currentGame.numberOfSquares(), currentGame.toString(), getContext());

        movingSquaresThread = MoveHandler.getInstance(SquarePreference.getSpeed(context));
        squares = new ArrayList<>();

        //Setting Display Fields
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper);
        this.context = context;
        this.resources = getResources();
        scoreTextColor = new Paint();
        scoreTextColor.setColor(Color.WHITE);
        scoreTextColor.setTextSize(70);

    }

    private void newGame() {
        int preferredGame = SquarePreference.getGameStyle(context);
        switch(preferredGame) {
            case 1:currentGame = new CountingGame();break;
            case 2:currentGame = new SpellingGame();break;
            case 3:currentGame = new HangmanGame();break;
            case 4:currentGame = new TapThePairGame();break;
            case 5:currentGame = new AlphabeticalGame();break;
        }
    }


    /**
     * Overriden onDraw Method. Draws a color on the canvas for background, and then creates and invokes the draw method of 5 numberedsquares. s
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
        this.canvas = canvas;

        if(!levelInitialized) {  //Start of Level
            backgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, this.canvas.getWidth(), this.canvas.getHeight(), true);
            createSquares(canvas);
            movingSquaresThread.register(this);
            levelInitialized = true;
            levelAnnouncerToast = Toast.makeText(getContext(), currentGame.getNextLevelLabel(getResources()), Toast.LENGTH_LONG);
            levelAnnouncerToast.show();

        }

        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        canvas.drawText(getContext().getString(R.string.currentScore)+userScore.getScore(), 10, canvas.getHeight() *.03f, scoreTextColor);
        canvas.drawText(getContext().getString(R.string.highScore)+userScore.readHighScore(), 10, canvas.getHeight() * .03f+60, scoreTextColor);
        for (NumberedSquare square : squares) {
            square.draw(this.canvas);
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
            checkSquareInSequence(new PointF(e.getX(), e.getY()));
        }
        return true;
    }

    public void checkSquareInSequence(PointF touchPoint) {
        TouchStatus currentStatus = TouchStatus.TRY_AGAIN;
        NumberedSquare tappedSquare = null;
        for (NumberedSquare square : squares) {
            if (square.getRectF().contains(touchPoint.x, touchPoint.y)) {
                currentStatus = currentGame.getTouchStatus(square);
                tappedSquare = square;
            }
        }
        switch(currentStatus) {
            case CONTINUE:
                if(tappedSquare != null) {
                    tappedSquare.freeze();
                    if (SquarePreference.effectEnabled(getContext()) && soundtrack != null) {
                        soundtrack.pause();
                        iceEffect.start();
                        iceEffect.setOnCompletionListener(mediaPlayer -> soundtrack.start());
                    }
                }
                break;
            case TRY_AGAIN:
                if(tappedSquare != null) {
                    Toast tryAgainToast = Toast.makeText(getContext(), currentGame.getTryAgainLabel(getResources()), Toast.LENGTH_SHORT);
                    tryAgainToast.show();
                    userScore.decrement();
                }
                break;
            case LEVEL_COMPLETE:
                levelInitialized = false;
                invalidate();
                break;
            case GAME_OVER:
                tappedSquare.freeze();
                promptContinue();
                break;
        }
    }

    private void promptContinue() {
        boolean highScore = false;
        String message = "";
        int currScore = userScore.getScore();
        int hScore = userScore.readHighScore();
        if(currScore > hScore) {
            highScore = true;
        }
        Activity activity = (Activity)getContext();
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        ab.setTitle(R.string.alertDialogTitle);
        if(highScore) {
            message += String.format("%s%n", getContext().getString(R.string.highScoreMessage));
        }
        message += String.format(getContext().getString(R.string.playAgainPrompt));
        ab.setMessage(message);
        ab.setPositiveButton(R.string.restartButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newGame();
                levelInitialized = false;
                userScore = new Score(currentGame.numberOfSquares(), currentGame.toString(), getContext());
                invalidate();
            }
        });
        ab.setNegativeButton(R.string.endGameButton, (dialog, which) -> {
            activity.finish();
        });
        AlertDialog dialog = ab.create();
        dialog.show();
        userScore.endGame();
    }

    /**
     * A method to create <code>n</code> number of squares on a given canvas.
     */
    private void createSquares(Canvas canvas) {
        resetSquaresArray();
        List<String> labels = currentGame.getSquareLabels();
        for (int i = 0; i < labels.size(); i++) {
            String potentialLabel = labels.get(i);
            NumberedSquare potential = new NumberedSquare(canvas, getResources(), getContext(), potentialLabel);
            boolean noIntersect = true;
            for (NumberedSquare square : squares) {
                if (potential.getRectF().intersect(square.getRectF())) {
                    noIntersect = false;
                    i--;
                    break;
                }
            }
            if (noIntersect) {
                squares.add(potential);

            }
        }
    }

    private void resetSquaresArray() {

        if(!squares.isEmpty()) {
            squares.clear();
        }
    }

    /**
     * Checks for collided squares within the <code>squares</code> ArrayList, and if a collision (intersection) is detected,
     * gives the collided squares to {@link #collisionHandling(NumberedSquare, NumberedSquare)}, to 'un' collide
     */
    public void checkForCollisions() {
        for(int i = 0; i < squares.size(); i++) {
            for(int j = 0; j < squares.size(); j++) {
                if(i > j) {
                    NumberedSquare iSquare = squares.get(i);
                    NumberedSquare jSquare = squares.get(j);
                    if (RectF.intersects(squares.get(i).getRectF(), squares.get(j).getRectF())) {
                        collisionHandling(iSquare, jSquare);
                    }
                }
            }
        }
    }

    /**
     * Takes in two NumberedSquares which intersect. This method swaps the x or y velocities of the squares
     * x, if the collision is on the left or right
     * y - if the collision is on the top or bottom
     * @param leftNumberedSquare - one collided <code>NumberedSquare</code>
     * @param rightNumberedSquare - other collided <code>NumberedSquare</code>
     */
    private void collisionHandling(NumberedSquare leftNumberedSquare, NumberedSquare rightNumberedSquare) {

        RectF lRectF = leftNumberedSquare.getRectF();
        RectF rRectF = rightNumberedSquare.getRectF();
        boolean leftFrozen, rightFrozen;
        leftFrozen = leftNumberedSquare.isFrozen();
        rightFrozen = rightNumberedSquare.isFrozen();
        boolean collisionIsYAxis = false;

        float dr = abs(lRectF.right - rRectF.left);
        float dl = abs(lRectF.left - rRectF.right);
        db = abs(lRectF.bottom - rRectF.top);
        dt = abs(lRectF.top - rRectF.bottom);

        smallest = min(min(db, dt), min(dr, dl));

        if (smallest == dt) {
            collisionIsYAxis = true;
            offsetY = -4;
        } else if (smallest == db) {
            collisionIsYAxis = true;
            offsetY = 4;
        } else if(smallest == dr) {
            offsetX = 4;
        } else if(smallest == dl) {
            offsetX = -4;
        }

        if(!leftFrozen && !rightFrozen) {

            //Actually exchanging the velocities
            if (collisionIsYAxis) {
                float tempY = leftNumberedSquare.velocity.y;
                leftNumberedSquare.velocity.y = rightNumberedSquare.velocity.y;
                rightNumberedSquare.velocity.y = tempY;
            } else {
                float tempX = leftNumberedSquare.velocity.x;
                leftNumberedSquare.velocity.x = rightNumberedSquare.velocity.x;
                rightNumberedSquare.velocity.x = tempX;
            }
            rRectF.offset(offsetX, offsetY);
        } else {
            frozenCollision(leftNumberedSquare, rightNumberedSquare);
        }
    }

    private void frozenCollision(NumberedSquare leftSquare, NumberedSquare rightSquare) {
        NumberedSquare nonFrozen = null;

        if(leftSquare.isFrozen()) {
            nonFrozen = rightSquare;
        } else {
            nonFrozen = leftSquare;
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
            checkForCollisions();
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

