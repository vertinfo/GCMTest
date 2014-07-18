package com.verticalinfo.gcmtest.gcmtest;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by dan on 7/16/14.
 */
public class GcmMessageHandler extends IntentService {

    String mes;
    private Handler handler;
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

        @Override
        public void onCreate() {
            // TODO Auto-generated method stub
            super.onCreate();
            handler = new Handler();
        }
        @Override
        protected void onHandleIntent(Intent intent) {
            Bundle extras = intent.getExtras();

            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            // The getMessageType() intent parameter must be the intent you received
            // in your BroadcastReceiver.
            String messageType = gcm.getMessageType(intent);

            mes = extras.getString("message");
            //showToast();
           // Log.i("GCM", "Received : (" + messageType + ")  " + extras.getString("message"));
            sendNotification("Received: " + mes);
            GcmBroadcastReceiver.completeWakefulIntent(intent);

        }

        public void showToast(){
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
                }
            });

        }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, GCMTestMain.class), 0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.powered_by_google_light)
                        .setContentTitle("GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setSound(alarmSound)
                        .setAutoCancel(true)
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
