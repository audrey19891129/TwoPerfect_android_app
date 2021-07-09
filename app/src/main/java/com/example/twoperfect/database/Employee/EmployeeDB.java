package com.example.twoperfect.database.Employee;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.twoperfect.MODEL.Employee;

public class EmployeeDB {

    private SQLiteDatabase db;
    private static final String DB_NAME = "twoperfect.db";
    private EmployeeSQLite employeeSQLite;
    private static final int VERSION = 1;

    public EmployeeDB(Context context){
        employeeSQLite = new EmployeeSQLite(context, DB_NAME, null, VERSION);
    }

    public void openForWrite(){
        db = employeeSQLite.getWritableDatabase();
    }

    public void openForRead(){
        db = employeeSQLite.getReadableDatabase();
    }

    public void close(){
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public long insertEmployee(Employee employee){
        Log.e("INSERT ", employee.toString() );
        ContentValues content = new ContentValues();
        content.put("id", employee.getId());
        content.put("lastname", employee.lastname);
        content.put("firstname", employee.firstname);
        content.put("phone", employee.phone);
        content.put("email", employee.email);
        content.put("title", employee.title);
        content.put("username", employee.username);
        content.put("password", employee.password);
        content.put("status", employee.status);
        content.put("photo", employee.photo);
        return db.insert("Employee", null, content);
    }

    public Employee getEmployee(){

        Cursor c = db.query("Employee", new String[]{"id", "lastname", "firstname", "phone", "email", "title", "username", "password", "status", "photo"}, null, null, null, null, null);
        if(c != null && c.moveToFirst()){
            c.moveToFirst();
            Employee emp = new Employee();
            emp.setId(c.getInt(0));
            emp.setLastname(c.getString(1));
            emp.setFirstname(c.getString(2));
            emp.setPhone(c.getString(3));
            emp.setEmail(c.getString(4));
            emp.setTitle(c.getString(5));
            emp.setUsername(c.getString(6));
            emp.setPassword(c.getString(7));
            emp.setStatus(c.getString(8));
            emp.setPhoto(c.getString(9));
            return emp;
        }
        c.close();
        return null;
    }
}
