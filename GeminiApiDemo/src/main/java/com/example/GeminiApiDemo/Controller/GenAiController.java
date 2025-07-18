package com.example.GeminiApiDemo.Controller;

import com.example.GeminiApiDemo.Service.GenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GenAiController {

    @Autowired
    private GenAiService genAiService;

    @GetMapping("/ask")
    public ResponseEntity<String> getAnswer(@RequestParam String payload) {
        try {
            // Get URL from Gemini
            String url = genAiService.getAnswer(payload + " Only return the image URL");

            // Return as plain text response
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error: " + e.getMessage());
        }
    }
}