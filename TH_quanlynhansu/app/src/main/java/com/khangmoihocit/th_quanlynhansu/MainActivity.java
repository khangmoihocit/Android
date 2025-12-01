package com.khangmoihocit.th_quanlynhansu;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText edtHoTen, edtChucVu, edtLuongCoBan;
    private Button btnThem, btnSua;
    private RecyclerView rcvNhanVien;
    private SearchView searchView;
    private List<NhanVien> listNhanVien;
    private NhanVienAdapter nhanVienAdapter;
    private NhanVienDAO nhanVienDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //
        edtHoTen = findViewById(R.id.edtHoTen);
        edtChucVu = findViewById(R.id.edtChucVu);
        edtLuongCoBan = findViewById(R.id.edtLuongCoBan);
        btnThem = findViewById(R.id.btnThem);
        btnSua = findViewById(R.id.btnSua);
        btnSua.setEnabled(false);
        searchView = findViewById(R.id.search);
        nhanVienDAO = AppDatabase.getInstance(this).nhanVienDAO();
        listNhanVien = new ArrayList<>();
        rcvNhanVien = findViewById(R.id.rcvNhanVien);
        rcvNhanVien.setLayoutManager(new LinearLayoutManager(this));

        nhanVienAdapter = new NhanVienAdapter(new NhanVienAdapter.IClickItemNhanVien() {
            @Override
            public void delete(NhanVien nhanVien) {
                clickDeleteNhanVien(nhanVien);
            }

            @Override
            public void clickItem(View view, int position) {
                clickItemUpdate(view, position);
            }
        });
        loadData();
        rcvNhanVien.setAdapter(nhanVienAdapter);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    NhanVien nhanVien = new NhanVien();
                    nhanVien.setHoTen(edtHoTen.getText().toString());
                    nhanVien.setChuVu(edtChucVu.getText().toString());
                    nhanVien.setLuongCoBan(Double.parseDouble(edtLuongCoBan.getText().toString()));

                    nhanVienDAO.insert(nhanVien);
                    loadData();
                    sendNotification("them nhan vien " + nhanVien.getHoTen() + " thanh cong");
                }catch (NumberFormatException ex){
                    Toast.makeText(MainActivity.this, "loi luong co ban k dung dinh dang", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    NhanVien nhanVien = nhanVienDAO.getById(idNhanVienCurrent);
                    nhanVien.setHoTen(edtHoTen.getText().toString());
                    nhanVien.setChuVu(edtChucVu.getText().toString());
                    nhanVien.setLuongCoBan(Double.parseDouble(edtLuongCoBan.getText().toString()));

                    nhanVienDAO.update(nhanVien);
                    loadData();
                    btnSua.setEnabled(false);
                    btnThem.setEnabled(true);
                }catch (NumberFormatException ex){
                    Toast.makeText(MainActivity.this, "loi luong co ban k dung dinh dang", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }
        });
    }

    private void sendNotification(String content){
        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setContentTitle("Thong bao")
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.notify((int) new Date().getTime(), notification); //add quyen
        }
    }

    private void search(String keyword){
        List<NhanVien> result = nhanVienDAO.findAll(keyword);
        nhanVienAdapter.setData(result);
    }

    private int idNhanVienCurrent = -1;
    private void clickItemUpdate(View view, int position) {
        btnThem.setEnabled(false);
        btnSua.setEnabled(true);

        NhanVien nhanVien = nhanVienAdapter.get(position);
        edtHoTen.setText(nhanVien.getHoTen());
        edtChucVu.setText(nhanVien.getChuVu());
        edtLuongCoBan.setText(nhanVien.getLuongCoBan().toString());
        idNhanVienCurrent = nhanVien.getId();
    }

    private void clickDeleteNhanVien(NhanVien nhanVien) {
        new AlertDialog.Builder(this)
                .setTitle("xoa nhan vien")
                .setMessage("ban chac chu")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nhanVienDAO.delete(nhanVien);
                        loadData();
                        edtHoTen.setText("");
                        edtChucVu.setText("");
                        edtLuongCoBan.setText("");
                    }
                }).setNegativeButton("no", null)
                .show();
    }

    private void loadData(){
        listNhanVien = nhanVienDAO.getAll();
        nhanVienAdapter.setData(listNhanVien);
    }
}