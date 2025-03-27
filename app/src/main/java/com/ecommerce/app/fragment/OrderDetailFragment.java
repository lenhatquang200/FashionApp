package com.ecommerce.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.app.R;
import com.ecommerce.app.adapter.CartAdapter;
import com.ecommerce.app.database.DatabaseHelper;
import com.ecommerce.app.model.CartItem;
import com.ecommerce.app.model.Order;

public class OrderDetailFragment extends Fragment {

    private TextView orderId;
    private TextView orderDate;
    private TextView orderStatus;
    private TextView orderShippingAddress;
    private TextView orderPaymentMethod;
    private RecyclerView orderItemsRecyclerView;
    private TextView orderTotal;

    private DatabaseHelper databaseHelper;
    private Order order;
    private long orderIdValue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
        
        // Get order ID from arguments
        if (getArguments() != null) {
            orderIdValue = getArguments().getLong("orderId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        orderId = view.findViewById(R.id.order_detail_id);
        orderDate = view.findViewById(R.id.order_detail_date);
        orderStatus = view.findViewById(R.id.order_detail_status);
        orderShippingAddress = view.findViewById(R.id.order_detail_address);
        orderPaymentMethod = view.findViewById(R.id.order_detail_payment);
        orderItemsRecyclerView = view.findViewById(R.id.order_items_recycler_view);
        orderTotal = view.findViewById(R.id.order_detail_total);

        // Set up RecyclerView
        orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load order data
        loadOrderDetails();
    }

    private void loadOrderDetails() {
        order = databaseHelper.getOrder(orderIdValue);

        if (order != null) {
            // Set order details
            orderId.setText("Order #" + order.getId());
            orderDate.setText(order.getFormattedDate());
            orderStatus.setText(order.getOrderStatus());
            orderShippingAddress.setText(order.getShippingAddress());
            orderPaymentMethod.setText(order.getPaymentMethod());
            orderTotal.setText(order.getFormattedTotal());

            // Set status color based on order status
            int colorResId;
            switch (order.getOrderStatus()) {
                case Order.STATUS_PENDING:
                    colorResId = R.color.order_status_pending;
                    break;
                case Order.STATUS_PROCESSING:
                    colorResId = R.color.order_status_processing;
                    break;
                case Order.STATUS_SHIPPED:
                    colorResId = R.color.order_status_shipped;
                    break;
                case Order.STATUS_DELIVERED:
                    colorResId = R.color.order_status_delivered;
                    break;
                case Order.STATUS_CANCELLED:
                    colorResId = R.color.order_status_cancelled;
                    break;
                default:
                    colorResId = R.color.black;
                    break;
            }
            orderStatus.setTextColor(requireContext().getResources().getColor(colorResId));

            // Set up order items adapter
            // Using CartAdapter but disabling interaction
            CartAdapter adapter = new CartAdapter(getContext(), order.getOrderItems(), null);
            orderItemsRecyclerView.setAdapter(adapter);
        }
    }
}
