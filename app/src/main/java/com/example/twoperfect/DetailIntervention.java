package com.example.twoperfect;

import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.twoperfect.CONTROLLER.adapter.AdapterListIntervention;
import com.example.twoperfect.CONTROLLER.adapter.AdapterListTasks;
import com.example.twoperfect.MODEL.Address;
import com.example.twoperfect.MODEL.Client;
import com.example.twoperfect.MODEL.Description;
import com.example.twoperfect.MODEL.Intervention;
import com.example.twoperfect.MODEL.Ticket;
import com.example.twoperfect.database.address.AddressDB;
import com.example.twoperfect.database.client.ClientDB;
import com.example.twoperfect.database.description.DescriptionDB;
import com.example.twoperfect.database.ticket.TicketDB;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class DetailIntervention extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    static Address address;
    static Client client;
    static Ticket ticket;
    static Description description;
    static Intervention intervention;
    static TextView txtDetailDescription, txtDetailDate, txtDetailStart, txtDetailEnd, txtDetailStatus, txtDetailNote, txtDetailComment, txtDetailFullName, txtDetailPhone, txtDetailAddress;
    static TicketDB ticketDB;
    static ClientDB clientDB;
    static AddressDB addressDB;
    static DescriptionDB descriptionDB;

    public DetailIntervention() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ticketDB = new TicketDB(getContext());
        clientDB = new ClientDB(getContext());
        addressDB = new AddressDB(getContext());
        descriptionDB = new DescriptionDB(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_intervention, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        txtDetailDescription = view.findViewById(R.id.txtDetailDescription);
        txtDetailDate = view.findViewById(R.id.txtDetailDate);
        txtDetailStart = view.findViewById(R.id.txtDetailStart);
        txtDetailEnd = view.findViewById(R.id.txtDetailEnd);
        txtDetailStatus = view.findViewById(R.id.txtDetailStatus);
        txtDetailNote = view.findViewById(R.id.txtDetailNote);
        txtDetailComment = view.findViewById(R.id.txtDetailComment);
        txtDetailFullName = view.findViewById(R.id.txtDetailFullName);
        txtDetailPhone = view.findViewById(R.id.txtDetailPhone);
        txtDetailAddress = view.findViewById(R.id.txtDetailAddress);

        if (AdapterListIntervention.interventionSelected == null) {
            intervention = AdapterListTasks.interventionSelected;
        } else {
            intervention = AdapterListIntervention.interventionSelected;
        }
        AdapterListIntervention.interventionSelected = null;
        AdapterListTasks.interventionSelected = null;

        txtDetailDate.setText(intervention.getDate());
        txtDetailStart.setText(intervention.getClientAvailStart());
        txtDetailEnd.setText(intervention.getClientAvailEnd());
        txtDetailStatus.setText(intervention.getStatus());
        txtDetailNote.setText(intervention.getNote());
        txtDetailComment.setText(intervention.getComment());

        ticketDB.openForRead();
        ticket = ticketDB.getTicketById(intervention.ticketId);
        ticketDB.close();

        addressDB.openForRead();
        address = addressDB.getAddressById(ticket.addressId);
        addressDB.close();
        txtDetailAddress.setText(address.toString());

        clientDB.openForRead();
        client = clientDB.getClientById(ticket.clientId);
        clientDB.close();
        txtDetailFullName.setText(client.firstname + " " + client.lastname);
        txtDetailPhone.setText(client.getPhone());

        descriptionDB.openForRead();
        description = descriptionDB.getDescriptionById(ticket.descriptionId);
        descriptionDB.close();
        txtDetailDescription.setText(description.toString());
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String street = address.toString();
        LatLng address = getLocationFromAddress(getContext(), street);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.addMarker(new MarkerOptions().position(address).title(client.firstname + " " + client.lastname).snippet(client.getPhone()).icon(BitmapDescriptorFactory.fromResource(R.drawable.client_marker_map_2)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address, 16f));

    }

    public static LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<android.location.Address> address;
        LatLng LL = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            android.location.Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            LL = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LL;

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}