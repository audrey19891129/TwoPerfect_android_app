package com.example.twoperfect;
import com.example.twoperfect.SERVICES.ForegroundServices;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import javax.security.auth.login.LoginException;


public class MainActivity extends AppCompatActivity implements LocationListener {

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;
    public static double LAT;
    public static double LONG;
    protected boolean gps_enabled, network_enabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(CheckingPermissionIsEnabledOrNot()){
            //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1*60*1000, 0, this);
            LocationRequest mLocationRequest = LocationRequest.create();
            mLocationRequest.setInterval(1*60*1000);
            mLocationRequest.setFastestInterval(5*1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationCallback mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            //TODO: UI updates.
                            Log.e("MAIN_ACTIVITY", "LAT : " + location.getLatitude() + " LONG : " + location.getLongitude() );
                        }
                    }
                }
            };
            LocationServices.getFusedLocationProviderClient(getApplicationContext()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }else{
            RequestMultiplePermission();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LAT = location.getLatitude();
        LONG = location.getLongitude();
        Log.e("MAIN_ACTIVITY", "LAT : " + location.getLatitude() +" LONG : " + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }

    private void RequestMultiplePermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS} , 0);
    }

    public boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED && SecondPermissionResult == PackageManager.PERMISSION_GRANTED && ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 0 :{
                for(int granted : grantResults){
                  if(granted == 0){
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5*60*1000, 500, this);
                    }else { restartApp(); }
                }
            }
        }
    }


    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        int mPendingIntentId = 2;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

    public void startService(View v){
        startService(new Intent(this, ForegroundServices.class));
    }

    public void stopService(View v){
        Intent intent = new Intent(this, ForegroundServices.class);
       stopService(intent);
    }

}