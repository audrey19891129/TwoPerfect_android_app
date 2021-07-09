package com.example.twoperfect.database.address;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AddressSQLite extends SQLiteOpenHelper {
    private static final String TABLE_ADDRESS = "Address";
    private static final String COL_ID = "id";
    private static final String COL_CLIENT_ID = "clientId";
    private static final String COL_CIVICNUMBER = "civicnumber";
    private static final String COL_APPARTMENT = "appartment";
    private static final String COL_STREET = "street";
    private static final String COL_CITY = "city";
    private static final String COL_PROVINCE = "province";
    private static final String COL_COUNTRY = "country";
    private static final String COL_ZIPCODE = "zipcode";

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS " + TABLE_ADDRESS + "(" +
            COL_ID + " INTEGER NOT NULL, " +
            COL_CLIENT_ID + " INTEGER NOT NULL, " +
            COL_CIVICNUMBER + " INTERGER NOT NULL, " +
            COL_APPARTMENT + " VARCHAR NOT NULL, " +
            COL_STREET + " VARCHAR NOT NULL, " +
            COL_CITY + " VARCHAR NOT NULL, " +
            COL_PROVINCE + " VARCHAR NOT NULL, " +
            COL_COUNTRY + " VARCHAR NOT NULL, " +
            COL_ZIPCODE + " VARCHAR NOT NULL);";

    public AddressSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        onCreate(db);
    }
}
