package com.mahmon.visual_timetable_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;


public class SelectThemeDialogFragment extends BaseActivity {

    // Variables used for saving and retrieving preferences
    private Context mContext;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Assign preference variables and create editor
        mContext = this;
        mPrefs = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();

        // Use the Builder class ton construct dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Build the dialog and set title
        builder.setTitle(R.string.txt_select_theme);
        // Add array of theme names to dialog
        builder.setItems(R.array.theme_names, new DialogInterface.OnClickListener() {
            // Add listener to the list, return item index in 'which'
            public void onClick(DialogInterface dialog, int which) {
                // Use editor to store selection as string
                mEditor.putString(SELECTED_THEME, toggleTheme(which));
                mEditor.commit();
            }
        });
        // Create the AlertDialog object and return it
        builder.create();
    }

    public String toggleTheme(int which) {
        // User clicked toggle_theme_button
        switch (which) {
            // User clicked Snow Storm
            case 0:
                return "snow_storm";
            // User clicked Starry Night
            case 1:
                return "starry_night";
            // User clicked Bumble Bee
            case 2:
                return "bumble_bee";
            // User clicked Lady Bug
            case 3:
                return "lady_bug";
            default:
                return "ERROR";
                //
        }
    }

}