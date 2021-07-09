package com.example.twoperfect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.twoperfect.CONTROLLER.Request;
import com.example.twoperfect.MODEL.Employee;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.Technician;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.SERVICES.Service_LogOut;
import com.example.twoperfect.SERVICES.Service_NewIntervention;
import com.example.twoperfect.database.intervention.InterventionDB;
import com.example.twoperfect.database.ticket.TicketDB;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class WelcomePage extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public WelcomePage() {}

    public static WelcomePage newInstance(String param1, String param2) {
        WelcomePage fragment = new WelcomePage();
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
        View view = inflater.inflate(R.layout.fragment_welcome_page, container, false);

        Button LogOut = view.findViewById(R.id.btnLogOut);
        LogOut.setOnClickListener(v->{

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("PREFS_CONNECTION","DISCONNECTED").apply();

            //START SERVICE LOGOUT
            Service_LogOut.scheduleJob(getContext());

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            ((MainActivity) getActivity()).stopService(view);
        });
        return view;
    }
}