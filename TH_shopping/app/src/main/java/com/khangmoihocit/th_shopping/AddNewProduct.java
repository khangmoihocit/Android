package com.khangmoihocit.th_shopping;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Thêm sản phẩm mới vào cơ sở dữ liệu
public class AddNewProduct extends AppCompatActivity {
    EditText edtName, edtPrice;
    Button btnSave;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        edtName = findViewById(R.id.edtProductName);
        edtPrice = findViewById(R.id.edtProductPrice);
        btnSave = findViewById(R.id.btnSave);
        db = new DatabaseHelper(this);

        btnSave.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String priceText = edtPrice.getText().toString().trim();

            if (name.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int price = Integer.parseInt(priceText);
            boolean inserted = db.insertProduct(name, price);
            if (inserted) {
                Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                finish(); // quay về MainActivity
            } else {
                Toast.makeText(this, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
