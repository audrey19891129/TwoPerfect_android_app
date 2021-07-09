package com.example.twoperfect.CONTROLLER.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.twoperfect.MODEL.Holiday;
import com.example.twoperfect.R;

import java.util.ArrayList;

public class AdapterListHoliday extends ArrayAdapter<Holiday> {

    public ArrayList<Holiday> holidays;
    public int HolidayLayout;
    public Context context;

    public AdapterListHoliday(@NonNull Context context, int HolidayLayout, ArrayList<Holiday> holidays) {
        super(context, HolidayLayout, holidays);
        this.context = context;
        this.HolidayLayout = HolidayLayout;
        this.holidays = holidays;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(HolidayLayout, parent, false);
        Holiday holiday = holidays.get(position);

        if(holiday!=null){
            TextView txtStart = convertView.findViewById(R.id.txtStartDateHoliday);
            TextView txtEnd = convertView.findViewById(R.id.txtEndDateHoliday);
            txtStart.setText(holiday.startDate);
            txtEnd.setText(holiday.endDate);
        }
        return convertView;
    }
}
