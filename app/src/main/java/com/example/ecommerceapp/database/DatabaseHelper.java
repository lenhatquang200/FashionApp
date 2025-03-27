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

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    
    private static final String DATABASE_NAME = "ecommerce.db";
    private static final int DATABASE_VERSION = 1;

    // Singleton instance
    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(EcommerceContract.SQL_CREATE_CATEGORIES);
        db.execSQL(EcommerceContract.SQL_CREATE_PRODUCTS);
        db.execSQL(EcommerceContract.SQL_CREATE_CART);
        db.execSQL(EcommerceContract.SQL_CREATE_ORDERS);
        db.execSQL(EcommerceContract.SQL_CREATE_ORDER_ITEMS);
        db.execSQL(EcommerceContract.SQL_CREATE_USERS);
        
        // Insert sample data
        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades
        db.execSQL("DROP TABLE IF EXISTS " + EcommerceContract.OrderItemEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EcommerceContract.OrderEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EcommerceContract.CartEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EcommerceContract.ProductEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EcommerceContract.CategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EcommerceContract.UserEntry.TABLE_NAME);
        onCreate(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Insert sample categories
        insertCategory(db, "Electronics", "Electronic devices and gadgets");
        insertCategory(db, "Clothing", "Apparel and accessories");
        insertCategory(db, "Books", "Books, e-books and publications");
        insertCategory(db, "Home & Kitchen", "Home appliances and kitchen items");
        
        // Insert sample products
        insertProduct(db, "Smartphone", "High-end smartphone with premium features", 799.99, 1, 
                "https://example.com/images/smartphone.jpg", 50);
        insertProduct(db, "Laptop", "Powerful laptop for work and gaming", 1299.99, 1, 
                "https://example.com/images/laptop.jpg", 30);
        insertProduct(db, "T-shirt", "Cotton T-shirt, comfortable fit", 19.99, 2, 
                "https://example.com/images/tshirt.jpg", 100);
        insertProduct(db, "Jeans", "Classic denim jeans", 49.99, 2, 
                "https://example.com/images/jeans.jpg", 80);
        insertProduct(db, "Novel", "Bestselling fiction novel", 14.99, 3, 
                "https://example.com/images/novel.jpg", 200);
        insertProduct(db, "Cookbook", "Recipe collection from famous chef", 29.99, 3, 
                "https://example.com/images/cookbook.jpg", 150);
        insertProduct(db, "Blender", "High-powered kitchen blender", 89.99, 4, 
                "https://example.com/images/blender.jpg", 40);
        insertProduct(db, "Coffee Maker", "Programmable coffee machine", 129.99, 4, 
                "https://example.com/images/coffeemaker.jpg", 35);

        // Insert default user
        insertUser(db, "default", "user@example.com", "password", "123 Main St", "555-1234");
    }

    private long insertCategory(SQLiteDatabase db, String name, String description) {
        ContentValues values = new ContentValues();
        values.put(EcommerceContract.CategoryEntry.COLUMN_NAME, name);
        values.put(EcommerceContract.CategoryEntry.COLUMN_DESCRIPTION, description);
        return db.insert(EcommerceContract.CategoryEntry.TABLE_NAME, null, values);
    }

    private long insertProduct(SQLiteDatabase db, String name, String description, double price, 
                              int categoryId, String imageUrl, int stock) {
        ContentValues values = new ContentValues();
        values.put(EcommerceContract.ProductEntry.COLUMN_NAME, name);
        values.put(EcommerceContract.ProductEntry.COLUMN_DESCRIPTION, description);
        values.put(EcommerceContract.ProductEntry.COLUMN_PRICE, price);
        values.put(EcommerceContract.ProductEntry.COLUMN_CATEGORY_ID, categoryId);
        values.put(EcommerceContract.ProductEntry.COLUMN_IMAGE_URL, imageUrl);
        values.put(EcommerceContract.ProductEntry.COLUMN_STOCK, stock);
        return db.insert(EcommerceContract.ProductEntry.TABLE_NAME, null, values);
    }

    private long insertUser(SQLiteDatabase db, String name, String email, String password, 
                           String address, String phone) {
        ContentValues values = new ContentValues();
        values.put(EcommerceContract.UserEntry.COLUMN_NAME, name);
        values.put(EcommerceContract.UserEntry.COLUMN_EMAIL, email);
        values.put(EcommerceContract.UserEntry.COLUMN_PASSWORD, password);
        values.put(EcommerceContract.UserEntry.COLUMN_ADDRESS, address);
        values.put(EcommerceContract.UserEntry.COLUMN_PHONE, phone);
        return db.insert(EcommerceContract.UserEntry.TABLE_NAME, null, values);
    }

    // Product methods
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        
        Cursor cursor = db.query(
                EcommerceContract.ProductEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = extractProductFromCursor(cursor);
                products.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        return products;
    }
    
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        
        String selection = EcommerceContract.ProductEntry.COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = {String.valueOf(categoryId)};
        
        Cursor cursor = db.query(
                EcommerceContract.ProductEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = extractProductFromCursor(cursor);
                products.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        return products;
    }
    
    public Product getProductById(int productId) {
        SQLiteDatabase db = getReadableDatabase();
        
        String selection = EcommerceContract.ProductEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(productId)};
        
        Cursor cursor = db.query(
                EcommerceContract.ProductEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        Product product = null;
        if (cursor != null && cursor.moveToFirst()) {
            product = extractProductFromCursor(cursor);
            cursor.close();
        }
        
        return product;
    }
    
    public List<Product> searchProducts(String query) {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        
        String selection = EcommerceContract.ProductEntry.COLUMN_NAME + " LIKE ? OR " + 
                          EcommerceContract.ProductEntry.COLUMN_DESCRIPTION + " LIKE ?";
        String[] selectionArgs = {"%" + query + "%", "%" + query + "%"};
        
        Cursor cursor = db.query(
                EcommerceContract.ProductEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Product product = extractProductFromCursor(cursor);
                products.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        return products;
    }
    
    private Product extractProductFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(EcommerceContract.ProductEntry._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.ProductEntry.COLUMN_NAME));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.ProductEntry.COLUMN_DESCRIPTION));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow(EcommerceContract.ProductEntry.COLUMN_PRICE));
        int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(EcommerceContract.ProductEntry.COLUMN_CATEGORY_ID));
        String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.ProductEntry.COLUMN_IMAGE_URL));
        int stock = cursor.getInt(cursor.getColumnIndexOrThrow(EcommerceContract.ProductEntry.COLUMN_STOCK));
        
        return new Product(id, name, description, price, categoryId, imageUrl, stock);
    }
    
    // Category methods
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        
        Cursor cursor = db.query(
                EcommerceContract.CategoryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(EcommerceContract.CategoryEntry._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.CategoryEntry.COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.CategoryEntry.COLUMN_DESCRIPTION));
                
                Category category = new Category(id, name, description);
                categories.add(category);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        return categories;
    }
    
    // Cart methods
    public List<CartItem> getCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        
        String query = "SELECT c." + EcommerceContract.CartEntry._ID + ", " +
                      "c." + EcommerceContract.CartEntry.COLUMN_PRODUCT_ID + ", " +
                      "c." + EcommerceContract.CartEntry.COLUMN_QUANTITY + ", " +
                      "p." + EcommerceContract.ProductEntry.COLUMN_NAME + ", " +
                      "p." + EcommerceContract.ProductEntry.COLUMN_PRICE + ", " +
                      "p." + EcommerceContract.ProductEntry.COLUMN_IMAGE_URL + " " +
                      "FROM " + EcommerceContract.CartEntry.TABLE_NAME + " c " +
                      "JOIN " + EcommerceContract.ProductEntry.TABLE_NAME + " p " +
                      "ON c." + EcommerceContract.CartEntry.COLUMN_PRODUCT_ID + " = p." + EcommerceContract.ProductEntry._ID;
        
        Cursor cursor = db.rawQuery(query, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int productId = cursor.getInt(1);
                int quantity = cursor.getInt(2);
                String productName = cursor.getString(3);
                double productPrice = cursor.getDouble(4);
                String imageUrl = cursor.getString(5);
                
                CartItem cartItem = new CartItem(id, productId, quantity, productName, productPrice, imageUrl);
                cartItems.add(cartItem);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        return cartItems;
    }
    
    public long addToCart(int productId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        
        // Check if the product already exists in the cart
        String selection = EcommerceContract.CartEntry.COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(productId)};
        
        Cursor cursor = db.query(
                EcommerceContract.CartEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        long result;
        
        if (cursor != null && cursor.moveToFirst()) {
            // Product already in cart, update quantity
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(EcommerceContract.CartEntry._ID));
            int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(EcommerceContract.CartEntry.COLUMN_QUANTITY));
            cursor.close();
            
            ContentValues values = new ContentValues();
            values.put(EcommerceContract.CartEntry.COLUMN_QUANTITY, currentQuantity + quantity);
            
            result = db.update(
                    EcommerceContract.CartEntry.TABLE_NAME,
                    values,
                    EcommerceContract.CartEntry._ID + " = ?",
                    new String[]{String.valueOf(id)}
            );
        } else {
            // Add new item to cart
            ContentValues values = new ContentValues();
            values.put(EcommerceContract.CartEntry.COLUMN_PRODUCT_ID, productId);
            values.put(EcommerceContract.CartEntry.COLUMN_QUANTITY, quantity);
            
            result = db.insert(EcommerceContract.CartEntry.TABLE_NAME, null, values);
        }
        
        return result;
    }
    
    public int updateCartItemQuantity(int cartItemId, int quantity) {
        SQLiteDatabase db = getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(EcommerceContract.CartEntry.COLUMN_QUANTITY, quantity);
        
        return db.update(
                EcommerceContract.CartEntry.TABLE_NAME,
                values,
                EcommerceContract.CartEntry._ID + " = ?",
                new String[]{String.valueOf(cartItemId)}
        );
    }
    
    public int removeFromCart(int cartItemId) {
        SQLiteDatabase db = getWritableDatabase();
        
        return db.delete(
                EcommerceContract.CartEntry.TABLE_NAME,
                EcommerceContract.CartEntry._ID + " = ?",
                new String[]{String.valueOf(cartItemId)}
        );
    }
    
    public int clearCart() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(EcommerceContract.CartEntry.TABLE_NAME, null, null);
    }
    
    // Order methods
    public long createOrder(String orderNumber, double totalAmount, String status, String shippingAddress) {
        SQLiteDatabase db = getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(EcommerceContract.OrderEntry.COLUMN_ORDER_NUMBER, orderNumber);
        values.put(EcommerceContract.OrderEntry.COLUMN_DATE, System.currentTimeMillis());
        values.put(EcommerceContract.OrderEntry.COLUMN_TOTAL_AMOUNT, totalAmount);
        values.put(EcommerceContract.OrderEntry.COLUMN_STATUS, status);
        values.put(EcommerceContract.OrderEntry.COLUMN_SHIPPING_ADDRESS, shippingAddress);
        
        return db.insert(EcommerceContract.OrderEntry.TABLE_NAME, null, values);
    }
    
    public void addOrderItem(long orderId, int productId, int quantity, double price) {
        SQLiteDatabase db = getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(EcommerceContract.OrderItemEntry.COLUMN_ORDER_ID, orderId);
        values.put(EcommerceContract.OrderItemEntry.COLUMN_PRODUCT_ID, productId);
        values.put(EcommerceContract.OrderItemEntry.COLUMN_QUANTITY, quantity);
        values.put(EcommerceContract.OrderItemEntry.COLUMN_PRICE, price);
        
        db.insert(EcommerceContract.OrderItemEntry.TABLE_NAME, null, values);
    }
    
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        
        Cursor cursor = db.query(
                EcommerceContract.OrderEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                EcommerceContract.OrderEntry.COLUMN_DATE + " DESC"
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Order order = extractOrderFromCursor(cursor);
                orders.add(order);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        return orders;
    }
    
    private Order extractOrderFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(EcommerceContract.OrderEntry._ID));
        String orderNumber = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.OrderEntry.COLUMN_ORDER_NUMBER));
        long date = cursor.getLong(cursor.getColumnIndexOrThrow(EcommerceContract.OrderEntry.COLUMN_DATE));
        double totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(EcommerceContract.OrderEntry.COLUMN_TOTAL_AMOUNT));
        String status = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.OrderEntry.COLUMN_STATUS));
        String shippingAddress = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.OrderEntry.COLUMN_SHIPPING_ADDRESS));
        
        return new Order(id, orderNumber, date, totalAmount, status, shippingAddress);
    }
    
    // User methods
    public User getUserById(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        
        String selection = EcommerceContract.UserEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        
        Cursor cursor = db.query(
                EcommerceContract.UserEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(EcommerceContract.UserEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.UserEntry.COLUMN_NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.UserEntry.COLUMN_EMAIL));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.UserEntry.COLUMN_ADDRESS));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.UserEntry.COLUMN_PHONE));
            
            user = new User(id, name, email, address, phone);
            cursor.close();
        }
        
        return user;
    }
    
    public User getDefaultUser() {
        // For simplicity, we're just returning the first user as default
        List<User> users = getAllUsers();
        return !users.isEmpty() ? users.get(0) : null;
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        
        Cursor cursor = db.query(
                EcommerceContract.UserEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(EcommerceContract.UserEntry._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.UserEntry.COLUMN_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.UserEntry.COLUMN_EMAIL));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.UserEntry.COLUMN_ADDRESS));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(EcommerceContract.UserEntry.COLUMN_PHONE));
                
                User user = new User(id, name, email, address, phone);
                users.add(user);
            } while (cursor.moveToNext());
            cursor.close();
        }
        
        return users;
    }
    
    public long updateUserProfile(int userId, String name, String email, String address, String phone) {
        SQLiteDatabase db = getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put(EcommerceContract.UserEntry.COLUMN_NAME, name);
        values.put(EcommerceContract.UserEntry.COLUMN_EMAIL, email);
        values.put(EcommerceContract.UserEntry.COLUMN_ADDRESS, address);
        values.put(EcommerceContract.UserEntry.COLUMN_PHONE, phone);
        
        return db.update(
                EcommerceContract.UserEntry.TABLE_NAME,
                values,
                EcommerceContract.UserEntry._ID + " = ?",
                new String[]{String.valueOf(userId)}
        );
    }
}
