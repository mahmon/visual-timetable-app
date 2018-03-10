package com.mahmon.visual_timetable_app.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mahmon.visual_timetable_app.R;

import java.util.List;

// Adapter class to join data to List View
public class EventList extends ArrayAdapter<Event> {

    // Variable to store the context
    private Activity context;
    // List variable to store Event objects
    List<Event> eventList;

    // Constructor; takes Activity and List<Event> as parameters
    public EventList(Activity context, List<Event> eventList) {
        // Pass the xml layout layout_event_list to the super class
        super(context, R.layout.layout_event_list, eventList);
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_event_list, null, true);
        // Link textViewEventHeading in listView to local variable
        TextView textViewEventHeading = listViewItem.findViewById(R.id.textViewEventHeading);
        // For each event in the list get a position value
        final Event event = eventList.get(position);
        // Get eventHeading and set to local variable
        textViewEventHeading.setText(event.getEventHeading());
        // Return this item to the view group
        return listViewItem;
    }

}