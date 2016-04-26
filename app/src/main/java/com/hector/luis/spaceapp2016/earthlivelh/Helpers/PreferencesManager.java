package com.hector.luis.spaceapp2016.earthlivelh.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hector Arredondo on 17/04/2016.
 */
public class PreferencesManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private final int PRIVATE_MODE = 0;
    private final String PREF_NAME = "EarthLivePref";
    private final String KEY_DB_LOAD = "dataBaseLoad";

    public PreferencesManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setDBload(boolean prValue) {
        editor.putBoolean(KEY_DB_LOAD, prValue);
        editor.commit();
    }

    public boolean idDBload() {
        return pref.getBoolean(KEY_DB_LOAD, false);
    }

}
