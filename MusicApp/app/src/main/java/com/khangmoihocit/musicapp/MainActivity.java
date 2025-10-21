package com.khangmoihocit.musicapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer; // Vẫn giữ để lấy Duration, nhưng không phát
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Button; // Dùng MaterialButton nhưng kiểu là Button
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton; // Import
import com.khangmoihocit.musicapp.model.MusicFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvSongs;
    MaterialButton btnPlay, btnPause, btnNext, btnPrev;
    List<MusicFile> musicList = new ArrayList<>();

    private static MediaPlayer localMediaPlayer; // Dùng để quản lý seekbar

    MusicFile currentSong;
    private final int REQUEST_PERMISSION = 100;

    SeekBar seekBar;
    TextView tvTime;
    Handler handler = new Handler();
    int currentIndex = -1;
    Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvSongs = findViewById(R.id.rvSongs);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        seekBar = findViewById(R.id.seekBar);
        tvTime = findViewById(R.id.tvTime);

        rvSongs.setLayoutManager(new LinearLayoutManager(this));

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        } else {
            loadMusic();
        }

        // Sự kiện các nút sẽ gửi Intent tới Service
        btnPlay.setOnClickListener(v -> sendActionToService(MusicService.ACTION_PLAY));
        btnPause.setOnClickListener(v -> sendActionToService(MusicService.ACTION_PAUSE));
        btnNext.setOnClickListener(v -> playNextSong());
        btnPrev.setOnClickListener(v -> playPreviousSong());

        // Xử lý SeekBar (Giữ nguyên)
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Chỉ cập nhật text thời gian khi người dùng kéo (fromUser)
                if (fromUser) {
                    int totalDuration = 0;
                    // Cố gắng lấy tổng thời gian từ Service hoặc localPlayer
                    if (MusicService.mediaPlayer != null) {
                        try { totalDuration = MusicService.mediaPlayer.getDuration(); } catch (Exception e) {}
                    } else if (localMediaPlayer != null) {
                        try { totalDuration = localMediaPlayer.getDuration(); } catch (Exception e) {}
                    }
                    tvTime.setText(formatTime(progress) + "/" + formatTime(totalDuration));
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {
                stopUpdatingSeekBar();
            }
            @Override public void onStopTrackingTouch(SeekBar seekBar) {
                sendSeekToService(seekBar.getProgress());

                // Và bắt đầu cập nhật seekbar tự động trở lại
                startUpdatingSeekBar();
            }
        });
    }

    private void sendSeekToService(int progress) {
        if (currentSong == null) return; // Không tua khi chưa có nhạc

        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.ACTION_SEEK);
        intent.putExtra("seekToPosition", progress);
        startService(intent);
    }

    // Hàm mới: Gửi Action (Play/Pause) đến Service
    private void sendActionToService(String action) {
        if (currentSong == null) {
            Toast.makeText(this, "Chưa chọn bài hát", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(action);
        startService(intent);

        // Cập nhật UI (giả định)
        if (action.equals(MusicService.ACTION_PLAY)) {
            startUpdatingSeekBar(); // Bắt đầu cập nhật lại
        } else if (action.equals(MusicService.ACTION_PAUSE)) {
            stopUpdatingSeekBar(); // Dừng cập nhật
        }
    }


    private void loadMusic() {
        musicList.clear();
        Cursor cursor =
                getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Audio.Media.IS_MUSIC + " != 0", null, null);

        if (cursor != null) {
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int dataColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            while (cursor.moveToNext()) {
                String title = cursor.getString(titleColumn);
                String artist = cursor.getString(artistColumn);
                String path = cursor.getString(dataColumn);
                musicList.add(new MusicFile(title, artist, path));
            }
            cursor.close();
        }

        MusicAdapter adapter = new MusicAdapter(musicList, musicFile -> {
            currentSong = musicFile;
            currentIndex = musicList.indexOf(musicFile);
            playSelectedSong(musicFile);
        });
        rvSongs.setAdapter(adapter);
    }

    // CẬP NHẬT HÀM: Giờ đây sẽ khởi chạy Service
    private void playSelectedSong(MusicFile file) {
        Intent serviceIntent = new Intent(this, MusicService.class);
        serviceIntent.putExtra("songPath", file.path);
        startService(serviceIntent);

        // Khởi tạo 1 MediaPlayer CỤC BỘ (local) chỉ để lấy Duration và cập nhật SeekBar
        // Đây là cách "ăn gian" vì chúng ta không dùng BoundService
        updateUIForNewSong(file);
    }

    // Hàm mới: Cập nhật UI (SeekBar/Time)
    private void updateUIForNewSong(MusicFile file) {
        if (localMediaPlayer != null) {
            localMediaPlayer.release();
        }
        localMediaPlayer = MediaPlayer.create(this, Uri.parse(file.path));
        if (localMediaPlayer == null) return;

        seekBar.setMax(localMediaPlayer.getDuration());
        startUpdatingSeekBar();

        // Giả lập setOnCompletionListener để tự động next (dựa trên duration)
        // (Cách này không chính xác 100% nếu Service bị lag/dừng)
        new Handler().postDelayed(this::playNextSong, localMediaPlayer.getDuration() + 200);
    }

    private void startUpdatingSeekBar() {
        stopUpdatingSeekBar(); // Dừng runnable cũ nếu có
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                // Chúng ta phải lấy tiến độ từ Service MediaPlayer
                if (MusicService.mediaPlayer != null) {
                    try {
                        if (MusicService.mediaPlayer.isPlaying()) {
                            int current = MusicService.mediaPlayer.getCurrentPosition();
                            int total = MusicService.mediaPlayer.getDuration();
                            seekBar.setProgress(current);
                            tvTime.setText(formatTime(current) + "/" + formatTime(total));
                        }
                    } catch (IllegalStateException e) {
                        // Service có thể đã dừng
                    }
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(updateSeekBar);
    }

    private void stopUpdatingSeekBar() {
        if (handler != null && updateSeekBar != null) {
            handler.removeCallbacks(updateSeekBar);
        }
    }


    private void playNextSong() {
        if (currentIndex != -1 && currentIndex + 1 < musicList.size()) {
            MusicFile next = musicList.get(currentIndex + 1);
            // Cập nhật currentIndex
            currentIndex = currentIndex + 1;
            currentSong = next;
            playSelectedSong(next);
        } else {
            Toast.makeText(this, "Hết danh sách nhạc", Toast.LENGTH_SHORT).show();
            currentIndex = -1; // Reset
            currentSong = null;
            stopUpdatingSeekBar();
        }
    }

    private void playPreviousSong() {
        if (currentIndex > 0) {
            MusicFile prev = musicList.get(currentIndex - 1);
            // Cập nhật currentIndex
            currentIndex = currentIndex - 1;
            currentSong = prev;
            playSelectedSong(prev);
        } else {
            Toast.makeText(this, "Đây là bài đầu tiên", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatTime(int millis) {
        int minutes = (millis / 1000) / 60;
        int seconds = (millis / 1000) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        stopUpdatingSeekBar();
        if (localMediaPlayer != null) {
            localMediaPlayer.release();
            localMediaPlayer = null;
        }
        // Tùy chọn: Dừng Service khi thoát App
        // stopService(new Intent(this, MusicService.class));
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] perms, @NonNull int[] results) {
        super.onRequestPermissionsResult(requestCode, perms, results);
        if (requestCode == REQUEST_PERMISSION) {
            if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) {
                loadMusic();
            } else {
                Toast.makeText(this, "Không có quyền đọc bộ nhớ!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}