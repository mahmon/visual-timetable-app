package com.mahmon.visual_timetable_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

// Class to manage Tool Bars for all activities
public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar topToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        configureTopToolbar(view);
        super.setContentView(view);
    }

    // Configure top t
    private void configureTopToolbar(View view) {
        topToolbar = view.findViewById(R.id.top_tool_bar);
        setSupportActionBar(topToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    // Set method calls for topActionBar
    public boolean topToolBarMethods(MenuItem item) {
        // Switch statement to manage menu user clicks
        switch (item.getItemId()) {
            // User clicked toggle_theme_button
            case R.id.btn_toggle_theme:
                // Display toast message to confirm click
                Toast.makeText(this,"Theme Toggled", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

}
