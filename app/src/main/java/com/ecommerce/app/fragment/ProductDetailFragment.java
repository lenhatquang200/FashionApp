package com.ecommerce.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.ecommerce.app.R;
import com.ecommerce.app.database.DatabaseHelper;
import com.ecommerce.app.model.CartItem;
import com.ecommerce.app.model.Product;

public class ProductDetailFragment extends Fragment {

    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private TextView productDescription;
    private RatingBar productRating;
    private TextView productReviewCount;
    private TextView productStock;
    private Button addToCartButton;

    private DatabaseHelper databaseHelper;
    private Product product;
    private long productId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
        
        // Get product ID from arguments
        if (getArguments() != null) {
            productId = getArguments().getLong("productId");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        productImage = view.findViewById(R.id.product_detail_image);
        productName = view.findViewById(R.id.product_detail_name);
        productPrice = view.findViewById(R.id.product_detail_price);
        productDescription = view.findViewById(R.id.product_detail_description);
        productRating = view.findViewById(R.id.product_detail_rating);
        productReviewCount = view.findViewById(R.id.product_detail_review_count);
        productStock = view.findViewById(R.id.product_detail_stock);
        addToCartButton = view.findViewById(R.id.add_to_cart_button);

        // Load product data
        loadProductData();

        // Set up add to cart button
        addToCartButton.setOnClickListener(v -> addToCart());
    }

    private void loadProductData() {
        product = databaseHelper.getProduct(productId);

        if (product != null) {
            productName.setText(product.getName());
            productPrice.setText(product.getFormattedPrice());
            productDescription.setText(product.getDescription());
            productRating.setRating(product.getRating());
            productReviewCount.setText("(" + product.getReviewCount() + " reviews)");
            productStock.setText("In Stock: " + product.getStock());

            // Load product image using Glide
            Glide.with(requireContext())
                 .load(product.getImageUrl())
                 .placeholder(R.drawable.placeholder_image)
                 .error(R.drawable.error_image)
                 .into(productImage);
        }
    }

    private void addToCart() {
        if (product != null) {
            CartItem cartItem = new CartItem();
            cartItem.setProductId(product.getId());
            cartItem.setProductName(product.getName());
            cartItem.setProductImageUrl(product.getImageUrl());
            cartItem.setProductPrice(product.getPrice());
            cartItem.setQuantity(1);

            databaseHelper.addToCart(cartItem);
            Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show();

            // Navigate to cart
            Navigation.findNavController(requireView()).navigate(R.id.action_productDetailFragment_to_cartFragment);
        }
    }
}
