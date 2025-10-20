package com.khangmoihocit.a1_sqllite_room;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.khangmoihocit.a1_sqllite_room.database.UserDatabase;

import java.util.List;

public class UpdateActivity extends AppCompatActivity {
    private EditText edtUsername, edtAddress;
    private Button btnUpdate;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        edtUsername = findViewById(R.id.edt_username);
        edtAddress = findViewById(R.id.edt_address);
        btnUpdate = findViewById(R.id.btn_update_user);

        mUser = (User) getIntent().getExtras().get("object_user");
        if(mUser != null){
            edtUsername.setText(mUser.getUsername());
            edtAddress.setText(mUser.getAddress());
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
    }

    private void updateUser() {
        String username = edtUsername.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(address)){
            return;
        }

        mUser.setUsername(username);
        mUser.setAddress(address);

        if(isUserExist(mUser)){
            Toast.makeText(this, "username exist", Toast.LENGTH_SHORT).show();
            return;
        }
        UserDatabase.getInstance(this).userDAO().updateUser(mUser);
        Toast.makeText(this, "Update user successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private boolean isUserExist(User user){
        List<User> users = UserDatabase.getInstance(this).userDAO().checkUser(user.getUsername());
        return users != null && !users.isEmpty();
    }
}