package com.khangmoihocit.firtapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FirstApp extends AppCompatActivity {
    private String Tag = "FirstActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(Tag, "onCreate");
        setContentView(R.layout.layout_firstactivity);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Toast.makeText(FirstApp.this, "ban bam vao nut second", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(FirstApp.this, SecondActivity.class);

                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(Tag, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Tag, "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(Tag, "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(Tag, "onStop");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(Tag, "onRestart");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(Tag, "onDestroy");

    }
}
