package com.khangmoihocit.a4_searchview;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcvUsers;
    private UserAdapter userAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcvUsers = findViewById(R.id.rcv_users);
        rcvUsers.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(getUsers());
        rcvUsers.setAdapter(userAdapter);

    }

    private List<User> getUsers(){
        List<User> users = new ArrayList<>();

        users.add(new User(R.drawable.img, "ha noi", "nguiyen van a"));
        users.add(new User(R.drawable.img, "bac ninh", "pham van khang"));
        users.add(new User(R.drawable.img, "tay nguyen", "nguyen thi c"));
        users.add(new User(R.drawable.img, "hai phong", "tran van f"));
        users.add(new User(R.drawable.img, "hoang mai, ha noi", "lo thi f"));
        return users;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.getFilter().filter(newText);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                userAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }
}