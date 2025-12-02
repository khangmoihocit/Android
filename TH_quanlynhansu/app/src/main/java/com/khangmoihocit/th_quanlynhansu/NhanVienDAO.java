package com.khangmoihocit.th_quanlynhansu;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NhanVienDAO {
    @Insert
    void insert(NhanVien nhanVien);
    @Update
    void update(NhanVien nhanVien);
    @Delete
    void delete(NhanVien nhanVien);

    @Query("select * from nhanvien")
    List<NhanVien> getAll();

    @Query("select * from nhanvien where id = :id")
    NhanVien getById(int id);

    @Query("select * from nhanvien where (:keyword is null or hoTen like '%'||:keyword||'%' or chuVu like '%'||:keyword||'%')")
    List<NhanVien> findAll(String keyword);
}
