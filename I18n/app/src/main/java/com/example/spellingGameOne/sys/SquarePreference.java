package com.example.spellingGameOne.sys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.widget.ListPopupWindow;


import com.example.spellingGameOne.R;
import com.example.spellingGameOne.splashScreen.SplashActivity;
import com.example.spellingGameOne.sys.MoveHandler;

import java.util.List;
import java.util.prefs.Preferences;

/**
 * Preference activity class, to add a preference ability to our Numbered Square program!
 * Allows the user to choose whether or not music plays, a sound effect plays, the speed of the squares, and the color of the squares!
 * @Author Jackson
 */
public class SquarePreference extends PreferenceActivity {


    /**
     * Overriding the onCreate, of course. Creates the four Preferences
     * @param b
     */
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        PreferenceScreen ps = getPreferenceManager().createPreferenceScreen(this);

        SwitchPreference music = new SwitchPreference(this);
        music.setTitle("Background Music");
        music.setSummaryOn("Background music will play");
        music.setSummaryOff("Background music will not play");
        music.setDefaultValue(true);
        music.setKey("M_PREF");

        SwitchPreference effect = new SwitchPreference(this);
        effect.setTitle("Ice Sound Effect");
        effect.setSummaryOn("Cubes will make a sound when frozen");
        effect.setSummaryOff("Cubes will be silent when frozen");
        effect.setDefaultValue(true);
        effect.setKey("E_PREF");

        ListPreference speed = new ListPreference(this);
        speed.setTitle("Square Velocity");
        speed.setSummary("How fast should the squares move?");
        speed.setKey("S_PREF");
        String[] entries = {"Fast", "Medium", "Slow"};
        speed.setEntries(entries);
        String[] values = {"50", "200", "500"};
        speed.setEntryValues(values);
        speed.setDefaultValue("200");

        ListPreference squareColor = new ListPreference(this);

        squareColor.setTitle("Square Color");
        squareColor.setSummary("Choose the color of your square");
        squareColor.setKey("C_PREF");
        String[] colorEnt = {"Red", "Green", "Blue"};
        squareColor.setEntries(colorEnt);
        String[] colorVal = {Integer.toString(R.drawable.frozen_red), Integer.toString(R.drawable.frozen_green), Integer.toString(R.drawable.frozen_blue)};
        squareColor.setEntryValues(colorVal);
        squareColor.setDefaultValue(Integer.toString(R.drawable.frozen_green));

        ListPreference gamePreference = new ListPreference(this);
        gamePreference.setTitle("Game Style");
        gamePreference.setSummary("Choose which game you wish to play.");
        gamePreference.setKey("GAME_PREF");
        String[] gameEnt = {"Counting Game", "Spelling Game", "Guess the Word", "Tap the Pair", "Alphabet Game"};
        gamePreference.setEntries(gameEnt);
        String[] gameVal = {"1", "2", "3", "4", "5"};
        gamePreference.setEntryValues(gameVal);
        gamePreference.setDefaultValue("1");

        ps.addPreference(music);
        ps.addPreference(speed);
        ps.addPreference(effect);
        ps.addPreference(squareColor);
        ps.addPreference(gamePreference);
        setPreferenceScreen(ps);
    }

    /**
     * Returns a boolean on whether or not the Music Preference is selected as on or off, true or false etc.
     * @param c - Context
     * @return - Returns a boolean on whether or not the Music Preference is selected as on or off, true or false etc.
     */
    public static boolean soundOn(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("M_PREF", true);

    }

    /**
     * Returns an integer of the speed, meant to be the value of the long parameter of the method: {@link MoveHandler#sendMessageDelayed(Message, long)}
     *
     * @param c
     * @return Returns an integer of the speed, meant to be the value of the long parameter of the method: {@link MoveHandler#sendMessageDelayed(Message, long)}
     */
    public static int getSpeed(Context c) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("S_PREF", "200"));
    }

    /**
     * Returns a boolean on if the user chose effect preference to be true (Enabled)
     * @param c
     * @return Returns a boolean on if the user chose effect preference to be true (Enabled)
     */
    public static boolean effectEnabled(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("E_PREF", true);
    }

    /**
     * Returns the integer value representative of the desired color image in the R.drawable folder
     * @param c
     * @return Returns the integer value representative of the desired color image in the R.drawable folder
     */
    public static int getBackground(Context c) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("C_PREF", String.valueOf(R.drawable.frozen_green)));
    }

    public static int getGameStyle(Context c) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("GAME_PREF", "1"));
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(this, SplashActivity.class);
        startActivity(a);

    }
}
