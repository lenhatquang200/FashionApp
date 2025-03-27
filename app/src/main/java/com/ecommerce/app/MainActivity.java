package com.ecommerce.app;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Initialize the NavController
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        
        // Set up the ActionBar with the NavController
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.homeFragment, R.id.cartFragment, R.id.ordersFragment, R.id.profileFragment, R.id.chatbotFragment)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                navController.navigate(R.id.homeFragment);
                return true;
            case R.id.navigation_cart:
                navController.navigate(R.id.cartFragment);
                return true;
            case R.id.navigation_orders:
                navController.navigate(R.id.ordersFragment);
                return true;
            case R.id.navigation_profile:
                navController.navigate(R.id.profileFragment);
                return true;
            case R.id.navigation_chatbot:
                navController.navigate(R.id.chatbotFragment);
                return true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
