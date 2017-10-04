package com.example.deepakrattan.jobschedulerdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

/**
 * Created by deepak.rattan on 10/4/2017.
 */

//We are creating a service that will be run at the time
// determined by the conditions

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NotificationJobService extends JobService {
    public static final int NOTIFICATION_ID = 0;

    //onStartJob() called when the system determines that your task should be run.
    // You implement the job to be done in this method.
    //returns a boolean indicating whether the job needs to continue on a
    // separate thread. If true, the work is offloaded to a different thread,
    // and your app must call jobFinished() explicitly in that thread to indicate
    // that the job is complete. If the return value is false,
    // the framework knows that the job is completed by the end of onStartJob()
    // and it will automatically call jobFinished() on your behalf.

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_content))
                .setSmallIcon(R.mipmap.ic_job)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        return false;
    }

    //onStopJob() called if the conditions are no longer met,
    // meaning that the job must be stopped.
    //returns a boolean that determines what to do if the job is not finished.
    // If the return value is true, the job will be rescheduled, otherwise, it will be dropped.

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
