package com.example.twoperfect.database.communication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.twoperfect.MODEL.Communication;

import java.util.ArrayList;

public class CommunicationDB {
    private static final int VERSION = 1;
    private static final String DB_NAME = "twoperfect.db";
    private static final String TABLE_COMMUNICATION = "Communication";

    // ID
    private static final String COL_ID = "id";
    private static final int NUM_COL_ID = 0;

    // TECHNICIAN ID
    private static final String COL_TECHNICIAN_ID = "technicianId";
    private static final int NUM_COL_TECHNICIAN_ID = 1;

    // INTERVENTION ID
    private static final String COL_INTERVENTION_ID = "interventionId";
    private static final int NUM_COL_INTERVENTION_ID = 2;

    // CLIENT ID
    private static final String COL_CLIENT_ID = "clientId";
    private static final int NUM_COL_CLIENT_ID = 3;

    // DATE TIME
    private static final String COL_DATE_TIME = "dateTime";
    private static final int NUM_COL_DATE_TIME = 4;

    // TYPE
    private static final String COL_TYPE = "type";
    private static final int NUM_COL_TYPE = 5;

    private SQLiteDatabase db;
    private CommunicationSQLite communication;

    public CommunicationDB(Context context){
        communication = new CommunicationSQLite(context, DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = communication.getWritableDatabase();
    }

    public void openForRead(){
        db = communication.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public long insertCommunication(Communication com){
        ContentValues content = new ContentValues();
        content.put(COL_ID, com.getId());
        content.put(COL_TECHNICIAN_ID, com.getTechnicianId());
        content.put(COL_INTERVENTION_ID, com.getInterventionId());
        content.put(COL_CLIENT_ID, com.getClientId());
        content.put(COL_DATE_TIME, com.getDateTime());
        content.put(COL_TYPE, com.getType());
        return db.insert(TABLE_COMMUNICATION, null, content);
    }

    public ArrayList<Communication> getAllCommunication(){
        Cursor c = db.query(TABLE_COMMUNICATION, new String[]{
                COL_ID,
                COL_TECHNICIAN_ID,
                COL_INTERVENTION_ID,
                COL_CLIENT_ID,
                COL_DATE_TIME,
                COL_TYPE
        }, null, null, null, null, COL_ID);
        if(c.getCount() == 0){
            c.close();
            return null;
        }
        ArrayList<Communication> listCommunication = new ArrayList<>();
        while(c.moveToNext()){
            Communication com = new Communication();
            com.setId(c.getInt(NUM_COL_ID));
            com.setTechnicianId(c.getInt(NUM_COL_TECHNICIAN_ID));
            com.setInterventionId(c.getInt(NUM_COL_INTERVENTION_ID));
            com.setClientId(c.getInt(NUM_COL_CLIENT_ID));
            com.setDateTime(c.getString(NUM_COL_DATE_TIME));
            com.setType(c.getString(NUM_COL_TYPE));
            listCommunication.add(com);
        }
        c.close();
        return listCommunication;
    }
}
