package com.mahmon.visual_timetable_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class DisplayEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        // Animation override:
        // Go_in for this activity, go_out for previous activity
        overridePendingTransition(R.anim.go_in, R.anim.go_out);
        // Implement top_action_bar as default action bar
        Toolbar topActionBar = findViewById(R.id.top_action_bar);
        setSupportActionBar(topActionBar);
    }

    // Implement the default options menu
    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        // Inflate the top_action_bar_menu onto top_action_bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_action_bar_menu, topMenu);
        return true;
    }

    // Set listeners and actions for items in top_action_bar_menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Switch statement to manage menu user clicks
        switch (item.getItemId()) {
            case R.id.btn_toggle_theme:
                // User clicked toggle_theme_button
                String toastText = "Toggle Theme Clicked";
                // Show confirmation message on click
                Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return true;
            default:
                // Invoke the superclass to handle unrecognised user action.
                return super.onOptionsItemSelected(item);
        }
    }

    // Method called onClick for button: btn_exit_app
    public void exitApp(View view) {
        // Destroy this activity
        // Return to previous activity (MainActivity)
        finish();
        // Animation override:
        // Back_out for this activity, back_in for previous activity
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

    // Method called onClick for button: btn_goto_edit_event
    public void editEvent(View view) {
        // Create new intent to start a new activity (AddEventActivity)
        Intent intent = new Intent(this, EditEventActivity.class);
        // Start activity
        startActivity(intent);
    }

    // Method called onClick for button: btn_goto_add_event
    public void addEvent(View view) {
        // Create new intent to start a new activity (AddEventActivity)
        Intent intent = new Intent(this, AddEventActivity.class);
        // Start activity
        startActivity(intent);
    }
}
