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
import android.view.MotionEvent;
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

public class DisplayEventsActivity extends BaseActivity implements EventAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private EventAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

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

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mEvents = new ArrayList<>();

        mAdapter = new EventAdapter(DisplayEventsActivity.this, mEvents);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(DisplayEventsActivity.this);


        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS);

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mEvents.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Event event = postSnapshot.getValue(Event.class);
                    event.setKey(postSnapshot.getKey());
                    mEvents.add(event);
                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DisplayEventsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(int position) {
        showUpdateDeleteDialog(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    /* UPDATE: Update event in database */
    // Method called to Update Events
    private void updateEvent(Event selectedEvent, int position, String newName) {
        // Get event key, generated when dialog inflates
        final String selectedKey = selectedEvent.getKey();
        // Use selectedKey to change event heading value
        mDatabaseRef.child(selectedKey).child("name").setValue(newName);
        Toast.makeText(getApplicationContext(), "Event Name Updated", Toast.LENGTH_SHORT).show();
    }

    /* DELETE: Delete events from database */
    // Method called to Delete Events
    private void deleteEvent(Event selectedEvent, int position) {
        final String selectedKey = selectedEvent.getKey();
        StorageReference eventRef = mStorage.getReferenceFromUrl(selectedEvent.getImageUrl());
        eventRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(DisplayEventsActivity.this, "Event Deleted", Toast.LENGTH_SHORT).show();
            }
        });
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


        final Event selectedEvent = mEvents.get(position);
        // Use eventHeading to setText for dialogTitle
        dialogTitle.setText("Update event '" + selectedEvent.getName() + "'");

        // Instantiate and AlertDialog object, used dialogBuilder to create it
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
                    updateEvent(selectedEvent, position, newName);
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
                deleteEvent(selectedEvent, position);
                // Dismiss the dialog
                alertDialog.dismiss();
            }
        });
    }

}