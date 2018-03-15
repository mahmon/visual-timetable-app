package com.mahmon.visual_timetable_app.model;

import com.google.firebase.database.Exclude;

public class Event {
    private String mName;
    private String mImageUrl;
    private String mKey;

    public Event() {
        //empty constructor needed
    }

    public Event(String name, String imageUrl) {
        mName = name;
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }

}