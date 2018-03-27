package com.mahmon.visual_timetable_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    // Method called when user clicks OK in the dialog
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // TODO
        String selectedDate = "" + getDateFromDatePicker(view);
        Toast.makeText(getContext(), selectedDate , Toast.LENGTH_LONG).show();
    }

    // Method to convert the date into a string - YYYYMMDD
    public int getDateFromDatePicker(DatePicker datePicker){

        // Get the various date values from the picker
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        // Add one to month - convert January from 00 to 01
        month = month + 1;
        int year =  datePicker.getYear();

        // Convert all the int values into strings (add leading zero's when required)
        String dd = String.format("%02d", day);
        String mm = String.format("%02d", month);
        String yyyy = "" + year;

        // Concantenate values into one long String
        String dateAsString = yyyy + mm + dd;

        // Convert String back into int
        int dateAsInt = Integer.parseInt(dateAsString);

        // Return the date value as an int
        return dateAsInt;
    }

}