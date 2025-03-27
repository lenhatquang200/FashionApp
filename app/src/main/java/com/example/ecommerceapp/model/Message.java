package com.example.ecommerceapp.model;

public class Message {
    public static final int TYPE_USER = 0;
    public static final int TYPE_BOT = 1;
    
    private int type;
    private String content;
    private long timestamp;

    public Message(int type, String content) {
        this.type = type;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public boolean isUserMessage() {
        return type == TYPE_USER;
    }
    
    public boolean isBotMessage() {
        return type == TYPE_BOT;
    }
}
