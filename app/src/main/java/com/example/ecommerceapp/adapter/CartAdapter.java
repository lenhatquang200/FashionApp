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

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.model.CartItem;
import com.example.ecommerceapp.utils.ImageLoader;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemActionListener listener;
    private NumberFormat currencyFormatter;

    public interface OnCartItemActionListener {
        void onRemoveItem(CartItem cartItem, int position);
        void onUpdateQuantity(CartItem cartItem, int newQuantity, int position);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemActionListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
        this.currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.bind(cartItem, position);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void updateCartItems(List<CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView textProductName;
        TextView textProductPrice;
        TextView textQuantity;
        ImageButton buttonMinus;
        ImageButton buttonPlus;
        ImageButton buttonRemove;
        TextView textTotalPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.image_product);
            textProductName = itemView.findViewById(R.id.text_product_name);
            textProductPrice = itemView.findViewById(R.id.text_product_price);
            textQuantity = itemView.findViewById(R.id.text_quantity);
            buttonMinus = itemView.findViewById(R.id.button_minus);
            buttonPlus = itemView.findViewById(R.id.button_plus);
            buttonRemove = itemView.findViewById(R.id.button_remove);
            textTotalPrice = itemView.findViewById(R.id.text_total_price);
        }

        public void bind(final CartItem cartItem, final int position) {
            textProductName.setText(cartItem.getProductName());
            textProductPrice.setText(currencyFormatter.format(cartItem.getProductPrice()));
            textQuantity.setText(String.valueOf(cartItem.getQuantity()));
            textTotalPrice.setText(currencyFormatter.format(cartItem.getTotalPrice()));
            
            // Load product image
            ImageLoader.loadImage(context, cartItem.getImageUrl(), imageProduct);
            
            // Set click listeners
            buttonMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newQuantity = cartItem.getQuantity() - 1;
                    if (newQuantity > 0) {
                        if (listener != null) {
                            listener.onUpdateQuantity(cartItem, newQuantity, position);
                        }
                    }
                }
            });
            
            buttonPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int newQuantity = cartItem.getQuantity() + 1;
                    if (listener != null) {
                        listener.onUpdateQuantity(cartItem, newQuantity, position);
                    }
                }
            });
            
            buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onRemoveItem(cartItem, position);
                    }
                }
            });
        }
    }
}
