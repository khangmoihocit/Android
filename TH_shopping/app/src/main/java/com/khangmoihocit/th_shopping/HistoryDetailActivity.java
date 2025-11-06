package com.khangmoihocit.th_shopping;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.khangmoihocit.th_shopping.adapter.OrderDetailAdapter;
import com.khangmoihocit.th_shopping.model.OrderDetail;

import java.util.ArrayList;

public class HistoryDetailActivity extends AppCompatActivity {

    TextView txtDetailOrderId;
    RecyclerView recyclerHistoryDetail;
    DatabaseHelper db;
    OrderDetailAdapter adapter;
    ArrayList<OrderDetail> detailList = new ArrayList<>();
    int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        db = new DatabaseHelper(this);
        txtDetailOrderId = findViewById(R.id.txtDetailOrderId);
        recyclerHistoryDetail = findViewById(R.id.recyclerHistoryDetail);

        // Lấy ID đơn hàng từ Intent
        orderId = getIntent().getIntExtra("ORDER_ID", -1);
        if (orderId == -1) {
            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        txtDetailOrderId.setText("Chi tiết đơn hàng #" + orderId);

        recyclerHistoryDetail.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDetailAdapter(this, detailList);
        recyclerHistoryDetail.setAdapter(adapter);

        loadOrderDetails();
    }

    private void loadOrderDetails() {
        detailList.clear();
        Cursor cursor = db.getOrderDetails(orderId);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int oId = cursor.getInt(1);
                String name = cursor.getString(2);
                int price = cursor.getInt(3);
                int qty = cursor.getInt(4);
                detailList.add(new OrderDetail(id, oId, name, price, qty));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
