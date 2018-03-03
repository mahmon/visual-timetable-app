package com.mahmon.visual_timetable_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DisplayEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        // Animation override, go_in for this activity, go_out for previous activity
        overridePendingTransition(R.anim.go_in, R.anim.go_out);
    }

    // Method called onClick for button: btn_exit_app
    public void exitApp(View view) {
        // Destroy this activity and return to previous activity (MainActivity)
        finish();
        // Animation override, back_out for this activity, back_in for previous activity
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
