package com.example.twoperfect.CONTROLLER.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.twoperfect.MODEL.Availability;
import com.example.twoperfect.R;

import java.util.ArrayList;

public class AdapterListAvail extends ArrayAdapter<Availability> {
    private Context context;
    private int layoutAvailList;
    ArrayList<Availability> availabilities;


    public AdapterListAvail(@NonNull Context context, int layoutAvailList, @NonNull ArrayList<Availability> availabilities) {
        super(context, layoutAvailList, availabilities);
        this.context = context;
        this.layoutAvailList = layoutAvailList;
        this.availabilities = availabilities;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layoutAvailList, parent, false);
        Availability avail = availabilities.get(position);

        if(avail!=null){
            TextView txtDay = convertView.findViewById(R.id.txtDay);
            TextView txtStart = convertView.findViewById(R.id.txtStartTimeavail);
            TextView txtEnd = convertView.findViewById(R.id.txtEndTimeAvail);
            txtStart.setText(avail.startTime);
            txtEnd.setText(avail.endTime);
            txtDay.setText(avail.day);
        }
        return convertView;
    }
}

