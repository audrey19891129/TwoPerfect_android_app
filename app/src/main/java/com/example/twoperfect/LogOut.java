package com.example.twoperfect;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;
import com.example.twoperfect.CONTROLLER.Request;
import com.example.twoperfect.MODEL.Employee;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.Technician;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.database.intervention.InterventionDB;
import com.example.twoperfect.database.ticket.TicketDB;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;

public class LogOut extends JobService {

    private static String TAG = "LogOut Service";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "job started");
        doBackgroundWork(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "job cancelled before completion");
        jobCancelled = true;
        return true;
    }

    public void doBackgroundWork(JobParameters params){
     new Thread(new Runnable() {
        @Override
        public void run() {

            if(jobCancelled){
                return;
            }
            try {
                Log.e(TAG, "Job Excecuting");
                Boolean bool = true;
                Gson gson = new Gson();
                Employee tech = Technician.getTechnician();
                tech.setStatus("unavailable");
                String technician = gson.toJson(tech);
                try {
                    int response = Request.PUT("employee", technician);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InterventionDB iDB = new InterventionDB(getApplicationContext());
                iDB.openForRead();
                ArrayList<Intervention> interventions = iDB.getAllInterventions();
                iDB.close();
                for(Intervention i : interventions){
                    String I = gson.toJson(i);
                    try {
                        int response = Request.PUT("intervention", I);
                        if(response != 200){
                            bool = false;
                            break;
                        }
                        else {
                            TicketDB tDB = new TicketDB(getApplicationContext());
                            tDB.openForRead();
                            ArrayList<Ticket> tickets = tDB.getAllTickets();
                            tDB.close();
                            for (Ticket t : tickets) {
                                String T = gson.toJson(t);
                                try {
                                    int response2 = Request.PUT("ticket", T);
                                    if(response2 != 200) {
                                        bool = false;
                                        break;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e(TAG, "Job Finished");
        }
    }).start();
}
}
