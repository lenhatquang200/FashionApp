package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.util.PreferenceManager;

/**
 * Splash screen activity shown on app startup
 */
public class SplashActivity extends AppCompatActivity {
    
    private static final int SPLASH_DURATION = 2000; // 2 seconds
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        // Initialize views
        ImageView logoImageView = findViewById(R.id.splash_logo);
        TextView appNameTextView = findViewById(R.id.splash_app_name);
        
        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        
        // Set animations
        logoImageView.startAnimation(fadeIn);
        appNameTextView.startAnimation(slideUp);
        
        // Initialize database in background
        initializeDatabase();
        
        // Delay and navigate to main activity
        new Handler(Looper.getMainLooper()).postDelayed(this::navigateToMainScreen, SPLASH_DURATION);
    }
    
    /**
     * Initialize the database on app startup
     */
    private void initializeDatabase() {
        // Get database instance to trigger creation if first run
        DatabaseHelper.getInstance(this);
    }
    
    /**
     * Navigate to the appropriate screen based on login status
     */
    private void navigateToMainScreen() {
        // Check if user is logged in
        PreferenceManager preferenceManager = new PreferenceManager(this);
        
        // For this demo app, we'll go directly to the MainActivity without login
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
