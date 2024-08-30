package com.guruvardaan.ghargharsurvey.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "trip.db";
    public static final String CONTACTS_TABLE_NAME = "visit";
    public static final String id = "id";
    public static final String TRIP_ID = "trip_id";
    public static final String TRIP_LAT = "trip_lat";
    public static final String TRIP_LONG = "trip_long";
    public static final String TRIP_STATUS = "trip_status";
    public static final String TRIP_SEND = "trip_send";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table visit " +
                        "(id integer primary key, trip_id text,trip_lat text,trip_long text,trip_status text, trip_send text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS visit");
        onCreate(db);
    }

    public boolean insertTrip(String trip_id, String trip_lat, String trip_long, String trip_status, String trip_send) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TRIP_ID, trip_id);
        contentValues.put(TRIP_LAT, trip_lat);
        contentValues.put(TRIP_LONG, trip_long);
        contentValues.put(TRIP_STATUS, trip_status);
        contentValues.put(TRIP_SEND, trip_send);
        db.insert("visit", null, contentValues);
        return true;
    }


    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateTrip(String id, String trip_id, String trip_lat, String trip_long, String trip_status, String trip_send) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("trip_id", trip_id);
        contentValues.put("trip_lat", trip_lat);
        contentValues.put("trip_long", trip_long);
        contentValues.put("trip_status", trip_status);
        contentValues.put("trip_send", trip_send);
        db.update("visit", contentValues, "id = ? ", new String[]{id});
        return true;
    }

    public Integer deleteRow(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("visit",
                "trip_id = ? ",
                new String[]{id});
    }

    @SuppressLint("Range")
    public ArrayList<String> getAllLatLong(String tid) {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from visit where trip_id=" + tid, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(TRIP_LAT)) + "," + res.getString(res.getColumnIndex(TRIP_LONG)));
            res.moveToNext();
        }
        return array_list;
    }
}