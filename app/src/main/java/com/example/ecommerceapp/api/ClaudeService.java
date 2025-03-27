package com.example.ecommerceapp.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class ClaudeService {
    private static ClaudeService instance;
    private final ClaudeApiClient apiClient;
    private final Handler mainHandler;
    private String conversationHistory = "";
    
    private ClaudeService() {
        apiClient = new ClaudeApiClient();
        mainHandler = new Handler(Looper.getMainLooper());
    }
    
    public static synchronized ClaudeService getInstance() {
        if (instance == null) {
            instance = new ClaudeService();
        }
        return instance;
    }
    
    public interface ChatResponseCallback {
        void onResponse(String response);
        void onError(String errorMessage);
    }
    
    public void sendMessage(final String message, final ChatResponseCallback callback) {
        apiClient.sendMessage(message, conversationHistory, new ClaudeApiClient.ResponseCallback() {
            @Override
            public void onResponse(final ApiResponse response) {
                // Save updated conversation history
                conversationHistory = response.getConversationHistory();
                
                // Return response on main thread
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response.getMessage());
                    }
                });
            }

            @Override
            public void onError(final String errorMessage) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(errorMessage);
                    }
                });
            }
        });
    }
    
    public void resetConversation() {
        conversationHistory = "";
    }
}
