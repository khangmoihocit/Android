package com.khangmoihocit.a1_sqllite_room.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.khangmoihocit.a1_sqllite_room.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insertUser(User user);

    @Query("select * from users")
    List<User> getAll();

    @Query("select * from users where username=:username")
    List<User> checkUser(String username);

    @Update
    void updateUser(User user);
}



