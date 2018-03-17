package com.mahmon.visual_timetable_app.view;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;
import com.mahmon.visual_timetable_app.model.Event;
import com.mahmon.visual_timetable_app.model.EventAdapter;

import java.util.ArrayList;
import java.util.List;

// Class to display events, implements the adapter and a listener
public class DisplayEventsActivity extends BaseActivity
        implements EventAdapter.OnItemClickListener {

    // Variables for RecyclerView and Adapter
    private RecyclerView mRecyclerView;
    private EventAdapter mAdapter;
    // Progress bar shown while adapter loads
    private ProgressBar mProgressCircle;
    // Variables for Firebase connections
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    // Listener variable used to kill listener
    private ValueEventListener mDBListener;
    // List used to hold events for display
    private List<Event> mEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        // Set bottom menu icons for this context (remove unwanted)
        getToolBarBottom().getMenu().removeItem(R.id.btn_enter_app);
        getToolBarBottom().getMenu().removeItem(R.id.btn_return_login);
        getToolBarBottom().getMenu().removeItem(R.id.btn_save_event);
        getToolBarBottom().getMenu().removeItem(R.id.btn_cancel_save);
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.shrink_out);
        // Attach mRecyclerView to recycler_view
        mRecyclerView = findViewById(R.id.recycler_view);
        // Instnatiate LinearLayoutManager for mRecyclerView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        // Get Firebase storageRef
        mStorage = FirebaseStorage.getInstance();
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

    // Implement the default options menu
    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        // Instantiate menu inflater object
        MenuInflater inflater = getMenuInflater();
        // Set menu_tool_bar_top as default options menu
        inflater.inflate(R.menu.menu_tool_bar_top, topMenu);
        return true;
    }

    // Set method calls for default option menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Attach topToolBarMethods to default menu
        toolBarMethodsTop(item);
        // Invoke the superclass to handle unrecognised user action.
        return super.onOptionsItemSelected(item);
    }

    // Method call when any list item is clicked, pass list position
    @Override
    public void onItemClick(int position) {
        // Call showUpdateDeleteDialog method below
        showUpdateDeleteDialog(position);
    }

    // Method to Inflate dialog box for updating and deleting events
    private void showUpdateDeleteDialog(final int position) {
        // Instantiate dialogBuilder object
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // Instantiate LayoutInflater object
        LayoutInflater inflater = getLayoutInflater();
        // Instantiate a dialogView and use inflater to inflate XML file to it
        final View dialogView = inflater.inflate(R.layout.dialog_update_delete_event, null);
        // Use dialogBuilder setView method passing in dialogView instantiated above
        dialogBuilder.setView(dialogView);
        // Create local variable and link to dialog_title
        final TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        // Create local variable and link to txt_enter_event_heading
        final EditText editTextName = dialogView.findViewById(R.id.txt_enter_event_heading);
        // Create local variable and link to btn_save_edited_event
        final ImageButton buttonUpdate = dialogView.findViewById(R.id.btn_save_edited_event);
        // Create local variable and link to btn_delete_event
        final ImageButton buttonDelete = dialogView.findViewById(R.id.btn_delete_event);
        // Instaniate an event object from the list position
        final Event selectedEvent = mEvents.get(position);
        // Use eventHeading to setText for dialogTitle
        String title = getResources().getString(R.string.txt_update_event_dialog_title)
                + " '" + selectedEvent.getName() + "'";
        dialogTitle.setText(title);
        // Instantiate an AlertDialog object, used dialogBuilder to create it
        final AlertDialog alertDialog = dialogBuilder.create();
        // Show the AlertDialog
        alertDialog.show();
        // Attach an onClickListener to buttonUpdate
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get text from editTextName, store in local variable
                String newName = editTextName.getText().toString().trim();
                // If user has typed in a value...
                if (!TextUtils.isEmpty(newName)) {
                    // Call the updateEvent method
                    updateEvent(selectedEvent, newName);
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
                deleteEvent(selectedEvent);
                // Dismiss the dialog
                alertDialog.dismiss();
            }
        });
    }

    /* UPDATE: Update event in database */
    // Method called to Update Events
    private void updateEvent(Event selectedEvent, String newName) {
        // Get event key, generated when dialog inflates
        final String selectedKey = selectedEvent.getKey();
        // Use selectedKey to change event heading value
        mDatabaseRef.child(selectedKey).child("name").setValue(newName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Confirm update
                Toast.makeText(getApplicationContext(),
                        "Event Name Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* DELETE: Delete events from database */
    // Method called to Delete Events
    private void deleteEvent(Event selectedEvent) {
        // Get event key, generated when dialog inflates
        final String selectedKey = selectedEvent.getKey();
        // Generate storageRef from selected event
        StorageReference eventRef = mStorage.getReferenceFromUrl(selectedEvent.getImageUrl());
        // Use storageRef to delete image
        eventRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Following image deletion, delete database entry
                mDatabaseRef.child(selectedKey).removeValue();
                // Confirm deletion
                Toast.makeText(DisplayEventsActivity.this,
                        "Event Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Destroy EventListener when Activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}