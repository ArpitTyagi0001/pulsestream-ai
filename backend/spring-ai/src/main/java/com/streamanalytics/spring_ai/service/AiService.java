package com.streamanalytics.spring_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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

        String message = """

        You are an analytics assistant.

        Total Events: %d

        Based only on this number, generate a short dashboard summary in 2-3 lines.

        Do not ask for more information.

        """.formatted(totalEvents);

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
