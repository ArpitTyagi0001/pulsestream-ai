package com.streamanalytics.event_consumer_service.service;

import com.streamanalytics.event_consumer_service.entity.Event;
import com.streamanalytics.event_consumer_service.repo.EventRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventService {
     private EventRepo eventRepo;

    public EventService(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    public void SaveEvent(String location){
        Event event = new Event();
        event.setLocation(location);
        event.setEventAt(LocalDateTime.now());

        eventRepo.save(event);
    }
}
