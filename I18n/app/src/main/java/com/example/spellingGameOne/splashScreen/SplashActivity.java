package com.example.spellingGameOne.splashScreen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.spellingGameOne.R;
import com.example.spellingGameOne.sys.SquarePreference;
import com.example.spellingGameOne.mainActivity.MainActivity;

/**
 * SplashActivity class which is the first screen the user sees when launching our new numbered square program
 *
 * Allows the user to continue to program with defaults, or access preference screen
 */
public class SplashActivity extends Activity {

    private SplashView iv;
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        iv = new SplashView(this);
        iv.setImageResource(R.drawable.splash);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(iv);

    }

    /**
     * Overriden onTouchEvent, which makes the program switch to other activities based on where the user touches (Buttons on screen)
     * @param e - MotionEvent
     * @return true
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            PointF touched = new PointF(e.getX(), e.getY());
            if(iv.settingsBounds.contains(touched.x, touched.y)) {
                Intent i = new Intent(this, SquarePreference.class);
                startActivity(i);
            } else {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }
}
