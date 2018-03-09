package com.mahmon.visual_timetable_app.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.model.Event;
import com.mahmon.visual_timetable_app.model.EventAdapter;
import com.mahmon.visual_timetable_app.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DisplayEventsActivity extends BaseActivity {

    // Variable to store RecyclerView
    private RecyclerView recyclerView;
    // List to store all Event objects
    private List<Event> eventList;
    // Declare adapter
    private EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);
        // Set bottom menu icons for this context
        getBottomToolbar().getMenu().removeItem(R.id.btn_enter_app);
        getBottomToolbar().getMenu().removeItem(R.id.btn_save_event);
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        // Start Listener and RecyclerView
        startListener();
        startRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Animation override:
        overridePendingTransition(R.anim.grow_in, R.anim.grow_out);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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

    /* Database Listener */
    // Methods call to implement database Listener
    public void startListener() {
        // Create instance and reference to database
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // Set reference to two child levels
        DatabaseReference mDatabaseReference =
                mDatabase.getReference().child("Visual Events");
        // Set listener to read from the database reference
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            // This method is called whenever data ais updated.
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Iterate through the data
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                while((iterator.hasNext())){
                    Event event = iterator.next().getValue(Event.class);
                    // Add each Event to the eventList
                    eventList.add(new Event(event.getTitle()));
                }
                // Refresh the RecyclerView
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    /* RecyclerView */
    // Method call to implement the RecyclerView
    public void startRecyclerView() {
        // Get recycler_view_events
        recyclerView = findViewById(R.id.recycler_view_events);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        adapter = new EventAdapter(this, eventList);
        // Set adapter to RecyclerView
        recyclerView.setAdapter(adapter);
    }

    // Animation override for the default back button:
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.grow_in, R.anim.grow_out);
    }

}
