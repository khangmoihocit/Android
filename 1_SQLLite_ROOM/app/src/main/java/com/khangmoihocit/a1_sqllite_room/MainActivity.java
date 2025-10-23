package com.khangmoihocit.a1_sqllite_room;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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
    private static final int MY_REQUEST_CODE = 10;
    private EditText edtUsername, edtAddress, edtSearch, edtYear;
    private Button btnAddUser;
    private TextView tvDeleteAll;
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

            @Override
            public void deleteUser(User user) {
                clickDeleteUser(user);
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
        
        tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDeleteAllUser();
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    handleSearchUser();
                    return true;
                }
                return false;
            }
        });
    }

    private void handleSearchUser() {
        String keyword = edtSearch.getText().toString().trim();
        mListUser = new ArrayList<>();
        mListUser = UserDatabase.getInstance(MainActivity.this).userDAO().findByUsername(keyword);
        userAdapter.setData(mListUser);
        hideSoftKeyboard();
    }

    private void initUI(){
        edtUsername = findViewById(R.id.edt_username);
        edtAddress = findViewById(R.id.edt_address);
        btnAddUser = findViewById(R.id.btn_add_user);
        rcvUser = findViewById(R.id.rcv_user);
        tvDeleteAll = findViewById(R.id.tv_delete_all);
        edtSearch = findViewById(R.id.edt_search);
        edtYear = findViewById(R.id.edt_year);
    }

    private void clickDeleteAllUser() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm delete user")
                .setMessage("Are you sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserDatabase.getInstance(MainActivity.this).userDAO().deleteAllUser();
                        Toast.makeText(MainActivity.this, "Delete all user successfully", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void clickDeleteUser(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm delete all user")
                .setMessage("Are you sure")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserDatabase.getInstance(MainActivity.this).userDAO().deleteUser(user);
                        Toast.makeText(MainActivity.this, "Delete user successfully", Toast.LENGTH_SHORT).show();
                        loadData();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    private void addUser() {
        String username = edtUsername.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String strYear = (edtYear.getText().toString().trim());

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(address) || strYear.isEmpty()){
            return;
        }

        try{
            Integer year = Integer.parseInt(strYear);
            User user = new User(username, address, year);
            if(isUserExist(user)){
                Toast.makeText(this, "username exist", Toast.LENGTH_SHORT).show();
                return;
            }
            UserDatabase.getInstance(this).userDAO().insertUser(user);
            Toast.makeText(this, "Add user successfully", Toast.LENGTH_SHORT).show();
        }catch (NumberFormatException ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        edtUsername.setText("");
        edtAddress.setText("");
        edtYear.setText("");
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



    private void clickUpdateUser(User user){
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);//nhan va truyen
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_user", user);
        intent.putExtras(bundle);
        startActivityForResult(intent, MY_REQUEST_CODE); //se chay method on activity result
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_REQUEST_CODE && resultCode == Activity.RESULT_OK){ //nhan result tu intent update
            loadData();
        }
    }
}