package com.example.twoperfect.database.intervention;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.twoperfect.MODEL.Description;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.database.description.DescriptionDB;

import org.chalup.microorm.MicroOrm;
import org.chalup.microorm.annotations.Column;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class InterventionDB {
    public MicroOrm microOrm = new MicroOrm();
    private static String[] columns = new String[]{"id", "ticketId", "technicianId", "date", "clientAvailStart", "clientAvailEnd","punchIn","punchOut","status","note", "comment", "communicationType"};
    private static final int VERSION = 1;
    private static final String DB_NAME = "twoperfect.db";
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
    private SQLiteDatabase db;
    private InterventionSQLite intervention;

    public InterventionDB(Context context){
        intervention = new InterventionSQLite(context, DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = intervention.getWritableDatabase();
    }

    public void openForRead(){
        db = intervention.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    private static class DB_Intervention{
        @Column(COL_ID)
        public int id;
        @Column(COL_TICKET_ID)
        public int ticketId;
        @Column(COL_TECHNICIAN_ID)
        public int technicianId;
        @Column(COL_DATE)
        public String date;
        @Column(COL_CLIENT_AVAIL_START)
        public String clientAvailStart;
        @Column(COL_CLIENT_AVAIL_END)
        public String clientAvailEnd;
        @Column(COL_PUNCH_IN)
        public String punchIn;
        @Column(COL_PUNCH_OUT)
        public String punchOut;
        @Column(COL_NOTE)
        public String note;
        @Column(COL_STATUS)
        public String status;
        @Column(COL_COMMENT)
        public String comment;
        @Column(COL_COMMUNICATION)
        public String communicationType;

        public DB_Intervention() {}

        public DB_Intervention(int id, int ticketId, int technicianId, String date, String clientAvailStart,
                            String clientAvailEnd, String punchIn, String punchOut, String status, String note, String comment, String communicationType) {
            super();
            this.id = id;
            this.ticketId = ticketId;
            this.technicianId = technicianId;
            this.date = date;
            this.clientAvailStart = clientAvailStart;
            this.clientAvailEnd = clientAvailEnd;
            this.punchIn = punchIn;
            this.punchOut = punchOut;
            this.status = status;
            this.note = note;
            this.comment = comment;
            this.communicationType = communicationType;
        }

        public String getCommunicationType() { return communicationType;}

        public void setCommunicationType(String communicationType) { this.communicationType = communicationType;}

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getClientAvailStart() {
            return clientAvailStart;
        }

        public void setClientAvailStart(String clientAvailStart) {this.clientAvailStart = clientAvailStart;}

        public String getClientAvailEnd() {
            return clientAvailEnd;
        }

        public void setClientAvailEnd(String clientAvailEnd) {this.clientAvailEnd = clientAvailEnd; }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTicketId() {
            return ticketId;
        }

        public void setTicketId(int ticketId) {
            this.ticketId = ticketId;
        }

        public int getTechnicianId() {
            return technicianId;
        }

        public void setTechnicianId(int technicianId) {
            this.technicianId = technicianId;
        }

        public String getPunchIn() {
            return punchIn;
        }

        public void setPunchIn(String punchIn) {
            this.punchIn = punchIn;
        }

        public String getPunchOut() {
            return punchOut;
        }

        public void setPunchOut(String punchOut) {
            this.punchOut = punchOut;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    public long insertIntervention(Intervention Intervention){
        ContentValues content = new ContentValues();
        content.put(COL_ID, Intervention.getId());
        content.put(COL_TICKET_ID, Intervention.getTicketId());
        content.put(COL_TECHNICIAN_ID, Intervention.getTechnicianId());
        content.put(COL_DATE, Intervention.getDate());
        content.put(COL_CLIENT_AVAIL_START, Intervention.getClientAvailStart());
        content.put(COL_CLIENT_AVAIL_END, Intervention.getClientAvailEnd());
        content.put(COL_PUNCH_IN, Intervention.getPunchIn());
        content.put(COL_PUNCH_OUT, Intervention.getPunchOut());
        content.put(COL_STATUS, Intervention.getStatus());
        content.put(COL_NOTE, Intervention.getNote());
        content.put(COL_COMMENT, Intervention.getComment());
        content.put(COL_COMMUNICATION, Intervention.getCommunicationType());
        return db.insert(TABLE_INTERVENTION, null, content);
    }

    public ArrayList<Intervention> getAllInterventions(){
        ArrayList<Intervention> listIntervention = new ArrayList<>();
        Cursor cursor = db.query(TABLE_INTERVENTION, columns, "status !=?", new String[]{"Cancellé"}, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return listIntervention;
        }
        while(cursor.moveToNext()){
            DB_Intervention dbi = microOrm.fromCursor(cursor, DB_Intervention.class);
            Intervention i = new Intervention(dbi.id, dbi.ticketId, dbi.technicianId, dbi.date, dbi.clientAvailStart, dbi.clientAvailEnd, dbi.punchIn, dbi.punchOut, dbi.status, dbi.note, dbi.comment, dbi.communicationType);
            listIntervention.add(i);
        }
        cursor.close();
        return listIntervention;
    }

    public ArrayList<Intervention> getInterventionsByDate(String date){
        ArrayList<Intervention> listIntervention = new ArrayList<>();
        Cursor cursor = db.query(TABLE_INTERVENTION, columns, "date=? and status !=?", new String[]{date, "Cancellé"}, null, null, null);
        if(cursor.getCount() == 0){
            cursor.close();
            return listIntervention;
        }
        while(cursor.moveToNext()){
            DB_Intervention dbi = microOrm.fromCursor(cursor, DB_Intervention.class);
            Intervention i = new Intervention(dbi.id, dbi.ticketId, dbi.technicianId, dbi.date, dbi.clientAvailStart, dbi.clientAvailEnd, dbi.punchIn, dbi.punchOut, dbi.status, dbi.note, dbi.comment, dbi.communicationType);
            listIntervention.add(i);
        }
        cursor.close();
        return listIntervention;
    }

    public ArrayList<Intervention> getTasksInterventions(){
        ArrayList<Intervention> listIntervention = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String today = sdf.format(cal.getTime());
        Cursor cursor = db.query(TABLE_INTERVENTION, columns, "(status=? or status=?) and status !=? and date=?", new String[]{"Assignée", "activated", "Cancellé",  today}, null, null, COL_CLIENT_AVAIL_START + " ASC");
        if(cursor.getCount() == 0){
            cursor.close();
            return listIntervention;
        }
        while(cursor.moveToNext()){
            DB_Intervention dbi = microOrm.fromCursor(cursor, DB_Intervention.class);
            Intervention i = new Intervention(dbi.id, dbi.ticketId, dbi.technicianId, dbi.date, dbi.clientAvailStart, dbi.clientAvailEnd, dbi.punchIn, dbi.punchOut, dbi.status, dbi.note, dbi.comment, dbi.communicationType);
            listIntervention.add(i);
        }
        cursor.close();
        return listIntervention;
    }

    public Intervention getInterventionById(int id){
        Cursor c = db.query(TABLE_INTERVENTION, columns, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if(c != null){
            c.moveToFirst();
            DB_Intervention dbi = microOrm.fromCursor(c, DB_Intervention.class);
            Intervention i = new Intervention(dbi.id, dbi.ticketId, dbi.technicianId, dbi.date, dbi.clientAvailStart, dbi.clientAvailEnd, dbi.punchIn, dbi.punchOut, dbi.status, dbi.note, dbi.comment, dbi.communicationType);
            return i;
        }
        c.close();
        return null;
    }

    public void modify(Intervention i){
        ContentValues cv = new ContentValues();
        cv.put("punchIn", i.punchIn);
        cv.put("punchOut", i.punchOut);
        cv.put("status", i.status);
        cv.put("comment", i.comment);

        db.update(TABLE_INTERVENTION, cv, "id=?", new String[]{String.valueOf(i.id)});
    }
}
