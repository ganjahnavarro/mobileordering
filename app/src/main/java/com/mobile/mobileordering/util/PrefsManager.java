package com.mobile.mobileordering.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by simplyph on 1/17/2016.
 */
public class PrefsManager {

    public String TABLE = "table";

    public SharedPreferences getPreferences() {
        return preferences;
    }

    private SharedPreferences preferences;

    public PrefsManager(Context c){
        preferences = c.getSharedPreferences("mobileordering", Context.MODE_PRIVATE);
    }


}
