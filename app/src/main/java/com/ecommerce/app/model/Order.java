package com.ecommerce.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private long id;
    private Date orderDate;
    private String orderStatus;
    private double totalAmount;
    private String shippingAddress;
    private String paymentMethod;
    private List<CartItem> orderItems;

    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_PROCESSING = "Processing";
    public static final String STATUS_SHIPPED = "Shipped";
    public static final String STATUS_DELIVERED = "Delivered";
    public static final String STATUS_CANCELLED = "Cancelled";

    public Order() {
        this.orderItems = new ArrayList<>();
    }

    public Order(long id, Date orderDate, String orderStatus, double totalAmount, 
                String shippingAddress, String paymentMethod) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.orderItems = new ArrayList<>();
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<CartItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<CartItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(CartItem item) {
        this.orderItems.add(item);
        recalculateTotalAmount();
    }

    public void recalculateTotalAmount() {
        double total = 0;
        for (CartItem item : orderItems) {
            total += item.getSubtotal();
        }
        this.totalAmount = total;
    }

    public String getFormattedTotal() {
        return String.format("$%.2f", totalAmount);
    }

    public String getFormattedDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM dd, yyyy");
        return sdf.format(orderDate);
    }
}
