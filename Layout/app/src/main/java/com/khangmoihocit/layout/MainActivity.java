package com.khangmoihocit.layout;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Spinner sp1, sp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_2);

        sp2 = findViewById(R.id.sp2);
        sp1 = findViewById(R.id.sp1);

        String[] list = {"PTIP", "HUST", "NEU", "UET"};
        String[] list1 = getResources().getStringArray(R.array.Country); //lay strings.xml bang code

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_layout, list);
        sp2.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.item_layout, list1);
        sp1.setAdapter(adapter1);
    }
}