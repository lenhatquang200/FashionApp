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
import com.ecommerce.app.adapter.OrderAdapter;
import com.ecommerce.app.database.DatabaseHelper;
import com.ecommerce.app.model.Order;

import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private TextView emptyOrdersMessage;
    
    private OrderAdapter orderAdapter;
    private DatabaseHelper databaseHelper;
    private List<Order> orderList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        ordersRecyclerView = view.findViewById(R.id.orders_recycler_view);
        emptyOrdersMessage = view.findViewById(R.id.empty_orders_message);

        // Set up RecyclerView
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadOrders();
    }

    private void loadOrders() {
        orderList = databaseHelper.getAllOrders();

        if (orderList.size() > 0) {
            // Show orders and hide empty message
            ordersRecyclerView.setVisibility(View.VISIBLE);
            emptyOrdersMessage.setVisibility(View.GONE);

            // Set up the adapter
            orderAdapter = new OrderAdapter(getContext(), orderList);
            ordersRecyclerView.setAdapter(orderAdapter);
        } else {
            // Show empty message and hide orders
            ordersRecyclerView.setVisibility(View.GONE);
            emptyOrdersMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh orders when returning to this fragment
        loadOrders();
    }
}
