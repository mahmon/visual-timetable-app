package com.mahmon.visual_timetable_app.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mahmon.visual_timetable_app.R;

import java.util.List;

public class EventList extends ArrayAdapter<Event> {
    private Activity context;
    List<Event> eventList;

    public EventList(Activity context, List<Event> eventList) {
        super(context, R.layout.layout_event_list, eventList);
        this.context = context;
        this.eventList = eventList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_event_list, null, true);
        TextView textViewEventHeading = listViewItem.findViewById(R.id.textViewEventHeading);
        final Event event = eventList.get(position);
        textViewEventHeading.setText(event.getEventHeading());
        return listViewItem;
    }


}