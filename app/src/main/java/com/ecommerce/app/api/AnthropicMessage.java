package com.ecommerce.app.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class AnthropicMessage {
    @SerializedName("model")
    private String model;
    
    @SerializedName("system")
    private String system;
    
    @SerializedName("messages")
    private List<Map<String, Object>> messages;
    
    @SerializedName("max_tokens")
    private int maxTokens;
    
    @SerializedName("temperature")
    private double temperature = 0.7;
    
    @SerializedName("top_p")
    private double topP = 1.0;
    
    @SerializedName("top_k")
    private int topK = 40;
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getSystem() {
        return system;
    }
    
    public void setSystem(String system) {
        this.system = system;
    }
    
    public List<Map<String, Object>> getMessages() {
        return messages;
    }
    
    public void setMessages(List<Map<String, Object>> messages) {
        this.messages = messages;
    }
    
    public int getMaxTokens() {
        return maxTokens;
    }
    
    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    
    public double getTopP() {
        return topP;
    }
    
    public void setTopP(double topP) {
        this.topP = topP;
    }
    
    public int getTopK() {
        return topK;
    }
    
    public void setTopK(int topK) {
        this.topK = topK;
    }
}
