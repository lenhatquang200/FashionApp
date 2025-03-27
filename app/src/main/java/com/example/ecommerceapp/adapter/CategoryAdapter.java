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
import com.example.ecommerceapp.model.Category;
import com.example.ecommerceapp.ui.products.ProductListActivity;

import java.util.List;

/**
 * Adapter for displaying categories in a RecyclerView
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    
    private final Context context;
    private List<Category> categoryList;
    
    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }
    
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        
        holder.categoryNameTextView.setText(category.getName());
        
        // Load category image using Glide
        Glide.with(context)
                .load(category.getImageUrl())
                .placeholder(R.drawable.placeholder_category)
                .error(R.drawable.placeholder_category)
                .into(holder.categoryImageView);
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductListActivity.class);
            intent.putExtra("category_id", category.getId());
            intent.putExtra("category_name", category.getName());
            context.startActivity(intent);
        });
    }
    
    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }
    
    /**
     * Update the adapter data
     * @param categoryList new list of categories
     */
    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }
    
    /**
     * ViewHolder class for category items
     */
    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImageView;
        TextView categoryNameTextView;
        
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImageView = itemView.findViewById(R.id.category_image);
            categoryNameTextView = itemView.findViewById(R.id.category_name);
        }
    }
}
