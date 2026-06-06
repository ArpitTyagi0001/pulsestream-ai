package com.streamanalytics.event_consumer_service.consumer;

import com.streamanalytics.event_consumer_service.config.AppConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LocationKafkaConsumer {

    @KafkaListener(topics = AppConstants.LOCATION_TOPIC_NAME , groupId = AppConstants.GROUP_ID)
    public void listen(String location){
        System.out.println("updated Location :" +  location);
    }
}
