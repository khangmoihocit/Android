package com.khangmoihocit.btth1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText ed1, ed2;
    private Button btnCong, btnTru, btnChia, btnNhan, btnPT;
    private TextView txResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        btnCong = findViewById(R.id.btnCong);

        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        txResult = findViewById(R.id.tvResult);

        btnCong = findViewById(R.id.btnCong);
        btnTru = findViewById(R.id.btnTru);
        btnNhan = findViewById(R.id.btnNhan);
        btnChia = findViewById(R.id.btnChia);
        btnPT = findViewById(R.id.btnPT);


        btnCong.setOnClickListener(listener);
        btnTru.setOnClickListener(listener);
        btnNhan.setOnClickListener(listener);
        btnChia.setOnClickListener(listener);
        btnPT.setOnClickListener(listener);


    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            double x, y;

            try {
                String s1 = ed1.getText().toString();
                String s2 = ed2.getText().toString();

                if (s1.isEmpty() || s2.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Vui lòng nhập đủ cả hai số",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                x = Double.parseDouble(s1);
                y = Double.parseDouble(s2);

            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this,
                        "Định dạng số không hợp lệ",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String operator = "";

            int viewId = v.getId();
            if (viewId == R.id.btnCong) {
                operator = "+";
            } else if (viewId == R.id.btnTru) {
                operator = "-";
            } else if (viewId == R.id.btnNhan) {
                operator = "*";
            } else if (viewId == R.id.btnChia) {
                operator = "/";
            } else if (viewId == R.id.btnPT) {
                operator = "%";
            }

            try {
                double result = tinhToan(x, y, operator);
                txResult.setText("Kết quả: " + result);
            } catch (IllegalArgumentException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private double tinhToan(double x, double y, String o){
        double res = 0;

        switch (o){
            case "+":
                res = x + y;
                break;
            case "-":
                res = x - y;
                break;
            case "*":
                res = x * y ;
                break;
            case "/":
                if (y == 0) {
                    throw new IllegalArgumentException("Không thể chia cho 0.");
                } else {
                    res = x / y;
                }
                break;

            case "%":
                res = x % y;
                break;
            default:
                throw new IllegalArgumentException("Phép toán không hợp lệ: " + o);
        }

        return res;
    }
}