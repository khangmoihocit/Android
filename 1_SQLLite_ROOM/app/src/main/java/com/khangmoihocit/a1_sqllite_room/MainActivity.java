package com.khangmoihocit.a1_sqllite_room;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khangmoihocit.a1_sqllite_room.database.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsername, edtAddress;
    private Button btnAddUser;
    private RecyclerView rcvUser;
    private List<User> mListUser;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        userAdapter = new UserAdapter(new UserAdapter.IClickItemUser() {
            @Override
            public void updateUser(User user) {
                clickUpdateUser(user);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvUser.setLayoutManager(linearLayoutManager);
        rcvUser.setAdapter(userAdapter);

        loadData();

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }

    private void clickUpdateUser(User user){

    }

    private void addUser() {
        String username = edtUsername.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(address)){
            return;
        }

        User user = new User(username, address);

        if(isUserExist(user)){
            Toast.makeText(this, "username exist", Toast.LENGTH_SHORT).show();
            return;
        }
        UserDatabase.getInstance(this).userDAO().insertUser(user);
        Toast.makeText(this, "Add user successfully", Toast.LENGTH_SHORT).show();

        edtUsername.setText("");
        edtAddress.setText("");
        hideSoftKeyboard();

        loadData();
    }

    private boolean isUserExist(User user){
        List<User> users = UserDatabase.getInstance(this).userDAO().checkUser(user.getUsername());
        return users != null && !users.isEmpty();
    }

    private void loadData(){
        mListUser = UserDatabase.getInstance(this).userDAO().getAll();
        userAdapter.setData(mListUser);
    }

    public void hideSoftKeyboard(){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }

    private void initUI(){
        edtUsername = findViewById(R.id.edt_username);
        edtAddress = findViewById(R.id.edt_address);
        btnAddUser = findViewById(R.id.btn_add_user);
        rcvUser = findViewById(R.id.rcv_user);
    }
}