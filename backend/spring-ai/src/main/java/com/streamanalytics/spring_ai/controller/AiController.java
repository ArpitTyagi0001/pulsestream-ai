package com.streamanalytics.spring_ai.controller;

import com.streamanalytics.spring_ai.service.AiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping("/chat-box")
    public String chatBox(@RequestParam String message){
        return aiService.getChat(message);
    }

    @GetMapping("/event-summary")
    public String Summary(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        return aiService.Summary(token);
    }
}
