package com.traidev.shokanjali.Extras;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.traidev.shokanjali.MainActivity;
import com.traidev.shokanjali.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMesgServices extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData() != null) {

            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);

            try {
                String title = object.getString("title");
                String text = object.getString("message");
                Log.d("stitle",title);

                showNotification(title, text);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void showNotification(String title, String message) {

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("userShokanjali",
                    "userShokanjali",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Shokanjali Details App");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "userShokanjali")
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setSmallIcon(R.mipmap.ic_launcher)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}


