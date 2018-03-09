package com.mahmon.visual_timetable_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

    // Used to set database node
    public static final String VISUAL_EVENTS = "Visual Events";

    // Database link and reference object
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseRef;

    // Toolbars
    private Toolbar topToolbar;
    private Toolbar bottomToolbar;

    // Getter method for passing bottom toolbar
    public Toolbar getBottomToolbar() {
        return bottomToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get Visual Events reference
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference(VISUAL_EVENTS);
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
                        // Create new intent to start DisplayEventsActivity
                        Intent intentEnter = new Intent(getBaseContext(), DisplayEventsActivity.class);
                        startActivity(intentEnter);
                        return true;
                    // User clicked btn_exit_app
                    case R.id.btn_exit_app:
                        // Create new intent to start StartActivity
                        Intent intentExit = new Intent(getBaseContext(), StartActivity.class);
                        startActivity(intentExit);
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
                        // Create new intent to start AddEventActivity
                        Intent intentAdd = new Intent(getBaseContext(), AddEventActivity.class);
                        startActivity(intentAdd);
                        return true;
                    // User clicked btn_save_event
                    case R.id.btn_save_event:
                        // Call add event method
                        addEvent();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    /* Write to database */
    // Add event to database
    private void addEvent() {
        // Link to txt_add_events
        EditText editText = findViewById(R.id.txt_add_events);
        // Store input from editText test in String eventHeading
        String eventHeading = editText.getText().toString().trim();
        // Check box is not blank
        if (!TextUtils.isEmpty(eventHeading)) {
            // Get unique ID from Firebase
            String eventID = mDatabaseRef.push().getKey();
            // Create Event Object
            Event event = new Event(eventID, eventHeading);
            // Save Event
            mDatabaseRef.child(eventID).setValue(event);
            // Toast to confirm event saved
            Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
            // Create new intent to start a new activity (DisplayEventsActivity)
            Intent intentSave = new Intent(getBaseContext(), DisplayEventsActivity.class);
            // Start activity
            startActivity(intentSave);
        } else {
            // Prompt
            Toast.makeText(this, "Please enter a heading", Toast.LENGTH_LONG).show();
        }
    }

}
