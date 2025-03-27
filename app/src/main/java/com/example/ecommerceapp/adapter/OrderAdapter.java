package com.example.ecommerceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.model.Order;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private OnOrderClickListener listener;
    private NumberFormat currencyFormatter;
    private SimpleDateFormat dateFormatter;

    public interface OnOrderClickListener {
        void onOrderClick(Order order, int position);
    }

    public OrderAdapter(Context context, List<Order> orderList, OnOrderClickListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
        this.currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        this.dateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order, position);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void updateOrderList(List<Order> newOrderList) {
        this.orderList = newOrderList;
        notifyDataSetChanged();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textOrderNumber;
        TextView textOrderDate;
        TextView textOrderStatus;
        TextView textOrderTotal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderNumber = itemView.findViewById(R.id.text_order_number);
            textOrderDate = itemView.findViewById(R.id.text_order_date);
            textOrderStatus = itemView.findViewById(R.id.text_order_status);
            textOrderTotal = itemView.findViewById(R.id.text_order_total);
        }

        public void bind(final Order order, final int position) {
            textOrderNumber.setText("Order #" + order.getOrderNumber());
            textOrderDate.setText(dateFormatter.format(order.getOrderDateAsDate()));
            textOrderStatus.setText(order.getStatus());
            textOrderTotal.setText(currencyFormatter.format(order.getTotalAmount()));
            
            // Set status color based on order status
            if ("Completed".equalsIgnoreCase(order.getStatus())) {
                textOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            } else if ("Processing".equalsIgnoreCase(order.getStatus())) {
                textOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
            } else if ("Cancelled".equalsIgnoreCase(order.getStatus())) {
                textOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
            
            // Set click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onOrderClick(order, position);
                    }
                }
            });
        }
    }
}
