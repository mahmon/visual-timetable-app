package com.mahmon.visual_timetable_app.View;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_timetable);
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

}