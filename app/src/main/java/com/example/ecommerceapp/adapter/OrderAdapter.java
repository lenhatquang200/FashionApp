package com.example.ecommerceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.model.Order;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying orders in a RecyclerView
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    
    private final Context context;
    private List<Order> orderList;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
    
    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }
    
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        
        // Set order details
        holder.orderIdTextView.setText(String.format(Locale.getDefault(), "Order #%d", order.getId()));
        holder.orderDateTextView.setText(dateFormat.format(order.getOrderDate()));
        holder.orderAmountTextView.setText(currencyFormat.format(order.getTotalAmount()));
        holder.orderStatusTextView.setText(order.getStatus());
        
        // Set up the nested RecyclerView for order items
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(context, order.getItems());
        holder.orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.orderItemsRecyclerView.setAdapter(orderItemAdapter);
        
        // Handle expand/collapse functionality
        holder.itemView.setOnClickListener(v -> {
            boolean isExpanded = holder.orderItemsRecyclerView.getVisibility() == View.VISIBLE;
            holder.orderItemsRecyclerView.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            holder.expandCollapseTextView.setText(isExpanded ? "▼ Show Items" : "▲ Hide Items");
        });
    }
    
    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }
    
    /**
     * Update the adapter data
     * @param orderList new list of orders
     */
    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder class for order items
     */
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView orderDateTextView;
        TextView orderAmountTextView;
        TextView orderStatusTextView;
        TextView expandCollapseTextView;
        RecyclerView orderItemsRecyclerView;
        
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.order_id);
            orderDateTextView = itemView.findViewById(R.id.order_date);
            orderAmountTextView = itemView.findViewById(R.id.order_amount);
            orderStatusTextView = itemView.findViewById(R.id.order_status);
            expandCollapseTextView = itemView.findViewById(R.id.order_expand_collapse);
            orderItemsRecyclerView = itemView.findViewById(R.id.order_items_recycler_view);
        }
    }
    
    /**
     * Adapter for order items within an order
     */
    private static class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
        
        private final Context context;
        private final List<Order.OrderItem> orderItems;
        private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        
        public OrderItemAdapter(Context context, List<Order.OrderItem> orderItems) {
            this.context = context;
            this.orderItems = orderItems;
        }
        
        @NonNull
        @Override
        public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
            return new OrderItemViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
            Order.OrderItem item = orderItems.get(position);
            
            holder.itemNameTextView.setText(item.getProductName());
            holder.itemQuantityTextView.setText(String.format(Locale.getDefault(), "Qty: %d", item.getQuantity()));
            holder.itemPriceTextView.setText(currencyFormat.format(item.getPrice()));
            holder.itemSubtotalTextView.setText(currencyFormat.format(item.getSubtotal()));
        }
        
        @Override
        public int getItemCount() {
            return orderItems == null ? 0 : orderItems.size();
        }
        
        /**
         * ViewHolder class for order item details
         */
        static class OrderItemViewHolder extends RecyclerView.ViewHolder {
            TextView itemNameTextView;
            TextView itemQuantityTextView;
            TextView itemPriceTextView;
            TextView itemSubtotalTextView;
            
            public OrderItemViewHolder(@NonNull View itemView) {
                super(itemView);
                itemNameTextView = itemView.findViewById(R.id.order_item_name);
                itemQuantityTextView = itemView.findViewById(R.id.order_item_quantity);
                itemPriceTextView = itemView.findViewById(R.id.order_item_price);
                itemSubtotalTextView = itemView.findViewById(R.id.order_item_subtotal);
            }
        }
    }
}
