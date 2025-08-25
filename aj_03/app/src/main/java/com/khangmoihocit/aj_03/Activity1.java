package com.khangmoihocit.aj_03;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Activity1 extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_activity1);

         textView = findViewById(R.id.textview1);
//        textView.setText("aaa");
//        String s = textView.getText().toString();


        Button button = findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Activity1.this, Activity2.class);

                String s = textView.getText().toString();
                intent.putExtra("data", s);

//                startActivity(intent);
                startActivityForResult(intent, Activity2.TRAVETU_ACTIVITY2);
            }
        });

        findViewById(R.id.btnRadio).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(Activity1.this, RadioActivity.class));
            }
        });

    }

    //lấy kết quả trả về từ activity 2
    @Override
    //onActivityResult chỉ được gọi khi khởi động activity khác bằng startActivityResult()
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Activity2.TRAVETU_ACTIVITY2){
            String s = data.getStringExtra("data");
            textView.setText(s);
        }
    }
}
