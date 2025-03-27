package com.ecommerce.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ecommerce.app.model.CartItem;
import com.ecommerce.app.model.Category;
import com.ecommerce.app.model.ChatMessage;
import com.ecommerce.app.model.Order;
import com.ecommerce.app.model.Product;
import com.ecommerce.app.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "ecommerce.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_CATEGORIES = "categories";
    private static final String TABLE_CART = "cart";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_ORDER_ITEMS = "order_items";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_CHAT_MESSAGES = "chat_messages";

    // Common Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE_URL = "image_url";

    // Products Table Columns
    private static final String KEY_PRICE = "price";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_STOCK = "stock";
    private static final String KEY_RATING = "rating";
    private static final String KEY_REVIEW_COUNT = "review_count";

    // Cart Table Columns
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_PRODUCT_IMAGE_URL = "product_image_url";
    private static final String KEY_PRODUCT_PRICE = "product_price";
    private static final String KEY_QUANTITY = "quantity";

    // Orders Table Columns
    private static final String KEY_ORDER_DATE = "order_date";
    private static final String KEY_ORDER_STATUS = "status";
    private static final String KEY_TOTAL_AMOUNT = "total_amount";
    private static final String KEY_SHIPPING_ADDRESS = "shipping_address";
    private static final String KEY_PAYMENT_METHOD = "payment_method";

    // Order Items Table Columns
    private static final String KEY_ORDER_ID = "order_id";

    // User Table Columns
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PROFILE_IMAGE_URL = "profile_image_url";

    // Chat Messages Table Columns
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_MESSAGE_TYPE = "message_type";
    private static final String KEY_TIMESTAMP = "timestamp";

    // Create table statements
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT NOT NULL, "
            + KEY_DESCRIPTION + " TEXT, "
            + KEY_PRICE + " REAL NOT NULL, "
            + KEY_IMAGE_URL + " TEXT, "
            + KEY_CATEGORY_ID + " INTEGER, "
            + KEY_STOCK + " INTEGER, "
            + KEY_RATING + " REAL, "
            + KEY_REVIEW_COUNT + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT NOT NULL, "
            + KEY_DESCRIPTION + " TEXT, "
            + KEY_IMAGE_URL + " TEXT"
            + ")";

    private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_PRODUCT_ID + " INTEGER NOT NULL, "
            + KEY_PRODUCT_NAME + " TEXT NOT NULL, "
            + KEY_PRODUCT_IMAGE_URL + " TEXT, "
            + KEY_PRODUCT_PRICE + " REAL NOT NULL, "
            + KEY_QUANTITY + " INTEGER NOT NULL"
            + ")";

    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ORDER_DATE + " INTEGER NOT NULL, "
            + KEY_ORDER_STATUS + " TEXT NOT NULL, "
            + KEY_TOTAL_AMOUNT + " REAL NOT NULL, "
            + KEY_SHIPPING_ADDRESS + " TEXT, "
            + KEY_PAYMENT_METHOD + " TEXT"
            + ")";

    private static final String CREATE_TABLE_ORDER_ITEMS = "CREATE TABLE " + TABLE_ORDER_ITEMS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_ORDER_ID + " INTEGER NOT NULL, "
            + KEY_PRODUCT_ID + " INTEGER NOT NULL, "
            + KEY_PRODUCT_NAME + " TEXT NOT NULL, "
            + KEY_PRODUCT_IMAGE_URL + " TEXT, "
            + KEY_PRODUCT_PRICE + " REAL NOT NULL, "
            + KEY_QUANTITY + " INTEGER NOT NULL"
            + ")";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_NAME + " TEXT NOT NULL, "
            + KEY_EMAIL + " TEXT, "
            + KEY_PHONE + " TEXT, "
            + KEY_ADDRESS + " TEXT, "
            + KEY_PROFILE_IMAGE_URL + " TEXT"
            + ")";

    private static final String CREATE_TABLE_CHAT_MESSAGES = "CREATE TABLE " + TABLE_CHAT_MESSAGES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_MESSAGE + " TEXT NOT NULL, "
            + KEY_MESSAGE_TYPE + " INTEGER NOT NULL, "
            + KEY_TIMESTAMP + " INTEGER NOT NULL"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_ORDER_ITEMS);
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CHAT_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_MESSAGES);

        // Create tables again
        onCreate(db);
    }

    // Initialize database with sample data
    public void populateInitialData() {
        // Add categories
        addCategory(new Category(1, "Electronics", "Electronic devices and gadgets", "https://cdn.pixabay.com/photo/2014/04/02/10/48/electronics-304668_1280.png"));
        addCategory(new Category(2, "Clothing", "Fashion clothes and accessories", "https://cdn.pixabay.com/photo/2017/08/01/08/29/woman-2563491_1280.jpg"));
        addCategory(new Category(3, "Books", "Books, novels and educational material", "https://cdn.pixabay.com/photo/2016/03/27/19/32/book-1283865_1280.jpg"));
        addCategory(new Category(4, "Home & Kitchen", "Home decor and kitchen appliances", "https://cdn.pixabay.com/photo/2016/11/18/14/20/kitchen-1834828_1280.jpg"));
        addCategory(new Category(5, "Sports & Outdoors", "Sports equipment and outdoor gear", "https://cdn.pixabay.com/photo/2014/11/17/13/17/crossfit-534615_1280.jpg"));

        // Add products
        // Electronics
        addProduct(new Product(1, "Smartphone X", "Latest smartphone with advanced features", 699.99, 
                "https://cdn.pixabay.com/photo/2016/12/09/11/33/smartphone-1894723_1280.jpg", 1, 100, 4.5f, 120));
        addProduct(new Product(2, "Laptop Pro", "High-performance laptop for professionals", 1299.99, 
                "https://cdn.pixabay.com/photo/2016/03/27/07/12/apple-1282241_1280.jpg", 1, 50, 4.7f, 85));
        addProduct(new Product(3, "Wireless Headphones", "Premium noise-canceling headphones", 199.99, 
                "https://cdn.pixabay.com/photo/2017/08/06/08/05/headphones-2590497_1280.jpg", 1, 200, 4.3f, 210));
        
        // Clothing
        addProduct(new Product(4, "Men's Casual Shirt", "Comfortable cotton shirt for everyday wear", 39.99, 
                "https://cdn.pixabay.com/photo/2017/08/06/12/52/people-2593731_1280.jpg", 2, 300, 4.1f, 95));
        addProduct(new Product(5, "Women's Summer Dress", "Elegant dress for summer occasions", 59.99, 
                "https://cdn.pixabay.com/photo/2016/03/23/08/34/woman-1274360_1280.jpg", 2, 150, 4.4f, 78));
        
        // Books
        addProduct(new Product(6, "The Great Novel", "Bestselling fiction book of the year", 15.99, 
                "https://cdn.pixabay.com/photo/2015/11/19/21/10/glasses-1052010_1280.jpg", 3, 500, 4.8f, 320));
        addProduct(new Product(7, "Cooking Masterclass", "Complete guide to culinary arts", 24.99, 
                "https://cdn.pixabay.com/photo/2014/09/25/22/05/cookbook-460403_1280.jpg", 3, 200, 4.6f, 150));
        
        // Home & Kitchen
        addProduct(new Product(8, "Coffee Maker", "Automatic coffee machine for home use", 89.99, 
                "https://cdn.pixabay.com/photo/2015/05/31/11/24/coffee-791439_1280.jpg", 4, 100, 4.2f, 65));
        addProduct(new Product(9, "Bedding Set", "Luxury cotton bedding set, queen size", 79.99, 
                "https://cdn.pixabay.com/photo/2016/11/19/13/06/bed-1839564_1280.jpg", 4, 75, 4.5f, 42));
        
        // Sports & Outdoors
        addProduct(new Product(10, "Yoga Mat", "Non-slip yoga mat for home workouts", 29.99, 
                "https://cdn.pixabay.com/photo/2017/07/02/19/24/dumbbells-2465478_1280.jpg", 5, 150, 4.3f, 88));
        addProduct(new Product(11, "Tennis Racket", "Professional tennis racket", 119.99, 
                "https://cdn.pixabay.com/photo/2016/05/09/11/09/tennis-1381230_1280.jpg", 5, 50, 4.7f, 36));

        // Add default user
        addUser(new User(1, "John Doe", "johndoe@example.com", "123-456-7890", "123 Main St, Anytown, USA", 
                "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"));
    }

    // ----- Product Operations -----
    
    public long addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(KEY_NAME, product.getName());
        values.put(KEY_DESCRIPTION, product.getDescription());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_IMAGE_URL, product.getImageUrl());
        values.put(KEY_CATEGORY_ID, product.getCategoryId());
        values.put(KEY_STOCK, product.getStock());
        values.put(KEY_RATING, product.getRating());
        values.put(KEY_REVIEW_COUNT, product.getReviewCount());
        
        long id = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        
        return id;
    }
    
    public Product getProduct(long productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_ID + " = " + productId;
        Log.d(TAG, selectQuery);
        
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        Product product = null;
        if (cursor.moveToFirst()) {
            product = new Product();
            product.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
            product.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            product.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
            product.setPrice(cursor.getDouble(cursor.getColumnIndex(KEY_PRICE)));
            product.setImageUrl(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL)));
            product.setCategoryId(cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)));
            product.setStock(cursor.getInt(cursor.getColumnIndex(KEY_STOCK)));
            product.setRating(cursor.getFloat(cursor.getColumnIndex(KEY_RATING)));
            product.setReviewCount(cursor.getInt(cursor.getColumnIndex(KEY_REVIEW_COUNT)));
        }
        
        cursor.close();
        db.close();
        
        return product;
    }
    
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        Log.d(TAG, selectQuery);
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex(KEY_PRICE)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL)));
                product.setCategoryId(cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)));
                product.setStock(cursor.getInt(cursor.getColumnIndex(KEY_STOCK)));
                product.setRating(cursor.getFloat(cursor.getColumnIndex(KEY_RATING)));
                product.setReviewCount(cursor.getInt(cursor.getColumnIndex(KEY_REVIEW_COUNT)));
                
                products.add(product);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        return products;
    }
    
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + 
                             " WHERE " + KEY_CATEGORY_ID + " = " + categoryId;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex(KEY_PRICE)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL)));
                product.setCategoryId(cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)));
                product.setStock(cursor.getInt(cursor.getColumnIndex(KEY_STOCK)));
                product.setRating(cursor.getFloat(cursor.getColumnIndex(KEY_RATING)));
                product.setReviewCount(cursor.getInt(cursor.getColumnIndex(KEY_REVIEW_COUNT)));
                
                products.add(product);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        return products;
    }
    
    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS + 
                             " WHERE " + KEY_NAME + " LIKE '%" + keyword + "%'" +
                             " OR " + KEY_DESCRIPTION + " LIKE '%" + keyword + "%'";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                product.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex(KEY_PRICE)));
                product.setImageUrl(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL)));
                product.setCategoryId(cursor.getInt(cursor.getColumnIndex(KEY_CATEGORY_ID)));
                product.setStock(cursor.getInt(cursor.getColumnIndex(KEY_STOCK)));
                product.setRating(cursor.getFloat(cursor.getColumnIndex(KEY_RATING)));
                product.setReviewCount(cursor.getInt(cursor.getColumnIndex(KEY_REVIEW_COUNT)));
                
                products.add(product);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        return products;
    }
    
    // ----- Category Operations -----
    
    public long addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(KEY_NAME, category.getName());
        values.put(KEY_DESCRIPTION, category.getDescription());
        values.put(KEY_IMAGE_URL, category.getImageUrl());
        
        long id = db.insert(TABLE_CATEGORIES, null, values);
        db.close();
        
        return id;
    }
    
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORIES;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                category.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                category.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                category.setImageUrl(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL)));
                
                categories.add(category);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        return categories;
    }
    
    // ----- Cart Operations -----
    
    public long addToCart(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        // Check if product already exists in cart
        String query = "SELECT * FROM " + TABLE_CART + 
                       " WHERE " + KEY_PRODUCT_ID + " = " + cartItem.getProductId();
        
        Cursor cursor = db.rawQuery(query, null);
        long id;
        
        if (cursor.moveToFirst()) {
            // Update existing cart item
            int quantity = cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY)) + cartItem.getQuantity();
            
            ContentValues values = new ContentValues();
            values.put(KEY_QUANTITY, quantity);
            
            id = cursor.getLong(cursor.getColumnIndex(KEY_ID));
            db.update(TABLE_CART, values, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        } else {
            // Add new cart item
            ContentValues values = new ContentValues();
            values.put(KEY_PRODUCT_ID, cartItem.getProductId());
            values.put(KEY_PRODUCT_NAME, cartItem.getProductName());
            values.put(KEY_PRODUCT_IMAGE_URL, cartItem.getProductImageUrl());
            values.put(KEY_PRODUCT_PRICE, cartItem.getProductPrice());
            values.put(KEY_QUANTITY, cartItem.getQuantity());
            
            id = db.insert(TABLE_CART, null, values);
        }
        
        cursor.close();
        db.close();
        
        return id;
    }
    
    public List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + TABLE_CART;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                CartItem cartItem = new CartItem();
                cartItem.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                cartItem.setProductId(cursor.getLong(cursor.getColumnIndex(KEY_PRODUCT_ID)));
                cartItem.setProductName(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_NAME)));
                cartItem.setProductImageUrl(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_IMAGE_URL)));
                cartItem.setProductPrice(cursor.getDouble(cursor.getColumnIndex(KEY_PRODUCT_PRICE)));
                cartItem.setQuantity(cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY)));
                cartItem.updateSubtotal();
                
                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        return cartItems;
    }
    
    public double getCartTotal() {
        double total = 0;
        
        List<CartItem> cartItems = getCartItems();
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        
        return total;
    }
    
    public void updateCartItemQuantity(long cartItemId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITY, quantity);
        
        db.update(TABLE_CART, values, KEY_ID + " = ?", new String[] { String.valueOf(cartItemId) });
        db.close();
    }
    
    public void removeFromCart(long cartItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, KEY_ID + " = ?", new String[] { String.valueOf(cartItemId) });
        db.close();
    }
    
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        db.close();
    }
    
    // ----- Order Operations -----
    
    public long createOrder(Order order, List<CartItem> cartItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues orderValues = new ContentValues();
        
        orderValues.put(KEY_ORDER_DATE, order.getOrderDate().getTime());
        orderValues.put(KEY_ORDER_STATUS, order.getOrderStatus());
        orderValues.put(KEY_TOTAL_AMOUNT, order.getTotalAmount());
        orderValues.put(KEY_SHIPPING_ADDRESS, order.getShippingAddress());
        orderValues.put(KEY_PAYMENT_METHOD, order.getPaymentMethod());
        
        long orderId = db.insert(TABLE_ORDERS, null, orderValues);
        
        // Add order items
        for (CartItem item : cartItems) {
            ContentValues itemValues = new ContentValues();
            itemValues.put(KEY_ORDER_ID, orderId);
            itemValues.put(KEY_PRODUCT_ID, item.getProductId());
            itemValues.put(KEY_PRODUCT_NAME, item.getProductName());
            itemValues.put(KEY_PRODUCT_IMAGE_URL, item.getProductImageUrl());
            itemValues.put(KEY_PRODUCT_PRICE, item.getProductPrice());
            itemValues.put(KEY_QUANTITY, item.getQuantity());
            
            db.insert(TABLE_ORDER_ITEMS, null, itemValues);
        }
        
        db.close();
        
        return orderId;
    }
    
    public Order getOrder(long orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + KEY_ID + " = " + orderId;
        
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        Order order = null;
        if (cursor.moveToFirst()) {
            order = new Order();
            order.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
            order.setOrderDate(new Date(cursor.getLong(cursor.getColumnIndex(KEY_ORDER_DATE))));
            order.setOrderStatus(cursor.getString(cursor.getColumnIndex(KEY_ORDER_STATUS)));
            order.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_AMOUNT)));
            order.setShippingAddress(cursor.getString(cursor.getColumnIndex(KEY_SHIPPING_ADDRESS)));
            order.setPaymentMethod(cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_METHOD)));
            
            // Get order items
            List<CartItem> orderItems = getOrderItems(orderId);
            order.setOrderItems(orderItems);
        }
        
        cursor.close();
        
        return order;
    }
    
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + " ORDER BY " + KEY_ORDER_DATE + " DESC";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                order.setOrderDate(new Date(cursor.getLong(cursor.getColumnIndex(KEY_ORDER_DATE))));
                order.setOrderStatus(cursor.getString(cursor.getColumnIndex(KEY_ORDER_STATUS)));
                order.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(KEY_TOTAL_AMOUNT)));
                order.setShippingAddress(cursor.getString(cursor.getColumnIndex(KEY_SHIPPING_ADDRESS)));
                order.setPaymentMethod(cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_METHOD)));
                
                orders.add(order);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        return orders;
    }
    
    private List<CartItem> getOrderItems(long orderId) {
        List<CartItem> orderItems = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + TABLE_ORDER_ITEMS + 
                             " WHERE " + KEY_ORDER_ID + " = " + orderId;
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                CartItem item = new CartItem();
                item.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                item.setProductId(cursor.getLong(cursor.getColumnIndex(KEY_PRODUCT_ID)));
                item.setProductName(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_NAME)));
                item.setProductImageUrl(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_IMAGE_URL)));
                item.setProductPrice(cursor.getDouble(cursor.getColumnIndex(KEY_PRODUCT_PRICE)));
                item.setQuantity(cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY)));
                item.updateSubtotal();
                
                orderItems.add(item);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        
        return orderItems;
    }
    
    public void updateOrderStatus(long orderId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_STATUS, status);
        
        db.update(TABLE_ORDERS, values, KEY_ID + " = ?", new String[] { String.valueOf(orderId) });
        db.close();
    }
    
    // ----- User Operations -----
    
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(KEY_NAME, user.getName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_ADDRESS, user.getAddress());
        values.put(KEY_PROFILE_IMAGE_URL, user.getProfileImageUrl());
        
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        
        return id;
    }
    
    public User getUser(long userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_ID + " = " + userId;
        
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            user.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            user.setProfileImageUrl(cursor.getString(cursor.getColumnIndex(KEY_PROFILE_IMAGE_URL)));
        }
        
        cursor.close();
        db.close();
        
        return user;
    }
    
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_ADDRESS, user.getAddress());
        values.put(KEY_PROFILE_IMAGE_URL, user.getProfileImageUrl());
        
        db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[] { String.valueOf(user.getId()) });
        db.close();
    }
    
    // ----- Chat Message Operations -----
    
    public long addChatMessage(ChatMessage message) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(KEY_MESSAGE, message.getMessage());
        values.put(KEY_MESSAGE_TYPE, message.getMessageType());
        values.put(KEY_TIMESTAMP, message.getTimestamp().getTime());
        
        long id = db.insert(TABLE_CHAT_MESSAGES, null, values);
        db.close();
        
        return id;
    }
    
    public List<ChatMessage> getChatHistory() {
        List<ChatMessage> messages = new ArrayList<>();
        
        String selectQuery = "SELECT * FROM " + TABLE_CHAT_MESSAGES + 
                             " ORDER BY " + KEY_TIMESTAMP + " ASC";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        if (cursor.moveToFirst()) {
            do {
                ChatMessage message = new ChatMessage();
                message.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                message.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
                message.setMessageType(cursor.getInt(cursor.getColumnIndex(KEY_MESSAGE_TYPE)));
                message.setTimestamp(new Date(cursor.getLong(cursor.getColumnIndex(KEY_TIMESTAMP))));
                
                messages.add(message);
            } while (cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        return messages;
    }
    
    public void clearChatHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT_MESSAGES, null, null);
        db.close();
    }
}
