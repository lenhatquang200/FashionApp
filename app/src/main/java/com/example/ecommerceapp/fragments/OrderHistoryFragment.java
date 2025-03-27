package com.example.ecommerceapp.fragments;

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

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapter.OrderAdapter;
import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.model.Order;

import java.util.List;

public class OrderHistoryFragment extends Fragment implements OrderAdapter.OnOrderClickListener {

    private RecyclerView recyclerViewOrders;
    private TextView textEmptyOrders;
    
    private OrderAdapter orderAdapter;
    private DatabaseHelper databaseHelper;
    private List<Order> orderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        
        recyclerViewOrders = view.findViewById(R.id.recycler_view_orders);
        textEmptyOrders = view.findViewById(R.id.text_empty_orders);
        
        databaseHelper = DatabaseHelper.getInstance(requireContext());
        
        // Set up recycler view
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        loadOrders();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Reload orders when fragment becomes visible again
        loadOrders();
    }
    
    private void loadOrders() {
        orderList = databaseHelper.getAllOrders();
        
        if (orderList.isEmpty()) {
            recyclerViewOrders.setVisibility(View.GONE);
            textEmptyOrders.setVisibility(View.VISIBLE);
        } else {
            recyclerViewOrders.setVisibility(View.VISIBLE);
            textEmptyOrders.setVisibility(View.GONE);
            
            // Update adapter
            if (orderAdapter == null) {
                orderAdapter = new OrderAdapter(requireContext(), orderList, this);
                recyclerViewOrders.setAdapter(orderAdapter);
            } else {
                orderAdapter.updateOrderList(orderList);
            }
        }
    }

    @Override
    public void onOrderClick(Order order, int position) {
        // We could navigate to an order details fragment here
        // For now just show a toast with the order number
        /* 
        Toast.makeText(requireContext(), 
                "Viewing details for Order #" + order.getOrderNumber(), 
                Toast.LENGTH_SHORT).show();
        
        // Example of navigating to an order detail view:
        Bundle args = new Bundle();
        args.putInt("order_id", order.getId());
        navController.navigate(R.id.orderDetailFragment, args);
        */
    }
}
