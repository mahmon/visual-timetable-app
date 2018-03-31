package com.mahmon.visual_timetable_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mahmon.visual_timetable_app.view.StartActivity;

import static com.mahmon.visual_timetable_app.BaseActivity.PREFS_NAME;
import static com.mahmon.visual_timetable_app.BaseActivity.SELECTED_THEME;

public class ThemeToggleActivity extends AppCompatActivity {

    // Variables used for saving and retrieving preferences
    private Context mContext;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    // Button variables
    private Button mBtnSnowman;
    private Button mBtnDarkKnight;
    private Button mBtnBumbleBee;
    private Button mBtnLadyBug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_toggle);
        // Assign preference variables and create editor
        mContext = this;
        mPrefs = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
        // Assign buttons to view
        mBtnSnowman = findViewById(R.id.btn_snowman);
        mBtnDarkKnight = findViewById(R.id.btn_dark_knight);
        mBtnBumbleBee = findViewById(R.id.btn_bumble_bee);
        mBtnLadyBug = findViewById(R.id.btn_lady_bug);
        // Assign listeners
        mBtnSnowman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call setSelectedTheme method, pass String theme_snowman
                setSelectedTheme(getResources().getString(R.string.theme_snowman));
            }
        });
        mBtnDarkKnight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call setSelectedTheme method, pass String theme_snowman
                setSelectedTheme(getResources().getString(R.string.theme_dark_knight));
            }
        });
        mBtnBumbleBee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call setSelectedTheme method, pass String theme_snowman
                setSelectedTheme(getResources().getString(R.string.theme_bumble_bee));
            }
        });
        mBtnLadyBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call setSelectedTheme method, pass String theme_snowman
                setSelectedTheme(getResources().getString(R.string.theme_lady_bug));
            }
        });
    }

    public void setSelectedTheme(String themeClicked) {
        // Commit clicked value to preferences
        mEditor.putString(SELECTED_THEME, themeClicked);
        mEditor.commit();
        finish();
    }

}
