package com.streamanalytics.spring_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class AiService {
    private final ChatClient chatClient;
    private final RestClient restClient;

    public AiService(ChatClient chatClient, RestClient restClient) {
        this.chatClient = chatClient;
        this.restClient = restClient;
    }

    public String getChat(String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    public String Summary() {
        Long totalEvents = restClient.get()
                .uri("http://localhost:8083/api/dashboard/total-events")
                .retrieve()
                .body(Long.class);
        
        String latestEvents = restClient.get()
                .uri("http://localhost:8083/api/dashboard/latest-events")
                .retrieve()
                .body(String.class);

        Map<String , Long> locationCount = restClient.get()
                .uri("http://localhost:8083/api/dashboard/location-count")
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Long>>(){});

        String message = """

        You are an analytics assistant.

        Total Events: %d
        
        Latest Events: %s
        
        Location Events: %s

        Generate a professional dashboard summary in 4-5 lines.
        Mention trends and active locations.
        Do not ask questions.
        """.formatted(totalEvents , latestEvents , locationCount);

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
