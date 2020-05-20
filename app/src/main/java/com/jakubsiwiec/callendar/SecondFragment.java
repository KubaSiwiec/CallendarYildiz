package com.jakubsiwiec.callendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


public class SecondFragment<AddReminder> extends Fragment {

    public TextView TVfrom;
    public TextView TVto;

    private int startHour, startMinute, finishHour, finishMinute;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TVfrom = (TextView) view.findViewById(R.id.textViewFrom);
        TVto = (TextView) view.findViewById(R.id.textViewTo);

        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        view.findViewById(R.id.buttonTimeFrom).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String timeChosen = "Starts at: " + selectedHour + ":" + selectedMinute;
                        TVfrom.setText(timeChosen);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Starting Time");
                mTimePicker.show();

            }
        });

        view.findViewById(R.id.buttonTimeTo).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        String timeChosen = "Finishes at: " + selectedHour + ":" + selectedMinute;
                        TVto.setText(timeChosen);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Ending Time");
                mTimePicker.show();

            }
        });

        view.findViewById(R.id.buttonDate).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentDate = Calendar.getInstance();
                int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                int month = mcurrentDate.get(Calendar.MONTH) + 1; //for some reason Calendar.MONTH returns value from 0 to 11
                int year = mcurrentDate.get(Calendar.YEAR);

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        String dateChosen = "Starts at: " + selectedYear + ":" + selectedMonth + ":" + selectedDayOfMonth;
                        Log.i("Date", dateChosen);
                    }
                }, year, month, day);

                mDatePicker.setTitle("Select Date");
                mDatePicker.show();

            }
        });
    }
}
