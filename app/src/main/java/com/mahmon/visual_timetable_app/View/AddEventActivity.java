package com.mahmon.visual_timetable_app.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.Model.Event;
import com.mahmon.visual_timetable_app.R;

public class AddEventActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        // Animation override:
        // Go_in for this activity, go_out for previous activity
        overridePendingTransition(R.anim.go_in, R.anim.go_out);
        // Setup action bars
        showBottomActionBar();
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
    // Back_in for this activity, back_out for previous activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

    /* Action Bars Set Up*/
    // Initiate bottomActionBar
    public void showBottomActionBar() {
        // Add the bottom action bar and inflate the menu
        Toolbar bottomActionBar = findViewById(R.id.bottom_action_bar);
        bottomActionBar.inflateMenu(R.menu.bottom_save_bar_menu);
        bottomActionBarMethods(bottomActionBar);
    }

    /* Action Bars Methods*/
    // Set method calls for bottomActionBar
    public void bottomActionBarMethods(Toolbar bottomActionBar) {
        // Create listeners for bottom_action_bar_menu
        bottomActionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked btn_save_added_event
                    case R.id.btn_save_added_event:
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
                        Intent intentAdd = new Intent(AddEventActivity.this,
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

}
