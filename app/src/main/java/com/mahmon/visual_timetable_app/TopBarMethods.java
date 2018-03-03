package com.mahmon.visual_timetable_app;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

// Class to manage top bar method calls, avoid duplication
public class TopBarMethods {

    // Method called when user clicks light bulb icon
    public static void lightBulbClicked(Context mContext){
        String toastText = "Toggle Theme Clicked";
        Toast toast = Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}