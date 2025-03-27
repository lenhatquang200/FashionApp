package com.ecommerce.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ecommerce.app.R;
import com.ecommerce.app.database.DatabaseHelper;
import com.ecommerce.app.model.CartItem;
import com.ecommerce.app.model.Order;
import com.ecommerce.app.model.User;

import java.util.Date;
import java.util.List;

public class CheckoutFragment extends Fragment {

    private EditText nameInput;
    private EditText emailInput;
    private EditText phoneInput;
    private EditText addressInput;
    private RadioGroup paymentMethodGroup;
    private TextView orderSummary;
    private TextView orderTotal;
    private Button placeOrderButton;
    private Button cancelButton;

    private DatabaseHelper databaseHelper;
    private List<CartItem> cartItems;
    private double total;
    private static final long DEFAULT_USER_ID = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        nameInput = view.findViewById(R.id.checkout_name);
        emailInput = view.findViewById(R.id.checkout_email);
        phoneInput = view.findViewById(R.id.checkout_phone);
        addressInput = view.findViewById(R.id.checkout_address);
        paymentMethodGroup = view.findViewById(R.id.payment_method_group);
        orderSummary = view.findViewById(R.id.order_summary);
        orderTotal = view.findViewById(R.id.order_total);
        placeOrderButton = view.findViewById(R.id.place_order_button);
        cancelButton = view.findViewById(R.id.cancel_order_button);

        // Load user data to pre-fill the form
        loadUserData();

        // Load cart items and display order summary
        loadCartItems();

        // Set up place order button
        placeOrderButton.setOnClickListener(v -> placeOrder());

        // Set up cancel button
        cancelButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_checkoutFragment_to_cartFragment);
        });
    }

    private void loadUserData() {
        User user = databaseHelper.getUser(DEFAULT_USER_ID);

        if (user != null) {
            nameInput.setText(user.getName());
            emailInput.setText(user.getEmail());
            phoneInput.setText(user.getPhone());
            addressInput.setText(user.getAddress());
        }
    }

    private void loadCartItems() {
        cartItems = databaseHelper.getCartItems();
        total = databaseHelper.getCartTotal();

        // Build order summary text
        StringBuilder summaryBuilder = new StringBuilder();
        for (CartItem item : cartItems) {
            summaryBuilder.append(item.getQuantity())
                          .append(" x ")
                          .append(item.getProductName())
                          .append(" - ")
                          .append(item.getFormattedSubtotal())
                          .append("\n");
        }

        orderSummary.setText(summaryBuilder.toString());
        orderTotal.setText(String.format("Total: $%.2f", total));
    }

    private void placeOrder() {
        // Basic validation
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected payment method
        int selectedPaymentId = paymentMethodGroup.getCheckedRadioButtonId();
        if (selectedPaymentId == -1) {
            Toast.makeText(requireContext(), "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }
        
        RadioButton selectedPaymentButton = getView().findViewById(selectedPaymentId);
        String paymentMethod = selectedPaymentButton.getText().toString();

        // Create the order
        Order order = new Order();
        order.setOrderDate(new Date());
        order.setOrderStatus(Order.STATUS_PENDING);
        order.setTotalAmount(total);
        order.setShippingAddress(address);
        order.setPaymentMethod(paymentMethod);

        // Save order to database
        long orderId = databaseHelper.createOrder(order, cartItems);

        if (orderId > 0) {
            // Clear the cart
            databaseHelper.clearCart();

            // Show success message
            Toast.makeText(requireContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();

            // Navigate to order confirmation or orders list
            Bundle bundle = new Bundle();
            bundle.putLong("orderId", orderId);
            Navigation.findNavController(requireView()).navigate(R.id.action_checkoutFragment_to_orderConfirmationFragment, bundle);
        } else {
            Toast.makeText(requireContext(), "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
