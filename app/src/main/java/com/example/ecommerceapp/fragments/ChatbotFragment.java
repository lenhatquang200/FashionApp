package com.example.ecommerceapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.adapter.MessageAdapter;
import com.example.ecommerceapp.api.ClaudeService;
import com.example.ecommerceapp.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatbotFragment extends Fragment {

    private RecyclerView recyclerViewMessages;
    private EditText editMessage;
    private ImageButton buttonSend;
    private ProgressBar progressBar;
    
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private ClaudeService claudeService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatbot, container, false);
        
        recyclerViewMessages = view.findViewById(R.id.recycler_view_messages);
        editMessage = view.findViewById(R.id.edit_message);
        buttonSend = view.findViewById(R.id.button_send);
        progressBar = view.findViewById(R.id.progress_bar);
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize chat
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(requireContext(), messageList);
        
        // Set up recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(layoutManager);
        recyclerViewMessages.setAdapter(messageAdapter);
        
        // Get Claude service instance
        claudeService = ClaudeService.getInstance();
        
        // Add welcome message
        Message welcomeMessage = new Message(
                Message.TYPE_BOT,
                "Hello! I'm your shopping assistant. How can I help you today?"
        );
        messageList.add(welcomeMessage);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        
        // Set up send button
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }
    
    private void sendMessage() {
        String message = editMessage.getText().toString().trim();
        if (message.isEmpty()) {
            return;
        }
        
        // Add user message to the list
        Message userMessage = new Message(Message.TYPE_USER, message);
        messageList.add(userMessage);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        
        // Clear input
        editMessage.setText("");
        
        // Scroll to the latest message
        recyclerViewMessages.smoothScrollToPosition(messageList.size() - 1);
        
        // Show loading
        progressBar.setVisibility(View.VISIBLE);
        
        // Send message to Claude
        claudeService.sendMessage(message, new ClaudeService.ChatResponseCallback() {
            @Override
            public void onResponse(String response) {
                // Hide loading
                progressBar.setVisibility(View.GONE);
                
                // Add bot message to the list
                Message botMessage = new Message(Message.TYPE_BOT, response);
                messageList.add(botMessage);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                
                // Scroll to the latest message
                recyclerViewMessages.smoothScrollToPosition(messageList.size() - 1);
            }

            @Override
            public void onError(String errorMessage) {
                // Hide loading
                progressBar.setVisibility(View.GONE);
                
                // Show error
                Toast.makeText(requireContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                
                // Add error message as bot message
                Message errorBotMessage = new Message(
                        Message.TYPE_BOT,
                        "Sorry, I'm having trouble connecting. Please try again later."
                );
                messageList.add(errorBotMessage);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                
                // Scroll to the latest message
                recyclerViewMessages.smoothScrollToPosition(messageList.size() - 1);
            }
        });
    }
}
