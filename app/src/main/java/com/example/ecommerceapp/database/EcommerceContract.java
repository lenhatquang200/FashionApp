package com.example.ecommerceapp.database;

import android.provider.BaseColumns;

public final class EcommerceContract {
    
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private EcommerceContract() {}
    
    /* Inner class that defines the Product table */
    public static class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_STOCK = "stock";
    }
    
    /* Inner class that defines the Category table */
    public static class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
    }
    
    /* Inner class that defines the Cart table */
    public static class CartEntry implements BaseColumns {
        public static final String TABLE_NAME = "cart";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_QUANTITY = "quantity";
    }
    
    /* Inner class that defines the Order table */
    public static class OrderEntry implements BaseColumns {
        public static final String TABLE_NAME = "orders";
        public static final String COLUMN_ORDER_NUMBER = "order_number";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TOTAL_AMOUNT = "total_amount";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_SHIPPING_ADDRESS = "shipping_address";
    }
    
    /* Inner class that defines the OrderItem table */
    public static class OrderItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "order_items";
        public static final String COLUMN_ORDER_ID = "order_id";
        public static final String COLUMN_PRODUCT_ID = "product_id";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";
    }
    
    /* Inner class that defines the User table */
    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_PHONE = "phone";
    }
    
    // SQL statements to create tables
    public static final String SQL_CREATE_PRODUCTS =
            "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                    ProductEntry._ID + " INTEGER PRIMARY KEY," +
                    ProductEntry.COLUMN_NAME + " TEXT NOT NULL," +
                    ProductEntry.COLUMN_DESCRIPTION + " TEXT," +
                    ProductEntry.COLUMN_PRICE + " REAL NOT NULL," +
                    ProductEntry.COLUMN_CATEGORY_ID + " INTEGER," +
                    ProductEntry.COLUMN_IMAGE_URL + " TEXT," +
                    ProductEntry.COLUMN_STOCK + " INTEGER," +
                    "FOREIGN KEY(" + ProductEntry.COLUMN_CATEGORY_ID + ") REFERENCES " +
                    CategoryEntry.TABLE_NAME + "(" + CategoryEntry._ID + "))";
    
    public static final String SQL_CREATE_CATEGORIES =
            "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                    CategoryEntry._ID + " INTEGER PRIMARY KEY," +
                    CategoryEntry.COLUMN_NAME + " TEXT NOT NULL," +
                    CategoryEntry.COLUMN_DESCRIPTION + " TEXT)";
    
    public static final String SQL_CREATE_CART =
            "CREATE TABLE " + CartEntry.TABLE_NAME + " (" +
                    CartEntry._ID + " INTEGER PRIMARY KEY," +
                    CartEntry.COLUMN_PRODUCT_ID + " INTEGER NOT NULL," +
                    CartEntry.COLUMN_QUANTITY + " INTEGER NOT NULL," +
                    "FOREIGN KEY(" + CartEntry.COLUMN_PRODUCT_ID + ") REFERENCES " +
                    ProductEntry.TABLE_NAME + "(" + ProductEntry._ID + "))";
    
    public static final String SQL_CREATE_ORDERS =
            "CREATE TABLE " + OrderEntry.TABLE_NAME + " (" +
                    OrderEntry._ID + " INTEGER PRIMARY KEY," +
                    OrderEntry.COLUMN_ORDER_NUMBER + " TEXT NOT NULL," +
                    OrderEntry.COLUMN_DATE + " INTEGER NOT NULL," +
                    OrderEntry.COLUMN_TOTAL_AMOUNT + " REAL NOT NULL," +
                    OrderEntry.COLUMN_STATUS + " TEXT NOT NULL," +
                    OrderEntry.COLUMN_SHIPPING_ADDRESS + " TEXT)";
    
    public static final String SQL_CREATE_ORDER_ITEMS =
            "CREATE TABLE " + OrderItemEntry.TABLE_NAME + " (" +
                    OrderItemEntry._ID + " INTEGER PRIMARY KEY," +
                    OrderItemEntry.COLUMN_ORDER_ID + " INTEGER NOT NULL," +
                    OrderItemEntry.COLUMN_PRODUCT_ID + " INTEGER NOT NULL," +
                    OrderItemEntry.COLUMN_QUANTITY + " INTEGER NOT NULL," +
                    OrderItemEntry.COLUMN_PRICE + " REAL NOT NULL," +
                    "FOREIGN KEY(" + OrderItemEntry.COLUMN_ORDER_ID + ") REFERENCES " +
                    OrderEntry.TABLE_NAME + "(" + OrderEntry._ID + ")," +
                    "FOREIGN KEY(" + OrderItemEntry.COLUMN_PRODUCT_ID + ") REFERENCES " +
                    ProductEntry.TABLE_NAME + "(" + ProductEntry._ID + "))";
    
    public static final String SQL_CREATE_USERS =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME + " TEXT NOT NULL," +
                    UserEntry.COLUMN_EMAIL + " TEXT NOT NULL," +
                    UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL," +
                    UserEntry.COLUMN_ADDRESS + " TEXT," +
                    UserEntry.COLUMN_PHONE + " TEXT)";
}
