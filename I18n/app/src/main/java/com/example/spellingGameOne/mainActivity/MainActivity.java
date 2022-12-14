package com.example.spellingGameOne.mainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.spellingGameOne.sys.NumberedSquare;

public class MainActivity extends AppCompatActivity {

    SquaredView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sv = new SquaredView(this);
        setContentView(sv);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sv.pauseSoundtrack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sv.resumeSoundtrack();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sv.destroySoundtrack();
    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}