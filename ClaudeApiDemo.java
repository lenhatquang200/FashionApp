import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.UnsupportedEncodingException;

public class ClaudeApiDemo {
    
    private static final String API_URL = "https://api.anthropic.com/v1/messages";
    private static final String MODEL = "claude-3-haiku-20240307";
    private static final String ANTHROPIC_VERSION = "2023-06-01";
    
    // Get API key from environment variable
    private static final String API_KEY = System.getenv("ANTHROPIC_API_KEY");
    
    public static void main(String[] args) {
        try {
            if (API_KEY == null || API_KEY.isEmpty()) {
                System.err.println("ANTHROPIC_API_KEY environment variable is not set");
                System.exit(1);
            }
            
            // User's product question
            String userQuestion = "Do you have this shirt in size XL?";
            System.out.println("User: " + userQuestion);
            
            // Call Claude API
            String response = callClaudeApi(userQuestion);
            System.out.println("Assistant: " + response);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String callClaudeApi(String userMessage) throws IOException {
        // Create URL and connection
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Set request method and headers
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("x-api-key", API_KEY);
        connection.setRequestProperty("anthropic-version", ANTHROPIC_VERSION);
        connection.setDoOutput(true);
        
        // Create the system prompt
        String systemPrompt = "You are a helpful customer service assistant for an e-commerce clothing store. "
            + "Provide friendly, concise answers to customer questions about products, shipping, returns, and sizing.";
            
        // Format user message in the correct structure
        // Create the request JSON manually
        String requestBody = "{\n"
            + "  \"model\": \"" + MODEL + "\",\n"
            + "  \"system\": \"" + systemPrompt + "\",\n"
            + "  \"max_tokens\": 1024,\n"
            + "  \"messages\": [\n"
            + "    {\n"
            + "      \"role\": \"user\",\n"
            + "      \"content\": [\n"
            + "        {\n"
            + "          \"type\": \"text\",\n"
            + "          \"text\": \"" + escapeJson(userMessage) + "\"\n"
            + "        }\n"
            + "      ]\n"
            + "    }\n"
            + "  ]\n"
            + "}";
            
        // Send the request
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        // Get the response
        int responseCode = connection.getResponseCode();
        
        if (responseCode == 200) {
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine).append("\n");
                }
            }
            
            // Parse the response to extract Claude's reply
            String fullResponse = response.toString();
            
            // Extract the text content using regex
            Pattern pattern = Pattern.compile("\"text\":\"(.*?)\"", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(fullResponse);
            
            if (matcher.find()) {
                String messageContent = matcher.group(1);
                // Unescape any escaped characters
                messageContent = messageContent.replace("\\n", "\n")
                                             .replace("\\\"", "\"")
                                             .replace("\\\\", "\\");
                return messageContent;
            } else {
                return "Could not extract response content";
            }
        } else {
            StringBuilder errorResponse = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    errorResponse.append(responseLine).append("\n");
                }
            }
            throw new IOException("API request failed with code " + responseCode + ": " + errorResponse.toString());
        }
    }
    
    // Helper method to escape JSON string values
    private static String escapeJson(String input) {
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}