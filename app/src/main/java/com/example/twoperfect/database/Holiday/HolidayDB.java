package com.example.twoperfect.database.Holiday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.twoperfect.MODEL.Availability;
import com.example.twoperfect.MODEL.Holiday;
import com.example.twoperfect.database.Availability.AvailabilitySQLite;

import java.util.ArrayList;

public class HolidayDB {
    private SQLiteDatabase db;
    private static final String DB_NAME = "twoperfect.db";
    private HolidaySQLite holidaySQLite;
    private static final int VERSION = 1;

    public HolidayDB(Context context){
        holidaySQLite = new HolidaySQLite(context, DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = holidaySQLite.getWritableDatabase();
    }

    public void openForRead(){
        db = holidaySQLite.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public long insertHoliday(Holiday h){
        ContentValues content = new ContentValues();
        content.put("id", h.getId());
        content.put("employeeId", h.employeeId);
        content.put("startDate", h.startDate);
        content.put("endDate",h.endDate);
        return db.insert("Holiday", null, content);
    }

    public ArrayList<Holiday> getHolidays(){

        Cursor c = db.query("Holiday", new String[]{"id", "employeeId", "startDate", "endDate"}, null, null, null, null, null);
        if(c.getCount() == 0){
            c.close();
            return new ArrayList<>();
        }
        ArrayList<Holiday> list = new ArrayList<>();
        while(c.moveToNext()){
            Holiday a = new Holiday();
            a.setId(c.getInt(0));
            a.setEmployeeId(c.getInt(1));
            a.setStartDate(c.getString(2));
            a.setEndDate(c.getString(3));
            list.add(a);
        }
        c.close();
        return list;
    }
}
