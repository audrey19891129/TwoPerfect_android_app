package com.example.twoperfect.database.ticket;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TicketSQLite extends SQLiteOpenHelper {
    private static final String TABLE_TICKET = "Ticket";
    private static final String COL_ID = "id";
    private static final String COL_ADDRESS_ID = "addressId";
    private static final String COL_CLIENT_ID = "clientId";
    private static final String COL_STATUS = "status";
    private static final String COL_DESCRIPTION_ID = "descriptionId";
    private static final String COL_CREATION_DATE = "creationDate";
    private static final String COL_CLOSING_DATE = "closingDate";

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS " + TABLE_TICKET + "(" +
            COL_ID + " INTEGER NOT NULL, " +
            COL_ADDRESS_ID + " INTEGER NOT NULL, " +
            COL_CLIENT_ID + " INTEGER NOT NULL, " +
            COL_STATUS + " VARCHAR NOT NULL, " +
            COL_DESCRIPTION_ID + " INTEGER NOT NULL, " +
            COL_CREATION_DATE + " VARCHAR NOT NULL, " +
            COL_CLOSING_DATE + " VARCHAR NOT NULL);";



    public TicketSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET);
        onCreate(db);
    }
}
