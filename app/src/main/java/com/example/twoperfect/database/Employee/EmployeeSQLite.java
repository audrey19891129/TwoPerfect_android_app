package com.example.twoperfect.database.Employee;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class EmployeeSQLite extends SQLiteOpenHelper {

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS  'Employee' (" +
            "'id' INTEGER NOT NULL, " +
            "'lastname'  VARCHAR NOT NULL, " +
            "'firstname'  VARCHAR NOT NULL, " +
            "'phone'  VARCHAR NOT NULL, " +
            "'email'  VARCHAR NOT NULL, " +
            "'title'  VARCHAR NOT NULL, " +
            "'username'  VARCHAR NOT NULL, " +
            "'password'  VARCHAR NOT NULL, " +
            "'status'  VARCHAR NOT NULL, " +
            "'photo'  VARCHAR NOT NULL ); ";

    public EmployeeSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS 'Employee'");
        onCreate(db);
    }
}
