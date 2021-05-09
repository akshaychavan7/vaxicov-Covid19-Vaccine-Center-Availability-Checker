package com.akshaychavan.vaxicov;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.akshaychavan.vaxicov.utility.GlobalCode;

/**
 * Created by Akshay Chavan on 08,May,2021
 * akshaychavan.kkwedu@gmail.com
 */
public class NotificationReceiver extends BroadcastReceiver {
    final String TAG = "NotificationReceiver";
    public final String NOTIFICATION_CHANNEL_ID = "Channel1";
    GlobalCode globalCode = GlobalCode.getInstance();
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        Log.e(TAG, "Inside NotificationReceiver");
        Intent repeating_intent = new Intent(context, MainActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.flag1)
                .setContentTitle("Vaccination Center Availability Notification")
                .setAutoCancel(true);

        if(globalCode.isPin_availability()) {
            builder.setContentText("vaccination appointments are available in your local area centers");
            notificationManager.notify(100, builder.build());
        } else if(globalCode.isDistrict_availability()) {
            builder.setContentText("vaccination appointments are available at your district");
            notificationManager.notify(100, builder.build());
        }

    }
}
