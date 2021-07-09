package com.example.twoperfect.database.Availability;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.twoperfect.MODEL.Availability;
import com.example.twoperfect.MODEL.Communication;
import com.example.twoperfect.MODEL.Employee;
import com.example.twoperfect.database.Employee.EmployeeSQLite;

import java.util.ArrayList;

public class AvailabilityDB {
    private SQLiteDatabase db;
    private static final String DB_NAME = "twoperfect.db";
    private AvailabilitySQLite availabilitySQLite;
    private static final int VERSION = 1;

    public AvailabilityDB(Context context){
        availabilitySQLite = new AvailabilitySQLite(context, DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = availabilitySQLite.getWritableDatabase();
    }

    public void openForRead(){
        db = availabilitySQLite.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public long insertAvailability(Availability a){
        ContentValues content = new ContentValues();
        content.put("id", a.getId());
        content.put("employeeId", a.employeeId);
        content.put("day", a.day);
        content.put("startTime", a.startTime);
        content.put("endTime", a.endTime);
        content.put("startDate", a.startDate);
        content.put("endDate",a.endDate);
        return db.insert("Availability", null, content);
    }

    public ArrayList<Availability> getAvailabilities(){

        Cursor c = db.query("Availability", new String[]{"id", "employeeId", "day", "startTime", "endTime", "startDate", "endDate"}, null, null, null, null, null);
        if(c.getCount() == 0){
            c.close();
            return new ArrayList<>();
        }
        ArrayList<Availability> list = new ArrayList<>();
        while(c.moveToNext()){
            Availability a = new Availability();
            a.setId(c.getInt(0));
            a.setEmployeeId(c.getInt(1));
            a.setDay(c.getString(2));
            a.setStartTime(c.getString(3));
            a.setEndTime(c.getString(4));
            a.setStartDate(c.getString(5));
            a.setEndDate(c.getString(6));
            list.add(a);
        }
        c.close();
        return list;
    }
}
