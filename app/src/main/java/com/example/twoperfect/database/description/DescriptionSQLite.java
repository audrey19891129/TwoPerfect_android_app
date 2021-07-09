package com.example.twoperfect.database.description;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DescriptionSQLite extends SQLiteOpenHelper {
    private static final String TABLE_DESCRIPTION = "Description";
    private static final String COL_ID = "id";
    private static final String COL_TYPE = "type";
    private static final String COL_SERVICE = "service";
    private static final String COL_PRODUCTDESC = "productDesc";

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS " + TABLE_DESCRIPTION + "(" +
            COL_ID + " INTEGER NOT NULL, " +
            COL_TYPE + " VARCHAR NOT NULL, " +
            COL_SERVICE + " VARCHAR NOT NULL, " +
            COL_PRODUCTDESC + " VARCHAR NOT NULL);";


    public DescriptionSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESCRIPTION);
        onCreate(db);
    }
}
