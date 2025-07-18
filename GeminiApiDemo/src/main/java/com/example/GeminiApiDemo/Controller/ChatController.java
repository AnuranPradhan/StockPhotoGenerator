package com.example.GeminiApiDemo.Controller;

import com.example.GeminiApiDemo.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/ask")
    public String getChatResponse(@RequestParam String prompt){
        return chatService.getChat(prompt);
    }

}
