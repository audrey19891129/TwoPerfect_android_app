package com.example.twoperfect.database.address;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.twoperfect.MODEL.Address;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.database.ticket.TicketDB;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;

public class AddressDB {

    private static String[] columns = new String[]{"id", "clientId", "civicnumber", "appartment", "street", "city", "province", "country", "zipcode"};
    public MicroOrm microOrm = new MicroOrm();
    private SQLiteDatabase db;
    private AddressSQLite address;
    private static final int VERSION = 1;
    private static final String DB_NAME = "twoperfect.db";
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

    public AddressDB(Context context){
        address = new AddressSQLite(context, DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = address.getWritableDatabase();
    }

    public void openForRead(){
        db = address.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public long insertAddress(Address address){
        ContentValues content = new ContentValues();
        content.put(COL_ID, address.getId());
        content.put(COL_CLIENT_ID, address.getClientId());
        content.put(COL_CIVICNUMBER, address.getCivicnumber());
        content.put(COL_APPARTMENT, address.getAppartment());
        content.put(COL_STREET, address.getStreet());
        content.put(COL_CITY, address.getCity());
        content.put(COL_PROVINCE, address.getProvince());
        content.put(COL_COUNTRY, address.getCountry());
        content.put(COL_ZIPCODE, address.getZipcode());
        return db.insert(TABLE_ADDRESS, null, content);
    }

    private static class DB_Address{
        @Column(COL_ID)
        public int id;
        @Column(COL_CLIENT_ID)
        public int clientId;
        @Column(COL_CIVICNUMBER)
        public int civicnumber;
        @Column(COL_APPARTMENT)
        public String appartment;
        @Column(COL_STREET)
        public String street;
        @Column(COL_CITY)
        public String city;
        @Column(COL_PROVINCE)
        public String province;
        @Column(COL_COUNTRY)
        public String country;
        @Column(COL_ZIPCODE)
        public String zipcode;

        public DB_Address() {};

        public DB_Address(int id, int clientId, int civicnumber, String appartment, String street, String city, String province, String country, String zipcode) {
            super();
            this.id = id;
            this.clientId = clientId;
            this.civicnumber = civicnumber;
            this.appartment = appartment;
            this.street = street;
            this.city = city;
            this.province = province;
            this.country = country;
            this.zipcode = zipcode;
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

        public int getCivicnumber() {
            return civicnumber;
        }

        public void setCivicnumber(int civicnumber) {
            this.civicnumber = civicnumber;
        }

        public String getAppartment() {
            return appartment;
        }

        public void setAppartment(String appartment) {
            this.appartment = appartment;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }
    }


    public ArrayList<Address> getAllAddresses(){
        ArrayList<Address> listAddress = new ArrayList<>();
        Cursor cursor = db.query(TABLE_ADDRESS, columns, null, null, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return listAddress;
        }
        while(cursor.moveToNext()){
            DB_Address dba = microOrm.fromCursor(cursor, DB_Address.class);
            Address address = new Address(dba.id, dba.clientId, dba.civicnumber, dba.appartment, dba.street, dba.city, dba.province, dba.country, dba.zipcode);
            listAddress.add(address);
        }
        cursor.close();
        return listAddress;
    }
    public Address getAddressById(int id){
        Cursor c = db.query(TABLE_ADDRESS, columns, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if(c != null){
            c.moveToFirst();
            DB_Address dba = microOrm.fromCursor(c, DB_Address.class);
            Address address = new Address(dba.id, dba.clientId, dba.civicnumber, dba.appartment, dba.street, dba.city, dba.province, dba.country, dba.zipcode);
            return address;
        }
        c.close();
        return null;
    }

}
