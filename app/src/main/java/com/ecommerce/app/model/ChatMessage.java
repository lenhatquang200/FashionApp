package com.ecommerce.app.model;

import java.util.Date;

public class ChatMessage {
    public static final int TYPE_USER = 0;
    public static final int TYPE_ASSISTANT = 1;
    
    private long id;
    private String message;
    private int messageType; // 0 for user, 1 for assistant
    private Date timestamp;

    public ChatMessage() {
        this.timestamp = new Date();
    }

    public ChatMessage(long id, String message, int messageType) {
        this.id = id;
        this.message = message;
        this.messageType = messageType;
        this.timestamp = new Date();
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isUser() {
        return messageType == TYPE_USER;
    }

    public boolean isAssistant() {
        return messageType == TYPE_ASSISTANT;
    }

    public String getFormattedTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
        return sdf.format(timestamp);
    }
}
