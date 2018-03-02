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
    }

    // Method called when user clicks btn_enter_app
    public void enterApp(View view) {
        Intent intent = new Intent(this, DisplayEventsActivity.class);
        startActivity(intent);
    }

}