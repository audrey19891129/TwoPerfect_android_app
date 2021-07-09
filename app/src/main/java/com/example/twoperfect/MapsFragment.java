package com.example.twoperfect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.twoperfect.MODEL.Address;
import com.example.twoperfect.MODEL.Client;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.database.address.AddressDB;
import com.example.twoperfect.database.client.ClientDB;
import com.example.twoperfect.database.intervention.InterventionDB;
import com.example.twoperfect.database.ticket.TicketDB;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.ArrayList;

public class MapsFragment extends Fragment {


    @SuppressLint("MissingPermission")
    private OnMapReadyCallback callback = googleMap -> {

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        LatLng tech = new LatLng(location.getLatitude(), location.getLongitude());
                        Log.e("MAIN_ACTIVITY", "LAT : " + location.getLatitude() + " LONG : " + location.getLongitude() );
                        MarkerOptions techMarker = new MarkerOptions().position(tech).title("You are here");
                        googleMap.addMarker(techMarker);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tech, 14f));
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        InterventionDB interventionDB = new InterventionDB(getContext());
        interventionDB.openForRead();
        ArrayList<Intervention> interventions = interventionDB.getTasksInterventions();
        interventionDB.close();
        TicketDB ticketDB = new TicketDB(getContext());
        AddressDB addressDB = new AddressDB(getContext());
        ClientDB clientDB = new ClientDB(getContext());

        for (Intervention i : interventions) {

            ticketDB.openForRead();
            Ticket ticket = ticketDB.getTicketById(i.ticketId);
            ticketDB.close();

            addressDB.openForRead();
            Address address = addressDB.getAddressById(ticket.addressId);
            addressDB.close();

            clientDB.openForRead();
            Client client = clientDB.getClientById(ticket.clientId);
            clientDB.close();

            String addr = address.toString();
            LatLng position = DetailIntervention.getLocationFromAddress(getContext(), addr);
            googleMap.addMarker(new MarkerOptions().position(position).title(client.firstname + " " + client.lastname).snippet(client.getPhone()).icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8_marker_48px)));
        }
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}