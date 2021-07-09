package com.example.twoperfect.database.description;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.twoperfect.MODEL.Description;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.database.ticket.TicketDB;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import java.util.ArrayList;

public class DescriptionDB {
    public MicroOrm microOrm = new MicroOrm();
    private static String[] columns = new String[]{"id", "type", "service", "productDesc"};
    private static final int VERSION = 1;
    private static final String DB_NAME = "twoperfect.db";
    private static final String TABLE_DESCRIPTION = "Description";
    private static final String COL_ID = "id";
    private static final String COL_TYPE = "type";
    private static final String COL_SERVICE = "service";
    private static final String COL_PRODUCTDESC = "productDesc";
    private SQLiteDatabase db;
    private DescriptionSQLite description;

    public DescriptionDB(Context context){
        description = new DescriptionSQLite(context, DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = description.getWritableDatabase();
    }

    public void openForRead(){
        db = description.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    private static class DB_Description{
        @Column(COL_ID)
        public int id;
        @Column(COL_TYPE)
        public String type;
        @Column(COL_SERVICE)
        public String service;
        @Column(COL_PRODUCTDESC)
        public String productDesc;

        public DB_Description() {};

        public DB_Description(int id, String type, String service, String productDesc) {
            super();
            this.id = id;
            this.type = type;
            this.service = service;
            this.productDesc = productDesc;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getProductDesc() {
            return productDesc;
        }

        public void setProductDesc(String productDesc) {
            this.productDesc = productDesc;
        }
    }

    public long insertDescription(Description description){
        ContentValues content = new ContentValues();
        content.put(COL_ID, description.getId());
        content.put(COL_TYPE, description.getType());
        content.put(COL_SERVICE, description.getService());
        content.put(COL_PRODUCTDESC, description.getProductDesc());
        return db.insert(TABLE_DESCRIPTION, null, content);
    }

    public ArrayList<Description> getAllDescriptions(){
        ArrayList<Description> listDescription = new ArrayList<>();
        Cursor cursor = db.query(TABLE_DESCRIPTION, columns, null, null, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return listDescription;
        }
        while(cursor.moveToNext()){
            DB_Description dbt = microOrm.fromCursor(cursor, DB_Description.class);
            Description description = new Description(dbt.id, dbt.type, dbt.service, dbt.productDesc);
            listDescription.add(description);
        }
        cursor.close();
        return listDescription;
    }

    public Description getDescriptionById(int id){
        Cursor c = db.query(TABLE_DESCRIPTION, columns, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if(c != null){
            c.moveToFirst();
            DB_Description dbt = microOrm.fromCursor(c, DB_Description.class);
            Description description = new Description(dbt.id, dbt.type, dbt.service, dbt.productDesc);
            return description;
        }
        c.close();
        return null;
    }
}
