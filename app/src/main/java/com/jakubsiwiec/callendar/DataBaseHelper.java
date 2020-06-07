package com.jakubsiwiec.callendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Time;
import java.util.Date;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private  static final String TAG = "DataBaseHelper";

    private static final String TABLE_NAME = "event_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "name";
    private static final String COL3 = "details";
    private static final String COL4 = "location";
    private static final String COL5 = "repeat";
    private static final String COL6 = "how_often_to_repeat";
    private static final String COL7 = "date";
    private static final String COL8 = "start_time";
    private static final String COL9 = "stop_time";
    private static final String COL10 = "when_to_remind";

    public DataBaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, " + COL3 + " TEXT, "
                + COL4 + " TEXT, " + COL5 + " BOOLEAN, " + COL6 + " TINYINT, " + COL7 + " DATE, " + COL8 + " TIME, " + COL9 + " TIME, " + COL10 + " BYTE);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(String name, String details, String location, boolean repeat, int how_often_to_repeat, Date date, Time start_time, Time stop_time, byte when_to_remind){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL2, name);
        contentValues.put(COL3, details);
        contentValues.put(COL4, location);
        contentValues.put(COL5, repeat);
        contentValues.put(COL6, how_often_to_repeat);
        contentValues.put(COL7, String.valueOf(date));
        contentValues.put(COL8, String.valueOf(start_time));
        contentValues.put(COL9, String.valueOf(stop_time));
        contentValues.put(COL10, when_to_remind);

        Log.d(TAG, "add data: adding: "  + name + ", " + details + ", " + location +
                "\nRepeat: " + repeat + ", how often repeat:" + how_often_to_repeat  +
                "\n " + date  + ", Start: " + start_time  + ", Stop:" + stop_time + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if something was inserted incorrectly
        if (result == -1) return false;
        else return true;

    }
}
