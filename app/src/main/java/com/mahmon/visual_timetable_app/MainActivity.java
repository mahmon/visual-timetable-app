package com.mahmon.visual_timetable_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Implement top_action_bar as default action bar for this activity
        Toolbar topActionBar = findViewById(R.id.top_action_bar);
        setSupportActionBar(topActionBar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Disable the Up button
        actionBar.setDisplayHomeAsUpEnabled(false);
        // Add the bottom action bar and inflate the menu
        Toolbar bottomActionBar = findViewById(R.id.bottom_action_bar);
        bottomActionBar.inflateMenu(R.menu.bottom_enter_bar_menu);
        // Create listeners for bottom_action_bar_menu
        bottomActionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked btn_enter_app
                    case R.id.btn_enter_app:
                        // Create new intent to start a new activity (AddEventActivity)
                        Intent intentAdd = new Intent(MainActivity.this,
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
            // User clicked toggle_theme_button
            case R.id.btn_toggle_theme:
                // Display toast message to confirm click
                Toast toast = Toast
                        .makeText(
                                this,
                                "You Clicked Toggle Theme",
                                Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return true;
            default:
                // Invoke the superclass to handle unrecognised user action.
                return super.onOptionsItemSelected(item);
        }
    }

    // onClick listener for button: btn_enter_app
    public void enterApp(View view) {
        // Create new intent to start a new activity (DisplayEventsActivity)
        Intent intent = new Intent(this, DisplayEventsActivity.class);
        // Start activity
        startActivity(intent);
    }

}