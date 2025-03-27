package com.ecommerce.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.ecommerce.app.R;
import com.ecommerce.app.database.DatabaseHelper;
import com.ecommerce.app.model.User;

public class ProfileFragment extends Fragment {

    private ImageView profileImage;
    private EditText nameInput;
    private EditText emailInput;
    private EditText phoneInput;
    private EditText addressInput;
    private Button saveButton;
    private Button viewOrdersButton;

    private DatabaseHelper databaseHelper;
    private User user;
    // For simplicity, we'll use a hardcoded user ID
    private static final long DEFAULT_USER_ID = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        profileImage = view.findViewById(R.id.profile_image);
        nameInput = view.findViewById(R.id.profile_name);
        emailInput = view.findViewById(R.id.profile_email);
        phoneInput = view.findViewById(R.id.profile_phone);
        addressInput = view.findViewById(R.id.profile_address);
        saveButton = view.findViewById(R.id.save_profile_button);
        viewOrdersButton = view.findViewById(R.id.view_orders_button);

        // Load user data
        loadUserData();

        // Set up save button
        saveButton.setOnClickListener(v -> saveUserData());

        // Set up view orders button
        viewOrdersButton.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_ordersFragment);
        });
    }

    private void loadUserData() {
        user = databaseHelper.getUser(DEFAULT_USER_ID);

        if (user != null) {
            nameInput.setText(user.getName());
            emailInput.setText(user.getEmail());
            phoneInput.setText(user.getPhone());
            addressInput.setText(user.getAddress());

            // Load profile image using Glide
            Glide.with(requireContext())
                 .load(user.getProfileImageUrl())
                 .placeholder(R.drawable.placeholder_profile)
                 .error(R.drawable.error_profile)
                 .circleCrop()
                 .into(profileImage);
        }
    }

    private void saveUserData() {
        if (user != null) {
            user.setName(nameInput.getText().toString().trim());
            user.setEmail(emailInput.getText().toString().trim());
            user.setPhone(phoneInput.getText().toString().trim());
            user.setAddress(addressInput.getText().toString().trim());

            databaseHelper.updateUser(user);
            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
