package com.mahmon.visual_timetable_app.view;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;

// Class to manage StartActivity
public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link this activity to the relevant XML layout
        setContentView(R.layout.activity_start_activity);
        // Set bottom menu icons for this context (remove unwanted)
        getBottomToolbar().getMenu().removeItem(R.id.btn_exit_app);
        getBottomToolbar().getMenu().removeItem(R.id.btn_zoom_out);
        getBottomToolbar().getMenu().removeItem(R.id.btn_zoom_in);
        getBottomToolbar().getMenu().removeItem(R.id.btn_add_event);
        getBottomToolbar().getMenu().removeItem(R.id.btn_save_event);
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    // Implement the default options menu
    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        // Instantiate menu inflater object
        MenuInflater inflater = getMenuInflater();
        // Set top_tool_bar_menu as default options menu
        inflater.inflate(R.menu.top_tool_bar_menu, topMenu);
        return true;
    }

    // Set method calls for default option menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Attach topToolBarMethods to default menu
        topToolBarMethods(item);
        // Invoke the superclass to handle unrecognised user action.
        return super.onOptionsItemSelected(item);
    }

}