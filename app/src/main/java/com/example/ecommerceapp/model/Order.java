package com.example.ecommerceapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private String orderNumber;
    private long orderDate;
    private double totalAmount;
    private String status;
    private String shippingAddress;
    private List<CartItem> items;

    public Order(int id, String orderNumber, long orderDate, double totalAmount, String status, String shippingAddress) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.items = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public long getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(long orderDate) {
        this.orderDate = orderDate;
    }

    public Date getOrderDateAsDate() {
        return new Date(orderDate);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItem(CartItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
    }
}
