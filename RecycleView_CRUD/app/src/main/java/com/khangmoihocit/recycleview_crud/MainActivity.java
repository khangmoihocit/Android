package com.khangmoihocit.recycleview_crud;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.khangmoihocit.recycleview_crud.model.CatAdapter;
import com.khangmoihocit.recycleview_crud.model.SpinnerAdapter;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner;
    private RecyclerView recyclerView;
    private CatAdapter catAdapter;
    private EditText eName, eDescribe, ePrice;
    private Button btnAdd, btnUpdate;
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

        recyclerView = findViewById(R.id.recycleView);
        eName = findViewById(R.id.edName);
        eDescribe = findViewById(R.id.edDescribe);
        ePrice = findViewById(R.id.edPrice);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setEnabled(false);
    }
}