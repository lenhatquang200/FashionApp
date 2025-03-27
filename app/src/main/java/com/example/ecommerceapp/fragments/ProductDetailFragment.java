package com.example.ecommerceapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.model.Product;
import com.example.ecommerceapp.utils.ImageLoader;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailFragment extends Fragment {

    private ImageView imageProduct;
    private TextView textProductName;
    private TextView textProductPrice;
    private TextView textProductDescription;
    private TextView textProductStock;
    private Button buttonAddToCart;
    private Button buttonBuyNow;
    
    private Product product;
    private DatabaseHelper databaseHelper;
    private NavController navController;
    private NumberFormat currencyFormatter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        
        imageProduct = view.findViewById(R.id.image_product);
        textProductName = view.findViewById(R.id.text_product_name);
        textProductPrice = view.findViewById(R.id.text_product_price);
        textProductDescription = view.findViewById(R.id.text_product_description);
        textProductStock = view.findViewById(R.id.text_product_stock);
        buttonAddToCart = view.findViewById(R.id.button_add_to_cart);
        buttonBuyNow = view.findViewById(R.id.button_buy_now);
        
        databaseHelper = DatabaseHelper.getInstance(requireContext());
        currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        
        // Get product from arguments
        if (getArguments() != null && getArguments().containsKey("product")) {
            product = getArguments().getParcelable("product");
            displayProductDetails();
            setupClickListeners();
        } else {
            // Handle error, product not found
            Toast.makeText(requireContext(), "Product not found", Toast.LENGTH_SHORT).show();
            navController.navigateUp();
        }
    }
    
    private void displayProductDetails() {
        if (product != null) {
            textProductName.setText(product.getName());
            textProductPrice.setText(currencyFormatter.format(product.getPrice()));
            textProductDescription.setText(product.getDescription());
            textProductStock.setText("In Stock: " + product.getStock());
            
            // Load product image
            ImageLoader.loadImage(requireContext(), product.getImageUrl(), imageProduct);
        }
    }
    
    private void setupClickListeners() {
        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
        
        buttonBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyNow();
            }
        });
    }
    
    private void addToCart() {
        long result = databaseHelper.addToCart(product.getId(), 1);
        if (result > 0) {
            Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void buyNow() {
        // Add to cart and navigate to checkout
        long result = databaseHelper.addToCart(product.getId(), 1);
        if (result > 0) {
            navController.navigate(R.id.checkoutFragment);
        } else {
            Toast.makeText(requireContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show();
        }
    }
}
