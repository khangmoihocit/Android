package com.khangmoihocit.th_chuong4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MonAnAdapter monAnAdapter;
    private ArrayList<MonAn> danhSachMonAn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = findViewById(R.id.recyclerViewMonAn);

        prepareData();

        monAnAdapter = new MonAnAdapter(this, danhSachMonAn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(monAnAdapter);
    }

    private void prepareData() {
        danhSachMonAn = new ArrayList<>();

        danhSachMonAn.add(new MonAn(1, "Phở Bò Đặc Biệt", "50.000đ", "Nước dùng đậm đà hầm từ xương, bánh phở mềm mại cùng thịt bò tươi ngon.", R.drawable.img_phobo));
        danhSachMonAn.add(new MonAn(2, "Bún Chả Hà Nội", "45.000đ", "Thịt nướng thơm lừng trên than hoa, ăn kèm bún tươi, rau sống và nước mắm chua ngọt.", R.drawable.img_buncha));
        danhSachMonAn.add(new MonAn(3, "Cơm Tấm Sườn Bì", "55.000đ", "Sườn nướng mật ong, bì dai giòn, chả trứng hấp mềm mại trên nền cơm tấm nóng hổi.", R.drawable.img_comtamsuonbi));
        danhSachMonAn.add(new MonAn(4, "Bánh Mì Heo Quay", "25.000đ", "Vỏ bánh mì giòn rụm, bên trong là lớp heo quay da giòn, thịt mềm, thêm chút dưa leo và đồ chua.", R.drawable.banh_mi));
        danhSachMonAn.add(new MonAn(5, "Hủ Tiếu Nam Vang", "50.000đ", "Sợi hủ tiếu dai, nước lèo trong ngọt từ xương, topping phong phú với tôm, thịt, trứng cút.", R.drawable.hu_tieu));

        danhSachMonAn.add(new MonAn(6, "Gỏi Cuốn Tôm Thịt", "30.000đ", "Món khai vị thanh mát với tôm, thịt, bún và rau sống được cuốn trong lớp bánh tráng mỏng.", R.drawable.goi_cuon));
        danhSachMonAn.add(new MonAn(7, "Bánh Xèo Miền Tây", "40.000đ", "Vỏ bánh giòn tan, nhân tôm thịt giá đỗ nóng hổi, ăn kèm rau rừng và nước mắm chua ngọt.", R.drawable.banh_xeo));
        danhSachMonAn.add(new MonAn(8, "Chả Cá Lã Vọng", "120.000đ", "Đặc sản Hà Nội với cá lăng nướng thơm lừng, ăn kèm bún, lạc rang, thì là và mắm tôm.", R.drawable.cha_ca));

    }
}

