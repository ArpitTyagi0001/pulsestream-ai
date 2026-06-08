package com.streamanalytics.event_consumer_service.consumer;

import com.streamanalytics.event_consumer_service.config.AppConstants;
import com.streamanalytics.event_consumer_service.entity.Event;
import com.streamanalytics.event_consumer_service.repo.EventRepo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class LocationKafkaConsumer {

    private EventRepo eventRepo;

    public LocationKafkaConsumer(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    @KafkaListener(topics = AppConstants.LOCATION_TOPIC_NAME , groupId = AppConstants.GROUP_ID)
    public void listen(String location){
        Event event = new Event();
        event.setLocation(location);
        event.setEventAt(java.time.LocalDateTime.now());

        eventRepo.save(event);
    }
}
