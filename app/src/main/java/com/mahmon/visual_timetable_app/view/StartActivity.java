package com.mahmon.visual_timetable_app.view;

import android.os.Bundle;

import com.mahmon.visual_timetable_app.BaseActivity;
import com.mahmon.visual_timetable_app.R;

// Class for Login screen
public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link this activity to the relevant XML layout
        setContentView(R.layout.activity_start);
        // Set bottom menu icons for this context (remove unwanted)
        getToolBarBottom().getMenu().removeItem(R.id.btn_exit_app);
        getToolBarBottom().getMenu().removeItem(R.id.btn_return_login);
        getToolBarBottom().getMenu().removeItem(R.id.btn_zoom_out);
        getToolBarBottom().getMenu().removeItem(R.id.btn_zoom_in);
        getToolBarBottom().getMenu().removeItem(R.id.btn_add_event);
        getToolBarBottom().getMenu().removeItem(R.id.btn_save_event);
        // Animation override:
        overridePendingTransition(R.anim.slide_in, R.anim.shrink_out);
    }

}