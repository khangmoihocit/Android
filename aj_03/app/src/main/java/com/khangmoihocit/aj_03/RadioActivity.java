package com.khangmoihocit.aj_03;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RadioActivity extends AppCompatActivity {
    private RadioButton rb_a, rb_b, rb_c;
    private Button btnResult;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_radio);


        rb_a = findViewById(R.id.radio1);
        rb_b = findViewById(R.id.radio2);
        rb_c = findViewById(R.id.radio3);

        btnResult = findViewById(R.id.btnResult);
        message = findViewById(R.id.msg);

        rb_a.setOnCheckedChangeListener(listener);
        rb_b.setOnCheckedChangeListener(listener);
        rb_c.setOnCheckedChangeListener(listener);

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb_c.isChecked())
                {
                    message.setText("your result is true");
                }else{
                    message.setText("your result is false");

                }
            }
        });
    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
            btnResult.setEnabled(true);

            if(isChecked){
                String s = buttonView.getText().toString();
                message.setText("your choose is : " + s);
            }
        }
    };

}