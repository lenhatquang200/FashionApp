package com.ecommerce.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.app.R;
import com.ecommerce.app.adapter.CartAdapter;
import com.ecommerce.app.database.DatabaseHelper;
import com.ecommerce.app.model.CartItem;

import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.CartItemListener {

    private RecyclerView cartRecyclerView;
    private TextView emptyCartMessage;
    private TextView cartTotal;
    private Button checkoutButton;
    private Button continueShoppingButton;

    private CartAdapter cartAdapter;
    private DatabaseHelper databaseHelper;
    private List<CartItem> cartItems;
    private double total;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        cartRecyclerView = view.findViewById(R.id.cart_recycler_view);
        emptyCartMessage = view.findViewById(R.id.empty_cart_message);
        cartTotal = view.findViewById(R.id.cart_total);
        checkoutButton = view.findViewById(R.id.checkout_button);
        continueShoppingButton = view.findViewById(R.id.continue_shopping_button);

        // Set up RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadCartItems();

        // Set up checkout button
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.size() > 0) {
                Navigation.findNavController(view).navigate(R.id.action_cartFragment_to_checkoutFragment);
            }
        });

        // Set up continue shopping button
        continueShoppingButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_cartFragment_to_homeFragment);
        });
    }

    private void loadCartItems() {
        cartItems = databaseHelper.getCartItems();
        total = databaseHelper.getCartTotal();

        if (cartItems.size() > 0) {
            // Show cart items and hide empty message
            cartRecyclerView.setVisibility(View.VISIBLE);
            emptyCartMessage.setVisibility(View.GONE);
            checkoutButton.setEnabled(true);

            // Display the total
            cartTotal.setText(String.format("Total: $%.2f", total));

            // Set up the adapter
            cartAdapter = new CartAdapter(getContext(), cartItems, this);
            cartRecyclerView.setAdapter(cartAdapter);
        } else {
            // Show empty message and hide cart items
            cartRecyclerView.setVisibility(View.GONE);
            emptyCartMessage.setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(false);
            cartTotal.setText("Total: $0.00");
        }
    }

    @Override
    public void onQuantityChanged() {
        // Recalculate the total when quantity changes
        total = databaseHelper.getCartTotal();
        cartTotal.setText(String.format("Total: $%.2f", total));
    }

    @Override
    public void onItemRemoved(CartItem item) {
        // Check if cart is empty after removing an item
        if (cartItems.isEmpty()) {
            cartRecyclerView.setVisibility(View.GONE);
            emptyCartMessage.setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(false);
            cartTotal.setText("Total: $0.00");
        }
    }
}
