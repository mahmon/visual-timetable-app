package com.mahmon.visual_timetable_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Animation override:
        // Go_in for this activity, go_out for previous activity
        overridePendingTransition(R.anim.go_in, R.anim.go_out);
    }

    // onClick listener for button: btn_enter_app
    public void enterAppClicked(View view) {
        // Method called from AppMethods
        AppMethods.enterApp(getBaseContext());
    }

}