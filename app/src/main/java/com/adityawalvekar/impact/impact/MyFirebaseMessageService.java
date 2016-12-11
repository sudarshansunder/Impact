package com.adityawalvekar.impact.impact;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessageService extends FirebaseMessagingService {

    private static final String GROUP_ID = "messages";
    private static int id = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        final String message = remoteMessage.getData().get("message_body");
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("New post! Please refresh your feed. ")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_impact)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(GROUP_ID);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(id, builder.build());
        Log.d("Notif ID", id + "");
        id++;
    }
}