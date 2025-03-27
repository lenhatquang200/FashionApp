package com.example.ecommerceapp.api;

import android.util.Log;

import com.example.ecommerceapp.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClaudeApiClient {
    private static final String TAG = "ClaudeApiClient";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String API_URL = "https://api.anthropic.com/v1/messages";
    
    private final OkHttpClient client;
    private final String apiKey;

    public ClaudeApiClient() {
        client = new OkHttpClient();
        // Get API key from constants, which should be loaded from environment variable
        apiKey = Constants.CLAUDE_API_KEY;
    }

    public interface ResponseCallback {
        void onResponse(ApiResponse response);
        void onError(String errorMessage);
    }

    public void sendMessage(String message, String conversationHistory, final ResponseCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "claude-3-opus-20240229");
            
            // Build system prompt
            String systemPrompt = "You are a helpful customer support assistant for an e-commerce app. " +
                    "Answer questions about products, orders, shipping, returns, and other shopping related queries. " +
                    "Be friendly, concise, and helpful. If you don't know the answer to a question, suggest contacting human support.";
            
            jsonBody.put("system", systemPrompt);
            
            // Add user message
            JSONArray messages = new JSONArray();
            if (conversationHistory != null && !conversationHistory.isEmpty()) {
                try {
                    // Parse conversation history back to JSON array if provided
                    messages = new JSONArray(conversationHistory);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing conversation history: " + e.getMessage());
                    // Start fresh if we can't parse the history
                    messages = new JSONArray();
                }
            }
            
            // Add the new user message
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", message);
            messages.put(userMessage);
            
            jsonBody.put("messages", messages);
            jsonBody.put("max_tokens", 1000);
            jsonBody.put("temperature", 0.7);
            
            RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
            
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .addHeader("x-api-key", apiKey)
                    .addHeader("anthropic-version", "2023-06-01")
                    .addHeader("content-type", "application/json")
                    .build();
            
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError("Network error: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        callback.onError("Server error: " + response.code() + " " + response.message());
                        return;
                    }
                    
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String content = jsonResponse.getJSONObject("content").getJSONArray("parts").getString(0);
                        
                        // Add the assistant's response to the messages array
                        JSONObject assistantMessage = new JSONObject();
                        assistantMessage.put("role", "assistant");
                        assistantMessage.put("content", content);
                        messages.put(assistantMessage);
                        
                        // Create API response
                        ApiResponse apiResponse = new ApiResponse(
                                true,
                                content,
                                messages.toString()
                        );
                        
                        callback.onResponse(apiResponse);
                    } catch (JSONException e) {
                        callback.onError("Error parsing response: " + e.getMessage());
                    }
                }
            });
        } catch (JSONException e) {
            callback.onError("Error creating request: " + e.getMessage());
        }
    }
}
