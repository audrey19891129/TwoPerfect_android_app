package com.example.twoperfect;

import android.app.Notification;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.twoperfect.CONTROLLER.ClientController;
import com.example.twoperfect.CONTROLLER.InterventionController;
import com.example.twoperfect.CONTROLLER.TicketController;
import com.example.twoperfect.MODEL.Address;
import com.example.twoperfect.MODEL.Client;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.database.address.AddressDB;
import com.example.twoperfect.database.client.ClientDB;
import com.example.twoperfect.database.intervention.InterventionDB;
import com.example.twoperfect.database.ticket.TicketDB;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class NewIntervention extends JobService {

    private static String TAG = "NewIntervention Service";
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
                    ArrayList<Intervention> interventions = InterventionController.getNewInterventions(getApplicationContext());
                    Log.e(TAG, "SIZE OF LIST : " + interventions.size());
                    InterventionDB interventionDB = new InterventionDB(getApplicationContext());
                    interventionDB.openForWrite();

                    for(Intervention i : interventions){
                        Log.e(TAG, "Interventions is not empty");

                        interventionDB.insertIntervention(i);
                        Ticket ticket = TicketController.setTicketToSQLiteByID(i.ticketId);
                        Address address = ticket.address;
                        Client client = ClientController.setClientToSQLiteByID(ticket.clientId);

                        TicketDB tdb = new TicketDB(getApplicationContext());
                        tdb.openForWrite();
                        tdb.insertTicket(ticket);
                        tdb.close();

                        Log.e(TAG, "New ticket inserted");

                        ClientDB cdb = new ClientDB(getApplicationContext());
                        cdb.openForRead();
                        ArrayList<Client> clients = cdb.getAllClients();
                        cdb.close();

                        if(!clients.contains(client)){
                            cdb.openForWrite();
                            cdb.insertClient(client);
                            cdb.close();
                        }
                        Log.e(TAG, "New client Inserted");


                        AddressDB adb = new AddressDB(getApplicationContext());
                        adb.openForRead();
                        ArrayList<Address> addresses = adb.getAllAddresses();
                        adb.close();

                        if(!addresses.contains(address)){
                            adb.openForWrite();
                            adb.insertAddress(address);
                            adb.close();
                        }
                        Log.e(TAG, "New address Inserted");
                    }
                    interventionDB.close();

                    Log.e(TAG, "job finished");

                    if(interventions.size()>0) {
                        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Notification notification = new NotificationCompat.Builder(getApplicationContext(), App.CHANNEL_ID)
                                .setContentTitle("New Intervention")
                                .setContentTitle("Une nouvelle tâche vous a été associée.")
                                .setSmallIcon(R.drawable.inter_telcom_logo)
                                .setSound(sound)
                                . build();
                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                        managerCompat.notify(2, notification);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}

