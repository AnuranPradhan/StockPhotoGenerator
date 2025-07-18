package com.example.GeminiApiDemo.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GenAiService {
    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GenAiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer(String payload) {
        Map<String, Object> request = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", payload)
                                }
                        )
                }
        );

        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return extractUrlFromResponse(response);
    }

    private String extractUrlFromResponse(String jsonResponse) {
        try {
            // Parse the JSON response
            JsonNode root = objectMapper.readTree(jsonResponse);

            // Get the full response text
            String fullText = root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

            // Extract URL using regex
            Pattern pattern = Pattern.compile("(https?://[^\\s]+)");
            Matcher matcher = pattern.matcher(fullText);

            if (matcher.find()) {
                return matcher.group(1);
            }

            throw new RuntimeException("No URL found in response: " + fullText);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing response: " + e.getMessage());
        }
    }
}