package com.ecommerce.app.api;

import android.os.Build;
import android.util.Log;

import com.ecommerce.app.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnthropicClient {
    private static final String TAG = "AnthropicClient";
    private static final String BASE_URL = "https://api.anthropic.com/";
    private static final String ANTHROPIC_VERSION = "2023-06-01";
    private static final String MODEL = "claude-3-haiku-20240307";
    
    private final AnthropicService anthropicService;
    private final String apiKey;
    
    public AnthropicClient() {
        // Set up logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        // Build HTTP client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        
        // Build Retrofit instance
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        
        // Create API service
        anthropicService = retrofit.create(AnthropicService.class);
        
        // Get API key from environment variable
        apiKey = System.getenv("ANTHROPIC_API_KEY");
        
        if (apiKey == null || apiKey.isEmpty()) {
            Log.w(TAG, "ANTHROPIC_API_KEY environment variable not set");
        }
    }
    
    public void getResponse(String userMessage, Callback<AnthropicResponse> callback) {
        // Create the system prompt for an e-commerce assistant
        String systemPrompt = "You are a helpful customer service assistant for an e-commerce app. " +
                "Provide friendly, concise responses to help customers with their shopping needs. " +
                "You can answer questions about products, shipping, returns, order status, and other " +
                "shopping-related inquiries. If you don't know specific details about an order or product, " +
                "politely explain that you can help them contact customer support.";
        
        // Create the message object
        Map<String, String> userContent = new HashMap<>();
        userContent.put("type", "text");
        userContent.put("text", userMessage);
        
        List<Map<String, String>> userContentList = new ArrayList<>();
        userContentList.add(userContent);
        
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("role", "user");
        userMap.put("content", userContentList);
        
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(userMap);
        
        AnthropicMessage request = new AnthropicMessage();
        request.setModel(MODEL);
        request.setSystem(systemPrompt);
        request.setMessages(messages);
        request.setMaxTokens(1024);
        
        // Make the API call
        anthropicService.sendMessage(apiKey, ANTHROPIC_VERSION, request)
                .enqueue(callback);
    }
}
