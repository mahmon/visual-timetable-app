package com.mahmon.visual_timetable_app.Model;

// Class for generating Event objects
public class Event {

    // String for event title (more attributes to be added later)
    private String title;

    // Default constructor
    public Event() {
    }

    // Constructor to make sure a value is set
    public Event(String title) {
        this.title = title;
    }

    // Getter method
    public String getTitle() {
        return title;
    }

}