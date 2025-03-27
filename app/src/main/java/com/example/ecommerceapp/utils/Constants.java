package com.example.ecommerceapp.utils;

import android.os.Build;

public class Constants {
    // API keys
    public static final String CLAUDE_API_KEY = System.getenv("CLAUDE_API_KEY") != null ? 
                                               System.getenv("CLAUDE_API_KEY") : "default_key";
    
    // App constants
    public static final String APP_NAME = "Ecommerce App";
    
    // Order status
    public static final String ORDER_STATUS_PENDING = "Pending";
    public static final String ORDER_STATUS_PROCESSING = "Processing";
    public static final String ORDER_STATUS_SHIPPED = "Shipped";
    public static final String ORDER_STATUS_DELIVERED = "Delivered";
    public static final String ORDER_STATUS_CANCELLED = "Cancelled";
    
    // Payment methods
    public static final String PAYMENT_METHOD_CREDIT_CARD = "Credit Card";
    public static final String PAYMENT_METHOD_PAYPAL = "PayPal";
    public static final String PAYMENT_METHOD_COD = "Cash on Delivery";
    
    // Shared Preferences
    public static final String PREF_NAME = "ecommerce_prefs";
    public static final String PREF_USER_ID = "user_id";
    
    // Intent extras
    public static final String EXTRA_PRODUCT_ID = "product_id";
    public static final String EXTRA_CATEGORY_ID = "category_id";
    public static final String EXTRA_ORDER_ID = "order_id";
}
