package com.example.GeminiApiDemo.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class ChatService {
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper(); // For JSON parsing

    public ChatService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getChat(String prompt) {
        Map<String, Object> request = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", prompt)
                                }
                        )
                }
        );

        String responseJson = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return extractTextFromResponse(responseJson);
    }

    private String extractTextFromResponse(String jsonResponse) {
        try {
            // Parse the JSON response
            JsonNode root = objectMapper.readTree(jsonResponse);

            // Navigate directly to the text value:
            // candidates[0].content.parts[0].text
            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            return "Error: " + e.getMessage() + " | Response: " + jsonResponse;
        }
    }
}