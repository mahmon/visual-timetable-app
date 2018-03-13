package com.mahmon.visual_timetable_app.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahmon.visual_timetable_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

// Recycler view adapter, joins data to recycler view
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    // Variables used to pass contect and event list
    private Context mContext;
    private List<Event> mEvents;

    // Constructor
    public EventAdapter(Context context, List<Event> events) {
        mContext = context;
        mEvents = events;
    }

    // Called when EventViewHolder is created
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view, pass in layout_single_event
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_single_event, parent,false);
        // Return the EventViewHolder passing in the view above
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        // Get position of current event and store in eventCurrent
        Event eventCurrent = mEvents.get(position);
        // Create holder and set text to eventCurrent heading
        holder.mTextViewEventName.setText(eventCurrent.getEventHeading());
        // Get eventCurrent image and pass to holder
        Picasso.with(mContext)
                .load(eventCurrent.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.mImageViewEventImage);
    }

    @Override
    public int getItemCount() {
        // Show as many events as in the list
        return mEvents.size();
    }

    // Create EventViewHolder
    public class EventViewHolder extends RecyclerView.ViewHolder {
        // View variables
        public TextView mTextViewEventName;
        public ImageView mImageViewEventImage;
        // Constructor passes view and links to XML tags
        public EventViewHolder(View itemView) {
            super(itemView);
            // Create link between the View variables and XML tags in layout_single_event
            mTextViewEventName = itemView.findViewById(R.id.text_view_card_heading);
            mImageViewEventImage = itemView.findViewById(R.id.image_view_card_image);
        }
    }
}
