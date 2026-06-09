package com.streamanalytics.event_consumer_service.consumer;

import com.streamanalytics.event_consumer_service.config.AppConstants;
import com.streamanalytics.event_consumer_service.service.EventService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LocationKafkaConsumer {

    private EventService eventService;

    public LocationKafkaConsumer(EventService eventService) {
        this.eventService = eventService;
    }

    @KafkaListener(topics = AppConstants.LOCATION_TOPIC_NAME , groupId = AppConstants.GROUP_ID)
    public void listen(String location){
       eventService.SaveEvent(location);
    }
}
