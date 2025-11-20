package com.khangmoihocit.a3_senddata_activity_to_activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 10;
    private EditText edtEmail;
    private EditText edtUsername;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edt_email);
        edtUsername = findViewById(R.id.edt_username);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivity();
            }
        });
    }

    //cách truyền theo biến và object
    private void nextActivity() {
        String strEmail = edtEmail.getText().toString().trim();
        String strUsername = edtUsername.getText().toString().trim();
        User user = new User(strUsername, strEmail);

        Intent intent = new Intent(MainActivity.this, MainActivity2.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user", user);
//        intent.putExtra("key_email", strEmail);
        intent.putExtras(bundle);
        startActivityForResult(intent, MY_REQUEST_CODE);
    }

    //nhan data tu activity 2
//    @Override
//    protected void onResume() {
//        super.onResume();
//        edtEmail.setText(AppUtil.mEmail);
//    }


    //nhận kết quả từ activity 2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MY_REQUEST_CODE && resultCode == MainActivity2.RESULT_OK){
//            String strEmailUpdate = data.getStringExtra("key_email");
            User user = (User) data.getExtras().get("object_user");
            edtEmail.setText(user.getEmail());
            edtUsername.setText(user.getUsername());
        }
    }
}