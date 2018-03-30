package com.mahmon.visual_timetable_app;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class SelectThemeDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class ton construct dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Build the dialog
        // Set title
        builder.setTitle(R.string.txt_select_theme);
        // Add array of theme names to dialog
        builder.setItems(R.array.theme_names, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                writeToast(which);
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }


    public void writeToast(int which) {
        //
        Toast.makeText(getActivity(), "" + which, Toast.LENGTH_LONG).show();
    }

}