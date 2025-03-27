package com.example.ecommerceapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ecommerceapp.model.CartItem;
import com.example.ecommerceapp.model.Category;
import com.example.ecommerceapp.model.Order;
import com.example.ecommerceapp.model.Product;
import com.example.ecommerceapp.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * SQLite Database helper class that manages database creation and version management
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    
    // Database Information
    private static final String DATABASE_NAME = "ecommerceapp.db";
    private static final int DATABASE_VERSION = 1;
    
    // Date formatter for storing dates in SQLite
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    
    // Singleton instance
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + 
                DatabaseContract.UserEntry.TABLE_NAME + " (" +
                DatabaseContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.UserEntry.COLUMN_USERNAME + " TEXT UNIQUE NOT NULL," +
                DatabaseContract.UserEntry.COLUMN_EMAIL + " TEXT UNIQUE NOT NULL," +
                DatabaseContract.UserEntry.COLUMN_FULL_NAME + " TEXT," +
                DatabaseContract.UserEntry.COLUMN_ADDRESS + " TEXT," +
                DatabaseContract.UserEntry.COLUMN_PHONE + " TEXT," +
                DatabaseContract.UserEntry.COLUMN_PASSWORD_HASH + " TEXT NOT NULL" +
                ");";

        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " + 
                DatabaseContract.CategoryEntry.TABLE_NAME + " (" +
                DatabaseContract.CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.CategoryEntry.COLUMN_NAME + " TEXT NOT NULL," +
                DatabaseContract.CategoryEntry.COLUMN_DESCRIPTION + " TEXT," +
                DatabaseContract.CategoryEntry.COLUMN_IMAGE_URL + " TEXT" +
                ");";

        final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + 
                DatabaseContract.ProductEntry.TABLE_NAME + " (" +
                DatabaseContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.ProductEntry.COLUMN_NAME + " TEXT NOT NULL," +
                DatabaseContract.ProductEntry.COLUMN_DESCRIPTION + " TEXT," +
                DatabaseContract.ProductEntry.COLUMN_PRICE + " REAL NOT NULL," +
                DatabaseContract.ProductEntry.COLUMN_IMAGE_URL + " TEXT," +
                DatabaseContract.ProductEntry.COLUMN_CATEGORY_ID + " INTEGER NOT NULL," +
                DatabaseContract.ProductEntry.COLUMN_STOCK_QUANTITY + " INTEGER DEFAULT 0," +
                DatabaseContract.ProductEntry.COLUMN_RATING + " REAL DEFAULT 0," +
                DatabaseContract.ProductEntry.COLUMN_IS_FEATURED + " INTEGER DEFAULT 0," +
                "FOREIGN KEY (" + DatabaseContract.ProductEntry.COLUMN_CATEGORY_ID + ") " +
                "REFERENCES " + DatabaseContract.CategoryEntry.TABLE_NAME + "(" + DatabaseContract.CategoryEntry._ID + ")" +
                ");";

        final String SQL_CREATE_CART_TABLE = "CREATE TABLE " + 
                DatabaseContract.CartEntry.TABLE_NAME + " (" +
                DatabaseContract.CartEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.CartEntry.COLUMN_PRODUCT_ID + " INTEGER NOT NULL," +
                DatabaseContract.CartEntry.COLUMN_QUANTITY + " INTEGER NOT NULL," +
                DatabaseContract.CartEntry.COLUMN_PRICE_AT_ADDITION + " REAL NOT NULL," +
                "FOREIGN KEY (" + DatabaseContract.CartEntry.COLUMN_PRODUCT_ID + ") " +
                "REFERENCES " + DatabaseContract.ProductEntry.TABLE_NAME + "(" + DatabaseContract.ProductEntry._ID + ")" +
                ");";

        final String SQL_CREATE_ORDERS_TABLE = "CREATE TABLE " + 
                DatabaseContract.OrderEntry.TABLE_NAME + " (" +
                DatabaseContract.OrderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.OrderEntry.COLUMN_USER_ID + " INTEGER," +
                DatabaseContract.OrderEntry.COLUMN_ORDER_DATE + " TEXT NOT NULL," +
                DatabaseContract.OrderEntry.COLUMN_TOTAL_AMOUNT + " REAL NOT NULL," +
                DatabaseContract.OrderEntry.COLUMN_SHIPPING_ADDRESS + " TEXT NOT NULL," +
                DatabaseContract.OrderEntry.COLUMN_PAYMENT_METHOD + " TEXT NOT NULL," +
                DatabaseContract.OrderEntry.COLUMN_STATUS + " TEXT NOT NULL," +
                "FOREIGN KEY (" + DatabaseContract.OrderEntry.COLUMN_USER_ID + ") " +
                "REFERENCES " + DatabaseContract.UserEntry.TABLE_NAME + "(" + DatabaseContract.UserEntry._ID + ")" +
                ");";

        final String SQL_CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE " + 
                DatabaseContract.OrderItemEntry.TABLE_NAME + " (" +
                DatabaseContract.OrderItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.OrderItemEntry.COLUMN_ORDER_ID + " INTEGER NOT NULL," +
                DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID + " INTEGER NOT NULL," +
                DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL," +
                DatabaseContract.OrderItemEntry.COLUMN_QUANTITY + " INTEGER NOT NULL," +
                DatabaseContract.OrderItemEntry.COLUMN_PRICE + " REAL NOT NULL," +
                "FOREIGN KEY (" + DatabaseContract.OrderItemEntry.COLUMN_ORDER_ID + ") " +
                "REFERENCES " + DatabaseContract.OrderEntry.TABLE_NAME + "(" + DatabaseContract.OrderEntry._ID + ")," +
                "FOREIGN KEY (" + DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID + ") " +
                "REFERENCES " + DatabaseContract.ProductEntry.TABLE_NAME + "(" + DatabaseContract.ProductEntry._ID + ")" +
                ");";

        final String SQL_CREATE_CHAT_MESSAGES_TABLE = "CREATE TABLE " + 
                DatabaseContract.ChatMessageEntry.TABLE_NAME + " (" +
                DatabaseContract.ChatMessageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseContract.ChatMessageEntry.COLUMN_USER_ID + " INTEGER," +
                DatabaseContract.ChatMessageEntry.COLUMN_MESSAGE + " TEXT NOT NULL," +
                DatabaseContract.ChatMessageEntry.COLUMN_IS_USER_MESSAGE + " INTEGER NOT NULL," +
                DatabaseContract.ChatMessageEntry.COLUMN_TIMESTAMP + " TEXT NOT NULL," +
                "FOREIGN KEY (" + DatabaseContract.ChatMessageEntry.COLUMN_USER_ID + ") " +
                "REFERENCES " + DatabaseContract.UserEntry.TABLE_NAME + "(" + DatabaseContract.UserEntry._ID + ")" +
                ");";

        // Execute create table statements
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
        db.execSQL(SQL_CREATE_CART_TABLE);
        db.execSQL(SQL_CREATE_ORDERS_TABLE);
        db.execSQL(SQL_CREATE_ORDER_ITEMS_TABLE);
        db.execSQL(SQL_CREATE_CHAT_MESSAGES_TABLE);
        
        // Insert initial data
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only for a demo, so we can simply drop the tables and recreate
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.ChatMessageEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.OrderItemEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.OrderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.CartEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.ProductEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.CategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Insert initial dummy data for demonstration purposes
     */
    private void insertInitialData(SQLiteDatabase db) {
        // Insert categories
        insertCategory(db, new Category(1, "Electronics", "Electronic devices and gadgets", "https://cdn-icons-png.flaticon.com/512/3659/3659898.png"));
        insertCategory(db, new Category(2, "Clothing", "Fashion items and apparel", "https://cdn-icons-png.flaticon.com/512/2589/2589175.png"));
        insertCategory(db, new Category(3, "Books", "Books and publications", "https://cdn-icons-png.flaticon.com/512/2436/2436882.png"));
        insertCategory(db, new Category(4, "Home & Kitchen", "Home and kitchen appliances", "https://cdn-icons-png.flaticon.com/512/2082/2082454.png"));
        insertCategory(db, new Category(5, "Beauty", "Beauty and personal care", "https://cdn-icons-png.flaticon.com/512/2442/2442280.png"));
        
        // Insert products for Electronics category
        insertProduct(db, new Product(1, "Smartphone X Pro", "Latest smartphone with advanced features", 
                899.99, "https://cdn-icons-png.flaticon.com/512/3659/3659899.png", 1, 100, 4.5f, true));
        insertProduct(db, new Product(2, "Laptop Ultra", "High performance laptop for professionals", 
                1299.99, "https://cdn-icons-png.flaticon.com/512/689/689396.png", 1, 50, 4.7f, true));
        insertProduct(db, new Product(3, "Wireless Earbuds", "True wireless earbuds with noise cancellation", 
                149.99, "https://cdn-icons-png.flaticon.com/512/2944/2944037.png", 1, 200, 4.2f, false));
        insertProduct(db, new Product(4, "Smart Watch", "Fitness and health tracking smart watch", 
                249.99, "https://cdn-icons-png.flaticon.com/512/3168/3168960.png", 1, 75, 4.0f, false));
        insertProduct(db, new Product(5, "Bluetooth Speaker", "Portable wireless speaker with rich sound", 
                79.99, "https://cdn-icons-png.flaticon.com/512/2395/2395179.png", 1, 150, 4.3f, false));
                
        // Insert products for Clothing category
        insertProduct(db, new Product(6, "Men's T-Shirt", "Comfortable cotton t-shirt for men", 
                24.99, "https://cdn-icons-png.flaticon.com/512/2589/2589303.png", 2, 300, 4.1f, false));
        insertProduct(db, new Product(7, "Women's Dress", "Elegant casual dress for women", 
                49.99, "https://cdn-icons-png.flaticon.com/512/5976/5976525.png", 2, 200, 4.4f, true));
        insertProduct(db, new Product(8, "Jeans", "Classic blue denim jeans", 
                39.99, "https://cdn-icons-png.flaticon.com/512/5976/5976759.png", 2, 250, 4.2f, false));
        insertProduct(db, new Product(9, "Sneakers", "Casual comfortable sneakers", 
                59.99, "https://cdn-icons-png.flaticon.com/512/5499/5499206.png", 2, 150, 4.3f, true));
        insertProduct(db, new Product(10, "Winter Jacket", "Warm insulated winter jacket", 
                89.99, "https://cdn-icons-png.flaticon.com/512/2589/2589061.png", 2, 100, 4.5f, false));
                
        // Insert products for Books category
        insertProduct(db, new Product(11, "Novel: The Adventure", "Bestselling adventure novel", 
                14.99, "https://cdn-icons-png.flaticon.com/512/3144/3144467.png", 3, 200, 4.7f, true));
        insertProduct(db, new Product(12, "Programming Guide", "Comprehensive guide to programming", 
                29.99, "https://cdn-icons-png.flaticon.com/512/3650/3650050.png", 3, 100, 4.8f, false));
        insertProduct(db, new Product(13, "Cookbook", "Collection of delicious recipes", 
                19.99, "https://cdn-icons-png.flaticon.com/512/1065/1065413.png", 3, 150, 4.2f, false));
        insertProduct(db, new Product(14, "Self-Help Book", "Book for personal development", 
                12.99, "https://cdn-icons-png.flaticon.com/512/3144/3144583.png", 3, 180, 4.4f, false));
        insertProduct(db, new Product(15, "Science Fiction", "Exciting sci-fi novel", 
                11.99, "https://cdn-icons-png.flaticon.com/512/3144/3144456.png", 3, 220, 4.6f, true));
    }

    private long insertCategory(SQLiteDatabase db, Category category) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CategoryEntry.COLUMN_NAME, category.getName());
        values.put(DatabaseContract.CategoryEntry.COLUMN_DESCRIPTION, category.getDescription());
        values.put(DatabaseContract.CategoryEntry.COLUMN_IMAGE_URL, category.getImageUrl());
        
        return db.insert(DatabaseContract.CategoryEntry.TABLE_NAME, null, values);
    }

    private long insertProduct(SQLiteDatabase db, Product product) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ProductEntry.COLUMN_NAME, product.getName());
        values.put(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION, product.getDescription());
        values.put(DatabaseContract.ProductEntry.COLUMN_PRICE, product.getPrice());
        values.put(DatabaseContract.ProductEntry.COLUMN_IMAGE_URL, product.getImageUrl());
        values.put(DatabaseContract.ProductEntry.COLUMN_CATEGORY_ID, product.getCategoryId());
        values.put(DatabaseContract.ProductEntry.COLUMN_STOCK_QUANTITY, product.getStockQuantity());
        values.put(DatabaseContract.ProductEntry.COLUMN_RATING, product.getRating());
        values.put(DatabaseContract.ProductEntry.COLUMN_IS_FEATURED, product.isFeatured() ? 1 : 0);
        
        return db.insert(DatabaseContract.ProductEntry.TABLE_NAME, null, values);
    }

    //------------------ User Methods ------------------//
    
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(DatabaseContract.UserEntry.COLUMN_USERNAME, user.getUsername());
        values.put(DatabaseContract.UserEntry.COLUMN_EMAIL, user.getEmail());
        values.put(DatabaseContract.UserEntry.COLUMN_FULL_NAME, user.getFullName());
        values.put(DatabaseContract.UserEntry.COLUMN_ADDRESS, user.getAddress());
        values.put(DatabaseContract.UserEntry.COLUMN_PHONE, user.getPhone());
        values.put(DatabaseContract.UserEntry.COLUMN_PASSWORD_HASH, user.getPasswordHash());
        
        long id = db.insert(DatabaseContract.UserEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }
    
    public User getUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(
                DatabaseContract.UserEntry.TABLE_NAME,
                null,
                DatabaseContract.UserEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_FULL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_PASSWORD_HASH))
            );
            cursor.close();
        }
        
        return user;
    }
    
    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(
                DatabaseContract.UserEntry.TABLE_NAME,
                null,
                DatabaseContract.UserEntry.COLUMN_USERNAME + " = ?",
                new String[]{username},
                null, null, null);
        
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_FULL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.UserEntry.COLUMN_PASSWORD_HASH))
            );
            cursor.close();
        }
        
        return user;
    }
    
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserEntry.COLUMN_EMAIL, user.getEmail());
        values.put(DatabaseContract.UserEntry.COLUMN_FULL_NAME, user.getFullName());
        values.put(DatabaseContract.UserEntry.COLUMN_ADDRESS, user.getAddress());
        values.put(DatabaseContract.UserEntry.COLUMN_PHONE, user.getPhone());
        
        // Don't update username or password hash here for security reasons
        
        int rowsAffected = db.update(
                DatabaseContract.UserEntry.TABLE_NAME,
                values,
                DatabaseContract.UserEntry._ID + " = ?",
                new String[]{String.valueOf(user.getId())}
        );
        
        db.close();
        return rowsAffected;
    }
    
    //------------------ Product Methods ------------------//
    
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseContract.ProductEntry.TABLE_NAME;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_STOCK_QUANTITY)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_RATING)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_IS_FEATURED)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return productList;
    }
    
    public List<Product> getFeaturedProducts() {
        List<Product> productList = new ArrayList<>();
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseContract.ProductEntry.TABLE_NAME,
                null,
                DatabaseContract.ProductEntry.COLUMN_IS_FEATURED + " = ?",
                new String[]{"1"},
                null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_STOCK_QUANTITY)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_RATING)),
                        true
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return productList;
    }
    
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> productList = new ArrayList<>();
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseContract.ProductEntry.TABLE_NAME,
                null,
                DatabaseContract.ProductEntry.COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)},
                null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_STOCK_QUANTITY)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_RATING)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_IS_FEATURED)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return productList;
    }
    
    public Product getProduct(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(
                DatabaseContract.ProductEntry.TABLE_NAME,
                null,
                DatabaseContract.ProductEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        
        Product product = null;
        if (cursor != null && cursor.moveToFirst()) {
            product = new Product(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_IMAGE_URL)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_CATEGORY_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_STOCK_QUANTITY)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_RATING)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_IS_FEATURED)) == 1
            );
            cursor.close();
        }
        
        return product;
    }
    
    public List<Product> searchProducts(String query) {
        List<Product> productList = new ArrayList<>();
        
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + DatabaseContract.ProductEntry.TABLE_NAME +
                " WHERE " + DatabaseContract.ProductEntry.COLUMN_NAME + " LIKE ? OR " +
                DatabaseContract.ProductEntry.COLUMN_DESCRIPTION + " LIKE ?";
        
        String likeQuery = "%" + query + "%";
        Cursor cursor = db.rawQuery(searchQuery, new String[]{likeQuery, likeQuery});
        
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_STOCK_QUANTITY)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_RATING)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_IS_FEATURED)) == 1
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return productList;
    }
    
    //------------------ Category Methods ------------------//
    
    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DatabaseContract.CategoryEntry.TABLE_NAME;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry.COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry.COLUMN_IMAGE_URL))
                );
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return categoryList;
    }
    
    public Category getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        Cursor cursor = db.query(
                DatabaseContract.CategoryEntry.TABLE_NAME,
                null,
                DatabaseContract.CategoryEntry._ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        
        Category category = null;
        if (cursor != null && cursor.moveToFirst()) {
            category = new Category(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry.COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry.COLUMN_IMAGE_URL))
            );
            cursor.close();
        }
        
        return category;
    }
    
    //------------------ Cart Methods ------------------//
    
    public long addToCart(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Check if the product already exists in the cart
        Cursor cursor = db.query(
                DatabaseContract.CartEntry.TABLE_NAME,
                null,
                DatabaseContract.CartEntry.COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(cartItem.getProductId())},
                null, null, null);
        
        long id;
        
        if (cursor != null && cursor.moveToFirst()) {
            // Product exists in cart, update quantity
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CartEntry.COLUMN_QUANTITY));
            int newQuantity = currentQuantity + cartItem.getQuantity();
            
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.CartEntry.COLUMN_QUANTITY, newQuantity);
            
            id = db.update(
                    DatabaseContract.CartEntry.TABLE_NAME,
                    values,
                    DatabaseContract.CartEntry.COLUMN_PRODUCT_ID + " = ?",
                    new String[]{String.valueOf(cartItem.getProductId())}
            );
            
            cursor.close();
        } else {
            // Product not in cart, add new item
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.CartEntry.COLUMN_PRODUCT_ID, cartItem.getProductId());
            values.put(DatabaseContract.CartEntry.COLUMN_QUANTITY, cartItem.getQuantity());
            values.put(DatabaseContract.CartEntry.COLUMN_PRICE_AT_ADDITION, cartItem.getPriceAtAddition());
            
            id = db.insert(DatabaseContract.CartEntry.TABLE_NAME, null, values);
        }
        
        db.close();
        return id;
    }
    
    public List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.*, p.name, p.description, p.image_url, p.category_id, p.stock_quantity, p.rating, p.is_featured " +
                "FROM " + DatabaseContract.CartEntry.TABLE_NAME + " c " +
                "JOIN " + DatabaseContract.ProductEntry.TABLE_NAME + " p " +
                "ON c." + DatabaseContract.CartEntry.COLUMN_PRODUCT_ID + " = p." + DatabaseContract.ProductEntry._ID;
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor.moveToFirst()) {
            do {
                CartItem cartItem = new CartItem(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.CartEntry._ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.CartEntry.COLUMN_PRODUCT_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CartEntry.COLUMN_QUANTITY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.CartEntry.COLUMN_PRICE_AT_ADDITION))
                );
                
                // Create and set the product object
                Product product = new Product(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.CartEntry.COLUMN_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.CartEntry.COLUMN_PRICE_AT_ADDITION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_IMAGE_URL)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_CATEGORY_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_STOCK_QUANTITY)),
                        cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_RATING)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ProductEntry.COLUMN_IS_FEATURED)) == 1
                );
                
                cartItem.setProduct(product);
                cartItems.add(cartItem);
                
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return cartItems;
    }
    
    public int updateCartItemQuantity(long cartItemId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CartEntry.COLUMN_QUANTITY, quantity);
        
        int rowsAffected = db.update(
                DatabaseContract.CartEntry.TABLE_NAME,
                values,
                DatabaseContract.CartEntry._ID + " = ?",
                new String[]{String.valueOf(cartItemId)}
        );
        
        db.close();
        return rowsAffected;
    }
    
    public int removeCartItem(long cartItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        int rowsAffected = db.delete(
                DatabaseContract.CartEntry.TABLE_NAME,
                DatabaseContract.CartEntry._ID + " = ?",
                new String[]{String.valueOf(cartItemId)}
        );
        
        db.close();
        return rowsAffected;
    }
    
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseContract.CartEntry.TABLE_NAME, null, null);
        db.close();
    }
    
    //------------------ Order Methods ------------------//
    
    public long createOrder(Order order, List<CartItem> cartItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        
        try {
            // Insert order
            ContentValues orderValues = new ContentValues();
            orderValues.put(DatabaseContract.OrderEntry.COLUMN_USER_ID, order.getUserId());
            orderValues.put(DatabaseContract.OrderEntry.COLUMN_ORDER_DATE, dateFormat.format(order.getOrderDate()));
            orderValues.put(DatabaseContract.OrderEntry.COLUMN_TOTAL_AMOUNT, order.getTotalAmount());
            orderValues.put(DatabaseContract.OrderEntry.COLUMN_SHIPPING_ADDRESS, order.getShippingAddress());
            orderValues.put(DatabaseContract.OrderEntry.COLUMN_PAYMENT_METHOD, order.getPaymentMethod());
            orderValues.put(DatabaseContract.OrderEntry.COLUMN_STATUS, order.getStatus());
            
            long orderId = db.insert(DatabaseContract.OrderEntry.TABLE_NAME, null, orderValues);
            
            if (orderId == -1) {
                throw new Exception("Failed to create order");
            }
            
            // Insert order items
            for (CartItem cartItem : cartItems) {
                ContentValues itemValues = new ContentValues();
                itemValues.put(DatabaseContract.OrderItemEntry.COLUMN_ORDER_ID, orderId);
                itemValues.put(DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID, cartItem.getProductId());
                itemValues.put(DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_NAME, cartItem.getProduct().getName());
                itemValues.put(DatabaseContract.OrderItemEntry.COLUMN_QUANTITY, cartItem.getQuantity());
                itemValues.put(DatabaseContract.OrderItemEntry.COLUMN_PRICE, cartItem.getPriceAtAddition());
                
                long orderItemId = db.insert(DatabaseContract.OrderItemEntry.TABLE_NAME, null, itemValues);
                
                if (orderItemId == -1) {
                    throw new Exception("Failed to add order item");
                }
            }
            
            // Clear cart after successful order creation
            db.delete(DatabaseContract.CartEntry.TABLE_NAME, null, null);
            
            db.setTransactionSuccessful();
            return orderId;
            
        } catch (Exception e) {
            Log.e(TAG, "Error creating order: " + e.getMessage());
            return -1;
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    
    public List<Order> getUserOrders(long userId) {
        List<Order> orders = new ArrayList<>();
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseContract.OrderEntry.TABLE_NAME,
                null,
                DatabaseContract.OrderEntry.COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null,
                DatabaseContract.OrderEntry.COLUMN_ORDER_DATE + " DESC");
        
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.OrderEntry._ID)));
                order.setUserId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.OrderEntry.COLUMN_USER_ID)));
                
                try {
                    String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.OrderEntry.COLUMN_ORDER_DATE));
                    order.setOrderDate(dateFormat.parse(dateStr));
                } catch (ParseException e) {
                    order.setOrderDate(new Date());
                }
                
                order.setTotalAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.OrderEntry.COLUMN_TOTAL_AMOUNT)));
                order.setShippingAddress(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.OrderEntry.COLUMN_SHIPPING_ADDRESS)));
                order.setPaymentMethod(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.OrderEntry.COLUMN_PAYMENT_METHOD)));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.OrderEntry.COLUMN_STATUS)));
                
                // Get order items
                List<Order.OrderItem> orderItems = getOrderItems(order.getId());
                order.setItems(orderItems);
                
                orders.add(order);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return orders;
    }
    
    private List<Order.OrderItem> getOrderItems(long orderId) {
        List<Order.OrderItem> items = new ArrayList<>();
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseContract.OrderItemEntry.TABLE_NAME,
                null,
                DatabaseContract.OrderItemEntry.COLUMN_ORDER_ID + " = ?",
                new String[]{String.valueOf(orderId)},
                null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                Order.OrderItem item = new Order.OrderItem(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.OrderItemEntry._ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.OrderItemEntry.COLUMN_ORDER_ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.OrderItemEntry.COLUMN_PRODUCT_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.OrderItemEntry.COLUMN_QUANTITY)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.OrderItemEntry.COLUMN_PRICE))
                );
                items.add(item);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        return items;
    }
}
