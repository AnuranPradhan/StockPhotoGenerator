package com.example.GeminiApiDemo.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class RecipeService {

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RecipeService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String createRecipe(String ingredients, String cuisine, String dietaryRestrictions) {
        // Build the prompt directly using string formatting
        String prompt = String.format(
                "Create a recipe using these ingredients: %s. " +
                        "Cuisine type: %s. " +
                        "Dietary restrictions: %s. " +
                        "Provide a detailed recipe with title, ingredients list, and cooking instructions.and please give the answer in\n" +
                        "a structure manner so that the reader can read easily",
                ingredients, cuisine, dietaryRestrictions
        );

        // Build the API request
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", prompt)
                                }
                        )
                }
        );

        // Send request to Gemini API
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Extract the recipe text from the response
        return extractRecipeText(response);
    }

    private String extractRecipeText(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error generating recipe: " + e.getMessage();
        }
    }
}