package com.mahmon.visual_timetable_app;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class DisplayEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        // Animation override:
        // Go_in for this activity, go_out for previous activity
        overridePendingTransition(R.anim.go_in, R.anim.go_out);
        // Implement top_action_bar as default action bar for this activity
        Toolbar topActionBar = findViewById(R.id.top_action_bar);
        setSupportActionBar(topActionBar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the Up button
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Add the bottom action bar and inflate the menu
        Toolbar bottomActionBar = findViewById(R.id.bottom_action_bar);
        bottomActionBar.inflateMenu(R.menu.bottom_action_bar_menu);
        // Create listeners for bottom_action_bar_menu
        bottomActionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked btn_zoom_out
                    case R.id.btn_zoom_out:
                        // Call zoomOut method from AppMethods
                        AppMethods.zoomOut(getBaseContext());
                        return true;
                        // User clicked btn_zoom_in
                    case R.id.btn_zoom_in:
                        // Call zoomIn method from AppMethods
                        AppMethods.zoomIn(getBaseContext());
                        return true;
                    // User clicked btn_goto_add_event
                    case R.id.btn_goto_add_event:
                        // Call addEvent method from AppMethods
                        AppMethods.addEvent(getBaseContext());
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    // Implement the default options menu
    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        // Inflate the top_action_bar_menu onto top_action_bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_action_bar_menu, topMenu);
        return true;
    }

    // Set method calls for items clicked in top_action_bar_menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Switch statement to manage menu user clicks
        switch (item.getItemId()) {
            // User clicked home button
            case android.R.id.home:
                // Call homeClicked method from AppMethods
                AppMethods.homeClicked(this);
                overridePendingTransition(R.anim.back_in, R.anim.back_out);
                return true;
            // User clicked toggle_theme_button
            case R.id.btn_toggle_theme:
                // Call toggleTheme method from AppMethods
                AppMethods.toggleTheme(getBaseContext());
                return true;
            default:
                // Invoke the superclass to handle unrecognised user action.
                return super.onOptionsItemSelected(item);
        }
    }

    // Animation override for the default back button:
    // Back_in for this activity, back_out for previous activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

    // onClick listener for button: btn_exit_app
    public void exitApp(View view) {
        // Call exitApp method from AppMethods
        AppMethods.exitApp(this);
        // Animation override:
        // Back_out for this activity, back_in for previous activity
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

    // onClick listener for button: btn_goto_edit_event
    public void editEvent(View view) {
        // Call editEvent method from AppMethods
        AppMethods.editEvent(getBaseContext());
    }

}
