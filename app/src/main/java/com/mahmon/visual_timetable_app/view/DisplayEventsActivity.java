package com.mahmon.visual_timetable_app.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;
import com.mahmon.visual_timetable_app.model.Event;
import com.mahmon.visual_timetable_app.model.EventList;

import java.util.ArrayList;
import java.util.List;

public class DisplayEventsActivity extends BaseActivity {

    //we will use these constants later to pass the artist name and id to another activity
    public static final String ARTIST_NAME = "net.simplifiedcoding.firebasedatabaseexample.artistname";
    public static final String ARTIST_ID = "net.simplifiedcoding.firebasedatabaseexample.artistid";

    // Declare ListView object
    ListView listViewEvents;
    //a list to store all the artist from firebase database
    List<Event> eventList;
    // Database reference object
    DatabaseReference databaseEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        // Set bottom menu icons for this context
        getBottomToolbar().getMenu().removeItem(R.id.btn_enter_app);
        getBottomToolbar().getMenu().removeItem(R.id.btn_save_event);
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        // Get reference of Visual Events node
        databaseEvents = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS);
        // Get views
        listViewEvents = findViewById(R.id.listViewEvents);
        //list to store events
        eventList = new ArrayList<>();
        // Listen for long click on event and launch showUpdateDeleteDialog method
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event event = eventList.get(i);
                showUpdateDeleteDialog(event.getEventID(), event.getEventHeading());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        /* Read from database */
        // Attach value event listener
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear previous artist list
                eventList.clear();
                // Iterate through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Event event = postSnapshot.getValue(Event.class);
                    //adding artist to the list
                    eventList.add(event);
                }
                // Create adapter
                EventList eventListAdapter = new EventList(DisplayEventsActivity.this, eventList);
                // Attach adapter to ListView
                listViewEvents.setAdapter(eventListAdapter);
                // Refresh the RecyclerView
                eventListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.grow_in, R.anim.grow_out);
    }

    /* Update to database */
    // Method to Update Events
    private boolean updateEvent(String eventID, String eventHeading) {
        // Get the event ID
        databaseEvents = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS).child(eventID);
        // Update Event
        Event event = new Event(eventID, eventHeading);
        databaseEvents.setValue(event);
        Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_SHORT).show();
        return true;
    }

    // Method to Inflate dialog box for editing events
    private void showUpdateDeleteDialog(final String eventID, String eventHeading) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_delete_event, null);
        dialogBuilder.setView(dialogView);

        final TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        final EditText editTextName = dialogView.findViewById(R.id.txt_edit_events);
        final ImageButton buttonUpdate = dialogView.findViewById(R.id.btn_save_edited_event);
        final ImageButton buttonDelete = dialogView.findViewById(R.id.btn_delete_event);
        dialogTitle.setText(eventHeading);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    updateEvent(eventID, name);
                    b.dismiss();
                } else {
                    // Prompt
                    Toast.makeText(getApplicationContext(), "Enter a new heading", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEvent(eventID);
                b.dismiss();
            }
        });
    }

    /* Delete from database */
    // Method to delete Event
    private boolean deleteEvent(String eventID) {
        // Get the event ID
        databaseEvents = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS).child(eventID);
        // Remove the event
        databaseEvents.removeValue();
        // Confirm the deletion
        Toast.makeText(getApplicationContext(), "Event Deleted", Toast.LENGTH_SHORT).show();
        return true;
    }

}