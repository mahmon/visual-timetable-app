package com.mahmon.visual_timetable_app.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;

// Class for Login screen
public class StartActivity extends BaseActivity {

    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link this activity to the relevant XML layout
        setContentView(R.layout.activity_start);

        // Theme switch...
        mSwitch = findViewById(R.id.switch_theme);
        // Listener
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                toggleTheme();
            }
        });

        // Set bottom menu icons for this context (remove unwanted)
        getToolBarBottom().getMenu().removeItem(R.id.btn_exit_app);
        getToolBarBottom().getMenu().removeItem(R.id.btn_return_login);
        getToolBarBottom().getMenu().removeItem(R.id.btn_zoom_out);
        getToolBarBottom().getMenu().removeItem(R.id.btn_zoom_in);
        getToolBarBottom().getMenu().removeItem(R.id.btn_add_event);
        getToolBarBottom().getMenu().removeItem(R.id.btn_cancel_save);
        getToolBarBottom().getMenu().removeItem(R.id.btn_save_event);
        getToolBarBottom().getMenu().removeItem(R.id.btn_delete_event);
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.shrink_out);
    }

    // Implement the default options menu
    @Override
    public boolean onCreateOptionsMenu(Menu topMenu) {
        // Instantiate menu inflater object
        MenuInflater inflater = getMenuInflater();
        // Set menu_tool_bar_top as default options menu
        inflater.inflate(R.menu.menu_tool_bar_top, topMenu);
        return true;
    }

    // Set method calls for default option menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Attach topToolBarMethods to default menu
        toolBarMethodsTop(item);
        // Invoke the superclass to handle unrecognised user action.
        return super.onOptionsItemSelected(item);
    }

    // TODO create Login screen and methods
}
