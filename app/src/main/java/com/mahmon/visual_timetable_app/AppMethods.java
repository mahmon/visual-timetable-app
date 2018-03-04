package com.mahmon.visual_timetable_app;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

// Class to manage top bar method calls, avoids code duplication
public class AppMethods {

    // Method called when user clicks btn_enter_app
    public static void enterApp(Context mContext) {
        // Create new intent to start a new activity (AddEventActivity)
        Intent intent = new Intent(mContext, DisplayEventsActivity.class);
        // Start activity
        mContext.startActivity(intent);
    }

    // Method called when user clicks light bulb icon
    public static void lightBulbClicked(Context mContext){
        String toastText = "Toggle Theme Clicked";
        Toast toast = Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void addEventClicked(Context mContext) {
        // Create new intent to start a new activity (AddEventActivity)
        Intent intent = new Intent(mContext, AddEventActivity.class);
        // Start activity
        mContext.startActivity(intent);
    }
}