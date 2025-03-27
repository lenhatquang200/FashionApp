package com.example.ecommerceapp.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapter.CartAdapter;
import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.model.CartItem;
import com.example.ecommerceapp.ui.checkout.CheckoutActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Fragment for displaying and managing shopping cart
 */
public class CartFragment extends Fragment implements CartAdapter.CartItemListener {

    private RecyclerView cartRecyclerView;
    private TextView emptyCartTextView;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private CartAdapter cartAdapter;
    private DatabaseHelper db;
    private List<CartItem> cartItems;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize database helper
        db = DatabaseHelper.getInstance(getContext());

        // Initialize views
        cartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        emptyCartTextView = view.findViewById(R.id.empty_cart_text_view);
        totalPriceTextView = view.findViewById(R.id.total_price_text_view);
        checkoutButton = view.findViewById(R.id.checkout_button);

        // Setup RecyclerView
        setupRecyclerView();

        // Setup click listeners
        setupClickListeners();

        // Load cart items
        loadCartItems();
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(getContext(), null);
        cartAdapter.setCartItemListener(this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void setupClickListeners() {
        checkoutButton.setOnClickListener(v -> {
            if (cartItems != null && !cartItems.isEmpty()) {
                // Calculate total price
                double totalPrice = calculateTotalPrice(cartItems);
                
                // Navigate to checkout
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                intent.putExtra("total_price", totalPrice);
                startActivity(intent);
            }
        });
    }

    private void loadCartItems() {
        cartItems = db.getCartItems();
        
        if (cartItems != null && !cartItems.isEmpty()) {
            cartAdapter.setCartItems(cartItems);
            emptyCartTextView.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);
            totalPriceTextView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.VISIBLE);
            
            // Calculate and display total price
            double totalPrice = calculateTotalPrice(cartItems);
            totalPriceTextView.setText(String.format("Total: %s", currencyFormat.format(totalPrice)));
        } else {
            emptyCartTextView.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
            totalPriceTextView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.GONE);
        }
    }
    
    private double calculateTotalPrice(List<CartItem> items) {
        double total = 0;
        for (CartItem item : items) {
            total += item.getQuantity() * item.getPriceAtAddition();
        }
        return total;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh cart data when fragment is resumed
        loadCartItems();
    }

    @Override
    public void onQuantityChanged() {
        // Recalculate total price when quantity changes
        double totalPrice = calculateTotalPrice(cartItems);
        totalPriceTextView.setText(String.format("Total: %s", currencyFormat.format(totalPrice)));
    }

    @Override
    public void onItemRemoved(CartItem cartItem) {
        // Reload cart items after removal
        loadCartItems();
    }
}
