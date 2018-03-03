package com.mahmon.visual_timetable_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EditEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        // Animation override:
        // Go_in for this activity, go_out for previous activity
        overridePendingTransition(R.anim.go_in, R.anim.go_out);
    }

    // Method called onClick for button: btn_save_edited_event
    public void saveEditedEvent(View view) {
        // Destroy this activity
        // Return to previous activity (DisplayEventsActivity)
        finish();
        // Animation override:
        // Back_out for this activity, back_in for previous activity
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

}
