package com.khangmoihocit.noteapps.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.khangmoihocit.noteapps.R;
import com.khangmoihocit.noteapps.alarm.AlarmHelper;
import com.khangmoihocit.noteapps.data.Note;
import com.khangmoihocit.noteapps.data.NotesRepo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditorActivity extends AppCompatActivity {
    private EditText edTitle, edContent;
    private TextView tvReminder;
    private Long remindAt = null;
    private long id = -1L;
    private final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        edTitle = findViewById(R.id.edTitle);
        edContent = findViewById(R.id.edContent);
        tvReminder = findViewById(R.id.tvReminder);

        id = getIntent().getLongExtra("id", -1L);
        if (id != -1L) {
            // Chế độ sửa: tải dữ liệu ghi chú có sẵn
            Note n = NotesRepo.getById(id);
            if (n != null) {
                edTitle.setText(n.title);
                edContent.setText(n.content);
                remindAt = n.remindAt;
            }
        }
        updateReminderLabel();

        findViewById(R.id.btnReminder).setOnClickListener(v -> pickDateTime());
        findViewById(R.id.btnSave).setOnClickListener(v -> saveNote());
        findViewById(R.id.btnShare).setOnClickListener(v -> shareNote());
    }

    private void pickDateTime() {
        final Calendar cal = Calendar.getInstance();
        if (remindAt != null) {
            cal.setTimeInMillis(remindAt);
        }

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            cal.set(year, month, dayOfMonth);
            new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);

                long selectedTime = cal.getTimeInMillis();
                if (selectedTime < System.currentTimeMillis()) {
                    Toast.makeText(this, "Không thể đặt nhắc nhở cho thời điểm đã qua!", Toast.LENGTH_SHORT).show();
                } else {
                    remindAt = selectedTime;
                    updateReminderLabel();
                    Toast.makeText(this, "Đã đặt nhắc nhở: " + fmt.format(new Date(remindAt)), Toast.LENGTH_SHORT).show();
                }
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateReminderLabel() {
        if (remindAt == null) {
            tvReminder.setText("Chưa đặt nhắc nhở");
        } else {
            tvReminder.setText("Nhắc lúc: " + fmt.format(new Date(remindAt)));
        }
    }

    private void saveNote() {
        final String title = edTitle.getText().toString().trim();
        final String content = edContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            Toast.makeText(this, "Ghi chú trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        long now = System.currentTimeMillis();
        if (id == -1L) {
            // Thêm mới
            Note n = new Note();
            n.title = title;
            n.content = content;
            n.createdAt = now;
            n.updatedAt = now;
            n.remindAt = remindAt;
            long newId = NotesRepo.insert(n);
            if (remindAt != null) {
                AlarmHelper.schedule(this, newId, remindAt, title);
            }
        } else {
            // Cập nhật
            Note n = NotesRepo.getById(id);
            if (n != null) {
                n.title = title;
                n.content = content;
                n.updatedAt = now;
                n.remindAt = remindAt;
                NotesRepo.update(n);

                // Hủy báo thức cũ và đặt lại (nếu có)
                AlarmHelper.cancel(this, n.id);
                if (remindAt != null) {
                    AlarmHelper.schedule(this, n.id, remindAt, title);
                }
            }
        }
        setResult(RESULT_OK);
        finish();
    }

    private void shareNote() {
        String title = edTitle.getText().toString();
        String content = edContent.getText().toString();
        String textToShare = (TextUtils.isEmpty(title) ? "" : title + "\n\n") + content;

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        sendIntent.setType("text/plain");

        startActivity(Intent.createChooser(sendIntent, "Chia sẻ ghi chú qua..."));
    }
}