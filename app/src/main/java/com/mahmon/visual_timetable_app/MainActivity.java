package com.mahmon.visual_timetable_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Animation override, go_in for this activity, go_out for previous activity
        overridePendingTransition(R.anim.go_in, R.anim.go_out);
    }

    // Method called onClick for button: btn_enter_app
    public void enterApp(View view) {
        // Create new intent to start a new activity (DisplayEventActivity)
        Intent intent = new Intent(this, DisplayEventsActivity.class);
        // Start activity
        startActivity(intent);
    }

}