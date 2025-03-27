package com.example.ecommerceapp.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapter.ProductAdapter;
import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.model.Product;

import java.util.List;

/**
 * Activity for searching products
 */
public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private ImageButton clearSearchButton;
    private RecyclerView searchResultsRecyclerView;
    private TextView emptyTextView;
    private ProductAdapter productAdapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize database helper
        db = DatabaseHelper.getInstance(this);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Products");

        // Initialize views
        searchEditText = findViewById(R.id.search_edit_text);
        clearSearchButton = findViewById(R.id.clear_search_button);
        searchResultsRecyclerView = findViewById(R.id.search_results_recycler_view);
        emptyTextView = findViewById(R.id.empty_view);

        // Setup RecyclerView
        setupRecyclerView();

        // Setup listeners
        setupListeners();
    }

    private void setupRecyclerView() {
        productAdapter = new ProductAdapter(this, null);
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        searchResultsRecyclerView.setAdapter(productAdapter);
    }

    private void setupListeners() {
        // Text change listener for search
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                performSearch(s.toString());
                // Show/hide clear button based on text
                clearSearchButton.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });

        // Clear button click listener
        clearSearchButton.setOnClickListener(v -> {
            searchEditText.setText("");
            emptyTextView.setText(R.string.search_hint);
            productAdapter.setProductList(null);
        });
    }

    private void performSearch(String query) {
        if (query.length() >= 2) { // Only search if query is at least 2 characters
            List<Product> searchResults = db.searchProducts(query);
            
            if (searchResults != null && !searchResults.isEmpty()) {
                productAdapter.setProductList(searchResults);
                searchResultsRecyclerView.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
            } else {
                productAdapter.setProductList(null);
                searchResultsRecyclerView.setVisibility(View.GONE);
                emptyTextView.setText(R.string.no_search_results);
                emptyTextView.setVisibility(View.VISIBLE);
            }
        } else {
            productAdapter.setProductList(null);
            searchResultsRecyclerView.setVisibility(View.GONE);
            emptyTextView.setText(R.string.search_hint);
            emptyTextView.setVisibility(View.VISIBLE);
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
