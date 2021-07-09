package com.example.twoperfect;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.example.twoperfect.CONTROLLER.adapter.AdapterListIntervention;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.database.TwoPerfectDB;
import com.example.twoperfect.database.intervention.InterventionDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarViewer extends Fragment {
    ListView idListIntervention;
    CalendarView calendarView;
    public static  ArrayList<Intervention> interventions;

    public CalendarViewer() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InterventionDB interventionDB = new InterventionDB(getContext());
        interventionDB.openForRead();
        interventions = interventionDB.getAllInterventions();
        interventionDB.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        idListIntervention = view.findViewById(R.id.idListIntervention);
        List<EventDay> events = new ArrayList<>();
        try {
            calendarView.setDate(Calendar.getInstance());
            for(Intervention intervention : interventions){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
                String interventionDate = intervention.getDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(interventionDate));
                events.add(new EventDay(calendar, android.R.drawable.btn_star_big_on));
            }
            calendarView.setEvents(events);
        } catch (OutOfDateRangeException | ParseException e) {
            Log.e("ERROR", e.getMessage());
        }

        calendarView.setOnDayClickListener(eventDay -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = eventDay.getCalendar();
            String date = sdf.format(calendar.getTime());
            InterventionDB interventionDB = new InterventionDB(getContext());
            interventionDB.openForRead();
            ArrayList<Intervention> listIntervention = interventionDB.getInterventionsByDate(date);
            interventionDB.close();
            AdapterListIntervention adapter = new AdapterListIntervention(getContext(), R.layout.fragment_intervention_item, listIntervention);
            idListIntervention.setAdapter(adapter);
        });
        return view;
    }
}