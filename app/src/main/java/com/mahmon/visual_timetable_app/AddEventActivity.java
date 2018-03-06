package com.mahmon.visual_timetable_app;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class AddEventActivity extends AppCompatActivity {

    // New branch - dev-firebase-CRUD

    // Declare a constant to store a value for event heading
    public static final String EVENT_HEADING = "com.mahmon.visual_timetable.EVENT_HEADING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
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
                // Destroy activity calling method, return to previous activity
                finish();
                // Animation override:
                // Back_out for this activity, back_in for previous activity
                overridePendingTransition(R.anim.back_in, R.anim.back_out);
                return true;
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

    // Animation override for the default back button:
    // Back_in for this activity, back_out for previous activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

    // onClick listener for button: btn_save_added_event
    public void saveAddedEvent(View view) {
        // Create instance and reference to database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // Set reference to two child levels
        DatabaseReference mDatabaseReference =
                mDatabase.getReference("Visual Events").child("Event Heading");
        // Declare intent to link to DisplayEventsActivity
        Intent intent = new Intent(this, DisplayEventsActivity.class);
        // Attach variable to txt_add_events
        EditText editText = findViewById(R.id.txt_add_events);
        // Store input from editText test in String eventHeading
        String eventHeading = editText.getText().toString();
        // Write string to the database
        mDatabaseReference.setValue(eventHeading);
        // Goto next activity
        startActivity(intent);
    }

}
