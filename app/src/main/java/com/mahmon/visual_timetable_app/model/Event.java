package com.mahmon.visual_timetable_app.model;

import com.google.firebase.database.IgnoreExtraProperties;

// Class for generating Event objects
@IgnoreExtraProperties
public class Event {

    // String for eventID
    private String eventID;
    // String for event eventHeading (more attributes to be added later)
    private String eventHeading;

    // Default constructor
    public Event() {
    }

    // Constructor to make sure a value is set
    public Event(String eventID, String eventHeading) {
        this.eventID = eventID;
        this.eventHeading = eventHeading;
    }

    // Getter methods
    public String getEventID() {
        return eventID;
    }
    public String getEventHeading() {
        return eventHeading;
    }

}