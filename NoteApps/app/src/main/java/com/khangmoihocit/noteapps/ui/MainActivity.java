package com.khangmoihocit.noteapps.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.khangmoihocit.noteapps.R;
import com.khangmoihocit.noteapps.data.NotesRepo;


import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {

    private NoteAdapter adapter;
    private EditText edSearch;
    private static final int REQ_EDITOR = 1001;

    // Thêm một hằng số để nhận diện yêu cầu quyền
    private static final int NOTIFICATION_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Yêu cầu quyền ngay khi Activity được tạo
        requestNotificationPermission();

        setupRecyclerView();
        setupSearch();
        setupFab();
    }

    // Hàm mới để kiểm tra và yêu cầu quyền
    private void requestNotificationPermission() {
        // Chỉ cần yêu cầu quyền này trên Android 13 (TIRAMISU) trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Nếu chưa có quyền, thì yêu cầu
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_CODE);
            }
        }
    }

    // Thêm hàm này để xử lý kết quả khi người dùng chọn Allow/Deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền
                Toast.makeText(this, "Đã cấp quyền thông báo!", Toast.LENGTH_SHORT).show();
            } else {
                // Người dùng từ chối, có thể hiển thị thông báo giải thích
                Toast.makeText(this, "Bạn đã từ chối quyền thông báo. Tính năng nhắc nhở có thể không hoạt động.", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Luôn làm mới danh sách khi quay lại màn hình
        refreshNoteList();
    }

    private void setupRecyclerView() {
        adapter = new NoteAdapter((note, anchorView) -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, anchorView);
            popupMenu.getMenu().add("Sửa");
            popupMenu.getMenu().add("Xoá");
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if ("Sửa".equals(menuItem.getTitle())) {
                    openEditor(note.id);
                } else if ("Xoá".equals(menuItem.getTitle())) {
                    deleteNote(note.id);
                }
                return true;
            });
            popupMenu.show();
        });

        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    private void setupSearch() {
        edSearch = findViewById(R.id.edSearch);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Tự động tìm kiếm khi người dùng gõ
                adapter.submit(NotesRepo.search(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFab() {
        findViewById(R.id.fabAdd).setOnClickListener(v -> openEditor(-1L));
    }

    private void refreshNoteList() {
        String query = edSearch == null ? null : edSearch.getText().toString();
        if (query == null || query.isEmpty()) {
            adapter.submit(NotesRepo.getAll());
        } else {
            adapter.submit(NotesRepo.search(query));
        }
    }

    private void openEditor(long id) {
        Intent intent = new Intent(this, EditorActivity.class);
        // id = -1L nghĩa là tạo ghi chú mới
        if (id != -1L) {
            intent.putExtra("id", id);
        }
        startActivityForResult(intent, REQ_EDITOR);
    }

    private void deleteNote(long id) {
        NotesRepo.delete(id);
        Toast.makeText(this, "Đã xoá ghi chú", Toast.LENGTH_SHORT).show();
        refreshNoteList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Nếu EditorActivity trả về kết quả OK, có nghĩa là đã có thay đổi (thêm/sửa)
        // -> cần làm mới lại danh sách
        if (requestCode == REQ_EDITOR && resultCode == RESULT_OK) {
            refreshNoteList();
        }
    }
}