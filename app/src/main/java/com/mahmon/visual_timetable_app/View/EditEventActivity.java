package com.mahmon.visual_timetable_app.View;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;

public class EditEventActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        // Animation override:
        // Go_in for this activity, go_out for previous activity
        overridePendingTransition(R.anim.go_in, R.anim.go_out);
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

    // Animation override for the default back button:
    // Back_in for this activity, back_out for previous activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

}