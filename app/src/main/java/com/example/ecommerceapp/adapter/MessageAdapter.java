package com.example.ecommerceapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_BOT = 1;
    
    private Context context;
    private List<Message> messageList;
    private SimpleDateFormat timeFormatter;

    public MessageAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
        this.timeFormatter = new SimpleDateFormat("h:mm a", Locale.US);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
            return new BotMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        
        if (holder.getItemViewType() == VIEW_TYPE_USER) {
            ((UserMessageViewHolder) holder).bind(message);
        } else {
            ((BotMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        return message.getType() == Message.TYPE_USER ? VIEW_TYPE_USER : VIEW_TYPE_BOT;
    }

    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        TextView textTime;
        
        public UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message);
            textTime = itemView.findViewById(R.id.text_time);
        }
        
        public void bind(Message message) {
            textMessage.setText(message.getContent());
            textTime.setText(timeFormatter.format(new Date(message.getTimestamp())));
            
            // Align to the right for user messages
            textMessage.setBackgroundResource(R.drawable.bg_message_user);
            
            // Set layout parameters to align right
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textMessage.getLayoutParams();
            params.leftMargin = (int) context.getResources().getDimension(R.dimen.message_margin_large);
            params.rightMargin = (int) context.getResources().getDimension(R.dimen.message_margin_small);
            textMessage.setLayoutParams(params);
        }
    }
    
    class BotMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        TextView textTime;
        
        public BotMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_message);
            textTime = itemView.findViewById(R.id.text_time);
        }
        
        public void bind(Message message) {
            textMessage.setText(message.getContent());
            textTime.setText(timeFormatter.format(new Date(message.getTimestamp())));
            
            // Align to the left for bot messages
            textMessage.setBackgroundResource(R.drawable.bg_message_bot);
            
            // Set layout parameters to align left
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) textMessage.getLayoutParams();
            params.leftMargin = (int) context.getResources().getDimension(R.dimen.message_margin_small);
            params.rightMargin = (int) context.getResources().getDimension(R.dimen.message_margin_large);
            textMessage.setLayoutParams(params);
        }
    }
}
