package com.example.twoperfect.database.Holiday;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HolidaySQLite extends SQLiteOpenHelper {

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS  'Holiday' (" +
            "'id' INTEGER NOT NULL, " +
            "'employeeId'  INTEGER NOT NULL, " +
            "'startDate'  DATE NOT NULL, " +
            "'endDate'  DATE NOT NULL ); ";

    public HolidaySQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'Holiday'");
        onCreate(db);
    }
}
