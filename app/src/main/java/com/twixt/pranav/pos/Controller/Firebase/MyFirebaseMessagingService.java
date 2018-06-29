package com.twixt.pranav.pos.Controller.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.twixt.pranav.pos.R;
import com.twixt.pranav.pos.View.Activity.Notification_activity;

/**
 * Created by Pranav on 1/12/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {




//        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//        Log.d(TAG, "Titleee " + remoteMessage.getNotification().getTitle());
//        Map map = remoteMessage.getData();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String text = remoteMessage.getNotification().getBody();
//        String ttle = remoteMessage.getNotification().getTitle();
//        Toast.makeText(this, ttle, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, Notification_activity.class);
        intent.putExtra("TITLE", remoteMessage.getData().get("title"));
        intent.putExtra("MESSAGE", text);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                0);
        Notification n = new Notification.Builder(this)
                .setContentTitle(remoteMessage.getData().get("title"))
//                .setContentText(remoteMessage.getNotification().getBody())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setSmallIcon(R.drawable.logo)
//                .setContentIntent(pIntent)
                .setStyle(new Notification.BigTextStyle().bigText(text))
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build();

//                .addAction(R.drawable.icon, "Call", pIntent)
//                .addAction(R.drawable.icon, "More", pIntent)
//                .addAction(R.drawable.icon, "And more", pIntent).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);

    }
}
