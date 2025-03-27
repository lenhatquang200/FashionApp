package com.ecommerce.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ecommerce.app.database.DatabaseHelper;
import com.ecommerce.app.util.AppPrefs;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize database and load initial data if needed
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        if (!AppPrefs.isDatabaseInitialized(this)) {
            databaseHelper.populateInitialData();
            AppPrefs.setDatabaseInitialized(this, true);
        }

        // Set up fade-in animation
        ImageView logoImageView = findViewById(R.id.splash_logo);
        TextView appNameTextView = findViewById(R.id.splash_app_name);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000);

        logoImageView.startAnimation(fadeIn);
        appNameTextView.startAnimation(fadeIn);

        // Navigate to MainActivity after splash screen
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
