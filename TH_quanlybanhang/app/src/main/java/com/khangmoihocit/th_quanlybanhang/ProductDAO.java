package com.khangmoihocit.th_quanlybanhang;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDAO {
    @Insert
    void insert(Product product);
    @Update
    void update(Product product);
    @Delete
    void delete(Product product);

    @Query("select * from products")
    List<Product> getAll();

    @Query("select * from products where id = :id")
    Product getById(int id);

    @Query("select * from products where (:keyword is null or title like '%'||:keyword||'%' or description like '%'||:keyword||'%')")
    List<Product> findAll(String keyword);
}
