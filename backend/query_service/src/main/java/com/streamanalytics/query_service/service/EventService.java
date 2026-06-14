package com.streamanalytics.query_service.service;

import com.streamanalytics.query_service.entity.Event;
import com.streamanalytics.query_service.repo.EventRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private EventRepo eventRepo;

    public EventService(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    @Cacheable(value = "events")
    public List<Event> getAllEvent() {
       return eventRepo.findAll();
    }

    @Cacheable(value = "events" , key = "#EventId")
    public Event EventById(Long EventId) {
        return  eventRepo.findById(EventId).orElseThrow(() -> new EntityNotFoundException("Event Not Found"));
    }
}