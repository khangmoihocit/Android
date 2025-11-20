package com.khangmoihocit.a3_senddata_activity_to_activity;

import android.app.Activity;
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

public class MainActivity2 extends AppCompatActivity {

    private EditText edtEmail, edtUsername;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        edtEmail = findViewById(R.id.edt_email);
        edtUsername = findViewById(R.id.edt_username);
        btnUpdate = findViewById(R.id.btn_update);

        //nhan data từ activity1
//        edtEmail.setText(getIntent().getStringExtra("key_email"));

        if(getIntent().getExtras() != null){
            User user = (User) getIntent().getExtras().get("object_user");
            edtEmail.setText(user.getEmail());
            edtUsername.setText(user.getUsername());
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backActivity();
            }
        });
    }

    private void backActivity() {
        String strEmailUpdate = edtEmail.getText().toString().trim();
        String strUsernameUpdate = edtUsername.getText().toString().trim();

        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user", new User(strUsernameUpdate, strEmailUpdate));
        setResult(Activity.RESULT_OK, new Intent().putExtras(bundle)); //trả về

        finish();
    }
}