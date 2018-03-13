package com.mahmon.visual_timetable_app.model;

import com.google.firebase.database.IgnoreExtraProperties;

// Class for generating Event objects
@IgnoreExtraProperties
public class Event {

    // String for event mEventHeading (more attributes to be added later)
    private String mEventHeading;
    // String to store the URL of the image
    private String mImageUrl;

    // Default constructor (No called but required by Firebase)
    public Event() {
    }

    // Constructor to make sure a value is set
    public Event( String eventHeading, String imageUrl) {
        mEventHeading = eventHeading;
        mImageUrl = imageUrl;
    }

    // Getter methods for mEventID,  mEventHeading and mImageUrl
    public String getEventHeading() {
        return mEventHeading;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    // Setter method for mIamgeUrl
    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;

    }

}