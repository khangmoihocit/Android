package com.khangmoihocit.th_sqlite;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private StudentViewModel studentViewModel;

    private EditText edtName, edtAge;
    private Button btnAdd, btnUpdate;
    private ListView lvStudents;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> studentListStrings;

    private List<Student> currentStudentList;
    private Student selectedStudent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        edtAge = findViewById(R.id.edtAge);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        lvStudents = findViewById(R.id.lvStudents);

        studentListStrings = new ArrayList<>();
        currentStudentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentListStrings);
        lvStudents.setAdapter(adapter);

        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        studentViewModel.getAllStudents().observe(this, students -> {
            studentListStrings.clear();
            currentStudentList.clear();
            currentStudentList.addAll(students);

            for (Student student : students) {
                studentListStrings.add(student.getName() + " - " + student.getAge() + " tuổi");
            }
            adapter.notifyDataSetChanged();
            clearFields();
        });

        btnAdd.setOnClickListener(v -> addStudent());

        btnUpdate.setOnClickListener(v -> updateStudent());
        btnUpdate.setEnabled(false);

        lvStudents.setOnItemClickListener((parent, view, position, id) -> {
            selectedStudent = currentStudentList.get(position);
            edtName.setText(selectedStudent.getName());
            edtAge.setText(String.valueOf(selectedStudent.getAge()));
            btnUpdate.setEnabled(true);
            btnAdd.setEnabled(false);
        });

        lvStudents.setOnItemLongClickListener((parent, view, position, id) -> {
            Student studentToDelete = currentStudentList.get(position);

            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận Xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa sinh viên " + studentToDelete.getName() + "?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        studentViewModel.delete(studentToDelete); // [cite: 320]
                        Toast.makeText(MainActivity.this, "Đã xóa " + studentToDelete.getName(), Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();

            return true;
        });
    }

    private void addStudent() {
        String name = edtName.getText().toString();
        String ageStr = edtAge.getText().toString();

        if (validateInput(name, ageStr)) {
            int age = Integer.parseInt(ageStr);
            studentViewModel.insert(new Student(name, age));
            Toast.makeText(this, "Đã thêm sinh viên", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStudent() {
        if (selectedStudent == null) {
            Toast.makeText(this, "Vui lòng chọn một sinh viên để sửa", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = edtName.getText().toString();
        String ageStr = edtAge.getText().toString();

        if (validateInput(name, ageStr)) {
            int age = Integer.parseInt(ageStr);
            Student updatedStudent = new Student(name, age);
            updatedStudent.setId(selectedStudent.getId());

            studentViewModel.update(updatedStudent);
            Toast.makeText(this, "Đã cập nhật sinh viên", Toast.LENGTH_SHORT).show();
            btnUpdate.setEnabled(false);
            btnAdd.setEnabled(true);
            clearFields();
        }
    }

    // Hàm kiểm tra đầu vào
    private boolean validateInput(String name, String ageStr) {
        if (name.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Tên và tuổi không được để trống", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Hàm xóa nội dung EditText và bỏ chọn
    private void clearFields() {
        edtName.setText("");
        edtAge.setText("");
        selectedStudent = null;
        edtName.clearFocus();
        edtAge.clearFocus();
    }
}
