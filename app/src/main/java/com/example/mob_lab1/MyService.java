package com.example.mob_lab1;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyService extends FirebaseMessagingService {
    private static final String TAG = "tag";

    public MyService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String id = "";
        String textData;

        // Check if message contains a data payload.
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        if (remoteMessage.getData().size() > 0) {

            id = remoteMessage.getData().get("id");
            textData = remoteMessage.getData().get("textData");
            NotificationCompat.Builder notificationBuilder = null;
            // Check if message contains a notification payload.
            notificationBuilder = new NotificationCompat.Builder(this, "lab_channel")
                    .setContentTitle("id of your objects is -" + id)
                    .setContentText("this is a plane")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);

            if (!id.isEmpty()) {
                Intent intent = new Intent(getApplicationContext(), Information.class);
                intent.putExtra("id", id);
                intent.putExtra("textData", textData);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                notificationBuilder.setContentIntent(pi);
            }
            Notification notification = notificationBuilder.build();
            NotificationManagerCompat.from(getApplicationContext()).notify(111, notification);
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
