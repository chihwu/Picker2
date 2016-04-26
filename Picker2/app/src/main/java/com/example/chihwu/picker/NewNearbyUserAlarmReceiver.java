package com.example.chihwu.picker;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.util.Log;
import android.provider.Settings;

/**
 * Created by ChihWu on 4/24/16.
 */
public class NewNearbyUserAlarmReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {

        Toast.makeText(context, "ALARM TRIGGERED", Toast.LENGTH_SHORT).show();
        /* We will send a notification to notify a new user is nearby when this alarm is triggered */
        Notification notification = new Notification.Builder(context)
                .setContentTitle("New Nearby User")
                .setContentText("A new user nearby is found. Check it out in your account.")
                .setSmallIcon(R.mipmap.ic_launcher)  // note that an icon is required in order for a notification to appear.
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000 })  // we make the Notification vibrate our phone
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)  // we also set the default notification sound for this notification
                .build();

        Log.i("CONTEXT CONTENT", context.toString());

        int mNotificationId = 001;
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationId, notification);
        /*******/
    }

}
