package com.example.ecommerceapp.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapter.ProductAdapter;
import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.model.Product;

import java.util.List;

public class SearchFragment extends Fragment implements ProductAdapter.OnProductClickListener {

    private EditText editSearch;
    private RecyclerView recyclerViewResults;
    private TextView textNoResults;
    
    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        
        editSearch = view.findViewById(R.id.edit_search);
        recyclerViewResults = view.findViewById(R.id.recycler_view_results);
        textNoResults = view.findViewById(R.id.text_no_results);
        
        databaseHelper = DatabaseHelper.getInstance(requireContext());
        
        // Set up recycler view
        recyclerViewResults.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        
        // Show all products initially
        List<Product> allProducts = databaseHelper.getAllProducts();
        productAdapter = new ProductAdapter(requireContext(), allProducts, this);
        recyclerViewResults.setAdapter(productAdapter);
        
        if (allProducts.isEmpty()) {
            recyclerViewResults.setVisibility(View.GONE);
            textNoResults.setVisibility(View.VISIBLE);
        } else {
            recyclerViewResults.setVisibility(View.VISIBLE);
            textNoResults.setVisibility(View.GONE);
        }
        
        // Set up search text listener
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                performSearch(s.toString());
            }
        });
    }
    
    private void performSearch(String query) {
        if (query.length() == 0) {
            // Show all products if query is empty
            List<Product> allProducts = databaseHelper.getAllProducts();
            updateSearchResults(allProducts);
        } else {
            // Search for products matching the query
            List<Product> searchResults = databaseHelper.searchProducts(query);
            updateSearchResults(searchResults);
        }
    }
    
    private void updateSearchResults(List<Product> products) {
        if (products.isEmpty()) {
            recyclerViewResults.setVisibility(View.GONE);
            textNoResults.setVisibility(View.VISIBLE);
        } else {
            recyclerViewResults.setVisibility(View.VISIBLE);
            textNoResults.setVisibility(View.GONE);
            productAdapter.updateProductList(products);
        }
    }

    @Override
    public void onProductClick(Product product, int position) {
        // Navigate to product detail fragment
        Bundle args = new Bundle();
        args.putParcelable("product", product);
        navController.navigate(R.id.productDetailFragment, args);
    }
}
