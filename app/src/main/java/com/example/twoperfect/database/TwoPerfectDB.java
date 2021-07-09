package com.example.twoperfect.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.twoperfect.MODEL.Address;
import com.example.twoperfect.MODEL.Availability;
import com.example.twoperfect.MODEL.Client;
import com.example.twoperfect.MODEL.Description;
import com.example.twoperfect.MODEL.Holiday;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.SQLite;
import com.example.twoperfect.MODEL.Technician;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.database.Availability.AvailabilityDB;
import com.example.twoperfect.database.Date.SessionDB;
import com.example.twoperfect.database.Employee.EmployeeDB;
import com.example.twoperfect.database.Holiday.HolidayDB;
import com.example.twoperfect.database.address.AddressDB;
import com.example.twoperfect.database.client.ClientDB;
import com.example.twoperfect.database.description.DescriptionDB;
import com.example.twoperfect.database.intervention.InterventionDB;
import com.example.twoperfect.database.ticket.TicketDB;

import java.util.ArrayList;

public class TwoPerfectDB {
    private static final int VERSION = 1;
    private static final String DB_NAME = "twoperfect.db";

    private SQLiteDatabase db;
    private TwoPerfectSQLite twoperfect;

    public TwoPerfectDB(Context context){
        twoperfect = new TwoPerfectSQLite(context, DB_NAME, null, VERSION);
    }
    public void openForWrite(){
        db = twoperfect.getWritableDatabase();
    }

    public void openForRead(){
        db = twoperfect.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public void constructTables(Context context, String now){

        SQLite sqlite = Technician.getTechnician().sqlite;
        HolidayDB holidayDB = new HolidayDB(context);
        AvailabilityDB availabilityDB = new AvailabilityDB(context);
        EmployeeDB employeeDB = new EmployeeDB(context);
        SessionDB sessionDB = new SessionDB(context);
        InterventionDB interventionDB = new InterventionDB(context);
        TicketDB ticketDB = new TicketDB(context);
        ClientDB clientDB = new ClientDB(context);
        AddressDB addressDB = new AddressDB(context);
        DescriptionDB descriptionDB = new DescriptionDB(context);

        //HOLIDAY
        holidayDB.openForWrite();
        holidayDB.getDb().execSQL("DELETE FROM Holiday");
        for(Holiday a : Technician.technician.holidays){
            holidayDB.insertHoliday(a);
        }
        holidayDB.close();

        //AVAILABILITY
        availabilityDB.openForWrite();
        availabilityDB.getDb().execSQL("DELETE FROM Availability");
        for(Availability a : Technician.technician.availability){
            availabilityDB.insertAvailability(a);
        }
        availabilityDB.close();

        //EMPLOYEE
        employeeDB.openForWrite();
        employeeDB.getDb().execSQL("DELETE FROM Employee");
        employeeDB.insertEmployee(Technician.getTechnician());
        employeeDB.close();

        //SESSION
        sessionDB.openForWrite();
        sessionDB.getDb().execSQL("DELETE FROM Session");
        sessionDB.insertSession(now);
        sessionDB.close();

        //INTERVENTIONS
        ArrayList<Intervention> interventions = sqlite.getInterventions();
        interventionDB.openForWrite();
        interventionDB.getDb().execSQL("DELETE FROM Intervention");
        for (Intervention i : interventions) {
            interventionDB.insertIntervention(i);
        }
        interventionDB.close();

        //TICKETS
        ArrayList<Ticket> tickets = sqlite.getTickets();
        ticketDB.openForWrite();
        ticketDB.getDb().execSQL("DELETE FROM Ticket");
        for (Ticket t : tickets) {
            ticketDB.insertTicket(t);
        }
        ticketDB.close();

        //CLIENTS
        ArrayList<Client> clients = sqlite.getClients();
        clientDB.openForWrite();
        clientDB.getDb().execSQL("DELETE FROM Client");
        for (Client c : clients) {
            clientDB.insertClient(c);
        }
        clientDB.close();

        //ADDRESSES
        ArrayList<Address> addresses = sqlite.getAddresses();
        addressDB.openForWrite();
        addressDB.getDb().execSQL("DELETE FROM Address");
        for (Address a : addresses) {
            addressDB.insertAddress(a);
        }
        addressDB.close();

        //DESCRIPTIONS
        ArrayList<Description> descriptions = sqlite.getDescriptions();
        descriptionDB.openForWrite();
        descriptionDB.getDb().execSQL("DELETE FROM Description");
        for (Description d : descriptions) {
            descriptionDB.insertDescription(d);
        }
        descriptionDB.close();
    }

}
