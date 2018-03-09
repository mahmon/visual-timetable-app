package com.mahmon.visual_timetable_app.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;

public class VisualTimetable extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_timetable);
        // Setup action bars
        showBottomActionBar();
    }

    // Implement the default options menu
    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        // Inflate the top_tool_bar_menu onto top_tool_bar
        MenuInflater inflater = getMenuInflater();
        // Set top_tool_bar_menu as default options menu
        inflater.inflate(R.menu.top_tool_bar_menu, topMenu);
        return true;
    }

    // Set method calls for items clicked in top_tool_bar_menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Set method calls for items clicked in top_tool_bar_menu
        topToolBarMethods(item);
        // Invoke the superclass to handle unrecognised user action.
        return super.onOptionsItemSelected(item);
    }

    /* Action Bars Set Up*/
    // Initiate bottomActionBar
    public void showBottomActionBar() {
        // Add the bottom action bar and inflate the menu
        Toolbar bottomActionBar = findViewById(R.id.bottom_action_bar);
        bottomActionBar.inflateMenu(R.menu.bottom_enter_bar_menu);
        bottomActionBarMethods(bottomActionBar);
    }
    /* Action Bars Methods*/
    // Set method calls for bottomActionBar
    public void bottomActionBarMethods(Toolbar bottomActionBar) {
        // Create listeners for bottom_action_bar_menu
        bottomActionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked btn_enter_app
                    case R.id.btn_enter_app:
                        // Create new intent to start a new activity (AddEventActivity)
                        Intent intentAdd = new Intent(VisualTimetable.this,
                                DisplayEventsActivity.class);
                        // Start activity
                        startActivity(intentAdd);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

}