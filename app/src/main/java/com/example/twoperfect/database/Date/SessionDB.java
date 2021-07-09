package com.example.twoperfect.database.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.twoperfect.MODEL.Communication;
import com.example.twoperfect.database.communication.CommunicationSQLite;

import java.util.ArrayList;

public class SessionDB {
    private static final int VERSION = 1;
    private static final String DB_NAME = "twoperfect.db";
    private static final String TABLE_SESSION = "Session";

    private static final String COL_DATE = "date";
    private static final int NUM_COL_ID = 0;

    private SQLiteDatabase db;
    private SessionSQLite session;

    public SessionDB(Context context){
        session = new SessionSQLite(context, DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = session.getWritableDatabase();
    }

    public void openForRead(){
        db = session.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public long insertSession(String date){
        ContentValues content = new ContentValues();
        content.put(COL_DATE, date);
        return db.insert(TABLE_SESSION, null, content);
    }

    public String getSession(){
        Cursor c = db.query(TABLE_SESSION, new String[]{COL_DATE}, null, null, null, null, null);
        String session = null;
        if(c.getCount() == 0){
            c.close();
            return null;
        }
        while(c.moveToNext()){
            session = c.getString(NUM_COL_ID);
        }
        c.close();
        return session;
    }
}
