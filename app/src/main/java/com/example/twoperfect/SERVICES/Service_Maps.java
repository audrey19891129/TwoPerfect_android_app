package com.example.twoperfect.SERVICES;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.example.twoperfect.MapsService;
import com.example.twoperfect.NewIntervention;

public class Service_Maps {

    private static String TAG = "Service_Maps";

    public static void scheduleJob(Context context){
        ComponentName serviceComponent = new ComponentName(context, MapsService.class);
        JobInfo.Builder builder = new JobInfo.Builder(5, serviceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network (wifi)
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        builder.setPersisted(true); // will keep running even if application is stopped
        builder.setPeriodic(15*60*1000); //will execute call every 15 minutes =====> 15 MINUTES IS THE MINIMUM!!!
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        int resultCode = scheduler.schedule(builder.build());
        if(resultCode == JobScheduler.RESULT_SUCCESS){
            Log.e(TAG, "Job Scheduled");
            scheduler.schedule(builder.build());
        }
        else{
            Log.e(TAG, "Job Scheduling failed");
        }
    }

    public static void cancelJob(Context context){
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(5);
        Log.e(TAG, "Job cancelled");
    }
}
