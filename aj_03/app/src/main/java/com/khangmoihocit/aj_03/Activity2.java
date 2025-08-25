package com.khangmoihocit.aj_03;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity2 extends AppCompatActivity {
    public static final int TRAVETU_ACTIVITY2 = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity2);

        //lấy data từ bên activity 1 truyền sang
        Intent i = getIntent();
        String data = i.getStringExtra("data");

        EditText editText = findViewById(R.id.editText);
        editText.setText(data);
        Button button = findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //gửi lại data sang bên activity 1
                Intent i = new Intent();
                i.putExtra("data", editText.getText().toString());
                setResult(TRAVETU_ACTIVITY2, i);


                finish();
            }
        });
    }
}