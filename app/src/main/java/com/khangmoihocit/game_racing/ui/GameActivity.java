package com.khangmoihocit.game_racing.ui;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.khangmoihocit.game_racing.databinding.ActivityGameBinding;


public class GameActivity extends AppCompatActivity implements SensorEventListener {

    // Khai báo ViewBinding
    private ActivityGameBinding binding;

    private SensorManager sm;
    private Sensor accel;
    private GameView gameView; // [cite: 93]

    private float fx = 0, fy = 0, offsetX = 0, offsetY = 0; // [cite: 94]
    private final float ALPHA = 0.1f; // [cite: 94]

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        // Sử dụng ViewBinding thay vì setContentView(R.layout.activity_game)
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Truy cập view thông qua binding object
        gameView = binding.gameView;

        // [cite: 99]
        int lv = getIntent().getIntExtra("level", 1);
        gameView.setLevel(lv);
        binding.tvLevel.setText("Lv" + lv);

        // [cite: 100]
        binding.btnPause.setOnClickListener(v -> {
            boolean p = gameView.togglePause();
            // [cite: 101]
            binding.tvPaused.animate().alpha(p ? 1f : 0f).setDuration(150).start();
            binding.btnPause.setText(p ? "Resume" : "Pause"); // [cite: 102]
        });

        // [cite: 103]
        binding.btnCalib.setOnClickListener(v -> {
            offsetX = fx;
            offsetY = fy;
            Toast.makeText(this, "Đã calibrate", Toast.LENGTH_SHORT).show(); // [cite: 104]
        });

        // [cite: 104]
        gameView.setHudListener(new GameView.HudListener() {
            @Override
            public void onHud(int level, int score, float timeLeft) {
                // Đây là code cập nhật HUD (giống hệt code cũ)
                runOnUiThread(() -> {
                    binding.tvLevel.setText("Lv" + level);
                    binding.tvScore.setText("Score: " + score);
                    binding.tvTime.setText(String.format(java.util.Locale.US, "%.1fs", timeLeft));
                });
            }

            @Override
            public void onGameOver(int finalScore) {
                // Đây là code MỚI khi game kết thúc
                runOnUiThread(() -> {
                    // 1. Hiện màn hình Game Over
                    binding.gameOverLayout.setVisibility(View.VISIBLE);
                    // 2. Cập nhật điểm cuối
                    binding.tvFinalScore.setText("Final Score: " + finalScore);

                    // 3. (Nên làm) Vô hiệu hóa các nút Pause/Calib
                    binding.btnPause.setEnabled(false);
                    binding.btnCalib.setEnabled(false);

                    // 4. (Nên làm) Ẩn chữ "PAUSED" nếu nó đang hiện
                    binding.tvPaused.animate().alpha(0f).setDuration(100).start();
                });
            }
        });

        binding.btnPlayAgain.setOnClickListener(v -> {
            // Cách đơn giản nhất để chơi lại là khởi động lại Activity này
            recreate();
        });

        sm = (SensorManager) getSystemService(SENSOR_SERVICE); // [cite: 107]
        accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); // [cite: 108]
    }

    @Override
    protected void onResume() {
        super.onResume();
//        gameView.startLoop(); // [cite: 110]
        if (accel != null) {
            sm.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME); // [cite: 110]
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        gameView.stopLoop(); // [cite: 111]
        if (accel != null) {
            sm.unregisterListener(this); // [cite: 111]
        }
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        float rx = e.values[0], ry = e.values[1]; // [cite: 113]
        // Low-pass filter để làm mượt dữ liệu sensor
        fx = fx + ALPHA * (rx - fx); // [cite: 113]
        fy = fy + ALPHA * (ry - fy); // [cite: 113]
        gameView.onTiltInput(fx - offsetX, fy - offsetY); // [cite: 113]
    }

    @Override
    public void onAccuracyChanged(Sensor s, int a) { // [cite: 115]
    }
}