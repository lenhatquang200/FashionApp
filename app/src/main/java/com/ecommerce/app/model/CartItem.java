package com.ecommerce.app.model;

public class CartItem {
    private long id;
    private long productId;
    private String productName;
    private String productImageUrl;
    private double productPrice;
    private int quantity;
    private double subtotal;

    public CartItem() {
    }

    public CartItem(long id, long productId, String productName, String productImageUrl, 
                   double productPrice, int quantity) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.subtotal = productPrice * quantity;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
        this.subtotal = productPrice * quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subtotal = productPrice * quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void updateSubtotal() {
        this.subtotal = productPrice * quantity;
    }

    public String getFormattedPrice() {
        return String.format("$%.2f", productPrice);
    }

    public String getFormattedSubtotal() {
        return String.format("$%.2f", subtotal);
    }
}
