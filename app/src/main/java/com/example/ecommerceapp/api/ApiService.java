package com.example.ecommerceapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Service interface for API calls using Retrofit
 * This would be used for network calls to a backend if needed
 */
public class ApiService {
    private static final String BASE_URL = "https://api.example.com/"; // Replace with your actual API URL
    
    private static Retrofit retrofit = null;
    
    /**
     * Get a Retrofit instance for making API calls
     * @return Retrofit instance
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
