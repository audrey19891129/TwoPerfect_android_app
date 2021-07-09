package com.example.twoperfect.database.ticket;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.twoperfect.MODEL.Address;
import com.example.twoperfect.MODEL.Description;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.Ticket;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;


public class TicketDB {

    public MicroOrm microOrm = new MicroOrm();
    private static String[] columns = new String[]{"id", "addressId", "clientId", "status", "descriptionId", "creationDate", "closingDate"};
    private SQLiteDatabase db;
    private TicketSQLite ticket;
    private static final int VERSION = 1;
    private static final String DB_NAME = "twoperfect.db";
    private static final String TABLE_TICKET = "Ticket";
    private static final String COL_ID = "id";
    private static final String COL_ADDRESS_ID = "addressId";
    private static final String COL_CLIENT_ID = "clientId";
    private static final String COL_STATUS = "status";
    private static final String COL_DESCRIPTION_ID = "descriptionId";
    private static final String COL_CREATION_DATE = "creationDate";
    private static final String COL_CLOSING_DATE = "closingDate";


    public TicketDB(Context context){
        ticket = new TicketSQLite(context, DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = ticket.getWritableDatabase();
    }

    public void openForRead(){
        db = ticket.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    private static class DB_Ticket {
        @Column(COL_ID)
        public int id;

        @Column(COL_ADDRESS_ID)
        public int addressId;

        @Column(COL_CLIENT_ID)
        public int clientId;

        @Column(COL_STATUS)
        public String status;

        @Column(COL_DESCRIPTION_ID)
        public int descriptionId;

        @Column(COL_CREATION_DATE)
        public String creationDate;

        @Column(COL_CLOSING_DATE)
        public String closingDate;

        public DB_Ticket() {};

        public DB_Ticket(int id, int addressId, int clientId, String status, int descriptionId, String creationDate, String closingDate) {
            super();
            this.id = id;
            this.addressId = addressId;
            this.clientId = clientId;
            this.status = status;
            this.descriptionId = descriptionId;
            this.creationDate = creationDate;
            this.closingDate = closingDate;
        }

        public int getAddressId() {
            return addressId;
        }

        public void setAddressId(int addressId) {
            this.addressId = addressId;
        }

        public int getDescriptionId() {
            return descriptionId;
        }

        public void setDescriptionId(int descriptionId) {
            this.descriptionId = descriptionId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getClientId() {
            return clientId;
        }

        public void setClientId(int clientId) {
            this.clientId = clientId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        public String getClosingDate() {
            return closingDate;
        }

        public void setClosingDate(String closingDate) {
            this.closingDate = closingDate;
        }
    }

    public long insertTicket(Ticket ticket){
        ContentValues content = new ContentValues();
        content.put(COL_ID, ticket.getId());
        content.put(COL_ADDRESS_ID, ticket.getAddressId());
        content.put(COL_CLIENT_ID, ticket.getClientId());
        content.put(COL_STATUS, ticket.getStatus());
        content.put(COL_DESCRIPTION_ID, ticket.getDescriptionId());
        content.put(COL_CREATION_DATE, ticket.getCreationDate());
        content.put(COL_CLOSING_DATE, ticket.getClosingDate());
        return db.insert(TABLE_TICKET, null, content);
    }

    public ArrayList<Ticket> getAllTickets(){
        ArrayList<Ticket> listTicket = new ArrayList<>();
        Cursor cursor = db.query(TABLE_TICKET, columns, null, null, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return listTicket;
        }
        while(cursor.moveToNext()){
            DB_Ticket dbticket = microOrm.fromCursor(cursor, DB_Ticket.class);
            Ticket ticket = new Ticket(dbticket.id, dbticket.addressId, dbticket.clientId, dbticket.status, dbticket.descriptionId, dbticket.creationDate, dbticket.closingDate);
            listTicket.add(ticket);
        }
        cursor.close();
        return listTicket;
    }

    public Ticket getTicketById(int id){
        Cursor c = db.query(TABLE_TICKET, columns, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if(c != null){
            c.moveToFirst();
            DB_Ticket dbticket = microOrm.fromCursor(c, DB_Ticket.class);
            Ticket ticket = new Ticket(dbticket.id, dbticket.addressId, dbticket.clientId, dbticket.status, dbticket.descriptionId, dbticket.creationDate, dbticket.closingDate);
            return ticket;
        }
        c.close();
        return null;
    }

    public void modify(Ticket t){
        ContentValues cv = new ContentValues();
        cv.put("status", t.status);
        cv.put("closingDate", t.closingDate);
        db.update(TABLE_TICKET, cv, "id=?", new String[]{String.valueOf(t.id)});
    }

}
