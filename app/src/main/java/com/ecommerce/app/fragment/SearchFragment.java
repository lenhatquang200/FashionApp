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

public class SearchFragment extends Fragment {

    private RecyclerView searchResultsRecyclerView;
    private TextView emptyResultsMessage;
    private TextView searchQueryText;

    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper;
    private String searchQuery;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
        
        // Get search query from arguments
        if (getArguments() != null) {
            searchQuery = getArguments().getString("searchQuery", "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        searchResultsRecyclerView = view.findViewById(R.id.search_results_recycler_view);
        emptyResultsMessage = view.findViewById(R.id.empty_results_message);
        searchQueryText = view.findViewById(R.id.search_query_text);

        // Set search query text
        searchQueryText.setText("Search results for: " + searchQuery);

        // Set up RecyclerView
        searchResultsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        performSearch();
    }

    private void performSearch() {
        List<Product> searchResults = databaseHelper.searchProducts(searchQuery);

        if (searchResults.size() > 0) {
            // Show results and hide empty message
            searchResultsRecyclerView.setVisibility(View.VISIBLE);
            emptyResultsMessage.setVisibility(View.GONE);

            // Set up the adapter
            productAdapter = new ProductAdapter(getContext(), searchResults);
            searchResultsRecyclerView.setAdapter(productAdapter);
        } else {
            // Show empty message and hide results
            searchResultsRecyclerView.setVisibility(View.GONE);
            emptyResultsMessage.setVisibility(View.VISIBLE);
        }
    }
}
