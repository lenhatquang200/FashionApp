package com.example.ecommerceapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapter.CartAdapter;
import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.model.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment implements CartAdapter.OnCartItemActionListener {

    private RecyclerView recyclerViewCart;
    private TextView textEmptyCart;
    private TextView textTotalAmount;
    private Button buttonCheckout;
    
    private CartAdapter cartAdapter;
    private DatabaseHelper databaseHelper;
    private NavController navController;
    private List<CartItem> cartItems;
    private NumberFormat currencyFormatter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        
        recyclerViewCart = view.findViewById(R.id.recycler_view_cart);
        textEmptyCart = view.findViewById(R.id.text_empty_cart);
        textTotalAmount = view.findViewById(R.id.text_total_amount);
        buttonCheckout = view.findViewById(R.id.button_checkout);
        
        databaseHelper = DatabaseHelper.getInstance(requireContext());
        currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        
        // Set up recycler view
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        
        loadCartItems();
        
        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartItems != null && !cartItems.isEmpty()) {
                    navController.navigate(R.id.checkoutFragment);
                } else {
                    Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Reload cart items when fragment becomes visible again
        loadCartItems();
    }
    
    private void loadCartItems() {
        cartItems = databaseHelper.getCartItems();
        
        if (cartItems.isEmpty()) {
            recyclerViewCart.setVisibility(View.GONE);
            textEmptyCart.setVisibility(View.VISIBLE);
            textTotalAmount.setText(currencyFormatter.format(0));
            buttonCheckout.setEnabled(false);
        } else {
            recyclerViewCart.setVisibility(View.VISIBLE);
            textEmptyCart.setVisibility(View.GONE);
            
            // Calculate total
            double total = 0;
            for (CartItem item : cartItems) {
                total += item.getTotalPrice();
            }
            textTotalAmount.setText(currencyFormatter.format(total));
            buttonCheckout.setEnabled(true);
            
            // Update adapter
            if (cartAdapter == null) {
                cartAdapter = new CartAdapter(requireContext(), cartItems, this);
                recyclerViewCart.setAdapter(cartAdapter);
            } else {
                cartAdapter.updateCartItems(cartItems);
            }
        }
    }

    @Override
    public void onRemoveItem(CartItem cartItem, int position) {
        int result = databaseHelper.removeFromCart(cartItem.getId());
        if (result > 0) {
            cartItems.remove(position);
            cartAdapter.notifyItemRemoved(position);
            
            if (cartItems.isEmpty()) {
                recyclerViewCart.setVisibility(View.GONE);
                textEmptyCart.setVisibility(View.VISIBLE);
                textTotalAmount.setText(currencyFormatter.format(0));
                buttonCheckout.setEnabled(false);
            } else {
                // Recalculate total
                double total = 0;
                for (CartItem item : cartItems) {
                    total += item.getTotalPrice();
                }
                textTotalAmount.setText(currencyFormatter.format(total));
            }
            
            Toast.makeText(requireContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Failed to remove item", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateQuantity(CartItem cartItem, int newQuantity, int position) {
        int result = databaseHelper.updateCartItemQuantity(cartItem.getId(), newQuantity);
        if (result > 0) {
            // Update item in the list
            cartItem.setQuantity(newQuantity);
            cartAdapter.notifyItemChanged(position);
            
            // Recalculate total
            double total = 0;
            for (CartItem item : cartItems) {
                total += item.getTotalPrice();
            }
            textTotalAmount.setText(currencyFormatter.format(total));
        } else {
            Toast.makeText(requireContext(), "Failed to update quantity", Toast.LENGTH_SHORT).show();
        }
    }
}
