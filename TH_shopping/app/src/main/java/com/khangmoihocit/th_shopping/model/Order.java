package com.khangmoihocit.th_shopping.model;

public class Order {
    public int id;
    public long orderDate;
    public int totalAmount;

    public Order(int id, long orderDate, int totalAmount) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }
}
