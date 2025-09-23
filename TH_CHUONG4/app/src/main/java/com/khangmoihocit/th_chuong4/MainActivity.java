package com.khangmoihocit.th_chuong4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddToCart1 = findViewById(R.id.btnAddToCart1);
        Button btnAddToCart2 = findViewById(R.id.btnAddToCart2);
        Button btnAddToCart3 = findViewById(R.id.btnAddToCart3);

        TextView tvFoodName1 = findViewById(R.id.tvFoodName1);
        TextView tvFoodName2 = findViewById(R.id.tvFoodName2);
        TextView tvFoodName3 = findViewById(R.id.tvFoodName3);

        btnAddToCart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = tvFoodName1.getText().toString();

                String message = "Đã thêm " + foodName + " vào giỏ hàng.";

                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        btnAddToCart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = tvFoodName2.getText().toString();

                String message = "Đã thêm " + foodName + " vào giỏ hàng.";

                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        btnAddToCart3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = tvFoodName3.getText().toString();

                String message = "Đã thêm " + foodName + " vào giỏ hàng.";

                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}