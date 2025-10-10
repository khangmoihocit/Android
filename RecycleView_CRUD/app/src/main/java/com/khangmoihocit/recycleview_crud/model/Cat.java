package com.khangmoihocit.recycleview_crud.model;

public class Cat {
    private int img;
    private String name, describe;
    private double price;

    public Cat(int img, double price, String describe, String name) {
        this.img = img;
        this.price = price;
        this.describe = describe;
        this.name = name;
    }

    public Cat(){}

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
