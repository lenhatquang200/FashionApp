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

import com.ecommerce.app.R;
import com.ecommerce.app.database.DatabaseHelper;
import com.ecommerce.app.model.Order;

public class OrderConfirmationFragment extends Fragment {

    private TextView orderConfirmationMessage;
    private TextView orderNumber;
    private TextView orderDate;
    private TextView orderTotal;
    private Button continueShoppingButton;
    private Button viewOrderButton;

    private DatabaseHelper databaseHelper;
    private long orderId;
    private Order order;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
        
        // Get order ID from arguments
        if (getArguments() != null) {
            orderId = getArguments().getLong("orderId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_confirmation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        orderConfirmationMessage = view.findViewById(R.id.order_confirmation_message);
        orderNumber = view.findViewById(R.id.order_confirmation_number);
        orderDate = view.findViewById(R.id.order_confirmation_date);
        orderTotal = view.findViewById(R.id.order_confirmation_total);
        continueShoppingButton = view.findViewById(R.id.continue_shopping_button);
        viewOrderButton = view.findViewById(R.id.view_order_button);

        // Load order data
        loadOrderData();

        // Set up button click listeners
        continueShoppingButton.setOnClickListener(v -> {
            // Navigate back to home
            Navigation.findNavController(view).navigate(R.id.action_orderConfirmationFragment_to_homeFragment);
        });

        viewOrderButton.setOnClickListener(v -> {
            // Navigate to order details
            Bundle bundle = new Bundle();
            bundle.putLong("orderId", orderId);
            Navigation.findNavController(view).navigate(R.id.action_orderConfirmationFragment_to_orderDetailFragment, bundle);
        });
    }

    private void loadOrderData() {
        order = databaseHelper.getOrder(orderId);

        if (order != null) {
            orderNumber.setText("Order #" + order.getId());
            orderDate.setText(order.getFormattedDate());
            orderTotal.setText(order.getFormattedTotal());
        }
    }
}
