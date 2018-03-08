package com.mahmon.visual_timetable_app.Activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import com.mahmon.visual_timetable_app.R;

public class EditEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        // Animation override:
        // Go_in for this activity, go_out for previous activity
        overridePendingTransition(R.anim.go_in, R.anim.go_out);
        // Setup action bars
        showTopActionBar();
        showBottomActionBar();
    }

    // Implement the default options menu
    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        // Inflate the top_action_bar_menu onto top_action_bar
        MenuInflater inflater = getMenuInflater();
        // Set top_action_bar_menu as default options menu
        inflater.inflate(R.menu.top_action_bar_menu, topMenu);
        return true;
    }

    // Set method calls for items clicked in top_action_bar_menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Set method calls for items clicked in top_action_bar_menu
        topActionBarMethods(item);
        // Invoke the superclass to handle unrecognised user action.
        return super.onOptionsItemSelected(item);
    }

    // Animation override for the default back button:
    // Back_in for this activity, back_out for previous activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);
    }

    /* Action Bars Set Up*/
    // Initiate topActionBar
    public void showTopActionBar() {
        // Implement top_action_bar as default action bar for this activity
        Toolbar topActionBar = findViewById(R.id.top_action_bar);
        setSupportActionBar(topActionBar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Disable the Up button
        actionBar.setDisplayHomeAsUpEnabled(false);
    }
    // Initiate bottomActionBar
    public void showBottomActionBar() {
        // Add the bottom action bar and inflate the menu
        Toolbar bottomActionBar = findViewById(R.id.bottom_action_bar);
        bottomActionBar.inflateMenu(R.menu.bottom_edit_bar_menu);
        bottomActionBarMethods(bottomActionBar);
    }

    /* Action Bars Methods*/
    // Set method calls for topActionBar
    public boolean topActionBarMethods(MenuItem item) {
        // Switch statement to manage menu user clicks
        switch (item.getItemId()) {
            // User clicked toggle_theme_button
            case R.id.btn_toggle_theme:
                // Display toast message to confirm click
                Toast toast = Toast
                        .makeText(
                                this,
                                "You Clicked Toggle Theme",
                                Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return true;
            default:
                // Invoke the superclass to handle unrecognised user action.
                return super.onOptionsItemSelected(item);
        }
    }

    // Set method calls for bottomActionBar
    public void bottomActionBarMethods(Toolbar bottomActionBar) {
        // Create listeners for bottom_action_bar_menu
        bottomActionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // User clicked btn_save_edit_event
                    case R.id.btn_save_edited_event:
                        // Destroy activity calling method, return to previous activity
                        finish();
                        // Animation override:
                        // Back_out for this activity, back_in for previous activity
                        overridePendingTransition(R.anim.back_in, R.anim.back_out);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

}