package com.example.twoperfect.CONTROLLER.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;

import com.example.twoperfect.CalendarViewer;
import com.example.twoperfect.MODEL.Description;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.MainPage;
import com.example.twoperfect.R;
import com.example.twoperfect.database.description.DescriptionDB;
import com.example.twoperfect.database.ticket.TicketDB;

import java.util.ArrayList;

public class AdapterListIntervention extends ArrayAdapter<Intervention> {
    public static Intervention interventionSelected = null;
    private Context context;
    private int layoutInterventionList;
    ArrayList<Intervention> interventionList;


    public AdapterListIntervention(@NonNull Context context, int layoutInterventionList, @NonNull ArrayList<Intervention> interventionList) {
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

        Intervention i = interventionList.get(position);
        if(i != null){

            TextView txtStart = convertView.findViewById(R.id.txtStartTime);
            TextView txtEnd = convertView.findViewById(R.id.txtEndTime);
            TextView txtDescription = convertView.findViewById(R.id.txtDescription);

            txtStart.setText(i.clientAvailStart);
            txtEnd.setText(i.clientAvailEnd);

            TicketDB ticketDB = new TicketDB(getContext());
            DescriptionDB descriptionDB = new DescriptionDB(getContext());

            ticketDB.openForRead();
            Ticket ticket = ticketDB.getTicketById(i.ticketId);
            ticketDB.close();

            descriptionDB.openForRead();
            Description description = descriptionDB.getDescriptionById(ticket.descriptionId);
            descriptionDB.close();
            txtDescription.setText(description.toString());

            ConstraintLayout cons = convertView.findViewById(R.id.cl_intervention_item);
                cons.setClickable(true);
            View finalConvertView = convertView;
            cons.setOnClickListener(v -> {
                    interventionSelected = i;
                    MainPage.tabViewer = CalendarViewer.class;
                    Navigation.findNavController(finalConvertView).navigate(R.id.action_mainPage_to_detailIntervention);
                });
        }
        return convertView;
    }
}
