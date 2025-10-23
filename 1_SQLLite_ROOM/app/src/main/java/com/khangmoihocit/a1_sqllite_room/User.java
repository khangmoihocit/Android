package com.khangmoihocit.a1_sqllite_room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users")
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String address;
    private Integer year;

    public User(){}

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public User(String username, String address, Integer year) {
        this.username = username;
        this.address = address;
        this.year = year;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
