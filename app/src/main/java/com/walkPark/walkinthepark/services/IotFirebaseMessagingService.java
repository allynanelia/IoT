package com.walkPark.walkinthepark.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.walkPark.walkinthepark.R;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.walkPark.walkinthepark.activities.MainActivity;
import com.walkPark.walkinthepark.events.PushNotificationEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by nanelia on 12/4/18.
 */

public class IotFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
           sendNotification(remoteMessage.getData());
        }
    }

    private void sendNotification(final Map<String,String> message) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new PushNotificationEvent(message));
            }
        }, 2000 );

    }

}
