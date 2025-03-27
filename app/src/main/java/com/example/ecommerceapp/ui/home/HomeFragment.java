package com.example.ecommerceapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapter.CategoryAdapter;
import com.example.ecommerceapp.adapter.ProductAdapter;
import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.model.Category;
import com.example.ecommerceapp.model.Product;
import com.example.ecommerceapp.ui.search.SearchActivity;
import com.example.ecommerceapp.ui.support.ChatbotActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Home fragment displaying featured products and categories
 */
public class HomeFragment extends Fragment {

    private RecyclerView featuredProductsRecyclerView;
    private RecyclerView categoriesRecyclerView;
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;
    private TextView searchBarTextView;
    private FloatingActionButton supportFab;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerViews();
        loadData();
        setupClickListeners();
    }

    private void initViews(View view) {
        featuredProductsRecyclerView = view.findViewById(R.id.recycler_view_featured_products);
        categoriesRecyclerView = view.findViewById(R.id.recycler_view_categories);
        searchBarTextView = view.findViewById(R.id.search_bar_text);
        supportFab = view.findViewById(R.id.fab_support);
    }

    private void setupRecyclerViews() {
        // Setup for featured products
        productAdapter = new ProductAdapter(getContext(), null);
        featuredProductsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        featuredProductsRecyclerView.setAdapter(productAdapter);

        // Setup for categories
        categoryAdapter = new CategoryAdapter(getContext(), null);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    private void loadData() {
        DatabaseHelper db = DatabaseHelper.getInstance(getContext());
        
        // Load featured products
        List<Product> featuredProducts = db.getFeaturedProducts();
        productAdapter.setProductList(featuredProducts);
        
        // Load categories
        List<Category> categories = db.getAllCategories();
        categoryAdapter.setCategoryList(categories);
    }

    private void setupClickListeners() {
        // Search bar click listener
        searchBarTextView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
        
        // Support FAB click listener
        supportFab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatbotActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when fragment is resumed
        loadData();
    }
}
