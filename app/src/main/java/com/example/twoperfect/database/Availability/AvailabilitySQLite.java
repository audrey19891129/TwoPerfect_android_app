package com.example.twoperfect.database.Availability;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AvailabilitySQLite extends SQLiteOpenHelper {

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS  'Availability' (" +
            "'id' INTEGER NOT NULL, " +
            "'employeeId'  INTEGER NOT NULL, " +
            "'day'  VARCHAR NOT NULL, " +
            "'startTime'  TIME NOT NULL, " +
            "'endTime'  TIME NOT NULL, " +
            "'startDate'  DATE NOT NULL, " +
            "'endDate'  DATE NOT NULL ); ";

    public AvailabilitySQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'Availability'");
        onCreate(db);
    }
}
