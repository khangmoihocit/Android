package com.khangmoihocit.btth2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;


import java.util.Random;

public class MainActivity2 extends AppCompatActivity {

    ImageView ivDice;
    LinearLayout mainLayout;

    final int[] diceImages = {
            R.drawable.mat1,
            R.drawable.mat2,
            R.drawable.mat3,
            R.drawable.mat4,
            R.drawable.mat5,
            R.drawable.mat6
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ivDice = findViewById(R.id.ivDice1);
        mainLayout = findViewById(R.id.mainLayout1);

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();
            }
        });
    }

    private void rollDice() {
        Random random = new Random();
        int randomNumber = random.nextInt(6);

        ivDice.setImageResource(diceImages[randomNumber]);
    }
}