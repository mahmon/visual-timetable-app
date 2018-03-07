package com.mahmon.visual_timetable_app;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DisplayEventsActivity extends AppCompatActivity {

    // Variable to store RecyclerView
    private RecyclerView recyclerView;
    // List to store all Event objects
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        // Animation override:
        // Go_in for this activity, go_out for previous activity
        overridePendingTransition(R.anim.go_in, R.anim.go_out);
        /* Manage RecyclerView */
        // Get recycler_view_events
        recyclerView = findViewById(R.id.recycler_view_events);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initialise event list
        eventList = new ArrayList<>();
        // Instantiate the EventAdapter
        EventAdapter adapter = new EventAdapter(this, eventList);
        // Set adapter to RecyclerView
        recyclerView.setAdapter(adapter);
        /* Manage ToolBar : topActionBar */
        // Implement top_action_bar as default action bar for this activity
        Toolbar topActionBar = findViewById(R.id.top_action_bar);
        setSupportActionBar(topActionBar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Disable the Up button
        actionBar.setDisplayHomeAsUpEnabled(false);
        /* Manage ToolBar : bottomActionBar */
        // Add the bottom action bar and inflate the menu
        Toolbar bottomActionBar = findViewById(R.id.bottom_action_bar);
        bottomActionBar.inflateMenu(R.menu.bottom_action_bar_menu);
        // Create listeners for bottom_action_bar_menu
        bottomActionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked btn_zoom_out
                    case R.id.btn_zoom_out:
                        // Display toast message to confirm click
                        Toast toastOut = Toast.makeText(
                                DisplayEventsActivity.this,
                                "You Clicked Zoom Out", Toast.LENGTH_SHORT);
                        toastOut.setGravity(Gravity.CENTER, 0, 0);
                        toastOut.show();
                        return true;
                    // User clicked btn_zoom_in
                    case R.id.btn_zoom_in:
                        // Display toast message to confirm click
                        Toast toastIn = Toast.makeText(
                                DisplayEventsActivity.this,
                                "You Clicked Zoom In", Toast.LENGTH_SHORT);
                        toastIn.setGravity(Gravity.CENTER, 0, 0);
                        toastIn.show();
                        return true;
                    // User clicked btn_goto_add_event
                    case R.id.btn_goto_add_event:
                        // Create new intent to start a new activity (AddEventActivity)
                        Intent intentAdd = new Intent(DisplayEventsActivity.this,
                                        AddEventActivity.class);
                        // Start activity
                        startActivity(intentAdd);
                        return true;
                    // User clicked btn_goto_edit_event
                    case R.id.btn_goto_edit_event:
                        // Create new intent to start a new activity (EditEventActivity)
                        Intent intentEdit = new Intent(DisplayEventsActivity.this,
                                EditEventActivity.class);
                        // Start activity
                        startActivity(intentEdit);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    /* Database listener */
    // On start read the data from the database
    @Override
    public void onStart() {
        super.onStart();
        // Create instance and reference to database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // Set reference to two child levels
        DatabaseReference mDatabaseReference =
                mDatabase.getReference("Visual Events").child("Event Heading");
        // Set listener to read from the database reference
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            // This method is called whenever data ais updated.
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Store the value returned in String eventTitle
                String eventTitle = dataSnapshot.getValue(String.class);
                // Pass value into a new Event object and add to list 10x
                for (int i=0; i<10; i++) {
                    eventList.add(new Event(eventTitle + (" ") + (i+1)));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
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
            // User clicked home button
            case android.R.id.home:
                // Destroy activity calling method, return to previous activity
                finish();
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

}
