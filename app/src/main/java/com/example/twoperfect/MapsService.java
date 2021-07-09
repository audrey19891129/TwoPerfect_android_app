package com.example.twoperfect;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.example.twoperfect.CONTROLLER.Request;
import com.example.twoperfect.MODEL.Coordinates;
import com.example.twoperfect.MODEL.Technician;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import java.io.IOException;

public class MapsService extends JobService {

    private static String TAG = "MAPS Service";
    private boolean jobCancelled = false;


    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e(TAG, "job started");
        doBackgroundWork(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e(TAG, "job cancelled before completion");
        jobCancelled = true;
        return true;
    }

    public void doBackgroundWork(JobParameters params) {
        Log.e(TAG, "doBackgroundWork");
        if (jobCancelled) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
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

                        Log.e(TAG, "LAT : " + location.getLatitude() + " LONG : " + location.getLongitude() );
                        Log.e(TAG, Technician.getTechnician().toString());
                        Coordinates coordinates = new Coordinates(Technician.getTechnician().getId(), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                        Gson gson = new Gson();
                        String json = gson.toJson(coordinates);
                        try {
                            int response = Request.setCoordinates(json);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }
}
