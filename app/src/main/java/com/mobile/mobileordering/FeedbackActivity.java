package com.mobile.mobileordering;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mobile.mobileordering.util.PrefsManager;

public class FeedbackActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        loadPreferences();
        loadListAndListeners();
    }

    private void loadPreferences(){
        PrefsManager prefsManager = new PrefsManager(this);
        TextView textView = (TextView) findViewById(R.id.tvCategoryTable);
        textView.setText(String.valueOf(prefsManager.getPreferences().getInt(prefsManager.TABLE, 1)));
    }

    private void loadListAndListeners(){

    }

}
