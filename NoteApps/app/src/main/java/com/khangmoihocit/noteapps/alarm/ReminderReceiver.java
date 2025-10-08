package com.khangmoihocit.noteapps.alarm;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.khangmoihocit.noteapps.App;
import com.khangmoihocit.noteapps.R;
import com.khangmoihocit.noteapps.ui.EditorActivity;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra(AlarmHelper.EXTRA_ID, -1);
        String title = intent.getStringExtra(AlarmHelper.EXTRA_TITLE);

        if (id == -1) return;

        // Tạo intent để mở lại EditorActivity khi người dùng nhấn vào thông báo
        Intent openEditorIntent = new Intent(context, EditorActivity.class);
        openEditorIntent.putExtra("id", id);

        int requestCode = (int)(id & 0x7fffffff);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent contentPendingIntent = PendingIntent.getActivity(context, requestCode, openEditorIntent, flags);

        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, App.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name) // Icon này sẽ được tạo ở Phần 6
                .setContentTitle("Nhắc nhở: " + (title == null || title.isEmpty() ? "Ghi chú" : title))
                .setContentText("Đến giờ xem lại ghi chú của bạn.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true); // Tự động xóa thông báo khi người dùng nhấn vào

        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(requestCode, builder.build());
    }
}