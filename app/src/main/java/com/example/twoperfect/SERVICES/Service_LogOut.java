package com.example.twoperfect.SERVICES;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;
import com.example.twoperfect.LogOut;

public class Service_LogOut {
    private static String TAG = "LogOut Service";

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, LogOut.class);
        JobInfo.Builder builder = new JobInfo.Builder(2, serviceComponent);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        builder.setRequiresCharging(false);
        builder.setPersisted(true);
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);


        int resultCode = scheduler.schedule(builder.build());
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.e(TAG, "Job Scheduled");
            scheduler.schedule(builder.build());
        }
        else
        Log.e(TAG, "Job Scheduling failed");

    }

    public static void cancelJob(Context context){
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(2);
        Log.e(TAG, "Job cancelled");
    }
}
