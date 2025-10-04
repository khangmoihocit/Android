package com.khangmoihocit.recycleview_crud;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.khangmoihocit.recycleview_crud.model.SpinnerAdapter;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private int []imgs = {R.drawable.img, R.drawable.img_1, R.drawable.img_2, R.drawable.img_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        spinner = findViewById(R.id.img);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this);
        spinner.setAdapter(spinnerAdapter);

    }
}