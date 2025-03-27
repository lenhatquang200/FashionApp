package com.example.ecommerceapp.model;

/**
 * Model class for Shopping Cart Item
 */
public class CartItem {
    private long id;
    private long productId;
    private int quantity;
    private double priceAtAddition;
    
    // Non-database fields
    private Product product;

    public CartItem() {
    }

    public CartItem(long id, long productId, int quantity, double priceAtAddition) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.priceAtAddition = priceAtAddition;
    }

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceAtAddition() {
        return priceAtAddition;
    }

    public void setPriceAtAddition(double priceAtAddition) {
        this.priceAtAddition = priceAtAddition;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Calculate the subtotal for this cart item
     * @return The total price for this item (quantity × price)
     */
    public double getSubtotal() {
        return quantity * priceAtAddition;
    }
}
