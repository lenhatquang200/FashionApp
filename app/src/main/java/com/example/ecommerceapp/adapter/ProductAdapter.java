package com.example.ecommerceapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.model.Product;
import com.example.ecommerceapp.ui.products.ProductDetailsActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying products in a RecyclerView
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    
    private final Context context;
    private List<Product> productList;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    
    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }
    
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        
        holder.productNameTextView.setText(product.getName());
        holder.productPriceTextView.setText(currencyFormat.format(product.getPrice()));
        
        // Load product image using Glide
        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder_product)
                .error(R.drawable.placeholder_product)
                .into(holder.productImageView);
        
        // Set rating
        holder.productRatingTextView.setText(String.format(Locale.getDefault(), "%.1f ★", product.getRating()));
        
        // Set click listener to open product details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("product_id", product.getId());
            context.startActivity(intent);
        });
    }
    
    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }
    
    /**
     * Update the adapter data
     * @param productList new list of products
     */
    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder class for product items
     */
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView productRatingTextView;
        
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.product_image);
            productNameTextView = itemView.findViewById(R.id.product_name);
            productPriceTextView = itemView.findViewById(R.id.product_price);
            productRatingTextView = itemView.findViewById(R.id.product_rating);
        }
    }
}
