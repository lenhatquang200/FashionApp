package com.example.ecommerceapp;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ecommerceapp.ui.cart.CartFragment;
import com.example.ecommerceapp.ui.categories.CategoriesFragment;
import com.example.ecommerceapp.ui.home.HomeFragment;
import com.example.ecommerceapp.ui.orders.OrdersFragment;
import com.example.ecommerceapp.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main activity for the E-commerce app
 * Contains bottom navigation and hosts various fragments
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    
    private BottomNavigationView bottomNavigationView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        
        // Load the home fragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
                
            case R.id.nav_categories:
                fragment = new CategoriesFragment();
                break;
                
            case R.id.nav_cart:
                fragment = new CartFragment();
                break;
                
            case R.id.nav_orders:
                fragment = new OrdersFragment();
                break;
                
            case R.id.nav_profile:
                fragment = new ProfileFragment();
                break;
        }
        
        return loadFragment(fragment);
    }
    
    /**
     * Load a fragment into the container
     * @param fragment Fragment to load
     * @return true if fragment was loaded successfully
     */
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
