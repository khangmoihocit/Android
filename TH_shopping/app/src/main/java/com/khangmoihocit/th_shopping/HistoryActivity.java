package com.khangmoihocit.th_shopping;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.khangmoihocit.th_shopping.adapter.OrderAdapter;
import com.khangmoihocit.th_shopping.model.Order;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerHistory;
    TextView txtEmptyHistory;
    DatabaseHelper db;
    OrderAdapter adapter;
    ArrayList<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = new DatabaseHelper(this);
        recyclerHistory = findViewById(R.id.recyclerHistory);
        txtEmptyHistory = findViewById(R.id.txtEmptyHistory);

        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderAdapter(this, orderList, order -> {
            // Mở chi tiết đơn hàng
            Intent intent = new Intent(HistoryActivity.this, HistoryDetailActivity.class);
            intent.putExtra("ORDER_ID", order.id);
            startActivity(intent);
        });
        recyclerHistory.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    private void loadHistory() {
        orderList.clear();
        Cursor cursor = db.getOrderHistory();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                long date = cursor.getLong(1);
                int total = cursor.getInt(2);
                orderList.add(new Order(id, date, total));
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (orderList.isEmpty()) {
            txtEmptyHistory.setVisibility(View.VISIBLE);
            recyclerHistory.setVisibility(View.GONE);
        } else {
            txtEmptyHistory.setVisibility(View.GONE);
            recyclerHistory.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }
}
