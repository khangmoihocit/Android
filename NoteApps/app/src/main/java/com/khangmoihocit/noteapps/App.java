package com.khangmoihocit.noteapps;


import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    // ID duy nhất cho kênh thông báo nhắc nhở
    public static final String CHANNEL_ID = "notes_reminders";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Chỉ tạo kênh trên API 26+ vì lớp NotificationChannel là mới
        // và không cần thiết trên các phiên bản cũ hơn.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notes Reminders";
            String description = "Channel for note reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Đăng ký kênh với hệ thống
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}