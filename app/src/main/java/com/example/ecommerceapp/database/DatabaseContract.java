package com.example.ecommerceapp.database;

import android.provider.BaseColumns;

/**
 * Database schema contract for the ECommerce app
 * Defines table names and column names for SQLite database
 */
public final class DatabaseContract {

    // To prevent someone from instantiating the contract class
    private DatabaseContract() {}

    /**
     * Users table schema
     */
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_FULL_NAME = "full_name";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_PASSWORD_HASH = "password_hash";
    }

    /**
     * Products table schema
     */
    public static class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_STOCK_QUANTITY = "stock_quantity";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_IS_FEATURED = "is_featured";
    }

    /**
     * Categories table schema
     */
    public static class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE_URL = "image_url";
    }

    /**
     * Cart items table schema
     */
    public static class CartEntry implements BaseColumns {
        public static final String TABLE_NAME = "cart_items";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE_AT_ADDITION = "price_at_addition";
    }

    /**
     * Orders table schema
     */
    public static class OrderEntry implements BaseColumns {
        public static final String TABLE_NAME = "orders";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_ORDER_DATE = "order_date";
        public static final String COLUMN_TOTAL_AMOUNT = "total_amount";
        public static final String COLUMN_SHIPPING_ADDRESS = "shipping_address";
        public static final String COLUMN_PAYMENT_METHOD = "payment_method";
        public static final String COLUMN_STATUS = "status";
    }

    /**
     * Order items table schema
     */
    public static class OrderItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "order_items";
        public static final String COLUMN_ORDER_ID = "order_id";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";
    }

    /**
     * Chat messages table schema
     */
    public static class ChatMessageEntry implements BaseColumns {
        public static final String TABLE_NAME = "chat_messages";
        public static final String COLUMN_USER_ID = "user_id";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_IS_USER_MESSAGE = "is_user_message";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
