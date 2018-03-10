package com.mahmon.visual_timetable_app.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    // Declare ListView to display all Event objects
    ListView listViewEvents;
    // Declare List to store Event objects from Firebase
    List<Event> eventList;
    // Declare a Database reference object
    DatabaseReference databaseEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link this activity to the relevant XML layout
        setContentView(R.layout.activity_display_events);
        // Set bottom menu icons for this context (remove unwanted)
        getBottomToolbar().getMenu().removeItem(R.id.btn_enter_app);
        getBottomToolbar().getMenu().removeItem(R.id.btn_save_event);
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        // Set database reference to Visual Events node
        databaseEvents = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS);
        // Get the listViewEvents and attach to local variable
        listViewEvents = findViewById(R.id.listViewEvents);
        // Instantiate eventList as an ArrayList of objects
        eventList = new ArrayList<>();
        // Listen for click on each event and launch showUpdateDeleteDialog method
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Instantiate new event from the eventList, get it's position
                Event event = eventList.get(i);
                // Pass event to showUpdateDeleteDialog with heading and ID parameters
                showUpdateDeleteDialog(event.getEventID(), event.getEventHeading());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /* READ: Read from database */
        // Attach value event listener to database
        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear previous event list
                eventList.clear();
                // Iterate through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Get event and pass to local event Object
                    Event event = postSnapshot.getValue(Event.class);
                    // Add the event object to the list
                    eventList.add(event);
                }
                // Create adapter (EventList object) and pass the list
                EventList eventListAdapter =
                        new EventList(DisplayEventsActivity.this, eventList);
                // Attach adapter to ListView
                listViewEvents.setAdapter(eventListAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Method run if database connection fails
            }
        });
    }

    // Implement the default options menu
    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        // Instantiate menu inflater object
        MenuInflater inflater = getMenuInflater();
        // Set top_tool_bar_menu as default options menu
        inflater.inflate(R.menu.top_tool_bar_menu, topMenu);
        return true;
    }

    // Set method calls for default option menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Attach topToolBarMethods to default menu
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

    /* UPDATE: Update event in database */
    // Method called to Update Events
    private void updateEvent(String eventID, String eventHeading) {
        // Get the event ID from database
        databaseEvents = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS).child(eventID);
        // Instantiate local Event object and pass in ID and Heading
        Event event = new Event(eventID, eventHeading);
        // Use this object to overwrite object in database
        databaseEvents.setValue(event);
        // Send update confirmation message to user
        Toast.makeText(getApplicationContext(), "Event Updated", Toast.LENGTH_SHORT).show();
    }

    /* DELETE: Delete events from database */
    // Method called to Delete Events
    private boolean deleteEvent(String eventID) {
        // Get the event ID from database
        databaseEvents = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS).child(eventID);
        // Remove the event object from the database
        databaseEvents.removeValue();
        // Send delete confirmation message to user
        Toast.makeText(getApplicationContext(), "Event Deleted", Toast.LENGTH_SHORT).show();
        return true;
    }

    // Method to Inflate dialog box for updating and deleting events
    private void showUpdateDeleteDialog(final String eventID, String eventHeading) {
        // Instantiate dialogBuilder object
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // Instantiate LayoutInflater object
        LayoutInflater inflater = getLayoutInflater();
        // Instantiate a dialogView and use inflater to inflate XML file to it
        final View dialogView = inflater.inflate(R.layout.update_delete_event, null);
        // Use dialogBuilder setView method passing in dialogView instantiated above
        dialogBuilder.setView(dialogView);
        // Create local variable and link to dialog_title
        final TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        // Create local variable and link to txt_edit_events
        final EditText editTextName = dialogView.findViewById(R.id.txt_edit_events);
        // Create local variable and link to btn_save_edited_event
        final ImageButton buttonUpdate = dialogView.findViewById(R.id.btn_save_edited_event);
        // Create local variable and link to btn_delete_event
        final ImageButton buttonDelete = dialogView.findViewById(R.id.btn_delete_event);
        // Use eventHeading to setText for dialogTitle
        dialogTitle.setText(eventHeading);
        // Instantiate and AlertDialog object, used dialogBuilder to create it
        final AlertDialog alertDialog = dialogBuilder.create();
        // Show the AlertDialog
        alertDialog.show();
        // Attach an onClickListener to buttonUpdate
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get text from editTextName, store in local variable
                String newHeading = editTextName.getText().toString().trim();
                // If user has typed in a value...
                if (!TextUtils.isEmpty(newHeading)) {
                    // Call the updateEvent method
                    updateEvent(eventID, newHeading);
                    // Dismiss the dialog
                    alertDialog.dismiss();
                // If the user has NOT typed in a value...
                } else {
                    // Prompt user to enter a new heading
                    Toast.makeText(getApplicationContext(),
                            "Enter a new heading", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Attach an onClickListener to buttonDelete
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the deleteEvent Method
                deleteEvent(eventID);
                // Dismiss the dialog
                alertDialog.dismiss();
            }
        });
    }

}