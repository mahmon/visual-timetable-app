package com.mahmon.visual_timetable_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.mahmon.visual_timetable_app.BaseActivity.PREFS_NAME;
import static com.mahmon.visual_timetable_app.BaseActivity.SELECTED_THEME;

public class ThemeToggleActivity extends AppCompatActivity {

    // Variables used for saving and retrieving preferences
    private Context mContext;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;
    private String mThemeValue;
    // Button variables
    private Button mBtnSnowman;
    private Button mBtnDarkKnight;
    private Button mBtnBumbleBee;
    private Button mBtnLadyBug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Assign preference variables
        mContext = this;
        mPrefs = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // Get value from mPrefs and assign to mThemeVale
        mThemeValue = mPrefs.getString(SELECTED_THEME, "");
        // Set the theme to the current selection
        setThemeSelection(mThemeValue);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_toggle);
        // Create editor for saved preferences
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

    // Set Theme Method
    public void setThemeSelection(String mThemeValue) {
        // Check value of selected theme and set accordingly
        if (mThemeValue.equals(getString(R.string.theme_snowman))) {
            // Set theme to selection
            setTheme(R.style.AppThemeSnowman);
        } else if (mThemeValue.equals(getString(R.string.theme_dark_knight))) {
            // Set theme to selection
            setTheme(R.style.AppThemeDarkKnight);
        } else if (mThemeValue.equals(getString(R.string.theme_bumble_bee))) {
            // Set theme to selection
            setTheme(R.style.AppThemeBumbleBee);
        } else if (mThemeValue.equals(getString(R.string.theme_lady_bug))) {
            // Set theme to selection
            setTheme(R.style.AppThemeLadyBug);
        }
    }

}