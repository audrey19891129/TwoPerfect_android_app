package com.example.twoperfect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.twoperfect.CONTROLLER.adapter.AdapterListTasks;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.database.intervention.InterventionDB;

import java.util.ArrayList;

public class Tasks extends Fragment {
    public Tasks() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        InterventionDB interventionDB = new InterventionDB(getContext());
        interventionDB.openForRead();
        ArrayList<Intervention> interventions = interventionDB.getTasksInterventions();
        interventionDB.close();
        TextView title = view.findViewById(R.id.txtTitle);
        if(!interventions.isEmpty()) {
            title.setText("Tâches du jour restantes");
            ListView listView = view.findViewById(R.id.idTasksList);
            AdapterListTasks adapter = new AdapterListTasks(getContext(), R.layout.fragment_task_item, interventions);
            listView.setAdapter(adapter);
        }
        else
            title.setText("Il n'y a aucune tâche restante pour aujourd'hui");
        return view;
    }
}