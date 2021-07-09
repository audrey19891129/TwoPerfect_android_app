package com.example.twoperfect.database.communication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CommunicationSQLite extends SQLiteOpenHelper {
    private static final String TABLE_COMMUNICATION = "Communication";
    private static final String COL_ID = "id";
    private static final String COL_TECHNICIAN_ID = "technicianId";
    private static final String COL_INTERVENTION_ID = "interventionId";
    private static final String COL_CLIENT_ID = "clientId";
    private static final String COL_DATE_TIME = "dateTime";
    private static final String COL_TYPE = "type";

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS " + TABLE_COMMUNICATION + "(" +
            COL_ID + " INTEGER NOT NULL, " +
            COL_TECHNICIAN_ID + " INTEGER NOT NULL, " +
            COL_INTERVENTION_ID + " INTEGER NOT NULL, " +
            COL_CLIENT_ID + " INTEGER NOT NULL, " +
            COL_DATE_TIME + " VARCHAR NOT NULL, " +
            COL_TYPE + " VARCHAR NOT NULL);";

    public CommunicationSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMUNICATION);
        onCreate(db);
    }
}
