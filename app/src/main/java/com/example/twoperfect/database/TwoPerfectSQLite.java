package com.example.twoperfect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.twoperfect.MODEL.Employee;
import com.example.twoperfect.database.Availability.AvailabilitySQLite;
import com.example.twoperfect.database.Date.SessionSQLite;
import com.example.twoperfect.database.Employee.EmployeeSQLite;
import com.example.twoperfect.database.Holiday.HolidaySQLite;
import com.example.twoperfect.database.address.AddressSQLite;
import com.example.twoperfect.database.client.ClientSQLite;
import com.example.twoperfect.database.communication.CommunicationSQLite;
import com.example.twoperfect.database.description.DescriptionSQLite;
import com.example.twoperfect.database.intervention.InterventionSQLite;
import com.example.twoperfect.database.ticket.TicketSQLite;

public class TwoPerfectSQLite extends SQLiteOpenHelper {

    public TwoPerfectSQLite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CommunicationSQLite.CREATE_DB);
        db.execSQL(InterventionSQLite.CREATE_DB);
        db.execSQL(TicketSQLite.CREATE_DB);
        db.execSQL(ClientSQLite.CREATE_DB);
        db.execSQL(AddressSQLite.CREATE_DB);
        db.execSQL(DescriptionSQLite.CREATE_DB);
        db.execSQL(SessionSQLite.CREATE_DB);
        db.execSQL(EmployeeSQLite.CREATE_DB);
        db.execSQL(AvailabilitySQLite.CREATE_DB);
        db.execSQL(HolidaySQLite.CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
