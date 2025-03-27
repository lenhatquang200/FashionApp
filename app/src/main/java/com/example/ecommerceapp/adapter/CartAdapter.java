package com.example.ecommerceapp.adapter;

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
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.model.CartItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying cart items in a RecyclerView
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    
    private final Context context;
    private List<CartItem> cartItems;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    private CartItemListener cartItemListener;
    
    public interface CartItemListener {
        void onQuantityChanged();
        void onItemRemoved(CartItem cartItem);
    }
    
    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }
    
    public void setCartItemListener(CartItemListener listener) {
        this.cartItemListener = listener;
    }
    
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        
        holder.productNameTextView.setText(cartItem.getProduct().getName());
        holder.productPriceTextView.setText(currencyFormat.format(cartItem.getPriceAtAddition()));
        holder.productQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
        
        // Calculate and set subtotal
        double subtotal = cartItem.getQuantity() * cartItem.getPriceAtAddition();
        holder.productSubtotalTextView.setText(currencyFormat.format(subtotal));
        
        // Load product image using Glide
        Glide.with(context)
                .load(cartItem.getProduct().getImageUrl())
                .placeholder(R.drawable.placeholder_product)
                .error(R.drawable.placeholder_product)
                .into(holder.productImageView);
        
        // Set click listeners for quantity adjustment
        holder.increaseQuantityButton.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() + 1;
            updateCartItemQuantity(cartItem, holder, newQuantity);
        });
        
        holder.decreaseQuantityButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                int newQuantity = cartItem.getQuantity() - 1;
                updateCartItemQuantity(cartItem, holder, newQuantity);
            }
        });
        
        holder.removeItemButton.setOnClickListener(v -> {
            DatabaseHelper db = DatabaseHelper.getInstance(context);
            db.removeCartItem(cartItem.getId());
            
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            
            if (cartItemListener != null) {
                cartItemListener.onItemRemoved(cartItem);
            }
        });
    }
    
    private void updateCartItemQuantity(CartItem cartItem, CartViewHolder holder, int newQuantity) {
        DatabaseHelper db = DatabaseHelper.getInstance(context);
        db.updateCartItemQuantity(cartItem.getId(), newQuantity);
        
        cartItem.setQuantity(newQuantity);
        holder.productQuantityTextView.setText(String.valueOf(newQuantity));
        
        // Update subtotal
        double subtotal = newQuantity * cartItem.getPriceAtAddition();
        holder.productSubtotalTextView.setText(currencyFormat.format(subtotal));
        
        if (cartItemListener != null) {
            cartItemListener.onQuantityChanged();
        }
    }
    
    @Override
    public int getItemCount() {
        return cartItems == null ? 0 : cartItems.size();
    }
    
    /**
     * Update the adapter data
     * @param cartItems new list of cart items
     */
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder class for cart items
     */
    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView productQuantityTextView;
        TextView productSubtotalTextView;
        ImageButton increaseQuantityButton;
        ImageButton decreaseQuantityButton;
        ImageButton removeItemButton;
        
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.cart_item_image);
            productNameTextView = itemView.findViewById(R.id.cart_item_name);
            productPriceTextView = itemView.findViewById(R.id.cart_item_price);
            productQuantityTextView = itemView.findViewById(R.id.cart_item_quantity);
            productSubtotalTextView = itemView.findViewById(R.id.cart_item_subtotal);
            increaseQuantityButton = itemView.findViewById(R.id.cart_item_increase);
            decreaseQuantityButton = itemView.findViewById(R.id.cart_item_decrease);
            removeItemButton = itemView.findViewById(R.id.cart_item_remove);
        }
    }
}
