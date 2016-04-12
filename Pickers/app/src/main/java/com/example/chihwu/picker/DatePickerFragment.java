package com.example.chihwu.picker;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;
import android.app.Dialog;
import java.util.Calendar;
import android.util.Log;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private Button BODdatePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        BODdatePicker = (Button)getActivity().findViewById(R.id.dob_btn);

        // Create a new instance of DatePickerDialog and return it
        /* Important:  here we use getContext() rather than getApplicationContext() as mentioned in the documents */
        return new DatePickerDialog(getContext() , this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.i("TIME", "Time Info");

        String dob = Integer.toString(day)+"/"+Integer.toString(month)+"/"+Integer.toString(year);
        BODdatePicker.setText(dob);

    }



}
