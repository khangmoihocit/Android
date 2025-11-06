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

    private ActivityGameBinding binding;
    private SensorManager sm;
    private Sensor gravitySensor; // Đổi tên cho rõ nghĩa
    private GameView gameView;

    // Biến để lưu giá trị cảm biến hiện tại (đã bỏ bộ lọc)
    private float currentRx = 0, currentRy = 0;
    // Biến để calibrate
    private float offsetX = 0, offsetY = 0;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gameView = binding.gameView;

        int lv = getIntent().getIntExtra("level", 1);
        gameView.setLevel(lv);
        binding.tvLevel.setText("Lv" + lv);

        binding.btnPause.setOnClickListener(v -> {
            boolean p = gameView.togglePause();
            binding.tvPaused.animate().alpha(p ? 1f : 0f).setDuration(150).start();
            binding.btnPause.setText(p ? "Resume" : "Pause");
        });

        binding.btnCalib.setOnClickListener(v -> {
            offsetX = currentRx;
            offsetY = currentRy;
            Toast.makeText(this, "Đã calibrate", Toast.LENGTH_SHORT).show();
        });

        gameView.setHudListener(new GameView.HudListener() {
            @Override
            public void onHud(int level, int score, float timeLeft) {
                runOnUiThread(() -> {
                    binding.tvLevel.setText("Lv" + level);
                    binding.tvScore.setText("Score: " + score);
                    binding.tvTime.setText(String.format(java.util.Locale.US, "%.1fs", timeLeft));
                });
            }

            @Override
            public void onGameOver(int finalScore) {
                runOnUiThread(() -> {
                    binding.gameOverLayout.setVisibility(View.VISIBLE);
                    binding.tvFinalScore.setText("Final Score: " + finalScore);
                    binding.btnPause.setEnabled(false);
                    binding.btnCalib.setEnabled(false);
                    binding.tvPaused.animate().alpha(0f).setDuration(100).start();
                });
            }
        });

        binding.btnPlayAgain.setOnClickListener(v -> {
            recreate();
        });

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        // ============================================
        // === SỬA LỖI 1: ĐỔI SANG CẢM BIẾN GRAVITY ===
        // ============================================
        gravitySensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (gravitySensor != null) {
            sm.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (gravitySensor != null) {
            sm.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        // ============================================
        // === SỬA LỖI 2: BỎ BỘ LỌC GÂY TRỄ (LAG) ===
        // ============================================

        // Lấy giá trị X và Y trực tiếp từ cảm biến
        currentRx = e.values[0]; // Trục X: Nghiêng trái/phải
        currentRy = e.values[1]; // Trục Y: Nghiêng trước/sau

        // Truyền thẳng giá trị đã calibrate vào GameView
        gameView.onTiltInput(currentRx - offsetX, currentRy - offsetY);
    }

    @Override
    public void onAccuracyChanged(Sensor s, int a) {
    }
}