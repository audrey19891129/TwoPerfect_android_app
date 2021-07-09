package com.example.twoperfect.database.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SessionSQLite extends SQLiteOpenHelper{
    private static final String TABLE_SESSION= "Session";
    private static final String COL_DATE = "date";
    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS " + TABLE_SESSION + "(" + COL_DATE + " DATE NOT NULL );";


    public SessionSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSION);
        onCreate(db);
    }
}
