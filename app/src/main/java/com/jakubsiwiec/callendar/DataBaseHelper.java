package com.jakubsiwiec.callendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
