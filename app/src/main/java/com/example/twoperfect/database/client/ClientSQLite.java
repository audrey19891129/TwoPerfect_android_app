package com.example.twoperfect.database.client;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ClientSQLite extends SQLiteOpenHelper {
    private static final String TABLE_CLIENT = "Client";
    private static final String COL_ID = "id";
    private static final String COL_LASTNAME = "lastname";
    private static final String COL_FIRSTNAME = "firstname";
    private static final String COL_PHONE = "phone";
    private static final String COL_EMAIL = "email";

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS " + TABLE_CLIENT + "(" +
            COL_ID + " INTEGER NOT NULL, " +
            COL_LASTNAME + " VARCHAR NOT NULL, " +
            COL_FIRSTNAME + " VARCHAR NOT NULL, " +
            COL_PHONE + " VARCHAR NOT NULL, " +
            COL_EMAIL + " VARCHAR NOT NULL);";


    public ClientSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENT);
        onCreate(db);
    }
}
