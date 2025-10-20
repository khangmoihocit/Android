package com.khangmoihocit.a1_sqllite_room.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.khangmoihocit.a1_sqllite_room.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    User insertUser(User user);

    @Query("select * from users")
    List<User> getAll();
}



