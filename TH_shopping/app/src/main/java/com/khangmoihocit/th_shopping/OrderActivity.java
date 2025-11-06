package com.khangmoihocit.th_shopping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.khangmoihocit.th_shopping.DatabaseHelper;
import com.khangmoihocit.th_shopping.R;
import com.khangmoihocit.th_shopping.adapter.CartAdapter;
import com.khangmoihocit.th_shopping.model.CartItem;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    RecyclerView recyclerCart;
    CartAdapter adapter;
    ArrayList<CartItem> cartList = new ArrayList<>();
    TextView txtTotal;
    Button btnCheckout; // Thêm
    DatabaseHelper db;

    private int currentTotal = 0; // Biến lưu tổng tiền

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerCart = findViewById(R.id.recyclerCart);
        txtTotal = findViewById(R.id.txtTotal);
        btnCheckout = findViewById(R.id.btnCheckout); // Thêm
        db = new DatabaseHelper(this);

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, cartList, new CartAdapter.OnCartActionListener() {
            @Override
            public void onIncrease(CartItem item) {
                db.updateCartQuantity(item.id, item.quantity + 1);
                loadCart();
            }

            @Override
            public void onDecrease(CartItem item) {
                if (item.quantity > 1) {
                    db.updateCartQuantity(item.id, item.quantity - 1);
                    loadCart();
                } else {
                    onDelete(item);
                }
            }

            @Override
            public void onDelete(CartItem item) {
                // Thêm xác nhận
                new AlertDialog.Builder(OrderActivity.this)
                        .setTitle("Xóa sản phẩm")
                        .setMessage("Bạn muốn xóa " + item.name + " khỏi giỏ hàng?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            db.deleteCartItem(item.id);
                            loadCart();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
        recyclerCart.setAdapter(adapter);

        // Xử lý sự kiện nút Thanh Toán
        btnCheckout.setOnClickListener(v -> checkout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }

    private void loadCart() {
        cartList.clear();
        Cursor cursor = db.getCartItems();
        currentTotal = 0; // Reset tổng tiền

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int price = cursor.getInt(2);
                int qty = cursor.getInt(3);
                currentTotal += price * qty;
                cartList.add(new CartItem(id, name, price, qty));
            } while (cursor.moveToNext());
        }
        cursor.close();

        txtTotal.setText("Tổng tiền: " + currentTotal + "đ");

        if (cartList.isEmpty()) {
            txtTotal.setText("Giỏ hàng trống");
            btnCheckout.setEnabled(false); // Không cho thanh toán nếu giỏ trống
        } else {
            btnCheckout.setEnabled(true);
        }
        adapter.notifyDataSetChanged();
    }

    private void checkout() {
        if (cartList.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Xác nhận trước khi thanh toán
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận Thanh Toán")
                .setMessage("Bạn có chắc chắn muốn thanh toán tổng số tiền " + currentTotal + "đ?")
                .setPositiveButton("Thanh Toán", (dialog, which) -> {
                    long orderId = db.finalizeOrder(cartList, currentTotal);
                    if (orderId != -1) {
                        Toast.makeText(this, "Thanh toán thành công! Mã đơn hàng: " + orderId, Toast.LENGTH_LONG).show();
                        finish(); // Đóng giỏ hàng, quay về MainActivity
                    } else {
                        Toast.makeText(this, "Lỗi khi xử lý đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}

