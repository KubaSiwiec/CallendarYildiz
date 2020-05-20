package com.jakubsiwiec.callendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


public class SecondFragment<AddReminder> extends Fragment {

    /*
    Variables
     */
    public TextView TVfrom;
    public TextView TVto;
    public Button bDate;
    public Switch swRepeat;
    public RadioGroup rgRepeat;
    public EditText editTextName;

    //define date variables and set them to -1 before user sets custom time
    private int finishHour = -1, finishMinute = -1, evDay = -1, evMonth = -1, evYear = -1, startMinute = -1, startHour = -1;





    /*
    Functions
     */


    //function to check if all of the datetime variables were set by user
    boolean checkIfDateTimeNotSet(int year, int month, int day, int startHour, int startMinute, int finishHour, int finishMinute){
        int[] vars = new int[]{year, month, day, startHour, startMinute, finishHour, finishMinute};
        for (int var : vars){
            if (var == -1) return true;
        }
        return false;
    }

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
        bDate = (Button) view.findViewById(R.id.buttonDate);
        swRepeat = (Switch) view.findViewById(R.id.switchRepeat);
        rgRepeat = (RadioGroup) view.findViewById(R.id.RBGroupRepeat);
        editTextName = (EditText) view.findViewById(R.id.editTextName);


        /*
        Save event function
         */
        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String evName = editTextName.getText().toString();

                if (checkIfDateTimeNotSet(evYear, evMonth, evDay, startHour, startMinute, finishHour, finishMinute) || evName.equals("")){
                    Toast.makeText(getContext(), "Before saving event set it's name, date, and time", Toast.LENGTH_LONG).show();
                }
                else{

                    /*
                    Zapis do bazy danych
                     */

                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                }


            }
        });


        /*
        Set start time function
         */
        view.findViewById(R.id.buttonTimeFrom).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        startHour = selectedHour;
                        startMinute = selectedMinute;
                        //print info under the button
                        String timeChosen = "Starts at: " + selectedHour + ":" + selectedMinute;
                        TVfrom.setText(timeChosen);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Starting Time");
                mTimePicker.show();

            }
        });

        /*
        Set finish time function
         */
        view.findViewById(R.id.buttonTimeTo).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //set variables to pass them to database
                        finishHour = selectedHour;
                        finishMinute = selectedMinute;
                        //print time under the button
                        String timeChosen = "Finishes at: " + selectedHour + ":" + selectedMinute;
                        TVto.setText(timeChosen);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Ending Time");
                mTimePicker.show();

            }
        });

        /*
        Set date function
         */
        bDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentDate = Calendar.getInstance();
                int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                int month = mcurrentDate.get(Calendar.MONTH);
                int year = mcurrentDate.get(Calendar.YEAR);

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                        //set variables to to add them to database
                        evDay = selectedDayOfMonth;
                        evMonth = selectedMonth + 1; //for some reason Calendar.MONTH returns value from 0 to 11
                        evYear = selectedYear;
                        //print chosen date on the button
                        String dateChosen = "Date: " + selectedYear + ":" + selectedMonth + ":" + selectedDayOfMonth;
                        bDate.setText(dateChosen);
                    }
                }, year, month, day);

                mDatePicker.setTitle("Select Date");
                mDatePicker.show();

            }
        });

        /*
        Enable or disable repeat
         */
        swRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //if repeat is not checked
                if (!swRepeat.isChecked()){
                    for (int i = 0; i < rgRepeat.getChildCount(); i++){
                        rgRepeat.getChildAt(i).setEnabled(false);
                    }
                } else {
                    for (int i = 0; i < rgRepeat.getChildCount(); i++){
                        rgRepeat.getChildAt(i).setEnabled(true);
                    }
                }

            }
        });
    }
}
