package com.mahmon.visual_timetable_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mahmon.visual_timetable_app.view.AddEventActivity;
import com.mahmon.visual_timetable_app.view.DisplayEventsActivity;
import com.mahmon.visual_timetable_app.view.FinishActivity;
import com.mahmon.visual_timetable_app.view.StartActivity;

// Class to manage Tool Bars for all activities
public class BaseActivity extends AppCompatActivity {

    // Constants used to store local data in shared preferences file
    public static final String PREFS_NAME = "prefs";
    public static final String SELECTED_THEME = "selectedTheme";

    // Constant used for database node
    public static final String VISUAL_EVENTS = "visualEvents";

    // Declare Toolbar objects
    private Toolbar mToolBarTop;
    private Toolbar mToolBarBottom;

    // Getter method for passing bottom toolbar
    public Toolbar getToolBarBottom() {
        return mToolBarBottom;
    }

    @Override
    // Method called by all Activities that extend this BaseActivity
    public void setContentView(int layoutResID) {
        // Instantiate local view object
        View view = getLayoutInflater().inflate(layoutResID, null);
        // Call configureToolBarTop method, passing in local view
        configureToolbarTop(view);
        // Call configureToolBarBottom method, passing in local view
        configureToolbarBottom(view);
        // Call super setContentView method, passing in local view object
        super.setContentView(view);

    }

    // Configure topToolbar
    private void configureToolbarTop(View view) {
        // Link topToolbar to XML tool_bar_top
        mToolBarTop = view.findViewById(R.id.tool_bar_top);
        // Declare topToolbar as the default ToolBar
        setSupportActionBar(mToolBarTop);
        // Disable to default back / home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    // Configure bottomToolbar
    private void configureToolbarBottom(View view) {
        // Link bottomToolbar to XML tool_bar_bottom
        mToolBarBottom = view.findViewById(R.id.tool_bar_bottom);
        // Inflate XML menu_tool_bar_bottom to bottomToolbar
        mToolBarBottom.inflateMenu(R.menu.menu_tool_bar_bottom);
        // Link bottomToolBarMethods and bottomToolbar
        toolBarMethodsBottom(mToolBarBottom);
    }

    // Set toolBarMethodsTop (called by all Activities that extend this BaseActivity)
    public boolean toolBarMethodsTop(MenuItem item) {
        // Switch statement to manage menu user clicks
        switch (item.getItemId()) {
            // User clicked toggle_theme_button
            case R.id.btn_toggle_theme:
                toggleTheme();
                return true;
            default:
                return false;
        }
    }

    // Set method calls for toolBarMethodsBottom
    public void toolBarMethodsBottom(Toolbar bottomToolbar) {
        // Create listeners for all buttons on menu_tool_bar_bottom
        bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked btn_enter_app
                    case R.id.btn_enter_app:
                        enterApp();
                        return true;
                    // User clicked btn_exit_app
                    case R.id.btn_exit_app:
                        exitApp();
                        return true;
                    // User clicked btn_return_login
                    case R.id.btn_return_login:
                        returnLogin();
                        return true;
                    // User clicked btn_zoom_out
                    case R.id.btn_zoom_out:
                        zoomOut();
                        return true;
                    // User clicked btn_zoom_in
                    case R.id.btn_zoom_in:
                        zoomIn();
                        return true;
                    // User clicked btn_add_event
                    case R.id.btn_add_event:
                        addEvent();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    /* ToolBar Methods */
    // Called by btn_toggle_theme
    public void toggleTheme() {
        // Instantiate new intent to start DisplayEventsActivity
        Intent intent = new Intent(getBaseContext(), ThemeToggleActivity.class);
        // Start Activity
        startActivity(intent);
    }

    // Called by btn_enter_app
    public void enterApp() {
        // Instantiate new intent to start DisplayEventsActivity
        Intent intent = new Intent(getBaseContext(), DisplayEventsActivity.class);
        // Start Activity
        startActivity(intent);
    }

    // Called by btn_exit_app
    public void exitApp() {
        // Instantiate new intent to start FinishActivity
        Intent intent = new Intent(getBaseContext(), FinishActivity.class);
        // Start Activity
        startActivity(intent);
    }
    // Called by btn_return_login
    public void returnLogin() {
        // Instantiate new intent to start StartActivity
        Intent intent = new Intent(getBaseContext(), StartActivity.class);
        // Start Activity
        startActivity(intent);
    }

    // Called by btn_zoom_out
    public void zoomOut() {
        // TODO Create Zoom Out method
        // Display toast message to confirm click
        Toast.makeText(getBaseContext(),"Zoom Out", Toast.LENGTH_SHORT).show();
    }

    // Called by btn_zoom_in
    public void zoomIn() {
        // TODO Create Zoom In method
        // Display toast message to confirm click
        Toast.makeText(getBaseContext(),"Zoom In", Toast.LENGTH_SHORT).show();
    }

    // Called by btn_cancel_save
    public void cancelAddUpdateDeleteEvent() {
        // Create new intent to start AddEventActivity
        Intent intent = new Intent(getBaseContext(), DisplayEventsActivity.class);
        // Start Activity
        startActivity(intent);
    }

    // Called by btn_save_event
    public void addEvent() {
        // Create new intent to start AddEventActivity
        Intent intent = new Intent(getBaseContext(), AddEventActivity.class);
        // Start Activity
        startActivity(intent);
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

