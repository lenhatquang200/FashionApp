package com.ecommerce.app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.app.R;
import com.ecommerce.app.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;

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
        
        holder.orderId.setText("Order #" + order.getId());
        holder.orderDate.setText(order.getFormattedDate());
        holder.orderAmount.setText(order.getFormattedTotal());
        holder.orderStatus.setText(order.getOrderStatus());
        
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
        holder.orderStatus.setTextColor(context.getResources().getColor(colorResId));
        
        // Set click listener to navigate to order details
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("orderId", order.getId());
            Navigation.findNavController(v).navigate(R.id.action_ordersFragment_to_orderDetailFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void updateList(List<Order> newList) {
        this.orderList = newList;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView orderDate;
        TextView orderAmount;
        TextView orderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderDate = itemView.findViewById(R.id.order_date);
            orderAmount = itemView.findViewById(R.id.order_amount);
            orderStatus = itemView.findViewById(R.id.order_status);
        }
    }
}
