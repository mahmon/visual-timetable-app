package com.mahmon.visual_timetable_app.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;
import com.mahmon.visual_timetable_app.ThemeToggleActivity;

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
        // Set the theme to the current selection
        setThemeSelection(mThemeValue);
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

    // Check if Toggle Theme Activity has be launched
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOGGLE_THEME_REQUEST) {
            recreate();
        }
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

    // Set method calls for default option menu (top menu)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Switch statement to manage menu user clicks
        switch (item.getItemId()) {
            // User clicked toggle_theme_button
            case R.id.btn_toggle_theme:
                // Instantiate new intent to start ToggleThemeActivity
                Intent intent = new Intent(this, ThemeToggleActivity.class);
                // Start Activity
                startActivityForResult(intent, TOGGLE_THEME_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // TODO create Login screen and methods
}
