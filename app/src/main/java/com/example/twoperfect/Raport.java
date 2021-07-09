package com.example.twoperfect;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;

import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.twoperfect.CONTROLLER.Request;
import com.example.twoperfect.CONTROLLER.adapter.AdapterListTasks;
import com.example.twoperfect.MODEL.Coordinates;
import com.example.twoperfect.MODEL.Employee;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.Task;
import com.example.twoperfect.MODEL.Technician;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.database.address.AddressDB;
import com.example.twoperfect.database.client.ClientDB;
import com.example.twoperfect.database.description.DescriptionDB;
import com.example.twoperfect.database.intervention.InterventionDB;
import com.example.twoperfect.database.ticket.TicketDB;
import com.google.gson.Gson;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Raport extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String InterventionComment = "";

    public Raport() {
    }

    public static Raport newInstance(String param1, String param2) {
        Raport fragment = new Raport();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raport, container, false);

        Intervention i = Task.getIntervention();

        Spinner status = view.findViewById(R.id.status);
        ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(getContext(), R.array.statusString, android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(adapterS);

        Spinner comment = view.findViewById(R.id.dropComment);
        ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(getContext(), R.array.comment, android.R.layout.simple_spinner_dropdown_item);
        comment.setAdapter(adapterC);

        Button Submit = view.findViewById(R.id.btnSubmit);
        Button Cancel = view.findViewById(R.id.btnCancel);

        TextView txtDetailDescription = view.findViewById(R.id.txtDetailDescription2);
        TextView txtDetailDate = view.findViewById(R.id.txtDetailDate2);
        TextView txtDetailStart = view.findViewById(R.id.txtDetailStart2);
        TextView  txtDetailEnd = view.findViewById(R.id.txtDetailEnd2);
        TextView txtDetailNote = view.findViewById(R.id.txtDetailNote2);
        EditText detailComment = view.findViewById(R.id.comment);
        detailComment.setEnabled(false);

        txtDetailDescription.setText(Task.getTicket().getDescription().toString());
        txtDetailDate.setText(Task.getIntervention().date);
        txtDetailStart.setText(Task.getIntervention().punchIn);
        txtDetailEnd.setText(Task.getIntervention().punchOut);
        txtDetailNote.setText(Task.getIntervention().note);

        InterventionDB iDB = new InterventionDB(getContext());
        TicketDB tDB = new TicketDB(getContext());


        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intervention i = Task.getIntervention();
                    i.setStatus(status.getSelectedItem().toString());
                    Task.setIntervention(i);
                    iDB.openForWrite();
                    iDB.modify(i);
                    iDB.close();
                    Ticket T = Task.getTicket();
                    T.setStatus(i.status);
                    tDB.openForWrite();
                    tDB.modify(T);
                    tDB.close();
                    Task.setTicket(T);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        comment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                detailComment.setEnabled(false);
                if (position > 0) {
                    if(comment.getSelectedItem().toString().equalsIgnoreCase("Autre raison ou commentaire"))
                        detailComment.setEnabled(true);
                    else {
                        Intervention i = Task.getIntervention();
                        i.setComment(comment.getSelectedItem().toString());
                        Task.setIntervention(i);
                        iDB.openForWrite();
                        iDB.modify(i);
                        iDB.close();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        detailComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                Intervention i = Task.getIntervention();
                if(detailComment.getText().toString().length() > 300){
                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("ERREUR")
                            .setMessage( "Le commentaire ne peut dépasser 300 caractères (incluant les espaces)")
                            .setNeutralButton("OK", (dialog, which) -> { })
                            .show();
                }
                else {
                    i.setComment(detailComment.getText().toString());
                    Task.setIntervention(i);
                    iDB.openForWrite();
                    iDB.modify(i);
                    iDB.close();
                }
            }
        });

        Submit.setOnClickListener(v->{

            if(status.getSelectedItem().toString().equalsIgnoreCase("selection")){
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("ERREUR")
                        .setMessage( "Vous devez sélectionner un statut pour fermer cette tâche.")
                        .setNeutralButton("OK", (dialog, which) -> { })
                        .show();
            }
            else{
                if(Task.getIntervention().getStatus().contentEquals("Incomplet") && Task.getIntervention().getComment().contentEquals("aucun commentaire")){
                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("ERREUR")
                            .setMessage( "Si la tâche est incomplète, vous devez en indiquer la raison.")
                            .setNeutralButton("OK", (dialog, which) -> { })
                            .show();
                }
                else{
                    if(closeTask(Task.getIntervention())){
                        Navigation.findNavController(view).navigate(R.id.action_raport_to_mainPage);
                    }
                }

            }
        });

        Cancel.setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_raport_to_mainPage);
        });

        return view;
    }

    public boolean closeTask(Intervention i) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String day = sdf.format(calendar.getTime());
        Gson gson = new Gson();
        String intervention = gson.toJson(i);
        if (i.status.contentEquals("Complet") && i.comment.trim().length() == 0) {
            i.setComment("aucun commentaire");
            Task.setIntervention(i);
            InterventionDB iDB = new InterventionDB(getContext());
            iDB.openForWrite();
            iDB.modify(i);
            iDB.close();
        }

        if (i.status.contentEquals("Incomplet") && i.comment.trim().length() == 0) {
            new AlertDialog.Builder(getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("ERREUR")
                    .setMessage("Si la tâche est incomplète, vous devez en indiquer la raison.")
                    .setNeutralButton("OK", (dialog, which) -> {
                    })
                    .show();
        } else {
            Employee tech = Technician.getTechnician();
            tech.setStatus("available");
            String technician = gson.toJson(tech);
            try {
                int response = Request.PUT("employee", technician);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                int response = Request.PUT("intervention", intervention);
                if (response == 200) {
                    Ticket t = Task.getTicket();
                    t.setStatus(i.status);
                    t.setClosingDate(day);
                    TicketDB tDB = new TicketDB(getContext());
                    tDB.openForWrite();
                    tDB.modify(t);
                    tDB.close();
                    Task.setTicket(t);
                    String ticket = gson.toJson(t);
                    try {
                        int response2 = Request.PUT("ticket", ticket);
                        if (response2 == 200) {
                            new AlertDialog.Builder(getContext())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("SUCCES")
                                    .setMessage("La tâche est complétée.")
                                    .setNeutralButton("OK", (dialog, which) -> {
                                    })
                                    .show();
                            Task.setIntervention(null);
                            Task.setTicket(null);
                            return true;
                        } else {
                            new AlertDialog.Builder(getContext())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("ERREUR")
                                    .setMessage("La tâche n'a pas pu être modifiée au serveur central.")
                                    .setNeutralButton("OK", (dialog, which) -> {
                                    })
                                    .show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("ERREUR")
                            .setMessage("La tâche n'a pas pu être modifiée au serveur central.")
                            .setNeutralButton("OK", (dialog, which) -> {
                            })
                            .show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}