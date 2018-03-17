package com.mahmon.visual_timetable_app.model;

import com.google.firebase.database.Exclude;

// Class used to instantiate Event objects for storage in database
public class Event {

    // Variables for name, image url and database key
    private String mName;
    private String mImageUrl;
    private String mKey;

    // Dfault constructor required by Firebase
    public Event() {
    }

    // Constructor to build event objects, pass name and imageUrl
    public Event(String name, String imageUrl) {
        mName = name;
        mImageUrl = imageUrl;
    }

    // Getter methods
    public String getName() {
        return mName;
    }
    public String getImageUrl() {
        return mImageUrl;
    }

    // Setter methods
    public void setName(String name) {
        mName = name;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    // Getter and setter for Key
    // Excluded from Firebase to avoid duplicate data
    @Exclude
    public String getKey() {
        return mKey;
    }
    @Exclude
    public void setKey(String key) {
        mKey = key;
    }

}