package com.example.ecommerceapp.api;

public class ApiResponse {
    private boolean success;
    private String message;
    private String conversationHistory;

    public ApiResponse(boolean success, String message, String conversationHistory) {
        this.success = success;
        this.message = message;
        this.conversationHistory = conversationHistory;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getConversationHistory() {
        return conversationHistory;
    }

    public void setConversationHistory(String conversationHistory) {
        this.conversationHistory = conversationHistory;
    }
}
