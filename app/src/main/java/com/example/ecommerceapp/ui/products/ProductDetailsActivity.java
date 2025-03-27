package com.example.ecommerceapp.ui.products;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.model.CartItem;
import com.example.ecommerceapp.model.Product;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Activity for displaying product details and adding to cart
 */
public class ProductDetailsActivity extends AppCompatActivity {

    private long productId;
    private Product product;
    private DatabaseHelper db;
    
    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;
    private TextView productDescriptionTextView;
    private TextView productRatingTextView;
    private TextView quantityTextView;
    private Button decreaseQuantityButton;
    private Button increaseQuantityButton;
    private Button addToCartButton;
    
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        
        // Get product ID from intent
        productId = getIntent().getLongExtra("product_id", -1);
        if (productId == -1) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialize database helper
        db = DatabaseHelper.getInstance(this);
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        // Initialize views
        initViews();
        
        // Load product data
        loadProductDetails();
        
        // Setup click listeners
        setupClickListeners();
    }
    
    private void initViews() {
        productImageView = findViewById(R.id.product_image);
        productNameTextView = findViewById(R.id.product_name);
        productPriceTextView = findViewById(R.id.product_price);
        productDescriptionTextView = findViewById(R.id.product_description);
        productRatingTextView = findViewById(R.id.product_rating);
        quantityTextView = findViewById(R.id.quantity_text_view);
        decreaseQuantityButton = findViewById(R.id.decrease_quantity_button);
        increaseQuantityButton = findViewById(R.id.increase_quantity_button);
        addToCartButton = findViewById(R.id.add_to_cart_button);
    }
    
    private void loadProductDetails() {
        product = db.getProduct(productId);
        
        if (product != null) {
            // Set title
            getSupportActionBar().setTitle(product.getName());
            
            // Set product details
            productNameTextView.setText(product.getName());
            productPriceTextView.setText(currencyFormat.format(product.getPrice()));
            productDescriptionTextView.setText(product.getDescription());
            productRatingTextView.setText(String.format(Locale.getDefault(), "%.1f ★", product.getRating()));
            
            // Load product image
            Glide.with(this)
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.placeholder_product)
                    .error(R.drawable.placeholder_product)
                    .into(productImageView);
            
            // Set initial quantity
            quantityTextView.setText(String.valueOf(quantity));
            
            // Update add to cart button text
            updateAddToCartButtonText();
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    private void setupClickListeners() {
        // Decrease quantity button
        decreaseQuantityButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityTextView.setText(String.valueOf(quantity));
                updateAddToCartButtonText();
            }
        });
        
        // Increase quantity button
        increaseQuantityButton.setOnClickListener(v -> {
            if (quantity < product.getStockQuantity()) {
                quantity++;
                quantityTextView.setText(String.valueOf(quantity));
                updateAddToCartButtonText();
            } else {
                Toast.makeText(this, "Maximum available quantity reached", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Add to cart button
        addToCartButton.setOnClickListener(v -> addToCart());
    }
    
    private void updateAddToCartButtonText() {
        double totalPrice = quantity * product.getPrice();
        addToCartButton.setText(String.format("Add to Cart - %s", currencyFormat.format(totalPrice)));
    }
    
    private void addToCart() {
        // Create cart item
        CartItem cartItem = new CartItem();
        cartItem.setProductId(product.getId());
        cartItem.setQuantity(quantity);
        cartItem.setPriceAtAddition(product.getPrice());
        
        // Add to database
        long result = db.addToCart(cartItem);
        
        if (result > 0) {
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
