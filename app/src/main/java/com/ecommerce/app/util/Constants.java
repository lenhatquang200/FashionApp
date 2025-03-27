package com.ecommerce.app.util;

public class Constants {
    // Database constants
    public static final String DB_NAME = "ecommerce.db";
    public static final int DB_VERSION = 1;
    
    // Fragment tags
    public static final String TAG_HOME_FRAGMENT = "home_fragment";
    public static final String TAG_PRODUCT_DETAIL_FRAGMENT = "product_detail_fragment";
    public static final String TAG_CART_FRAGMENT = "cart_fragment";
    public static final String TAG_PROFILE_FRAGMENT = "profile_fragment";
    public static final String TAG_ORDERS_FRAGMENT = "orders_fragment";
    public static final String TAG_CHECKOUT_FRAGMENT = "checkout_fragment";
    public static final String TAG_SEARCH_FRAGMENT = "search_fragment";
    public static final String TAG_CHATBOT_FRAGMENT = "chatbot_fragment";
    
    // Payment methods
    public static final String PAYMENT_METHOD_CREDIT_CARD = "Credit Card";
    public static final String PAYMENT_METHOD_PAYPAL = "PayPal";
    public static final String PAYMENT_METHOD_CASH_ON_DELIVERY = "Cash on Delivery";
    
    // Chatbot related constants
    public static final String ANTHROPIC_API_URL = "https://api.anthropic.com/";
    public static final String ANTHROPIC_MODEL = "claude-3-haiku-20240307";
    
    // API keys are obtained from environment variables in AnthropicClient.java
}
