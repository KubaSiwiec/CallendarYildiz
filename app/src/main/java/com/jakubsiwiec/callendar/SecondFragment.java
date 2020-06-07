package com.jakubsiwiec.callendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


public class SecondFragment<AddReminder> extends Fragment {

    /*
    Variables
     */
    private DataBaseHelper dataBaseHelper;

    public TextView TVfrom;
    public TextView TVto;
    public Button bDate;
    public Switch swRepeat;
    public RadioGroup rgRepeat;
    public EditText editTextName;
    public EditText editTextDetails;
    public EditText editTextLocation;
    public RadioButton rbChecked;
    public CheckBox cb5min, cb15min, cb1h, cb6h, cb1d;

    //define date variables and set them to -1 before user sets custom time
    private int finishHour = -1, finishMinute = -1, evDay = -1, evMonth = -1, evYear = -1, startMinute = -1, startHour = -1;

    //declare String variables
    private String evName, evDetails, evLocation;

    //repeat or not bool
    private boolean evRepeat = false;

    //when to repeat
    private int evHowOftenRepeat = 0;

    //when to remind bools
    private boolean remind5min, remind15min, remind1h, remind6h, remind1d;




    /*
    Functions
     */

    //database insertion
    public void addData(String name, String details, String location, boolean repeat, int how_often_to_repeat, Date date, Time start_time, Time stop_time, byte when_to_remind){
        boolean insertData = dataBaseHelper.addData(name, details, location, repeat, how_often_to_repeat, date, start_time, stop_time, when_to_remind);
        if(insertData){
            Toast.makeText(getContext(), "Inserted correctly", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getContext(), "Wrooooong!", Toast.LENGTH_LONG).show();

        }
    }


    //function to check if all of the datetime variables were set by user
    boolean checkIfDateTimeNotSet(int year, int month, int day, int startHour, int startMinute, int finishHour, int finishMinute){
        int[] vars = new int[]{year, month, day, startHour, startMinute, finishHour, finishMinute};
        for (int var : vars){
            if (var == -1) return true;
        }
        return false;
    }

    byte setRemindBits(boolean remind5min, boolean remind15min, boolean remind1h, boolean remind6h, boolean remind1d){
        /*
        The time of reminders will be passed to data base as BYTE type, with specific bits containing
        information about single time before the event
         */
        byte when_to_remind = 0;

        if (remind5min) when_to_remind |= 1 << 0;
        if (remind15min) when_to_remind |= 1 << 1;
        if (remind1h) when_to_remind |= 1 << 2;
        if (remind6h) when_to_remind |= 1 << 3;
        if (remind1d) when_to_remind |= 1 << 4;

        return when_to_remind;
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

        dataBaseHelper = new DataBaseHelper(getContext());

        //View objects definition to access their properties
        TVfrom = (TextView) view.findViewById(R.id.textViewFrom);
        TVto = (TextView) view.findViewById(R.id.textViewTo);
        bDate = (Button) view.findViewById(R.id.buttonDate);
        swRepeat = (Switch) view.findViewById(R.id.switchRepeat);
        rgRepeat = (RadioGroup) view.findViewById(R.id.RBGroupRepeat);
        editTextName = (EditText) view.findViewById(R.id.editTextName);
        editTextDetails = (EditText) view.findViewById(R.id.editTextDetails);
        editTextLocation = (EditText) view.findViewById(R.id.editTextLocation);
        cb5min = (CheckBox) view.findViewById(R.id.checkBox5m);
        cb15min = (CheckBox) view.findViewById(R.id.checkBox15m);
        cb1h = (CheckBox) view.findViewById(R.id.checkBox1h);
        cb6h = (CheckBox) view.findViewById(R.id.checkBox6h);
        cb1d = (CheckBox) view.findViewById(R.id.checkBox1d);




        /*
        Save event function
         */
        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                evName = editTextName.getText().toString();

                if (checkIfDateTimeNotSet(evYear, evMonth, evDay, startHour, startMinute, finishHour, finishMinute) || evName.equals("")){
                    Toast.makeText(getContext(), "Before saving event set it's name, date, and time", Toast.LENGTH_LONG).show();
                }
                else{

                    //Trzeba zapisać zmienne do bazy danych
                    //Inne funkcje w tym fragmencie ogarniają zapis daty i czasu do zmiennych
                    //Są one w zmiennych: evYear, evMonth, evDay, startHour, startMinute, finishHour, finishMinute
                    //Trzeba je tu zqapisać w bazie
                    //Nazwa jest definiowana wyżej w tej funkcji jako evName
                    //Reszta zmiennych: opis, lokację, powtarzanie i remindery będą zdefiniowane tu i później zapisane do bazy

                    //details and location
                    evDetails = editTextDetails.getText().toString();
                    evLocation = editTextLocation.getText().toString();

                    //repeat - is true when user checked repeat switch
                    evRepeat = swRepeat.isChecked();

                    //how often to repeat
                    //tags: 1 for weekly, 2 for monthly, 3 for yearly
                    //iterate through radio buttons to see which one is selected
                    if (evRepeat){
                        Log.i("Selected", String.valueOf(rgRepeat.getCheckedRadioButtonId()));
                        rbChecked = (RadioButton) rgRepeat.findViewById(rgRepeat.getCheckedRadioButtonId());
                        evHowOftenRepeat = Integer.parseInt((String) rbChecked.getTag());
                    }
                    //repeat not selected
                    else evHowOftenRepeat = 0;


                    //when to remind the user - every checkbox is treaten separately
                    //later this datastructure may be changed to array of bools
                    remind5min = cb5min.isChecked();
                    remind15min = cb15min.isChecked();
                    remind1h = cb1h.isChecked();
                    remind6h = cb6h.isChecked();
                    remind1d = cb1d.isChecked();

                    byte evWhenToRemind = setRemindBits(remind5min, remind15min, remind1h, remind6h, remind1d);

                    Date evDate = new GregorianCalendar(evYear, evMonth, evDay).getTime();
                    Time evStartTime = new Time(startHour, startMinute, 0);
                    Time evFinishTime = new Time(finishHour, finishMinute, 0);



                    Log.i("Name", evName);
                    Log.i("Details", evDetails);
                    Log.i("Location", evLocation);
                    Log.i("DateTime", String.valueOf(evYear) + "." + String.valueOf(evMonth) + "." + String.valueOf(evDay) + "  From: " + String.valueOf(startHour) + ":" + String.valueOf(startMinute) + " to: " + String.valueOf(finishHour) + ":" + String.valueOf(finishMinute));
                    Log.i("Repeat", String.valueOf(evRepeat));
                    Log.i("How often to repeat", String.valueOf(evHowOftenRepeat));
                    Log.i("Remind", "5min:" + String.valueOf(remind5min) + ", 15 min:" + String.valueOf(remind15min) + ", 1h: " + String.valueOf(remind1h) + ", 6h: " + String.valueOf(remind6h) + ", 1day: " + String.valueOf(remind1d));
                    Log.i("Remind byte form", String.valueOf(evWhenToRemind));

                    addData(evName, evDetails, evLocation, evRepeat, evHowOftenRepeat, evDate, evStartTime, evFinishTime, evWhenToRemind);


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
