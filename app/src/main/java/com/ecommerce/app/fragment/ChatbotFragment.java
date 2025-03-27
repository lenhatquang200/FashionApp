package com.ecommerce.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecommerce.app.R;
import com.ecommerce.app.adapter.ChatAdapter;
import com.ecommerce.app.api.AnthropicClient;
import com.ecommerce.app.api.AnthropicResponse;
import com.ecommerce.app.database.DatabaseHelper;
import com.ecommerce.app.model.ChatMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private Button sendButton;
    private ProgressBar loadingIndicator;

    private ChatAdapter chatAdapter;
    private DatabaseHelper databaseHelper;
    private AnthropicClient anthropicClient;
    private Handler handler;

    private static final String TAG = "ChatbotFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
        anthropicClient = new AnthropicClient();
        handler = new Handler(Looper.getMainLooper());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatbot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        chatRecyclerView = view.findViewById(R.id.chat_recycler_view);
        messageInput = view.findViewById(R.id.message_input);
        sendButton = view.findViewById(R.id.send_button);
        loadingIndicator = view.findViewById(R.id.loading_indicator);

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true); // To show newest messages at the bottom
        chatRecyclerView.setLayoutManager(layoutManager);

        // Load chat history
        loadChatHistory();

        // Set up send button
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void loadChatHistory() {
        List<ChatMessage> chatHistory = databaseHelper.getChatHistory();
        
        chatAdapter = new ChatAdapter(requireContext(), chatHistory);
        chatRecyclerView.setAdapter(chatAdapter);
        
        if (chatHistory.size() > 0) {
            chatRecyclerView.scrollToPosition(chatHistory.size() - 1);
        } else {
            // If no chat history, add a welcome message
            addWelcomeMessage();
        }
    }

    private void addWelcomeMessage() {
        ChatMessage welcomeMessage = new ChatMessage();
        welcomeMessage.setMessage("Hello! I'm your shopping assistant. How can I help you with your shopping today?");
        welcomeMessage.setMessageType(ChatMessage.TYPE_ASSISTANT);
        
        databaseHelper.addChatMessage(welcomeMessage);
        chatAdapter.addMessage(welcomeMessage);
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        
        if (messageText.isEmpty()) {
            return;
        }
        
        // Add user message to chat
        ChatMessage userMessage = new ChatMessage();
        userMessage.setMessage(messageText);
        userMessage.setMessageType(ChatMessage.TYPE_USER);
        
        long messageId = databaseHelper.addChatMessage(userMessage);
        userMessage.setId(messageId);
        
        chatAdapter.addMessage(userMessage);
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
        
        // Clear input
        messageInput.setText("");
        
        // Show loading indicator
        loadingIndicator.setVisibility(View.VISIBLE);
        sendButton.setEnabled(false);
        
        // Get response from Claude
        getClaudeResponse(messageText);
    }

    private void getClaudeResponse(String userMessage) {
        anthropicClient.getResponse(userMessage, new Callback<AnthropicResponse>() {
            @Override
            public void onResponse(Call<AnthropicResponse> call, Response<AnthropicResponse> response) {
                handler.post(() -> {
                    loadingIndicator.setVisibility(View.GONE);
                    sendButton.setEnabled(true);
                    
                    if (response.isSuccessful() && response.body() != null) {
                        String assistantMessage = response.body().getContent();
                        addAssistantMessage(assistantMessage);
                    } else {
                        handleApiError(response);
                    }
                });
            }

            @Override
            public void onFailure(Call<AnthropicResponse> call, Throwable t) {
                handler.post(() -> {
                    loadingIndicator.setVisibility(View.GONE);
                    sendButton.setEnabled(true);
                    
                    Log.e(TAG, "API call failed", t);
                    Toast.makeText(requireContext(), "Failed to connect to assistant: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    
                    // Add a fallback message
                    addAssistantMessage("I'm sorry, I'm having trouble connecting to the server right now. Please try again later.");
                });
            }
        });
    }

    private void addAssistantMessage(String messageText) {
        ChatMessage assistantMessage = new ChatMessage();
        assistantMessage.setMessage(messageText);
        assistantMessage.setMessageType(ChatMessage.TYPE_ASSISTANT);
        
        long messageId = databaseHelper.addChatMessage(assistantMessage);
        assistantMessage.setId(messageId);
        
        chatAdapter.addMessage(assistantMessage);
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() - 1);
    }

    private void handleApiError(Response<AnthropicResponse> response) {
        String errorMsg;
        try {
            errorMsg = response.errorBody() != null ? 
                      response.errorBody().string() : 
                      "Unknown error occurred";
        } catch (Exception e) {
            errorMsg = "Error processing response";
        }
        
        Log.e(TAG, "API error: " + errorMsg);
        Toast.makeText(requireContext(), "Assistant error: " + response.code(), Toast.LENGTH_SHORT).show();
        
        // Add a fallback message
        addAssistantMessage("I'm sorry, I encountered an error while processing your request. Please try again.");
    }
}
