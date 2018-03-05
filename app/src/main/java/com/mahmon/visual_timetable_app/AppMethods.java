package com.mahmon.visual_timetable_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Class to manage top bar method calls, avoids code duplication
public class AppMethods extends AppCompatActivity {

    /* Methods for top_action_bar_menu */
    // Method called when user clicks btn_toggle_theme
    public static void toggleTheme(Context mContext){
        makeToast(mContext, "Toggle Theme Clicked");
    }
    // Method called when user clicks home button
    public static void homeClicked(Activity activity) {
        // Destroy activity calling method, return to previous activity
        activity.finish();
    }
    /* END */

    /* Methods for bottom_action_bar_menu */
    // Method called when user clicks btn_goto_add_event
    public static void addEvent(Context mContext) {
        // Create new intent to start a new activity (AddEventActivity)
        Intent intent = new Intent(mContext, AddEventActivity.class);
        // Start activity
        mContext.startActivity(intent);
    }

    // Method called when user clicks btn_zoom_out
    public static void zoomOut(Context mContext){
        makeToast(mContext, "Zoom Out Clicked");
    }

    // Method called when user clicks btn_zoom_in
    public static void zoomIn(Context mContext){
        makeToast(mContext, "Zoom In Clicked");
    }
    /* END */

    /* All other app methods */
    // Method called when user clicks btn_enter_app
    public static void enterApp(Context mContext) {
        // Create new intent to start a new activity (AddEventActivity)
        Intent intent = new Intent(mContext, DisplayEventsActivity.class);
        // Start activity
        mContext.startActivity(intent);
    }

    // Method called when user clicks btn_exit_app
    public static void exitApp(Activity activity) {
        // Destroy activity calling method, return to previous activity
        activity.finish();
    }

    // Method called when user clicks btn_goto_edit_event
    public static void editEvent(Context mContext) {
        // Create new intent to start a new activity (EditEventActivity)
        Intent intent = new Intent(mContext, EditEventActivity.class);
        // Start activity
        mContext.startActivity(intent);
    }

    // Method called when user clicks btn_save_edited_event
    public static void saveEditedEvent(Activity activity) {
        // Destroy activity calling method, return to previous activity
        activity.finish();
    }

    // Method called when user clicks btn_save_added_event
    public static void saveAddedEvent(Activity activity) {
        // Test write to Firebase database
        // Create database instance
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        // Create database reference, set a child with value 'Visual Events'
        DatabaseReference mDatabaseReference =
                mDatabase.getReference().child("Visual Events").child("Heading");
        //getReference("Visual Events");
        // Add another
        mDatabaseReference.setValue("Playtime");
        // Destroy activity calling method, return to previous activity
        activity.finish();
    }
    /* END */

    /* Methods used only in this class */
    // Method to generate Toast messages
    public static void makeToast(Context mContext, String message) {
        Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}