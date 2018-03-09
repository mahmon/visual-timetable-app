package com.mahmon.visual_timetable_app.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahmon.visual_timetable_app.R;

import java.util.List;

// Class to manage and display data in RecyclerView - recyclerViewEvents
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>{

    // Variable to return the context of this class
    private Context context;
    // Variable to store a list of 'Event objects
    private List<Event> eventList;

    // Constructor
    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate and return EventViewHolder
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_list, null);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        // Get the event of the specified position
        Event event = eventList.get(position);
        // Bind the data with viewHolder views
        holder.text_view_event_title.setText(event.getTitle());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // Class for creating EventViewHolder
    public class EventViewHolder extends RecyclerView.ViewHolder {

        // Get reference to text_view_event_title
        TextView text_view_event_title = itemView.findViewById(R.id.text_view_event_title);

        // Constructor for this class

        public EventViewHolder(View itemView) {
            super(itemView);
            this.text_view_event_title = itemView.findViewById(R.id.text_view_event_title);
        }

    }

}
