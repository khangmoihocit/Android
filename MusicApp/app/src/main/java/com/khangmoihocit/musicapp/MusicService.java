package com.khangmoihocit.musicapp;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.RemoteViews;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MusicService extends Service {

    public static MediaPlayer mediaPlayer;
    public static final String CHANNEL_ID = "MusicChannel";

    // Các Action hằng số
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_SEEK = "action_seek";

    private String currentSongTitle = "Không có bài hát";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_STICKY; // Tránh lỗi null

        String action = intent.getAction();

        // Khi bắt đầu phát bài mới từ MainActivity
        if (intent.getStringExtra("songPath") != null) {
            String songPath = intent.getStringExtra("songPath");
            currentSongTitle = songPath.substring(songPath.lastIndexOf("/") + 1);
            playMusic(songPath);
            updateNotification(currentSongTitle);
        }

        // Xử lý các action (từ Notification hoặc Activity)
        if (action != null) {
            switch (action) {
                case ACTION_PLAY:
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        updateNotification(currentSongTitle);
                    }
                    break;
                case ACTION_PAUSE:
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        updateNotification(currentSongTitle);
                    }
                    break;
                case ACTION_NEXT:
                    // TODO: Xử lý chuyển bài
                    break;

                // THÊM CASE MỚI ĐỂ XỬ LÝ TUA NHẠC
                case ACTION_SEEK:
                    int position = intent.getIntExtra("seekToPosition", 0);
                    if (mediaPlayer != null) {
                        mediaPlayer.seekTo(position);
                    }
                    break;
            }
        }

        return START_STICKY;
    }

    private void playMusic(String path) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, Uri.parse(path));
        if (mediaPlayer != null) {
            mediaPlayer.start();
            // TODO: Xử lý setOnCompletionListener để tự động next
        }
    }

    private void updateNotification(String songTitle) {
        // Tạo RemoteViews
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.notification_music);
        views.setTextViewText(R.id.tvSongTitle, songTitle);

        // Tạo PendingIntent cho Play/Pause
        PendingIntent playPending, pausePending;

        Intent playIntent = new Intent(this, NotificationReceiver.class);
        playIntent.setAction(ACTION_PLAY);
        playPending = PendingIntent.getBroadcast(this, 0, playIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent pauseIntent = new Intent(this, NotificationReceiver.class);
        pauseIntent.setAction(ACTION_PAUSE);
        pausePending = PendingIntent.getBroadcast(this, 1, pauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Tạo PendingIntent cho Next
        Intent nextIntent = new Intent(this, NotificationReceiver.class);
        nextIntent.setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this, 2, nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Gán sự kiện click dựa trên trạng thái
        boolean isPlaying = (mediaPlayer != null && mediaPlayer.isPlaying());

        views.setOnClickPendingIntent(R.id.imgPlayPause, isPlaying ? pausePending : playPending);
        views.setOnClickPendingIntent(R.id.imgNext, nextPending);

        // Cập nhật icon Play/Pause
        views.setImageViewResource(R.id.imgPlayPause,
                isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);

        // Xây dựng Notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(views)
                .setOngoing(true) // Giữ thông báo
                .build();

        startForeground(1, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Music Channel", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Kênh phát nhạc");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
    }
}