package com.mahmon.visual_timetable_app.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;
import com.mahmon.visual_timetable_app.ThemeToggleActivity;
import com.mahmon.visual_timetable_app.model.Event;
import com.mahmon.visual_timetable_app.model.EventAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Class to display events, implements the adapter and a listener
public class DisplayEventsActivity extends BaseActivity
        implements EventAdapter.OnItemClickListener {

    // Variables used for saving and retrieving theme preferences
    private Context mContext;
    private SharedPreferences mPrefs;
    private String mThemeValue;
    // Variables for RecyclerView and Adapter
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManger;
    private EventAdapter mAdapter;
    // Progress bars shown while adapter loads or image updates
    private ProgressBar mProgressCircle;
    // Variables for Firebase connections
    private DatabaseReference mDatabaseRef;
    // Listener variable used to kill listener
    private ValueEventListener mDBListener;
    // List used to hold events for display
    private List<Event> mEvents;
    // Variable for passing key to update edit event activity
    private int selectedEventDate;
    private String selectedEventName;
    private String selectedEventImageUrl;
    private String selectedEventDescription;
    private String selectedEventKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Assign preference variables
        mContext = this;
        mPrefs = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // Get value from mPrefs and assign to mThemeVale
        mThemeValue = mPrefs.getString(SELECTED_THEME, "");
        // Set the theme to the current selection
        setThemeSelection(mThemeValue);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        // Set bottom menu icons for this context (remove unwanted)
        getToolBarBottom().getMenu().removeItem(R.id.btn_enter_app);
        getToolBarBottom().getMenu().removeItem(R.id.btn_return_login);
        getToolBarBottom().getMenu().removeItem(R.id.btn_cancel_save);
        getToolBarBottom().getMenu().removeItem(R.id.btn_save_event);
        getToolBarBottom().getMenu().removeItem(R.id.btn_delete_event);
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.shrink_out);
        // Attach mRecyclerView to recycler_view
        mRecyclerView = findViewById(R.id.recycler_view);
        // Instantiate LinearLayoutManager for mRecyclerView
        mLayoutManger = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManger);
        // Attach mProgressCircle to progress_circle
        mProgressCircle = findViewById(R.id.progress_circle);
        // Instantiate mEvents as ArrayList
        mEvents = new ArrayList<>();
        // Instantiate new EventAdapter
        mAdapter = new EventAdapter(DisplayEventsActivity.this, mEvents);
        // Connect mRecyclerView to  mAdapter
        mRecyclerView.setAdapter(mAdapter);
        // Attach Click listener to EventAdapter
        mAdapter.setOnItemClickListener(DisplayEventsActivity.this);
        // Get Firebase database reference for VISUAL_EVENTS node
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS);
        // Instantiate database listener
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            // Method called on activity load and on any data changes
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear local list every time to prevent duplicate entry
                mEvents.clear();
                // For loop to iterate through database
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Store each Event in a local Event Object
                    Event event = postSnapshot.getValue(Event.class);
                    // Get event node key from database and set it to local event
                    event.setKey(postSnapshot.getKey());
                    // Add the local Event to local list
                    mEvents.add(event);
                }
                // Update Adapter every time
                mAdapter.notifyDataSetChanged();
                // Hide the progress bar
                mProgressCircle.setVisibility(View.INVISIBLE);
                // Sort Array by date, using comparator in Event class
                Collections.sort(mEvents, new Event());
            }
            // Called if database cannot be reached
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Display the error message
                Toast.makeText(DisplayEventsActivity.this,
                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                // Hide progress bard
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check if Toggle Theme Activity has be launched
        if (requestCode == TOGGLE_THEME_REQUEST) {
            recreate();
        }
    }

    // Implement the default options menu
    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        // Instantiate menu inflater object
        MenuInflater inflater = getMenuInflater();
        // Set menu_tool_bar_top as default options menu
        inflater.inflate(R.menu.menu_tool_bar_top, topMenu);
        return true;
    }

    // Set method calls for default option menu (top menu)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Switch statement to manage menu user clicks
        switch (item.getItemId()) {
            // User clicked toggle_theme_button
            case R.id.btn_toggle_theme:
                // Instantiate new intent to start ToggleThemeActivity
                Intent intent = new Intent(this, ThemeToggleActivity.class);
                // Start Activity
                startActivityForResult(intent, TOGGLE_THEME_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Method call when any list item is clicked, pass list position
    @Override
    public void onItemClick(int position) {
        // Call showUpdateDeleteDialog method below
        showUpdateDeleteActivity(position);
    }

    // Method to pass information for updating and deleting events
    private void showUpdateDeleteActivity(final int position) {
        // Instantiate an event object from the list position
        final Event selectedEvent = mEvents.get(position);
        // Store the event details in passable strings
        selectedEventDate = selectedEvent.getDate();
        selectedEventName = selectedEvent.getName();
        selectedEventImageUrl = selectedEvent.getImageUrl();
        selectedEventDescription = selectedEvent.getDescription();
        selectedEventKey = selectedEvent.getKey();
        // Instantiate new intent to start DisplayEventsActivity
        Intent intent = new Intent(getBaseContext(), UpdateDeleteEventActivity.class);
        // Create bundle to pass event data
        Bundle data = new Bundle();
        data.putInt("EXTRA_EVENT_DATE", selectedEventDate);
        data.putString("EXTRA_EVENT_NAME", selectedEventName);
        data.putString("EXTRA_EVENT_IMAGE_URL", selectedEventImageUrl);
        data.putString("EXTRA_EVENT_DESCRIPTION", selectedEventDescription);
        data.putString("EXTRA_EVENT_KEY", selectedEventKey);
        // Package the bundle into intent
        intent.putExtras(data);
        // Start Activity
        startActivity(intent);
    }

    // Destroy EventListener when Activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}