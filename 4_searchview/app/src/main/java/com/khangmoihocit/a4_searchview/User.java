package com.khangmoihocit.a4_searchview;

public class User {
    private int image;
    private String name;
    private String address;

    public User() {
    }

    public User(int image, String address, String name) {
        this.image = image;
        this.address = address;
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
