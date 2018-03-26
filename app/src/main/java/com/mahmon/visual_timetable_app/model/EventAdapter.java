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

// Adapter class to join event list to recycler view
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ImageViewHolder> {

    // Context variable to pass context
    private Context mContext;
    // List to hold events
    private List<Event> mEvents;
    // Click listener object
    private OnItemClickListener mListener;

    // Constructor, passes context and event list
    public EventAdapter(Context context, List<Event> events) {
        mContext = context;
        mEvents = events;
    }

    // Override method to create ImageViewHolder
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Instantiate new view and inflate an event item
        View v = LayoutInflater.from(mContext).inflate(R.layout.event_item, parent, false);
        return new ImageViewHolder(v);
    }

    // Override method to bind data to ImageView
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        // Get event at each list position
        Event eventCurrent = mEvents.get(position);
        // Get event name, picture, desciption and load into current event
        holder.textViewName.setText(eventCurrent.getName());
        Picasso.with(mContext)
                .load(eventCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
        holder.textViewDescription.setText(eventCurrent.getDescription());
    }

    // Override method to return size of list
    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    // ImageViewHolder class, implements Listener
    public class ImageViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        // Variables to hold the name and the image
        public TextView textViewName;
        public ImageView imageView;
        public TextView textViewDescription;

        // Constructor
        public ImageViewHolder(View itemView) {
            super(itemView);

            // Link textViewName to text_view_name
            textViewName = itemView.findViewById(R.id.text_view_name);
            // Link imageView to image_view_upload
            imageView = itemView.findViewById(R.id.image_view_upload);
            // Link textView description to text_view_description
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            // Set the onClickListener
            itemView.setOnClickListener(this);

        }

        // Get event position
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

    }

    // Interface, allows click methods to be set outside of this Adapter
    public interface OnItemClickListener {
        // Contract to require @Override onItemClick method
        void onItemClick(int position);
    }

    // Attach listener to mListener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}