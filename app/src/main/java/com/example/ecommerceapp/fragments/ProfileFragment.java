package com.example.ecommerceapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.database.DatabaseHelper;
import com.example.ecommerceapp.model.User;

public class ProfileFragment extends Fragment {

    private EditText editName;
    private EditText editEmail;
    private EditText editAddress;
    private EditText editPhone;
    private Button buttonSave;
    
    private DatabaseHelper databaseHelper;
    private User currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        editName = view.findViewById(R.id.edit_name);
        editEmail = view.findViewById(R.id.edit_email);
        editAddress = view.findViewById(R.id.edit_address);
        editPhone = view.findViewById(R.id.edit_phone);
        buttonSave = view.findViewById(R.id.button_save);
        
        databaseHelper = DatabaseHelper.getInstance(requireContext());
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        loadUserProfile();
        
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
    }
    
    private void loadUserProfile() {
        // Get default user
        currentUser = databaseHelper.getDefaultUser();
        
        if (currentUser != null) {
            editName.setText(currentUser.getName());
            editEmail.setText(currentUser.getEmail());
            editAddress.setText(currentUser.getAddress());
            editPhone.setText(currentUser.getPhone());
        }
    }
    
    private void saveUserProfile() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        
        // Basic validation
        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Name and email are required", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (currentUser != null) {
            long result = databaseHelper.updateUserProfile(currentUser.getId(), name, email, address, phone);
            
            if (result > 0) {
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                // Refresh user data
                currentUser = databaseHelper.getUserById(currentUser.getId());
            } else {
                Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
