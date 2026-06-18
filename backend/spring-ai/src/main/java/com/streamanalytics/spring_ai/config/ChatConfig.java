package com.streamanalytics.spring_ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ChatConfig {
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder.build();
    }

    @Bean
    public RestClient restClient(){
        return RestClient.create();
    }
}
