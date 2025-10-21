package com.khangmoihocit.musicapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null) return;

        Intent serviceIntent = new Intent(context, MusicService.class);

        // Chuyển action nhận được tới Service
        serviceIntent.setAction(action);
        context.startService(serviceIntent);
    }
}
