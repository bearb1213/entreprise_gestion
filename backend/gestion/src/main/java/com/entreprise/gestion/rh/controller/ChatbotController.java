package com.entreprise.gestion.rh.controller;

import com.entreprise.gestion.rh.dto.*;
import com.entreprise.gestion.rh.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:3000") // autorise le front React (port 3000)
public class ChatbotController {

    private final GroqChatService groqChatService;

    public ChatbotController(GroqChatService groqChatService) {
        this.groqChatService = groqChatService;
    }

    @PostMapping("/ask")
    public ResponseEntity<ChatResponse> ask(@RequestBody ChatRequest request) {
        if (request.getMessage() == null || request.getMessage().isBlank()) {
            return ResponseEntity.badRequest().body(new ChatResponse("Le champ 'message' est requis."));
        }

        String answer = groqChatService.askHRBot(request);
        return ResponseEntity.ok(new ChatResponse(answer));
    }
}