package com.jakubsiwiec.callendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {


    /*
    Variables
     */
    private static final String TAG = "ListDataFragment";
    private DataBaseHelper dataBaseHelper;
    private ListView listViewEvents;


    /*
    Functions
     */
    private void populateListView(){
        Log.d(TAG, "Populate list view: displaying data in the ListView");
        Cursor data = dataBaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //represent date in readable format
            String fullDate = data.getString(6);
            String displayDate = fullDate.substring(0, 10) + " " + fullDate.substring(fullDate.length() - 4);

            //trim seconds
            String fullStartTime = data.getString(7);
            String fullFinishTime = data.getString(8);

            String dispStartTime = fullStartTime.substring(0, 5);
            String dispFinishTime = fullFinishTime.substring(0, 5);

            listData.add(data.getString(0) + ". " + data.getString(1) + "\n " + data.getString(2) + ", Location: " + data.getString(3)
                    + "\nDate:" + displayDate + ",   " + dispStartTime + "-" + dispFinishTime);
            Log.d(TAG, data.getString(1));
        }

        Log.d(TAG, String.valueOf(data.getCount()));

        ListAdapter adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listData);
        listViewEvents.setAdapter(adapter);

    }

    private AlertDialog deleteDialog(Object o){

        String idAndName = o.toString();
        int dotIndex = idAndName.indexOf(".");
        String idText = idAndName.substring(0,dotIndex);
        int id = Integer.parseInt(idText);

        Cursor data = dataBaseHelper.getData();

        data.moveToPosition(id);

        String eventTitle = data.getString(1);

        //represent date in readable format
        String fullDate = data.getString(6);
        String displayDate = fullDate.substring(0, 10) + " " + fullDate.substring(fullDate.length() - 4);

        //trim seconds
        String fullStartTime = data.getString(7);
        String fullFinishTime = data.getString(8);

        String dispStartTime = fullStartTime.substring(0, 5);
        String dispFinishTime = fullFinishTime.substring(0, 5);

        String eventInfo = data.getString(2) + ", Location: " + data.getString(3)
                + "\nDate:" + displayDate + ",   " + dispStartTime + "-" + dispFinishTime;

        AlertDialog.Builder detailsDialogBuilder = new AlertDialog.Builder(getActivity());
        detailsDialogBuilder.setMessage(eventInfo)
                            .setTitle(eventTitle);

        detailsDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        detailsDialogBuilder.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });


        return detailsDialogBuilder.create();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewEvents = (ListView) view.findViewById(R.id.listViewEvents);
        dataBaseHelper = new DataBaseHelper(getContext());

        populateListView();

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = listViewEvents.getItemAtPosition(position);

                AlertDialog deleteDialog = deleteDialog(o);
                deleteDialog.show();
            }
        });


        view.findViewById(R.id.button_add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });


    }
}
