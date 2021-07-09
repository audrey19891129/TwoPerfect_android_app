package com.example.twoperfect.CONTROLLER.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import com.example.twoperfect.CONTROLLER.CommunicationController;

import com.example.twoperfect.CONTROLLER.Request;
import com.example.twoperfect.CalendarViewer;
import com.example.twoperfect.MODEL.Client;
import com.example.twoperfect.MODEL.Communication;
import com.example.twoperfect.MODEL.Description;

import com.example.twoperfect.MODEL.Employee;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.Task;

import com.example.twoperfect.MODEL.Technician;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.MainPage;
import com.example.twoperfect.R;
import com.example.twoperfect.Tasks;
import com.example.twoperfect.database.TwoPerfectDB;

import com.example.twoperfect.database.client.ClientDB;
import com.example.twoperfect.database.communication.CommunicationDB;
import com.example.twoperfect.database.description.DescriptionDB;
import com.example.twoperfect.database.intervention.InterventionDB;
import com.example.twoperfect.database.ticket.TicketDB;
import com.google.gson.Gson;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AdapterListTasks extends ArrayAdapter<Intervention> {
    public static Intervention interventionSelected = null;
    public static Button Activate;
    public static Button PunchIn;
    public static Button PunchOut;
    public static Button Raport;
    private Context context;
    private int layoutInterventionList;

    ArrayList<Intervention> interventionList;

    public AdapterListTasks(@NonNull Context context, int layoutInterventionList, @NonNull ArrayList<Intervention> interventionList) {
        super(context, layoutInterventionList, interventionList);
        this.context = context;
        this.layoutInterventionList = layoutInterventionList;
        this.interventionList = interventionList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layoutInterventionList, parent, false);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendars = Calendar.getInstance();
        String date = simpleDateFormat.format(calendars.getTime());

        CommunicationDB communicationDB = new CommunicationDB(getContext());
        InterventionDB iDB = new InterventionDB(getContext());

        Intervention i = interventionList.get(position);
        if(i != null){

            TextView txtStart = convertView.findViewById(R.id.txtStartTime);
            TextView txtEnd = convertView.findViewById(R.id.txtEndTime);
            TextView txtDescription = convertView.findViewById(R.id.txtDescription);
            Activate = convertView.findViewById(R.id.btnActivation);
            PunchIn = convertView.findViewById(R.id.btnPunchIn);
            PunchOut = convertView.findViewById(R.id.btnPunchOut);
            Raport = convertView.findViewById(R.id.btnRaport);
            PunchIn.setEnabled(false);
            PunchOut.setEnabled(false);

            if(position > 0){
                PunchIn.setVisibility(convertView.GONE);
                PunchOut.setVisibility(convertView.GONE);
                Activate.setVisibility(convertView.GONE);
            }

            Log.e("TASK  ", i.toString() );

            if(i.status.contentEquals("activated")){
                if(i.punchIn.contentEquals("00:00:00")) { // task activated but not punched in
                    Activate.setEnabled(false);
                    PunchIn.setEnabled(true);
                    PunchOut.setEnabled(false);
                    Raport.setEnabled(false);
                }
                else{
                    if(i.punchOut.contentEquals("00:00:00")){    // task activated but punched in
                        Activate.setEnabled(false);
                        PunchIn.setEnabled(false);
                        PunchOut.setEnabled(true);
                        Raport.setEnabled(false);
                    }
                    else{
                        Activate.setEnabled(false); // task activated and punched out; report not sent
                        PunchIn.setEnabled(false);
                        PunchOut.setEnabled(false);
                        Raport.setEnabled(true);
                    }
                }
            }
            else{
                Activate.setEnabled(true);
                PunchIn.setEnabled(false);
                PunchOut.setEnabled(false);
                Raport.setEnabled(true);
            }
            txtStart.setText(i.clientAvailStart);
            txtEnd.setText(i.clientAvailEnd);

            TwoPerfectDB twoPerfectDB = new TwoPerfectDB(getContext());
            twoPerfectDB.openForRead();

            TicketDB ticketDB = new TicketDB(getContext());
            DescriptionDB descriptionDB = new DescriptionDB(getContext());
            ClientDB clientDB = new ClientDB(getContext());

            ticketDB.openForRead();
            Ticket ticket = ticketDB.getTicketById(i.ticketId);
            ticketDB.close();

            descriptionDB.openForRead();
            Description description = descriptionDB.getDescriptionById(ticket.descriptionId);
            descriptionDB.close();
            txtDescription.setText(description.toString());
            ticket.setDescription(description);
            Task.setTicket(ticket);

            clientDB.openForRead();
            Client client = clientDB.getClientById(ticket.clientId);
            clientDB.close();

            twoPerfectDB.close();

            ConstraintLayout cons = convertView.findViewById(R.id.cl_intervention_item);
            cons.setClickable(true);
            View finalConvertView = convertView;
            cons.setOnClickListener(v -> {
                interventionSelected = i;
                MainPage.tabViewer = Tasks.class;
                Navigation.findNavController(finalConvertView).navigate(R.id.action_mainPage_to_detailIntervention);
            });

            Communication communication = new Communication(i.getTechnicianId(), i.getId(), ticket.getClientId(), date, i.getCommunicationType());

            Activate.setOnClickListener(v -> {

                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.icons8_sms_30px)
                        .setTitle("ACTIVATION")
                        .setMessage("Etes-vous sur(e) de vouloir activer cette tâche?")
                        .setPositiveButton("Valider", (dialog, which) -> {
                            interventionSelected = i;
                            switch(i.getCommunicationType()){
                                case "phone":
                                    new AlertDialog.Builder(getContext())
                                            .setIcon(android.R.drawable.sym_action_call)
                                            .setTitle("Contacter le client par : TELEPHONE")
                                            .setMessage("Voulez-vous contacter le client ?")
                                            .setPositiveButton("Oui", (d1, v1) -> {
                                                // -1
                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + client.phone));
                                                context.startActivity(callIntent);

                                                i.setStatus("activated");
                                                Task.setIntervention(i);
                                                iDB.openForWrite();
                                                iDB.modify(i);
                                                Log.e("SMS", "wrinting to db");
                                                iDB.close();
                                                Task.setTicket(ticket);
                                                MainPage.tabViewer = Tasks.class;
                                                Navigation.findNavController(finalConvertView).navigate(R.id.action_mainPage_self);
                                                // RUN SERVICES IN BACK GROUND WITH NEW THREAD
                                                Thread t = new Thread(() -> {
                                                    try {
                                                        CommunicationController.setCommunicationToMySQL(communication);
                                                        CommunicationController.setCommunicationToMySQL(communication);
                                                        Gson gson = new Gson();
                                                        Employee tech = Technician.getTechnician();
                                                        tech.setStatus("busy");
                                                        String technician = gson.toJson(tech);
                                                        int response = Request.PUT("employee", technician);
                                                        Log.e("SERVICES RESPONSE 1", " SEND TO MYSQL SUCCESS !");
                                                    } catch (IOException e) {
                                                        CommunicationController.setCommunicationToSQLite(communication, getContext());
                                                        Log.e("SERVICES RESPONSE 2", e.getMessage());
                                                    }
                                                });
                                                t.start();
                                            })
                                            .setNegativeButton("Non", (d2, v2) -> {
                                                // -2
                                                new AlertDialog.Builder(getContext())
                                                        .setIcon(android.R.drawable.ic_delete)
                                                        .setTitle("Intervention non activée !")
                                                        .setMessage("Vous devez appeller le client pour activater l'intervention")
                                                        .setNeutralButton("OK", (dia, gs) -> {})
                                                        .show();
                                            })
                                            .show();
                                    break;
                                case "sms":
                                    final EditText msg = new EditText(getContext());
                                    msg.setText("Bonjour, votre technicien "+Technician.technician.firstname+" est en route vers votre demeure et devrait y etre dans 60 minutes");
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT
                                    );
                                    msg.setLayoutParams(layoutParams);
                                    new AlertDialog.Builder(getContext())
                                            .setIcon(R.drawable.icons8_sms_30px)
                                            .setTitle("Contacter le client par : SMS")
                                            .setMessage("Veuillez écrire un message au client")
                                            .setView(msg)
                                            .setPositiveButton("Envoyer", (d3, v3) -> {
                                                // -1
                                                SmsManager sms = SmsManager.getDefault();
                                                //sms.sendTextMessage("514-549-8739", null, msg.getText().toString(), null, null);
                                                sms.sendTextMessage(client.phone, null, msg.getText().toString(), null, null);

                                                i.setStatus("activated");
                                                Task.setIntervention(i);
                                                iDB.openForWrite();
                                                iDB.modify(i);
                                                Log.e("SMS", "wrinting to db");
                                                iDB.close();
                                                Task.setTicket(ticket);
                                                MainPage.tabViewer = Tasks.class;
                                                Navigation.findNavController(finalConvertView).navigate(R.id.action_mainPage_self);

                                                // RUN SERVICES IN BACK GROUND WITH NEW THREAD
                                                Thread t = new Thread(() -> {
                                                    try {
                                                        CommunicationController.setCommunicationToMySQL(communication);
                                                        Gson gson = new Gson();
                                                        Employee tech = Technician.getTechnician();
                                                        tech.setStatus("busy");
                                                        String technician = gson.toJson(tech);
                                                        int response = Request.PUT("employee", technician);
                                                        Log.e("SERVICES RESPONSE 1", " SEND TO MYSQL SUCCESS !");
                                                    } catch (IOException e) {
                                                        CommunicationController.setCommunicationToSQLite(communication, getContext());
                                                        Log.e("SERVICES RESPONSE 2", e.getMessage());
                                                    }
                                                });
                                                t.start();
                                            })
                                            .setNegativeButton("Cancel", (d4, v4) -> {
                                                // -2
                                                new AlertDialog.Builder(getContext())
                                                        .setIcon(android.R.drawable.ic_delete)
                                                        .setTitle("Intervention non activé !")
                                                        .setMessage("Vous devez envoyer un SMS au client pour activer l'intervention")
                                                        .setNeutralButton("OK", (dia, gs) -> {})
                                                        .show();
                                            })
                                            .show();
                                    break;
                            }
                        })
                        .setNegativeButton("Canceller", (dialog, which) -> {})
                        .show();
            });

            PunchIn.setOnClickListener(v -> {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.icons8_sms_30px)
                        .setTitle("ARRIVÉE")
                        .setMessage("Etes-vous sur(e) de vouloir poinçonner votre arrivée?")
                        .setPositiveButton("Valider", (dialog, which) -> {
                            interventionSelected = i;
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            Calendar calendar = Calendar.getInstance();
                            String time = sdf.format(calendar.getTime());
                            Log.e("TAG", time);
                            i.setPunchIn(time);
                            Task.setIntervention(i);
                            iDB.openForWrite();
                            iDB.modify(i);
                            iDB.close();
                            MainPage.tabViewer = Tasks.class;
                            Navigation.findNavController(finalConvertView).navigate(R.id.action_mainPage_self);
                        })
                        .setNegativeButton("Canceller", (dialog, which) -> {})
                        .show();
            });

            PunchOut.setOnClickListener(v -> {
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.icons8_sms_30px)
                        .setTitle("DÉPART")
                        .setMessage("Etes-vous sur(e) de vouloir poinçonner votre départ?")
                        .setPositiveButton("Valider", (dialog, which) -> {
                            interventionSelected = i;
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                            Calendar calendar = Calendar.getInstance();
                            String time = sdf.format(calendar.getTime());
                            i.setPunchOut(time);
                            Task.setIntervention(i);
                            iDB.openForWrite();
                            iDB.modify(i);
                            iDB.close();
                            Navigation.findNavController(finalConvertView).navigate(R.id.action_mainPage_to_raport);
                        })
                        .setNegativeButton("Canceller", (dialog, which) -> {})
                        .show();

            });

            Raport.setOnClickListener(v -> {
                i.setStatus("activated");
                interventionSelected = i;
                Task.setIntervention(i);
                Task.setTicket(ticket);
                Navigation.findNavController(finalConvertView).navigate(R.id.action_mainPage_to_raport);
            });
        }

        return convertView;
    }
}
