package com.mahmon.visual_timetable_app.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;

// Class for Login screen
public class StartActivity extends BaseActivity {

    // Variables used for saving and retrieving theme preferences
    private Context mContext;
    private SharedPreferences mPrefs;
    private String mThemeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Assign preference variables
        mContext = this;
        mPrefs = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // Get value from mPrefs and assign to mThemeVale
        mThemeValue = mPrefs.getString(SELECTED_THEME, "");
        // Check value of selected theme and set accordingly
        if (mThemeValue.equals(getString(R.string.theme_snowman))) {
            // Set theme to selection
            setTheme(R.style.AppTheme);
        } else if (mThemeValue.equals(getString(R.string.theme_dark_knight))) {
            // Set theme to selection
            setTheme(R.style.AppTheme_Yellow);
        } else if (mThemeValue.equals(getString(R.string.theme_bumble_bee))) {
            // Set theme to selection
            setTheme(R.style.AppTheme_Yellow);
        } else if (mThemeValue.equals(getString(R.string.theme_lady_bug))) {
            // Set theme to selection
            setTheme(R.style.AppTheme_Yellow);
        }
        super.onCreate(savedInstanceState);
        // Link this activity to the relevant XML layout
        setContentView(R.layout.activity_start);
        // Set bottom menu icons for this context (remove unwanted)
        getToolBarBottom().getMenu().removeItem(R.id.btn_exit_app);
        getToolBarBottom().getMenu().removeItem(R.id.btn_return_login);
        getToolBarBottom().getMenu().removeItem(R.id.btn_zoom_out);
        getToolBarBottom().getMenu().removeItem(R.id.btn_zoom_in);
        getToolBarBottom().getMenu().removeItem(R.id.btn_add_event);
        getToolBarBottom().getMenu().removeItem(R.id.btn_cancel_save);
        getToolBarBottom().getMenu().removeItem(R.id.btn_save_event);
        getToolBarBottom().getMenu().removeItem(R.id.btn_delete_event);
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.shrink_out);
    }

    // Implement the default options menu
    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        // Instantiate menu inflater object
        MenuInflater inflater = getMenuInflater();
        // Set menu_tool_bar_top as default options menu
        inflater.inflate(R.menu.menu_tool_bar_top, topMenu);
        return true;
    }

    // Set method calls for default option menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Attach topToolBarMethods to default menu
        toolBarMethodsTop(item);
        // Invoke the superclass to handle unrecognised user action.
        return super.onOptionsItemSelected(item);
    }

    // TODO create Login screen and methods
}
