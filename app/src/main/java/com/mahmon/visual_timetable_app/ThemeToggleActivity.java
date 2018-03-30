package com.mahmon.visual_timetable_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mahmon.visual_timetable_app.view.StartActivity;

import static com.mahmon.visual_timetable_app.BaseActivity.PREFS_NAME;
import static com.mahmon.visual_timetable_app.BaseActivity.SELECTED_THEME;

public class ThemeToggleActivity extends AppCompatActivity {

    // Variables used for saving and retrieving preferences
    private Context mContext;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    // Button variables
    private Button mBtnSnow;
    private Button mBtnYellow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_toggle);
        // Assign preference variables and create editor
        mContext = this;
        mPrefs = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
        // Assign buttons to view
        mBtnSnow = findViewById(R.id.btn_snow);
        mBtnYellow = findViewById(R.id.btn_yellow);
        // Assign listeners
        mBtnSnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedTheme("snow");
                setTheme(R.style.AppTheme);
            }
        });
        mBtnYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedTheme("yellow");
                setTheme(R.style.AppTheme_Yellow);
            }
        });
    }

    public void setSelectedTheme(String themeClicked) {
        // Commit clicked value to preferences
        mEditor.putString(SELECTED_THEME, themeClicked);
        mEditor.commit();
        // Instantiate new intent to start DisplayEventsActivity
        Intent intent = new Intent(getBaseContext(), StartActivity.class);
        // Start Activity
        startActivity(intent);
    }

}
