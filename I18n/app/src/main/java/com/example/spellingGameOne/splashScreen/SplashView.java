package com.example.spellingGameOne.splashScreen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;

import com.example.spellingGameOne.R;
import com.example.spellingGameOne.splashScreen.SplashActivity;

/**
 * SplashView subclass of ImageView, to serve as the view the {@link SplashActivity} class
 */
public class SplashView extends androidx.appcompat.widget.AppCompatImageView {

    private Bitmap settings;
    private Bitmap start;
    public RectF settingsBounds, startBounds;
    private float buttonSize;

    /**
     * Constructor, initializes the settings and start fields to the various Bitmaps, using BitmapFactory
     * @param c
     */
    public SplashView(Context c) {
        super(c);
        settings = BitmapFactory.decodeResource(getResources(), R.drawable.settings);
        start = BitmapFactory.decodeResource(getResources(), R.drawable.start);
    }

    /**
     * Overriden onDraw() Method, which adds the preferences button and the start button to proceed to the various activities, either preferences or the game.
     * @param c
     */
    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        buttonSize = c.getWidth()*0.20f;
        float left = c.getWidth()-310;
        float centerWidth = c.getWidth()/2;
        float centeredButton = centerWidth - buttonSize/2;

        settingsBounds = new RectF(left, 10, left+300, 310);
        settings = Bitmap.createScaledBitmap(settings, (int)buttonSize, (int)buttonSize, false);

        startBounds = new RectF(centeredButton, 10, centeredButton+buttonSize, 10+buttonSize);
        start = Bitmap.createScaledBitmap(start, (int)buttonSize,(int)buttonSize, false);

        c.drawBitmap(settings, left, 10, null);
        c.drawBitmap(start, centeredButton, 10, null);


    }


}
