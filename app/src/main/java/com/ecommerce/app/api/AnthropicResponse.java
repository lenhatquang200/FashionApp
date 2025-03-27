package com.ecommerce.app.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class AnthropicResponse {
    @SerializedName("id")
    private String id;
    
    @SerializedName("type")
    private String type;
    
    @SerializedName("role")
    private String role;
    
    @SerializedName("content")
    private List<Map<String, String>> content;
    
    @SerializedName("model")
    private String model;
    
    @SerializedName("stop_reason")
    private String stopReason;
    
    @SerializedName("stop_sequence")
    private String stopSequence;
    
    @SerializedName("usage")
    private Map<String, Integer> usage;
    
    public String getId() {
        return id;
    }
    
    public String getType() {
        return type;
    }
    
    public String getRole() {
        return role;
    }
    
    public String getModel() {
        return model;
    }
    
    public String getStopReason() {
        return stopReason;
    }
    
    public String getStopSequence() {
        return stopSequence;
    }
    
    public Map<String, Integer> getUsage() {
        return usage;
    }
    
    public String getContent() {
        if (content != null && !content.isEmpty()) {
            Map<String, String> firstContent = content.get(0);
            if (firstContent.containsKey("text")) {
                return firstContent.get("text");
            }
        }
        return "No response from assistant";
    }
}
