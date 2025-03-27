package com.ecommerce.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.app.R;
import com.ecommerce.app.adapter.ProductAdapter;
import com.ecommerce.app.database.DatabaseHelper;
import com.ecommerce.app.model.Product;

import java.util.List;

public class CategoryProductsFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private TextView emptyProductsMessage;
    
    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper;
    private int categoryId;
    private String categoryName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
        
        // Get category ID and name from arguments
        if (getArguments() != null) {
            categoryId = getArguments().getInt("categoryId");
            categoryName = getArguments().getString("categoryName", "Category");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        productsRecyclerView = view.findViewById(R.id.category_products_recycler_view);
        emptyProductsMessage = view.findViewById(R.id.empty_products_message);
        TextView categoryTitle = view.findViewById(R.id.category_title);
        
        // Set category title
        categoryTitle.setText(categoryName);

        // Set up RecyclerView
        productsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        loadCategoryProducts();
    }

    private void loadCategoryProducts() {
        List<Product> products = databaseHelper.getProductsByCategory(categoryId);

        if (products.size() > 0) {
            // Show products and hide empty message
            productsRecyclerView.setVisibility(View.VISIBLE);
            emptyProductsMessage.setVisibility(View.GONE);

            // Set up the adapter
            productAdapter = new ProductAdapter(getContext(), products);
            productsRecyclerView.setAdapter(productAdapter);
        } else {
            // Show empty message and hide products
            productsRecyclerView.setVisibility(View.GONE);
            emptyProductsMessage.setVisibility(View.VISIBLE);
        }
    }
}
