package com.khangmoihocit.th_shopping.model;

public class OrderDetail {
    public int id;
    public int orderId;
    public String productName;
    public int productPrice;
    public int quantity;

    public OrderDetail(int id, int orderId, String productName, int productPrice, int quantity) {
        this.id = id;
        this.orderId = orderId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }
}
