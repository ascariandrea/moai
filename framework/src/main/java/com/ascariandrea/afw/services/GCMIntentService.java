package com.ascariandrea.afw.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.ascariandrea.afw.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;


/**
 * Created by andreaascari on 24/02/14.
 */
public class GCMIntentService extends IntentService {
    private static final String TAG = GCMIntentService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 100;
    private NotificationManager mNotificationManager;
    private int notificationCount = 0;
    private int mCounter = 0;

    public GCMIntentService() {
        super("Gift!");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i(TAG, extras.toString());
                sendNotification(extras);

            }
        }
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(Bundle extras) {
        mNotificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        buildNotificationPayload(extras, new Intent(), stackBuilder);
    }

    protected void buildNotificationPayload(Bundle extras, Intent notificationIntent, TaskStackBuilder stackBuilder) {

    }

    protected void buildNotification(Notification notification, Intent intent, TaskStackBuilder stackBuilder) {
        mCounter++;
        Log.d(TAG, "Build notification: " + mCounter);


        stackBuilder.addNextIntent(intent);


        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(notification.getSmallIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), notification.getLargeIcon()))
                .setContentTitle(notification.getTitle())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notification.getMessage()))
                .setContentText(notification.getMessage())
                .setAutoCancel(true)
                .setLights(0xFF00FFcc, 500, 500);

        PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);


        android.app.Notification n = mBuilder.build();

        n.flags |= android.app.Notification.FLAG_AUTO_CANCEL;
        n.defaults |= android.app.Notification.DEFAULT_LIGHTS;
        n.defaults |= android.app.Notification.DEFAULT_VIBRATE;

        mNotificationManager.notify(0, n);

    }


    public static class Notification {
        private String mTitle;
        private String mMessage;
        private int mSmallIcon;
        private int mLargeIcon;

        public Notification(String title, String message) {
            this.mTitle = title;
            this.mMessage = message;
        }

        public void setTitle(String title) {
            this.mTitle = title;
        }

        public void setMessage(String message) {
            this.mMessage = message;
        }

        public void setmSmallIcon(int smallIcon) {
            this.mSmallIcon = smallIcon;
        }

        public void setLargeIcon(int largeIcon) {
            mLargeIcon = largeIcon;
        }


        public int getSmallIcon() {
            return mSmallIcon;
        }

        public String getTitle() {
            return mTitle;
        }

        public String getMessage() {
            return mMessage;
        }

        public int getLargeIcon() {
            return mLargeIcon;
        }
    }

}
