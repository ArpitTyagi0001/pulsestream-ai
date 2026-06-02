package com.streamanalytics.event_service.service;

import com.streamanalytics.event_service.config.AppConstants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final KafkaTemplate<String , String> kafkaTemplate;

    public KafkaService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void updatedLocation(String location){
        kafkaTemplate.send(AppConstants.Location_Topic_Name , location);
    }
}
