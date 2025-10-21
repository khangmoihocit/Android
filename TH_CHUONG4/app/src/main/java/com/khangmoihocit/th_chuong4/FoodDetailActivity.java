package com.khangmoihocit.th_chuong4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FoodDetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView tvFoodName, tvFoodPrice, tvFoodDescription;
    private Button btnAddToCart;
    private MonAn mMonAn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        initUI();

        mMonAn = (MonAn) getIntent().getExtras().get("object_food");

        if(mMonAn != null){
            imageView.setImageResource(mMonAn.getHinhAnh());
            tvFoodName.setText(mMonAn.getTenMon());
            tvFoodPrice.setText(mMonAn.getGia());
            tvFoodDescription.setText(mMonAn.getMoTa());
        }

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
//        finish();
    }

    private void initUI(){
        imageView = findViewById(R.id.ivDetailFoodImage);
        tvFoodName = findViewById(R.id.tvDetailFoodName);
        tvFoodPrice = findViewById(R.id.tvDetailFoodPrice);
        tvFoodDescription = findViewById(R.id.tvDetailFoodDescription);
        btnAddToCart = findViewById(R.id.btnDetailAddToCart);
    }
}