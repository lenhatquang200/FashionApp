package com.example.ecommerceapp.model;

public class CartItem {
    private int id;
    private int productId;
    private int quantity;
    private String productName;
    private double productPrice;
    private String imageUrl;

    public CartItem(int id, int productId, int quantity, String productName, double productPrice, String imageUrl) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getTotalPrice() {
        return productPrice * quantity;
    }
}
