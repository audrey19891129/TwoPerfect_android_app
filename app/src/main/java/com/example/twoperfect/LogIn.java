package com.example.twoperfect;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.twoperfect.CONTROLLER.EmployeeController;
import com.example.twoperfect.CONTROLLER.Request;
import com.example.twoperfect.MODEL.Address;
import com.example.twoperfect.MODEL.Availability;
import com.example.twoperfect.MODEL.Client;
import com.example.twoperfect.MODEL.Coordinates;
import com.example.twoperfect.MODEL.Description;
import com.example.twoperfect.MODEL.Employee;
import com.example.twoperfect.MODEL.Holiday;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.SQLite;
import com.example.twoperfect.MODEL.Technician;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.database.Availability.AvailabilityDB;
import com.example.twoperfect.database.Date.SessionDB;
import com.example.twoperfect.database.Employee.EmployeeDB;
import com.example.twoperfect.database.Holiday.HolidayDB;
import com.example.twoperfect.database.TwoPerfectDB;
import com.example.twoperfect.database.address.AddressDB;
import com.example.twoperfect.database.client.ClientDB;
import com.example.twoperfect.database.description.DescriptionDB;
import com.example.twoperfect.database.intervention.InterventionDB;
import com.example.twoperfect.database.ticket.TicketDB;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LogIn extends Fragment {
    SharedPreferences sharedPreferences;
    private static final String PREFS = "PREFS";
    private static final String PREFS_CONNECTION = "PREFS_CONNECTION";
    private static final String PREFS_USERNAME = "PREFS_USERNAME";
    private static final String PREFS_PASSWORD = "PREFS_PASSWORD";
    Button btnNotification, btnLog, btnActivatePhone, btnActivateSMS;
    CheckBox cbRememberMe;
    EditText user , pass;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar cal = Calendar.getInstance();
    private String now = sdf.format(cal.getTime());


    public LogIn() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        sharedPreferences = getContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        TwoPerfectDB twoPerfectDB = new TwoPerfectDB(getContext());
        twoPerfectDB.openForWrite();
        EmployeeDB employeeDB = new EmployeeDB(getContext());
        SessionDB sessionDB = new SessionDB(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!sharedPreferences.contains(PREFS_CONNECTION)){
            editor.putString(PREFS_CONNECTION, "").apply();
        }

        btnLog = view.findViewById(R.id.btnLogin);
        btnNotification = view.findViewById(R.id.btnNotification);
        btnActivatePhone = view.findViewById(R.id.btnActivatePhone);
        btnActivateSMS = view.findViewById(R.id.btnActivateSMS);
        user = view.findViewById(R.id.username);
        pass = view.findViewById(R.id.password);
        cbRememberMe = view.findViewById(R.id.cbRememberMe);
        if(sharedPreferences.contains(PREFS_USERNAME) && sharedPreferences.contains(PREFS_PASSWORD)){
            user.setText(sharedPreferences.getString(PREFS_USERNAME, null));
            pass.setText(sharedPreferences.getString(PREFS_PASSWORD, null));
            cbRememberMe.setChecked(true);
        }

        MainPage.tabViewer = WelcomePage.class;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Intervention", "new Intervention", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        btnLog.setOnClickListener(v -> {

            String username = user.getText().toString();
            String password = pass.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("ERREUR")
                        .setMessage("Vous devez saisir votre nom d'utilisateur et votre mot de passe.")
                        .setNeutralButton("OK", (dialog, which) -> {
                        })
                        .show();
            }
            else {
                rememberMe(cbRememberMe.isChecked());

                sessionDB.openForRead();
                String session = sessionDB.getSession();
                sessionDB.close();

                employeeDB.openForRead();
                Employee empdb = employeeDB.getEmployee();
                employeeDB.close();

                if(empdb != null && session.contentEquals(now) && empdb.username.contentEquals(user.getText())
                        && empdb.password.contentEquals(pass.getText()) && sharedPreferences.getString(PREFS_CONNECTION, null).contentEquals("CONNECTED")) {
                    Technician.setTechnician(empdb);
                    Navigation.findNavController(view).navigate(R.id.action_logIn_to_mainPage);
                }
                else {
                    boolean bool = false;
                    try {
                        bool = EmployeeController.getLogIn(username, password);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    if (bool) {
                        Log.e("MAIN", Technician.getTechnician().toString());
                        Navigation.findNavController(view).navigate(R.id.action_logIn_to_mainPage);
                        boolean checked = cbRememberMe.isChecked();
                        rememberMe(checked);
                        Gson gson = new Gson();
                        Employee tech = Technician.getTechnician();
                        tech.setStatus("available");
                        String technician = gson.toJson(tech);
                        try {
                            int response = Request.PUT("employee", technician);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.e("NOT CONNECTED ", "NOT CONNECTED" );
                        twoPerfectDB.constructTables(getContext(), now);
                        editor.putString(PREFS_CONNECTION, "CONNECTED").apply();

                        Thread t = new Thread(() -> {
                            ((MainActivity) getActivity()).startService(view);
                        });
                        t.start();
                    } else {
                        new AlertDialog.Builder(getContext())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("ERREUR")
                                .setMessage("Verifiez votre nom d'utilisateur et votre mot de passe.")
                                .setNeutralButton("OK", (dialog, which) -> {
                                })
                                .show();
                    }
                }
            }
            if(Technician.technician!=null) {
                twoPerfectDB.close();
                Double LAT = MainActivity.LAT;
                Double LONG = MainActivity.LONG;
                Coordinates coordinates = new Coordinates(Technician.getTechnician().getId(), String.valueOf(LAT), String.valueOf(LONG));
                Gson gson = new Gson();
                String json = gson.toJson(coordinates);
                try {
                    int response = Request.setCoordinates(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("ERREUR")
                        .setMessage("Verifiez votre connection internet.")
                        .setNeutralButton("OK", (dialog, which) -> {
                        })
                        .show();
            }
        });

            btnNotification.setOnClickListener(v -> {

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "Intervention");
                builder.setContentTitle("New Intervention");
                builder.setContentTitle("Une nouvelle tâche à était associé à vous, veuillez voir votre nouvelle tâche");
                builder.setSmallIcon(R.drawable.client_marker_map);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
                managerCompat.notify(1, builder.build());

            });

            btnActivatePhone.setOnClickListener(v -> {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.sym_action_call)
                        .setTitle("Contacter le client par : TEL")
                        .setMessage("Voulez-vous contacter le client ?")
                        .setPositiveButton("Oui", (dialog, which) -> {
                            // -1
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + "5145498739"));
                            startActivity(callIntent);
                        })
                        .setNegativeButton("Non", (dialog, which) -> {
                            // -2
                            new AlertDialog.Builder(getContext())
                                    .setIcon(android.R.drawable.ic_delete)
                                    .setTitle("Intervention non activé !")
                                    .setMessage("Vous devez contacter le client pour activater l'intervention")
                                    .setNeutralButton("OK", (i, g) -> {
                                    })
                                    .show();
                        })
                        .show();
            });

            btnActivateSMS.setOnClickListener(v -> {
                final EditText msg = new EditText(getContext());
                msg.setText("bonjour, votre technicien Vincent est en route à votre demeure le temps restant : 45 min");
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                msg.setLayoutParams(layoutParams);
                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.icons8_sms_30px)
                        .setTitle("Contacter le client par : SMS")
                        .setMessage("Voulez-vous écrire un message au client ?")
                        .setView(msg)
                        .setPositiveButton("Oui", (dialog, which) -> {
                            // -1
                            SmsManager sms = SmsManager.getDefault();
                            sms.sendTextMessage("514-549-8739", null, msg.getText().toString(), null, null);
                        })
                        .setNegativeButton("Non", (dialog, which) -> {
                            // -2
                            new AlertDialog.Builder(getContext())
                                    .setIcon(android.R.drawable.ic_delete)
                                    .setTitle("Intervention non activé !")
                                    .setMessage("Vous devez envoyer un SMS au client pour activater l'intervention")
                                    .setNeutralButton("OK", (i, g) -> {
                                    })
                                    .show();
                        })
                        .show();
            });
            return view;
    }


    public void rememberMe(boolean checked){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(checked){
            editor.putString(PREFS_USERNAME, user.getText().toString()).apply();
            editor.putString(PREFS_PASSWORD, pass.getText().toString()).apply();
        }else{
            editor.remove(PREFS_USERNAME).apply();
            editor.remove(PREFS_PASSWORD).apply();
        }
    }
}
