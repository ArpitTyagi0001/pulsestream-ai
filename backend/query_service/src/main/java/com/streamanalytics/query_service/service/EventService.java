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
    public List<Event> getEvent() {
        return eventRepo.findAll();
    }

    @Cacheable(value = "events" , key = "#eventId")
    public Event getEventId(Long eventId) {
        return eventRepo.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event Not Found"));
    }

    @Cacheable(value = "events")
    public long count() {
        return eventRepo.count();
    }

    @Cacheable(value = "events")
    public Event getLatest() {
        return eventRepo.
    }
}