package com.khangmoihocit.th_sqlite;
import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentRepository {
    private final StudentDao studentDao;
    private final LiveData<List<Student>> allStudents;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public StudentRepository(Application application) {
        StudentDatabase db = StudentDatabase.getDatabase(application);
        studentDao = db.studentDao();
        allStudents = studentDao.getAllStudents();
    }

    public LiveData<List<Student>> getAllStudents() {
        return allStudents;
    }

    public void insert(Student student) {
        executorService.execute(() -> studentDao.insert(student));
    }

    public void update(Student student) {
        executorService.execute(() -> studentDao.update(student));
    }

    public void delete(Student student) {
        executorService.execute(() -> studentDao.delete(student));
    }

    public void deleteAll() {
        executorService.execute(studentDao::deleteAll);
    }
}

