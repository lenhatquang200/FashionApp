package com.example.ecommerceapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.model.CartItem;
import com.example.ecommerceapp.model.User;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CheckoutFragment extends Fragment {

    private TextView textSubtotal;
    private TextView textShipping;
    private TextView textTotal;
    private EditText editAddress;
    private EditText editCardNumber;
    private EditText editCardExpiry;
    private EditText editCardCvv;
    private RadioGroup radioPaymentMethod;
    private Button buttonPlaceOrder;
    
    private DatabaseHelper databaseHelper;
    private NavController navController;
    private List<CartItem> cartItems;
    private NumberFormat currencyFormatter;
    private User currentUser;
    
    private double subtotal = 0;
    private double shipping = 5.99; // Fixed shipping cost

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        
        textSubtotal = view.findViewById(R.id.text_subtotal);
        textShipping = view.findViewById(R.id.text_shipping);
        textTotal = view.findViewById(R.id.text_total);
        editAddress = view.findViewById(R.id.edit_address);
        editCardNumber = view.findViewById(R.id.edit_card_number);
        editCardExpiry = view.findViewById(R.id.edit_card_expiry);
        editCardCvv = view.findViewById(R.id.edit_card_cvv);
        radioPaymentMethod = view.findViewById(R.id.radio_payment_method);
        buttonPlaceOrder = view.findViewById(R.id.button_place_order);
        
        databaseHelper = DatabaseHelper.getInstance(requireContext());
        currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        
        // Load user and cart data
        loadUserData();
        loadCartItems();
        
        // Setup button click listener
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
    }
    
    private void loadUserData() {
        currentUser = databaseHelper.getDefaultUser();
        if (currentUser != null && currentUser.getAddress() != null) {
            editAddress.setText(currentUser.getAddress());
        }
    }
    
    private void loadCartItems() {
        cartItems = databaseHelper.getCartItems();
        
        if (cartItems.isEmpty()) {
            // No items in cart, go back
            Toast.makeText(requireContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
            return;
        }
        
        // Calculate subtotal
        subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotalPrice();
        }
        
        // Update UI
        textSubtotal.setText(currencyFormatter.format(subtotal));
        textShipping.setText(currencyFormatter.format(shipping));
        textTotal.setText(currencyFormatter.format(subtotal + shipping));
    }
    
    private void placeOrder() {
        // Validate form
        String address = editAddress.getText().toString().trim();
        String cardNumber = editCardNumber.getText().toString().trim();
        String cardExpiry = editCardExpiry.getText().toString().trim();
        String cardCvv = editCardCvv.getText().toString().trim();
        
        if (address.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter shipping address", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (cardNumber.isEmpty() || cardExpiry.isEmpty() || cardCvv.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter payment details", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create order
        String orderNumber = generateOrderNumber();
        double totalAmount = subtotal + shipping;
        
        long orderId = databaseHelper.createOrder(orderNumber, totalAmount, "Processing", address);
        
        // Add order items
        for (CartItem cartItem : cartItems) {
            databaseHelper.addOrderItem(orderId, cartItem.getProductId(), cartItem.getQuantity(), cartItem.getProductPrice());
        }
        
        // Clear cart
        databaseHelper.clearCart();
        
        // Show success message
        Toast.makeText(requireContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();
        
        // Navigate to order history
        navController.navigate(R.id.orderHistoryFragment);
    }
    
    private String generateOrderNumber() {
        // Generate a random order number
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
