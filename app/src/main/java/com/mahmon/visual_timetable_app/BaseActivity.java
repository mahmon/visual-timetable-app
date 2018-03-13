package com.mahmon.visual_timetable_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmon.visual_timetable_app.model.Event;
import com.mahmon.visual_timetable_app.view.*;

// Class to manage Tool Bars for all activities
public class BaseActivity extends AppCompatActivity {

    // Used to set global database node
    public static final String VISUAL_EVENTS = "Visual Events";
    // View items
    private EditText mEnterEventHeading;
    private String mEventHeading;
    // database variables
    private DatabaseReference mDatabaseRef;
    private String mEventID;
    // Toolbar variables
    private Toolbar mToolBarTop;
    private Toolbar mToolBarBottom;

    // Getter method for passing bottom toolbar
    public Toolbar getToolBarBottom() {
        return mToolBarBottom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Instantiate database reference linked to VISUAL_EVENTS node
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(VISUAL_EVENTS);
    }

    @Override
    // Method called by all Activities that extend this BaseActivity
    public void setContentView(int layoutResID) {
        // Instantiate local view object
        View view = getLayoutInflater().inflate(layoutResID, null);
        // Call configureToolBarTop method, passing in local view
        configureToolbarTop(view);
        // Call configureToolBarBottom method, passing in local view
        configureToolbarBottom(view);
        // Call super setContentView method, passing in local view object
        super.setContentView(view);
    }

    // Configure topToolbar
    private void configureToolbarTop(View view) {
        // Link topToolbar to XML tool_bar_top
        mToolBarTop = view.findViewById(R.id.tool_bar_top);
        // Declare topToolbar as the default ToolBar
        setSupportActionBar(mToolBarTop);
        // Disable default back / home button
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    // Configure bottomToolbar
    private void configureToolbarBottom(View view) {
        // Link bottomToolbar to XML tool_bar_bottom
        mToolBarBottom = view.findViewById(R.id.tool_bar_bottom);
        // Inflate XML menu_tool_bar_bottom to bottomToolbar
        mToolBarBottom.inflateMenu(R.menu.menu_tool_bar_bottom);
        // Link bottomToolBarMethods and bottomToolbar
        toolBarMethodsBottom(mToolBarBottom);
    }

    // Set method calls for toolBarMethodsTop
    public boolean toolBarMethodsTop(MenuItem item) {
        // Switch statement to manage menu user clicks
        switch (item.getItemId()) {
            // User clicked toggle_theme_button
            case R.id.btn_toggle_theme:
                toggleTheme();
                return true;
            default:
                return false;
        }
    }

    // Set method calls for toolBarMethodsBottom
    public void toolBarMethodsBottom(Toolbar bottomToolbar) {
        // Create listeners for all buttons on menu_tool_bar_bottom
        bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked btn_enter_app
                    case R.id.btn_enter_app:
                        enterApp();
                        return true;
                    // User clicked btn_exit_app
                    case R.id.btn_exit_app:
                        exitApp();
                        return true;
                    // User clicked btn_return_login
                    case R.id.btn_return_login:
                        returnLogin();
                        return true;
                    // User clicked btn_zoom_out
                    case R.id.btn_zoom_out:
                        zoomOut();
                        return true;
                    // User clicked btn_zoom_in
                    case R.id.btn_zoom_in:
                        zoomIn();
                        return true;
                    // User clicked btn_add_event
                    case R.id.btn_add_event:
                        addEvent();
                        return true;
                    // User clicked btn_save_event
                    case R.id.btn_save_event:
                        // Call save event method
                        saveEvent();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    /* ToolBar Methods */
    // Called by btn_toggle_theme
    public void toggleTheme() {
        // TODO Create Theme Toggle method
        // Display toast message to confirm click
        Toast.makeText(this,"Theme Toggled", Toast.LENGTH_SHORT).show();
    }

    // Called by btn_enter_app
    public void enterApp() {
        // Instantiate new intent to start DisplayEventsActivity
        Intent intent = new Intent(getBaseContext(), DisplayEventsActivity.class);
        // Start Activity
        startActivity(intent);
    }

    // Called by btn_exit_app
    public void exitApp() {
        // Instantiate new intent to start FinishActivity
        Intent intent = new Intent(getBaseContext(), FinishActivity.class);
        // Start Activity
        startActivity(intent);
    }
    // Called by btn_return_login
    public void returnLogin() {
        // Instantiate new intent to start StartActivity
        Intent intent = new Intent(getBaseContext(), StartActivity.class);
        // Start Activity
        startActivity(intent);
    }

    // Called by btn_zoom_out
    public void zoomOut() {
        // TODO Create Zoom Out method
        // Display toast message to confirm click
        Toast.makeText(getBaseContext(),"Zoom Out", Toast.LENGTH_SHORT).show();
    }

    // Called by btn_zoom_in
    public void zoomIn() {
        // TODO Create Zoom In method
        // Display toast message to confirm click
        Toast.makeText(getBaseContext(),"Zoom In", Toast.LENGTH_SHORT).show();
    }

    // Called by btn_add_event
    public void addEvent() {
        // Create new intent to start AddEventActivity
        Intent intentAdd = new Intent(getBaseContext(), AddEventActivity.class);
        // Start Activity
        startActivity(intentAdd);
    }

    /* CREATE: Write to database */
    // Save event to database
    private void saveEvent() {
        // Link mEnterEventHeading to txt_enter_event_heading
        mEnterEventHeading = findViewById(R.id.txt_enter_event_heading);
        // Store input from editText test in String mEventHeading
        mEventHeading = mEnterEventHeading.getText().toString().trim();
        // If EditText box is NOT blank...
        if (!TextUtils.isEmpty(mEventHeading)) {
            // Get an auto generated unique ID from Firebase
            mEventID = mDatabaseRef.push().getKey();
            // Instantiate new Event Object, pass in Firebase ID and mEventHeading
            Event event = new Event(mEventID, mEventHeading);
            // Save the Event object to Firebase at the retrieved ID
            mDatabaseRef.child(mEventID).setValue(event);
            // Display Confirmation message
            Toast.makeText(getBaseContext(),
                    "Event saved", Toast.LENGTH_SHORT).show();
            // Create new intent to start a new activity (DisplayEventsActivity)
            final Intent intentSave =
                    new Intent(getBaseContext(), DisplayEventsActivity.class);
            // Pause 5000 milliseconds
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Start activity
                    startActivity(intentSave);
                }
            }, 1000);
        // If EditText box IS blank...
        } else {
            // Prompt user to enter a heading
            Toast.makeText(this,
                    "Please enter a heading", Toast.LENGTH_SHORT).show();
        }
    }

}
