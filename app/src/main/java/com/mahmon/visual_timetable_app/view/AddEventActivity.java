package com.mahmon.visual_timetable_app.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;

public class AddEventActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        // Set bottom menu icons for this context
        getBottomToolbar().getMenu().removeItem(R.id.btn_enter_app);
        getBottomToolbar().getMenu().removeItem(R.id.btn_exit_app);
        getBottomToolbar().getMenu().removeItem(R.id.btn_zoom_out);
        getBottomToolbar().getMenu().removeItem(R.id.btn_zoom_in);
        getBottomToolbar().getMenu().removeItem(R.id.btn_add_event);
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.grow_in, R.anim.grow_out);
    }

}
