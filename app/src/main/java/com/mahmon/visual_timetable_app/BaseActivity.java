package com.mahmon.visual_timetable_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmon.visual_timetable_app.model.Event;
import com.mahmon.visual_timetable_app.view.*;

// Class to manage Tool Bars for all activities
public class BaseActivity extends AppCompatActivity {

    private Toolbar topToolbar;
    private Toolbar bottomToolbar;

    // Getter method for passing toolbar
    public Toolbar getBottomToolbar() {
        return bottomToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        configureTopToolbar(view);
        configureBottomToolbar(view);
        super.setContentView(view);
    }

    // Configure top tool bar
    private void configureTopToolbar(View view) {
        topToolbar = view.findViewById(R.id.top_tool_bar);
        setSupportActionBar(topToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    // Configure bottom tool bar and attach methods
    private void configureBottomToolbar(View view) {
        bottomToolbar = view.findViewById(R.id.bottom_tool_bar);
        bottomToolbar.inflateMenu(R.menu.bottom_tool_bar_menu);
        bottomToolBarMethods(bottomToolbar);
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

    // Set method calls for bottomActionBar
    public void bottomToolBarMethods(Toolbar bottomToolbar) {
        // Create listeners for bottom_display_bar_menu
        bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked btn_enter_app
                    case R.id.btn_enter_app:
                        // Create new intent to start a new activity (DisplayEventsActivity)
                        Intent intentEnter = new Intent(getBaseContext(), DisplayEventsActivity.class);
                        // Start activity
                        startActivity(intentEnter);
                        return true;
                    // User clicked btn_exit_app
                    case R.id.btn_exit_app:
                        Intent openMainActivity = new Intent(getBaseContext(), StartActivity.class);
                        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivityIfNeeded(openMainActivity, 0);
                        return true;
                    // User clicked btn_zoom_out
                    case R.id.btn_zoom_out:
                        // Display toast message to confirm click
                        Toast.makeText(getBaseContext(), "Zoom Out", Toast.LENGTH_SHORT).show();
                        return true;
                    // User clicked btn_zoom_in
                    case R.id.btn_zoom_in:
                        // Display toast message to confirm click
                        Toast.makeText(getBaseContext(), "Zoom In", Toast.LENGTH_SHORT).show();
                        return true;
                    // User clicked btn_add_event
                    case R.id.btn_add_event:
                        // Create new intent to start a new activity (AddEventActivity)
                        Intent intentAdd = new Intent(getBaseContext(), AddEventActivity.class);
                        // Start activity
                        startActivity(intentAdd);
                        return true;
                    // User clicked btn_save_event
                    case R.id.btn_save_event:
                        /* Write to database */
                        // Create instance and reference to database
                        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                        // Set reference to two child levels
                        DatabaseReference mDatabaseReference =
                                mDatabase.getReference("Visual Events");
                        // Get ID value and store in eventID
                        String eventID = mDatabaseReference.push().getKey();
                        // Attach variable to txt_add_events
                        EditText editText = findViewById(R.id.txt_add_events);
                        // Store input from editText test in String eventHeading
                        String eventHeading = editText.getText().toString();
                        // Create new Event object and pass in value of eventHeading
                        Event event = new Event(eventHeading);
                        // Write Event object to the database
                        mDatabaseReference.child(eventID).setValue(event);
                        // Create new intent to start a new activity (DisplayEventsActivity)
                        Intent intentSave = new Intent(getBaseContext(), DisplayEventsActivity.class);
                        // Start activity
                        startActivity(intentSave);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

}
