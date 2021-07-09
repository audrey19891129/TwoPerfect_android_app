package com.example.twoperfect.SERVICES;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import static com.example.twoperfect.App.CHANNEL_ID;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.twoperfect.MainActivity;
import com.example.twoperfect.R;
import com.example.twoperfect.SERVICES.Service_Maps;
import com.example.twoperfect.SERVICES.Service_NewIntervention;

public class ForegroundServices extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("TwoPerfectService")
                .setContentText("L'application est activee")
                .setSmallIcon(R.drawable.inter_telcom_logo)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(3, notification);
        //do heavy work on a background thread
        //stopSelf();*/
        Service_NewIntervention.scheduleJob(getApplicationContext());
        Service_Maps.scheduleJob(getApplicationContext());
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
