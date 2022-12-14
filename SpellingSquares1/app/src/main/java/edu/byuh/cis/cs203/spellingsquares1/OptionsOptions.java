package edu.byuh.cis.cs203.spellingsquares1;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

/**
 * Created by draperg on 10/18/17.
 */

public class OptionsOptions extends PreferenceActivity {
    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        PreferenceScreen ps = getPreferenceManager().createPreferenceScreen(this);
        CheckBoxPreference music = new CheckBoxPreference(this);
        music.setTitle("Toggle background music");
        music.setSummaryOn("Background music will play");
        music.setSummaryOff("Silent mode");
        music.setKey("MUSIC_PREF");
        music.setDefaultValue(true);
        ps.addPreference(music);
        ListPreference speedPref = new ListPreference(this);
        speedPref.setTitle("Cube Speed");
        speedPref.setSummary("How fast should the cubes fly?");
        speedPref.setKey("FLY_SPEED");
        String[] entries = {"Slow", "Medium", "Fast"};
        speedPref.setEntries(entries);
        String[] values = {"1", "2", "3"};
        speedPref.setEntryValues(values);
        speedPref.setDefaultValue("1"); //slow
        ps.addPreference(speedPref);
        setPreferenceScreen(ps);
    }

    /**
     * This is a "façade" method to hide the nastiness of Android's
     * Preferences API.
     * @param c the Context (i.e. Activity) object
     * @return true if music is on; false if music is off.
     */
    public static boolean getMusicOption(Context c) {
        return PreferenceManager.getDefaultSharedPreferences(c)
                .getBoolean("MUSIC_PREF", true);
    }

    /**
     * This is a "façade" method to hide the nastiness of Android's
     * Preferences API.
     * @param c the Context (i.e. Activity) object
     * @return 1=slow, 2=medium, 3=fast
     */
    public static int getCubeSpeed(Context c) {
        String speed = PreferenceManager.getDefaultSharedPreferences(c)
                .getString("FLY_SPEED", "1");
        return Integer.parseInt(speed);
    }

}
