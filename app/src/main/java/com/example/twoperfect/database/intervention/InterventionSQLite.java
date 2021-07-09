package com.example.twoperfect.database.intervention;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.twoperfect.database.ticket.TicketSQLite;

public class InterventionSQLite extends SQLiteOpenHelper {
    private static final String TABLE_INTERVENTION = "Intervention";
    private static final String COL_ID = "id";
    private static final String COL_TICKET_ID = "ticketId";
    private static final String COL_TECHNICIAN_ID = "technicianId";
    private static final String COL_DATE = "date";
    private static final String COL_CLIENT_AVAIL_START = "clientAvailStart";
    private static final String COL_CLIENT_AVAIL_END = "clientAvailEnd";
    private static final String COL_PUNCH_IN = "punchIn";
    private static final String COL_PUNCH_OUT = "punchOut";
    private static final String COL_STATUS = "status";
    private static final String COL_NOTE = "note";
    private static final String COL_COMMENT = "comment";
    private static final String COL_COMMUNICATION = "communicationType";

    public static final String CREATE_DB = "CREATE TABLE IF NOT EXISTS " + TABLE_INTERVENTION + "(" +
            COL_ID + " INTEGER NOT NULL, " +
            COL_TICKET_ID + " INTEGER NOT NULL, " +
            COL_TECHNICIAN_ID + " INTEGER NOT NULL, " +
            COL_DATE + " VARCHAR NOT NULL, " +
            COL_CLIENT_AVAIL_START + " VARCHAR NOT NULL, " +
            COL_CLIENT_AVAIL_END + " VARCHAR NOT NULL, " +
            COL_PUNCH_IN + " VARCHAR NOT NULL, " +
            COL_PUNCH_OUT + " VARCHAR NOT NULL, " +
            COL_STATUS + " VARCHAR NOT NULL, " +
            COL_NOTE + " VARCHAR NOT NULL, " +
            COL_COMMENT + " VARCHAR NOT NULL, " +
            COL_COMMUNICATION + " VARCHAR NOT NULL );" ;



    public InterventionSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INTERVENTION);
        onCreate(db);
    }
}
