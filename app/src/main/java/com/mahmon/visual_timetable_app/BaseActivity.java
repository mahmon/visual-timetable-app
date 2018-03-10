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

    // Used to set global database node
    public static final String VISUAL_EVENTS = "Visual Events";

    // Declare database object and reference object
    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseRef;

    // Declare Toolbar objects
    private Toolbar topToolbar;
    private Toolbar bottomToolbar;

    // Getter method for passing bottom toolbar
    public Toolbar getBottomToolbar() {
        return bottomToolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiate database object linked to Firebase
        mDatabase = FirebaseDatabase.getInstance();
        // Instantiate database reference linked to VISUAL_EVENTS node
        mDatabaseRef = mDatabase.getReference(VISUAL_EVENTS);
    }

    @Override
    // Method called by all Activities that extend this BaseActivity
    public void setContentView(int layoutResID) {
        // Instantiate local view object
        View view = getLayoutInflater().inflate(layoutResID, null);
        // Call configureTopToolbar method, passing in local view
        configureTopToolbar(view);
        // Call configureBottomToolbar method, passing in local view
        configureBottomToolbar(view);
        // Call super setContentView method, passing in local view object
        super.setContentView(view);
    }

    // Configure topToolbar
    private void configureTopToolbar(View view) {
        // Link topToolbar to XML top_tool_bar
        topToolbar = view.findViewById(R.id.top_tool_bar);
        // Declare topToolbar as the default ToolBar
        setSupportActionBar(topToolbar);
        // Disable to default back / home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    // Configure bottomToolbar
    private void configureBottomToolbar(View view) {
        // Link bottomToolbar to XML bottom_tool_bar
        bottomToolbar = view.findViewById(R.id.bottom_tool_bar);
        // Inflate XML bottom_tool_bar_menu to bottomToolbar
        bottomToolbar.inflateMenu(R.menu.bottom_tool_bar_menu);
        // Link bottomToolBarMethods and bottomToolbar
        bottomToolBarMethods(bottomToolbar);
    }

    // Set topToolBarMethods (called by all Activities that extend this BaseActivity)
    public boolean topToolBarMethods(MenuItem item) {
        // Switch statement to manage menu user clicks
        switch (item.getItemId()) {
            // User clicked toggle_theme_button
            case R.id.btn_toggle_theme:
                // TODO Create Theme Toggle method
                // Display toast message to confirm click
                Toast.makeText(this,"Theme Toggled", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    // Set bottomToolBarMethods
    public void bottomToolBarMethods(Toolbar bottomToolbar) {
        // Create listeners for all buttons on bottom_tool_bar_menu
        bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked btn_enter_app
                    case R.id.btn_enter_app:
                        // Instantiate new intent to start DisplayEventsActivity
                        Intent intentEnter =
                                new Intent(getBaseContext(), DisplayEventsActivity.class);
                        // Start Activity
                        startActivity(intentEnter);
                        return true;
                    // User clicked btn_exit_app
                    case R.id.btn_exit_app:
                        // Instantiate new intent to start StartActivity
                        Intent intentExit =
                                new Intent(getBaseContext(), StartActivity.class);
                        // Start Activity
                        startActivity(intentExit);
                        return true;
                    // User clicked btn_zoom_out
                    case R.id.btn_zoom_out:
                        // TODO Create Zoom Out method
                        // Display toast message to confirm click
                        Toast.makeText(getBaseContext(),
                                "Zoom Out", Toast.LENGTH_SHORT).show();
                        return true;
                    // User clicked btn_zoom_in
                    case R.id.btn_zoom_in:
                        // TODO Create Zoom In method
                        // Display toast message to confirm click
                        Toast.makeText(getBaseContext(),
                                "Zoom In", Toast.LENGTH_SHORT).show();
                        return true;
                    // User clicked btn_add_event
                    case R.id.btn_add_event:
                        // Create new intent to start AddEventActivity
                        Intent intentAdd =
                                new Intent(getBaseContext(), AddEventActivity.class);
                        // Start Activity
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

    /* CREATE: Write to database */
    // Add event to database
    private void addEvent() {
        // Create local EditText variable and link to txt_add_events
        EditText editText = findViewById(R.id.txt_add_events);
        // Store input from editText test in String eventHeading
        String eventHeading = editText.getText().toString().trim();
        // If EditText box is NOT blank...
        if (!TextUtils.isEmpty(eventHeading)) {
            // Get an auto generated unique ID from Firebase
            String eventID = mDatabaseRef.push().getKey();
            // Instantiate new Event Object, pass in Firebase ID and eventHeading
            Event event = new Event(eventID, eventHeading);
            // Save the Event object to Firebase to the retrieved ID
            mDatabaseRef.child(eventID).setValue(event);
            // Confirmation event saved message
            Toast.makeText(this, "Event saved", Toast.LENGTH_SHORT).show();
            // Create new intent to start a new activity (DisplayEventsActivity)
            Intent intentSave =
                    new Intent(getBaseContext(), DisplayEventsActivity.class);
            // Start activity
            startActivity(intentSave);
        // If EditText box IS blank...
        } else {
            // Prompt user to enter a heading
            Toast.makeText(this, "Please enter a heading", Toast.LENGTH_LONG).show();
        }
    }

}
