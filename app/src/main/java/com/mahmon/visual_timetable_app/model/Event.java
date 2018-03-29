package com.mahmon.visual_timetable_app.model;

import com.google.firebase.database.Exclude;

import java.util.Comparator;

// Class used to instantiate Event objects for storage in database
public class Event implements Comparator<Event> {

    // Need to add descriptions and allow for links to open browser

    // Variables for name, image url and database key
    private int mDate;
    private String mName;
    private String mImageUrl;
    private String mDescription;
    private String mKey;

    // Default constructor required by Firebase
    public Event() {
    }

    // Constructor to build event objects, pass name and imageUrl
    public Event(int date, String name, String imageUrl, String description) {
        mDate = date;
        mName = name;
        mImageUrl = imageUrl;
        mDescription = description;
    }


    // Overriding compare method, sort by date oldest first
    @Override
    public int compare(Event e, Event e1) {
        return e.mDate - e1.mDate;
    }

    // Getter methods
    public int getDate() {
        return mDate;
    }
    public String getName() {
        return mName;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    public String getDescription() {
        return mDescription;
    }

    // Setter methods
    public void setDate(int date) {
        mDate = date;
    }
    public void setName(String name) {
        mName = name;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
    public void setDescription(String description) {
        mDescription = description;
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