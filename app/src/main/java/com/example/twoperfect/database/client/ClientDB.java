package com.example.twoperfect.database.client;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.twoperfect.MODEL.Client;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.database.intervention.InterventionSQLite;
import com.example.twoperfect.database.ticket.TicketDB;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;

public class ClientDB {
    public MicroOrm microOrm = new MicroOrm();
    private static String[] columns = new String[]{"id", "lastname", "firstname", "phone", "email"};
    private SQLiteDatabase db;
    private ClientSQLite client;
    private static final int VERSION = 1;
    private static final String DB_NAME = "twoperfect.db";
    private static final String TABLE_CLIENT = "Client";
    private static final String COL_ID = "id";
    private static final String COL_LASTNAME = "lastname";
    private static final String COL_FIRSTNAME = "firstname";
    private static final String COL_PHONE = "phone";
    private static final String COL_EMAIL = "email";

    public ClientDB(Context context){
        client = new ClientSQLite(context, DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = client.getWritableDatabase();
    }

    public void openForRead(){
        db = client.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public long insertClient(Client client){
        ContentValues content = new ContentValues();
        content.put(COL_ID, client.getId());
        content.put(COL_LASTNAME, client.getLastname());
        content.put(COL_FIRSTNAME, client.getFirstname());
        content.put(COL_PHONE, client.getPhone());
        content.put(COL_EMAIL, client.getEmail());
        return db.insert(TABLE_CLIENT, null, content);
    }

    private static class DB_Client{
        @Column(COL_ID)
        int id;
        @Column(COL_LASTNAME)
        public String lastname;
        @Column(COL_FIRSTNAME)
        public String firstname;
        @Column(COL_PHONE)
        public String phone;
        @Column(COL_EMAIL)
        public String email;

        public DB_Client() {}

        public DB_Client(int id, String lastname, String firstname, String phone, String email) {
            super();
            this.id = id;
            this.lastname = lastname;
            this.firstname = firstname;
            this.phone = phone;
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }


    public ArrayList<Client> getAllClients(){
        ArrayList<Client> listClient = new ArrayList<>();
        Cursor cursor = db.query(TABLE_CLIENT, columns, null, null, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return listClient;
        }
        while(cursor.moveToNext()){
            DB_Client dbc = microOrm.fromCursor(cursor, DB_Client.class);
            Client client = new Client(dbc.id, dbc.lastname, dbc.firstname, dbc.phone, dbc.email);
            listClient.add(client);
        }
        cursor.close();
        return listClient;
    }

    public Client getClientById(int id){

        Cursor c = db.query(TABLE_CLIENT, columns, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if(c != null){
            c.moveToFirst();
            DB_Client dbc = microOrm.fromCursor(c, DB_Client.class);
            Client client = new Client(dbc.id, dbc.lastname, dbc.firstname, dbc.phone, dbc.email);
            return client;
        }
        c.close();
        return null;
    }
}
