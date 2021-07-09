package com.example.twoperfect;

import android.app.Fragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.twoperfect.MODEL.Technician;
import com.google.android.material.tabs.TabLayout;

public class MainPage extends Fragment {
    public static Class tabViewer;
    public static View mainPageView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    TabLayout tabLayout;

    public MainPage() {}


    public static MainPage newInstance(String param1, String param2) {
        MainPage fragment = new MainPage();
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
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);
        mainPageView = view;
        tabLayout = view.findViewById(R.id.tabs);

        TextView tech = view.findViewById(R.id.idTechnicianName);
        tech.setText("Bienvenue " + Technician.getTechnician().firstname + " " + Technician.getTechnician().lastname);

        Log.e("MAIN", Technician.getTechnician().toString());

        getParentFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, tabViewer, null)
                .commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            switch(tab.getPosition())

            {
                case 0:
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, WelcomePage.class, null)
                            .commit();
                    break;
                case 1:
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, Tasks.class, null)
                            .commit();
                    break;
                case 2:
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, Infos.class, null)
                            .commit();
                    break;

                case 3:
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, CalendarViewer.class, null)
                            .commit();
                    break;

                case 4:
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, MapsFragment.class, null)
                            .commit();
                    break;
            }
        }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            // called when tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            // called when a tab is reselected
            }
        });

        return view;
    }

}