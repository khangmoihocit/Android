package com.khangmoihocit.th_quanlynhansu;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "CHANNEL_1";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    //them name o android mainfest nua
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "chanel 1";
            String description = "chanel 1 1";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null) notificationManager.createNotificationChannel(channel);
        }
    }
}