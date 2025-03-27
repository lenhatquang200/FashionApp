package com.ecommerce.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ecommerce.app.R;
import com.ecommerce.app.database.DatabaseHelper;
import com.ecommerce.app.model.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private CartItemListener listener;
    private DatabaseHelper databaseHelper;

    public interface CartItemListener {
        void onQuantityChanged();
        void onItemRemoved(CartItem item);
    }

    public CartAdapter(Context context, List<CartItem> cartItemList, CartItemListener listener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.listener = listener;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        
        holder.productName.setText(cartItem.getProductName());
        holder.productPrice.setText(cartItem.getFormattedPrice());
        holder.productQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.productSubtotal.setText(cartItem.getFormattedSubtotal());
        
        // Load image using Glide
        Glide.with(context)
             .load(cartItem.getProductImageUrl())
             .placeholder(R.drawable.placeholder_image)
             .error(R.drawable.error_image)
             .into(holder.productImage);
        
        // Set up quantity increment button
        holder.incrementBtn.setOnClickListener(v -> {
            int quantity = cartItem.getQuantity() + 1;
            cartItem.setQuantity(quantity);
            databaseHelper.updateCartItemQuantity(cartItem.getId(), quantity);
            holder.productQuantity.setText(String.valueOf(quantity));
            holder.productSubtotal.setText(cartItem.getFormattedSubtotal());
            if (listener != null) {
                listener.onQuantityChanged();
            }
        });
        
        // Set up quantity decrement button
        holder.decrementBtn.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                int quantity = cartItem.getQuantity() - 1;
                cartItem.setQuantity(quantity);
                databaseHelper.updateCartItemQuantity(cartItem.getId(), quantity);
                holder.productQuantity.setText(String.valueOf(quantity));
                holder.productSubtotal.setText(cartItem.getFormattedSubtotal());
                if (listener != null) {
                    listener.onQuantityChanged();
                }
            }
        });
        
        // Set up remove button
        holder.removeBtn.setOnClickListener(v -> {
            databaseHelper.removeFromCart(cartItem.getId());
            cartItemList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItemList.size());
            if (listener != null) {
                listener.onItemRemoved(cartItem);
                listener.onQuantityChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public void updateList(List<CartItem> newList) {
        this.cartItemList = newList;
        notifyDataSetChanged();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productQuantity;
        TextView productSubtotal;
        ImageButton incrementBtn;
        ImageButton decrementBtn;
        ImageButton removeBtn;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.cart_item_image);
            productName = itemView.findViewById(R.id.cart_item_name);
            productPrice = itemView.findViewById(R.id.cart_item_price);
            productQuantity = itemView.findViewById(R.id.cart_item_quantity);
            productSubtotal = itemView.findViewById(R.id.cart_item_subtotal);
            incrementBtn = itemView.findViewById(R.id.cart_increment_btn);
            decrementBtn = itemView.findViewById(R.id.cart_decrement_btn);
            removeBtn = itemView.findViewById(R.id.cart_remove_btn);
        }
    }
}
