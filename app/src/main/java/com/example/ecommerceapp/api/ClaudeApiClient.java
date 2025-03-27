package com.example.ecommerceapp.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Client for interacting with Claude API
 */
public class ClaudeApiClient {
    private static final String TAG = "ClaudeApiClient";
    
    // Claude API Constants
    private static final String BASE_URL = "https://api.anthropic.com/v1";
    private static final String CHAT_ENDPOINT = "/messages";
    private static final String ANTHROPIC_VERSION = "2023-06-01";
    private static final String MODEL = "claude-3-opus-20240229";
    private static final int MAX_TOKENS = 1000;
    
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    
    private String apiKey;
    private OkHttpClient client;
    
    public ClaudeApiClient() {
        // Get API key from environment or use a fallback for development
        this.apiKey = System.getenv("CLAUDE_API_KEY");
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            Log.w(TAG, "CLAUDE_API_KEY environment variable not set. Using fallback.");
            this.apiKey = "fallback_key_for_development";
        }
        
        // Initialize OkHttpClient with appropriate timeouts
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }
    
    /**
     * Send a message to Claude API and get a response
     * 
     * @param userMessage the message from the user
     * @param context optional context for the conversation
     * @return the response from Claude
     */
    public String sendMessage(String userMessage, String context) throws IOException, JSONException {
        // Prepare the request body
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("max_tokens", MAX_TOKENS);
        
        JSONArray messages = new JSONArray();
        
        // Add system message with context if provided
        if (context != null && !context.isEmpty()) {
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a helpful customer support assistant for an e-commerce app. " +
                    "You should be friendly, informative, and helpful. " + context);
            messages.put(systemMessage);
        }
        
        // Add user message
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.put(userMsg);
        
        requestBody.put("messages", messages);
        
        // Build the request
        Request request = new Request.Builder()
                .url(BASE_URL + CHAT_ENDPOINT)
                .addHeader("x-api-key", apiKey)
                .addHeader("anthropic-version", ANTHROPIC_VERSION)
                .addHeader("content-type", "application/json")
                .post(RequestBody.create(requestBody.toString(), JSON))
                .build();
        
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            
            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            // Extract the assistant's message
            JSONObject content = jsonResponse.getJSONObject("content");
            return content.optString("text", "I'm sorry, I couldn't process that request.");
        } catch (Exception e) {
            Log.e(TAG, "Error sending message to Claude API", e);
            return "I'm sorry, there was an error connecting to the support system. Please try again later.";
        }
    }
}
