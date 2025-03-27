package com.example.ecommerceapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapter.CategoryAdapter;
import com.example.ecommerceapp.adapter.ProductAdapter;
import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.model.Category;
import com.example.ecommerceapp.model.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener, ProductAdapter.OnProductClickListener {

    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper;
    private FloatingActionButton fabChat;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        recyclerViewCategories = view.findViewById(R.id.recycler_view_categories);
        recyclerViewProducts = view.findViewById(R.id.recycler_view_products);
        fabChat = view.findViewById(R.id.fab_chat);
        
        // Set up database helper
        databaseHelper = DatabaseHelper.getInstance(requireContext());
        
        // Set up category recycler view
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(requireContext(), 
                LinearLayoutManager.HORIZONTAL, false));
        List<Category> categories = databaseHelper.getAllCategories();
        categoryAdapter = new CategoryAdapter(requireContext(), categories, this);
        recyclerViewCategories.setAdapter(categoryAdapter);
        
        // Set up product recycler view
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        List<Product> products = databaseHelper.getAllProducts();
        productAdapter = new ProductAdapter(requireContext(), products, this);
        recyclerViewProducts.setAdapter(productAdapter);
        
        // Set up chat button
        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.chatbotFragment);
            }
        });
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onCategoryClick(Category category, int position) {
        // Update product list based on selected category
        List<Product> productsByCategory = databaseHelper.getProductsByCategory(category.getId());
        productAdapter.updateProductList(productsByCategory);
        
        // Show toast notification
        Toast.makeText(requireContext(), "Category: " + category.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProductClick(Product product, int position) {
        // Navigate to product detail fragment
        Bundle args = new Bundle();
        args.putParcelable("product", product);
        navController.navigate(R.id.productDetailFragment, args);
    }
}
