package com.streamanalytics.event_service.service;

import com.streamanalytics.event_service.config.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    KafkaTemplate<String , String> kafkaTemplate;

    public boolean updatedLocation(String location){
        kafkaTemplate.send(AppConstants.Location_Topic_Name , location);
        return true;
    }
}
