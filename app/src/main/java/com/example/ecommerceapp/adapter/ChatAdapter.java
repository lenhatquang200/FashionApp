package com.example.ecommerceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying chat messages in a RecyclerView
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_ASSISTANT = 2;

    private final Context context;
    private List<ChatMessage> messages;
    private SimpleDateFormat timeFormat;

    public ChatAdapter(Context context, List<ChatMessage> messages) {
        this.context = context;
        this.messages = messages;
        this.timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        return message.isUserMessage() ? VIEW_TYPE_USER : VIEW_TYPE_ASSISTANT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_message_user, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_message_assistant, parent, false);
            return new AssistantMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        
        if (holder instanceof UserMessageViewHolder) {
            UserMessageViewHolder viewHolder = (UserMessageViewHolder) holder;
            viewHolder.messageTextView.setText(message.getMessage());
            viewHolder.timeTextView.setText(timeFormat.format(message.getTimestamp()));
        } else if (holder instanceof AssistantMessageViewHolder) {
            AssistantMessageViewHolder viewHolder = (AssistantMessageViewHolder) holder;
            viewHolder.messageTextView.setText(message.getMessage());
            viewHolder.timeTextView.setText(timeFormat.format(message.getTimestamp()));
        }
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    /**
     * Add a new message to the chat
     * @param message The message to add
     * @param isUserMessage Whether the message is from the user
     */
    public void addMessage(String message, boolean isUserMessage) {
        this.messages.add(new ChatMessage(message, isUserMessage, new Date()));
        notifyItemInserted(messages.size() - 1);
    }

    /**
     * Update the entire message list
     * @param messages New list of messages
     */
    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for user messages
     */
    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timeTextView;

        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_message_user);
            timeTextView = itemView.findViewById(R.id.text_timestamp_user);
        }
    }

    /**
     * ViewHolder for assistant (Claude) messages
     */
    static class AssistantMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView timeTextView;

        public AssistantMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_message_assistant);
            timeTextView = itemView.findViewById(R.id.text_timestamp_assistant);
        }
    }

    /**
     * Model class for chat messages
     */
    public static class ChatMessage {
        private String message;
        private boolean isUserMessage;
        private Date timestamp;

        public ChatMessage(String message, boolean isUserMessage, Date timestamp) {
            this.message = message;
            this.isUserMessage = isUserMessage;
            this.timestamp = timestamp;
        }

        public String getMessage() {
            return message;
        }

        public boolean isUserMessage() {
            return isUserMessage;
        }

        public Date getTimestamp() {
            return timestamp;
        }
    }
}
