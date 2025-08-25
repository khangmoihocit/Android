package com.khangmoihocit.bai_tap_tren_lop;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class BTVN01Activity extends AppCompatActivity {

    private EditText ed1, ed2, ed3;
    private RadioButton rdCong, rdTru, rdNhan, rdChia;
    private Button btnTinhToan;
    private String o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_btvn01);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        rdCong.setOnCheckedChangeListener(listener);
        rdTru.setOnCheckedChangeListener(listener);
        rdNhan.setOnCheckedChangeListener(listener);
        rdChia.setOnCheckedChangeListener(listener);

        if(rdCong.isChecked()){
            o = rdCong.getText().toString();
        }

        btnTinhToan = findViewById(R.id.btnTinhToan);
        btnTinhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double x, y, z;

                try{
                    x = Double.parseDouble(ed1.getText().toString());
                    y = Double.parseDouble(ed2.getText().toString());
                    z = Double.parseDouble(ed3.getText().toString());

                    double result = tinhToan(x, y, z, o);

                    DecimalFormat df = new DecimalFormat("#.##");
                    String s = df.format(result);

                    TextView tv = findViewById(R.id.viewResult);
                    tv.setText("Kết quả: " + s);

                }catch (NumberFormatException ex){
                    Toast.makeText(BTVN01Activity.this
                            , "Lỗi format string: " + ex.getMessage()
                            , Toast.LENGTH_LONG).show();
                }catch (IllegalArgumentException ex){
                    Toast.makeText(BTVN01Activity.this
                            , "Lỗi: " + ex.getMessage()
                            , Toast.LENGTH_SHORT).show();
                }catch (RuntimeException ex){
                    Log.e("error", ex.getMessage());
                }
            }
        });

    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                o = buttonView.getText().toString();
            }
        }
    };

    private void init(){
        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        ed3 = findViewById(R.id.ed3);

        rdCong = findViewById(R.id.rdCong);
        rdTru = findViewById(R.id.rdTru);
        rdNhan = findViewById(R.id.rdNhan);
        rdChia = findViewById(R.id.rdChia);
    }

    private double tinhToan(double x, double y, double z, String o){
        double res = 0;

        switch (o){
            case "Cộng":
                res = x + y + z;
                break;
            case "Trừ":
                res = x - y - z;
                break;
            case "Nhân":
                res = x * y * z;
                break;
            case "Chia":
                if (y == 0 || z == 0) {
                    throw new IllegalArgumentException("Không thể chia cho 0.");
                } else {
                    res = x / y / z;
                }
                break;
            default:
                throw new IllegalArgumentException("Phép toán không hợp lệ: " + o);
        }

        return res;
    }

}