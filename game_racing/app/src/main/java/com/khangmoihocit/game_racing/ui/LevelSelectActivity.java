package com.khangmoihocit.game_racing.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.khangmoihocit.game_racing.R;

public class LevelSelectActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_level_select);

        // [cite: 42]
        int[] ids = {R.id.btnL1, R.id.btnL2, R.id.btnL3, R.id.btnL4, R.id.btnL5};
        for (int id : ids) {
            findViewById(id).setOnClickListener(this); // [cite: 43]
        }
    }

    // --- CẢI TIẾN ---
    // Code gốc trong tài liệu [cite: 45, 46] có vẻ bị lỗi OCR/gõ máy.
    // Dưới đây là phiên bản đã sửa lại cho chính xác.
    @Override
    public void onClick(View v) {
        int lv = 1; // [cite: 45]
        int id = v.getId();

        if (id == R.id.btnL1) {
            lv = 1;
        } else if (id == R.id.btnL2) {
            lv = 2;
        } else if (id == R.id.btnL3) {
            lv = 3;
        } else if (id == R.id.btnL4) {
            lv = 4;
        } else if (id == R.id.btnL5) {
            lv = 5;
        }

        Intent it = new Intent(this, GameActivity.class); // [cite: 47]
        it.putExtra("level", lv); // [cite: 47, 48]
        startActivity(it); // [cite: 48]
    }
}