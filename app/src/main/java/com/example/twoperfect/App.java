package com.example.twoperfect;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID = "Channel123";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nc = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationsService",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            nc.setDescription("This is Channel123");


            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(nc);
        }
    }
}
