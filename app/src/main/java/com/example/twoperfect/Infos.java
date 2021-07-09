package com.example.twoperfect;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.twoperfect.CONTROLLER.adapter.AdapterListAvail;
import com.example.twoperfect.CONTROLLER.adapter.AdapterListHoliday;
import com.example.twoperfect.MODEL.Availability;
import com.example.twoperfect.MODEL.Employee;
import com.example.twoperfect.MODEL.Holiday;
import com.example.twoperfect.MODEL.Technician;
import com.example.twoperfect.database.Availability.AvailabilityDB;
import com.example.twoperfect.database.Holiday.HolidayDB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Infos extends Fragment {
    TextView txtInfoFullName, txtInfoTitle, txtInfoPhone, txtInfoEmail, txtConn;
    ListView listAvail;
    ListView listHoliday;

    public Infos() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_infos, container, false);
        Employee tech = Technician.getTechnician();
        ImageView photo = view.findViewById(R.id.employeePicture);
        Picasso.get().load(tech.photo).into(photo);
        txtInfoFullName = view.findViewById(R.id.txtInfoFullName);
        txtInfoTitle = view.findViewById(R.id.txtInfoTitle);
        txtInfoPhone = view.findViewById(R.id.txtInfoPhone);
        txtInfoEmail = view.findViewById(R.id.txtInfoEmail);

        txtInfoFullName.setText(tech.getFirstname() + " " + tech.getLastname());
        txtInfoTitle.setText(tech.getTitle());
        txtInfoPhone.setText(tech.getPhone());
        txtInfoEmail.setText(tech.getEmail());

        listAvail = view.findViewById(R.id.listAvailability);
        listHoliday = view.findViewById(R.id.listHolidays);

        ArrayList<Availability> concise = new ArrayList<Availability>();
        AvailabilityDB availabilityDB = new AvailabilityDB(getContext());
        availabilityDB.openForRead();
        ArrayList<Availability> listA = availabilityDB.getAvailabilities();
        availabilityDB.close();

        for (int i = 0; i < listA.size(); i++) {
            Availability aIndex = listA.get(i);
            Availability newA = new Availability();
            if(i % 2 != 0){
                Availability c = listA.get(i-1);
                newA.setDay(aIndex.day);
                newA.setStartTime(c.startTime);
                newA.setEndTime(aIndex.endTime);
                concise.add(newA);
            }
        }

        AdapterListAvail adapt1 = new AdapterListAvail(getContext(), R.layout.fragment_avail_item, concise);
        listAvail.setAdapter(adapt1);

        HolidayDB holidayDB = new HolidayDB(getContext());
        holidayDB.openForRead();
        ArrayList<Holiday> listH = holidayDB.getHolidays();
        holidayDB.close();

        AdapterListHoliday adapt2 = new AdapterListHoliday(getContext(), R.layout.fragment_holiday_item, listH);
        listHoliday.setAdapter(adapt2);

        return view;
    }
}