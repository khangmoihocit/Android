package com.khangmoihocit.th_shopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button; // Thêm
import android.widget.SearchView;
import android.widget.Toast;
import com.khangmoihocit.th_shopping.adapter.ProductAdapter;
import com.khangmoihocit.th_shopping.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter adapter;
    ArrayList<Product> productList = new ArrayList<>();
    DatabaseHelper db;
    FloatingActionButton fab;
    SearchView searchView;
    Button btnGoToCart, btnGoToHistory; // Thêm

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerProducts);
        fab = findViewById(R.id.fabAdd);
        searchView = findViewById(R.id.searchView);
        btnGoToCart = findViewById(R.id.btnGoToCart); // Thêm
        btnGoToHistory = findViewById(R.id.btnGoToHistory); // Thêm

        // Xử lý sự kiện cho 2 nút mới
        btnGoToCart.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, OrderActivity.class))
        );

        btnGoToHistory.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, HistoryActivity.class))
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, productList, product -> {
            db.insertToCart(product.id);
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            startActivity(new Intent(this, AddNewProduct.class));
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }

    private void loadProducts() {
        productList.clear();
        Cursor cursor = db.getAllProducts();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int price = cursor.getInt(2);
                productList.add(new Product(id, name, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.setOriginalList(new ArrayList<>(productList));
        adapter.notifyDataSetChanged();
    }
}

