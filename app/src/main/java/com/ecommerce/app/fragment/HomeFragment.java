package com.ecommerce.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.app.R;
import com.ecommerce.app.adapter.CategoryAdapter;
import com.ecommerce.app.adapter.ProductAdapter;
import com.ecommerce.app.database.DatabaseHelper;
import com.ecommerce.app.model.Category;
import com.ecommerce.app.model.Product;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView categoryRecyclerView;
    private RecyclerView featuredProductsRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        databaseHelper = new DatabaseHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up category RecyclerView
        categoryRecyclerView = view.findViewById(R.id.category_recycler_view);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Category> categories = databaseHelper.getAllCategories();
        categoryAdapter = new CategoryAdapter(getContext(), categories);
        categoryRecyclerView.setAdapter(categoryAdapter);

        // Set up featured products RecyclerView
        featuredProductsRecyclerView = view.findViewById(R.id.featured_products_recycler_view);
        featuredProductsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        List<Product> products = databaseHelper.getAllProducts();
        productAdapter = new ProductAdapter(getContext(), products);
        featuredProductsRecyclerView.setAdapter(productAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("searchQuery", query);
                Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_searchFragment, bundle);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            Navigation.findNavController(requireView()).navigate(R.id.action_homeFragment_to_cartFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
