package com.mahmon.visual_timetable_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    // Method called when user clicks btn_save_added_event
    public void saveAddedEvent(View view) {
        Intent intent = new Intent(this, DisplayEventsActivity.class);
        startActivity(intent);
    }
}
